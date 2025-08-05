package com.oktech.boasaude.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO para criar um novo produto.
 * Contém os campos necessários para o registro de um produto.
 * @author João Martins
 * @version 1.0
 */

public record CreateProductDto(
    @NotBlank(message = "O nome do produto é obrigatório")
    String name,

    @NotBlank(message = "A descrição do produto é obrigatória")
    String description,

    @NotBlank(message = "O preço do produto é obrigatório")
    String category,

    @NotNull(message = "A quantidade em estoque do produto é obrigatória")
    @Positive(message = "A quantidade em estoque não pode ser negativa")
    Integer stock,

    @NotNull(message = "O preço do produto é obrigatório")
    @Positive(message = "O preço do produto não pode ser negativo")
    Integer price
) {}