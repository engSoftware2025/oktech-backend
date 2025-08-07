package com.oktech.boasaude.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO para criar um novo pedido.
 * Contém os campos necessários para o registro de um pedido.
 * @author João Martins
 * @version 1.0
 */

public record CreateOrderItemDto(
    @NotNull(message = "A quantidade do produto é obrigatória")
    @Positive(message = "A quantidade não pode ser negativa")
    Integer quantity,

    @NotNull(message = "O ID do produto é obrigatório")
    UUID productId
) {}

