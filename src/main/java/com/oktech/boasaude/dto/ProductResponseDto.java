package com.oktech.boasaude.dto;
import com.oktech.boasaude.entity.Product;

/** * DTO para resposta de produto.
 * Contém os campos necessários para a representação de um produto na API.
 * @author João Martins
 * @version 1.0
 */

public record ProductResponseDto(
    String id,
    String name,
    String description,
    Integer price,
    String category,
    Integer stock
) {
    /**
     * Construtor para criar um ProductResponseDto a partir de uma entidade Product.
     * @param product A entidade Product a ser convertida.
     */
    public ProductResponseDto(Product product) {
        this(
            product.getId().toString(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getCategory(),
            product.getStock()
        );
    }
}

