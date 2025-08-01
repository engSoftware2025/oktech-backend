package com.oktech.boasaude.dto;

import java.util.UUID;

/**
 * DTO para resposta de loja.
 * Contém os campos necessários para a representação de uma loja na API.
 * @author Lucas do Ouro
 * @version 1.0
 */

public record ShopResponseDto(
    UUID id,
    String name,
    String description,
    String cnpj
) {} 