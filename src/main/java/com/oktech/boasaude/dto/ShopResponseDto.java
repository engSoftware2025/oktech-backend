package com.oktech.boasaude.dto;

import java.util.UUID;

import com.oktech.boasaude.entity.Shop;

/**
 * DTO para resposta de loja.
 * Contém os campos necessários para a representação de uma loja na API.
 * 
 * @author Lucas do Ouro
 * @version 1.0
 */

public record ShopResponseDto(
        UUID id,
        String name,
        String description,
        String cnpj) {
    /**
     * Construtor para criar um ShopResponseDto a partir de uma entidade Shop.
     * 
     * @param shop A entidade Shop a ser convertida.
     */
    public ShopResponseDto(Shop shop) {
        this(shop.getId(), shop.getName(), shop.getDescription(), shop.getCnpj());
    }

}