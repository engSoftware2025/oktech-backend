package com.oktech.boasaude.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import com.oktech.boasaude.dto.CreateProductDto;
import com.oktech.boasaude.dto.ProductResponseDto;
import com.oktech.boasaude.entity.Product;
import com.oktech.boasaude.entity.User;
import com.oktech.boasaude.service.ProductService;

/**
 * ProductController é responsável por gerenciar as operações relacionadas aos produtos.
 * Ele pode incluir funcionalidades como listar produtos, buscar detalhes de um produto específico,
 * criar novos produtos, atualizar informações de produtos existentes, etc.
 * @author João Martins
 * @version 1.0
 */


@RestController
@RequestMapping("/v1/products")

public class ProductController {

    private final ProductService productService;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @PostMapping("/create/{shopId}")
    public ResponseEntity<ProductResponseDto> createProduct
    (
        @Valid @RequestBody CreateProductDto createProductDto, 
        @PathVariable UUID shopId, 
        Authentication authentication
    ) 
    {
        try
        { 
            if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
                logger.warn("User not authenticated");
                return ResponseEntity.status(401).build();
            }
    
            User user = (User) authentication.getPrincipal();
            
            Product productResponse = productService.createProduct(createProductDto, shopId, user);
            
            logger.info("Product created successfully with ID: {}", productResponse.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(new ProductResponseDto(productResponse));
        }
        catch(Exception e) {
            logger.error("Error creating product: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
        @ParameterObject @PageableDefault(page = 0, size = 10) Pageable pageable) {
        
        Page<Product> productsPage = productService.getAllProducts(pageable);

        Page<ProductResponseDto> products = productsPage.map(ProductResponseDto::new);
        logger.info("Products retrieved successfully, count: {}", products.getTotalElements());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable UUID id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            logger.warn("Product not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        logger.info("Product retrieved successfully with ID: {}", id);
        return ResponseEntity.ok(new ProductResponseDto(product));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
        @PathVariable UUID id, 
        @Valid @RequestBody CreateProductDto createProductDto, 
        Authentication authentication) {
        try{
            if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
                logger.warn("User not authenticated");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
    
            User user = (User) authentication.getPrincipal();

            Product updatedProduct = productService.updateProduct(id, createProductDto, user);
            
            logger.info("Product updated successfully with ID: {}", updatedProduct.getId());
            return ResponseEntity.ok(new ProductResponseDto(updatedProduct));

        }catch(Exception e) {
            logger.error("Error updating product: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable UUID id, Authentication authentication) {
        try{
            if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
                logger.warn("User not authenticated");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
    
            User currentUser = (User) authentication.getPrincipal();
    
            productService.deleteProduct(id, currentUser);
            
            logger.info("Product deleted successfully with ID: {}", id);
            return ResponseEntity.ok(Map.of("message", "Product deleted"));

        }catch(Exception e) {
            logger.error("Error deleting product: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error deleting product"));
        }
    }
}
