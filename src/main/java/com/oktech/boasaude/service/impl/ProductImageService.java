package com.oktech.boasaude.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.oktech.boasaude.entity.Product;
import com.oktech.boasaude.entity.ProductImage;
import com.oktech.boasaude.repository.ProductImageRepository;
import com.oktech.boasaude.repository.ProductRepository;

@Service
public class ProductImageService {

    private static final int MAX_IMAGES_PER_PRODUCT = 5;
    List<String> ALLOWED_IMAGE_EXTENSIONS = List.of(".jpg", ".jpeg", ".png", ".gif");

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${storage.path:uploads/images}")
    private String storagePath;

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;

    public ProductImageService(ProductImageRepository productImageRepository, ProductRepository productRepository) {
        this.productImageRepository = productImageRepository;
        this.productRepository = productRepository;
    }

    /**
     * Salva um ou múltiplos arquivos de imagem associados a um produto específico
     * 
     * @param files     Lista de arquivos de imagem a serem salvos (máximo 5)
     * @param productId O ID do produto ao qual as imagens serão associadas
     * @return Lista das imagens salvas
     * @throws RuntimeException se o produto não for encontrado, se exceder o limite
     *                          ou se ocorrer erro ao salvar
     */
    public List<ProductImage> saveFilesWithProduct(List<MultipartFile> files, UUID productId) {
        validateFiles(files);

        Product product = findProductById(productId);
        validateImageLimit(productId, files.size());

        return saveImageFiles(files, product);
    }

    private void validateFiles(List<MultipartFile> files) {
        if (files.size() > MAX_IMAGES_PER_PRODUCT) {
            throw new RuntimeException("Maximum " + MAX_IMAGES_PER_PRODUCT + " images allowed per product");
        }

        if (files.isEmpty() || files.stream().allMatch(MultipartFile::isEmpty)) {
            throw new RuntimeException("At least one valid image file is required");
        }

        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();

            if (fileName == null || fileName.isEmpty()) {
                throw new RuntimeException("File name cannot be empty");
            }

            String extension = fileName.substring(fileName.lastIndexOf('.')).toLowerCase();

            if (!ALLOWED_IMAGE_EXTENSIONS.contains(extension)) {
                logger.error("Invalid file type: {}", extension);
                throw new RuntimeException("Invalid file type: " + extension + ". Allowed types: "
                        + String.join(", ", ALLOWED_IMAGE_EXTENSIONS));
            }
        }

    }

    private Product findProductById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
    }

    private void validateImageLimit(UUID productId, int newImagesCount) {
        long existingImagesCount = countImagesByProductId(productId);
        long totalAfterUpload = existingImagesCount + newImagesCount;

        if (totalAfterUpload > MAX_IMAGES_PER_PRODUCT) {
            throw new RuntimeException(String.format(
                    "Product can have maximum %d images. Current: %d, trying to add: %d",
                    MAX_IMAGES_PER_PRODUCT, existingImagesCount, newImagesCount));
        }
    }

    private List<ProductImage> saveImageFiles(List<MultipartFile> files, Product product) {
        List<ProductImage> savedImages = new ArrayList<>();

        try {
            Path uploadPath = createUploadDirectory();

            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    ProductImage savedImage = saveImageFile(file, product, uploadPath);
                    savedImages.add(savedImage);
                }
            }

            return savedImages;
        } catch (IOException e) {
            cleanupFailedUploads(savedImages);
            throw new RuntimeException("Failed to store files", e);
        }
    }

    private Path createUploadDirectory() throws IOException {
        Path uploadPath = Paths.get(storagePath);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        return uploadPath;
    }

    private ProductImage saveImageFile(MultipartFile file, Product product, Path uploadPath) throws IOException {
        String uniqueFileName = generateUniqueFileName(file.getOriginalFilename());
        Path filePath = uploadPath.resolve(uniqueFileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        ProductImage productImage = new ProductImage();
        productImage.setImageUrl(filePath.toString());
        productImage.setProduct(product);

        return productImageRepository.save(productImage);
    }

    private String generateUniqueFileName(String originalFileName) {
        return System.currentTimeMillis() + "_" +
                UUID.randomUUID().toString().substring(0, 8) + "_" +
                originalFileName;
    }

    private void cleanupFailedUploads(List<ProductImage> savedImages) {
        for (ProductImage savedImage : savedImages) {
            try {
                productImageRepository.delete(savedImage);
                Files.deleteIfExists(Paths.get(savedImage.getImageUrl()));
            } catch (Exception cleanupException) {
                System.err.println("Failed to cleanup file: " + savedImage.getImageUrl());
            }
        }
    }

    /**
     * Retorna todas as imagens de um produto pelo ID do produto
     * 
     * @param productId O ID do produto
     * @return Lista de imagens do produto
     */
    public List<ProductImage> getImagesByProductId(UUID productId) {
        return productImageRepository.findByProductId(productId);
    }

    /**
     * Retorna uma imagem específica pelo seu ID
     * 
     * @param imageId O ID da imagem
     * @return Optional contendo a imagem se encontrada
     */
    public Optional<ProductImage> getImageById(UUID imageId) {
        return productImageRepository.findById(imageId);
    }

    /**
     * Conta o número de imagens de um produto específico
     * 
     * @param productId O ID do produto
     * @return Número de imagens do produto
     */
    public long countImagesByProductId(UUID productId) {
        return productImageRepository.findByProductId(productId).size();
    }

    /**
     * Deleta todas as imagens associadas a um produto específico
     * 
     * @param productId O ID do produto cujas imagens serão deletadas
     */
    public void deleteAllImagesByProductId(UUID productId) {
        List<ProductImage> images = productImageRepository.findByProductId(productId);

        for (ProductImage image : images) {
            try {
                // Deleta o arquivo físico
                Path imagePath = Paths.get(image.getImageUrl());
                Files.deleteIfExists(imagePath);
                logger.info("Deleted image file: {}", image.getImageUrl());
            } catch (IOException e) {
                logger.error("Failed to delete image file: {}", image.getImageUrl(), e);
                // Continua com a exclusão do registro no banco mesmo se não conseguir deletar o
                // arquivo
            }
        }

        // Deleta todos os registros de imagem do banco de dados
        productImageRepository.deleteAll(images);
        logger.info("Deleted {} images for product with id: {}", images.size(), productId);
    }
}
