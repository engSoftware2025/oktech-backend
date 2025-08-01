package com.oktech.boasaude.mapper;

import org.springframework.stereotype.Component;

import com.oktech.boasaude.dto.ShopCreateRequestDto;
import com.oktech.boasaude.dto.ShopResponseDto;
import com.oktech.boasaude.entity.Shop;

/**
 * Mapper para converter entre DTOs e entidades de loja.
 * Facilita a transformação de dados entre as camadas de apresentação e persistência.
 * @author Lucas do Ouro
 * @version 1.0
 */

@Component
public class ShopMapper {
    
    public Shop toEntity(ShopCreateRequestDto dto) {
        Shop shop = new Shop();
        shop.setName(dto.name());
        shop.setDescription(dto.description());
        shop.setCnpj(dto.cnpj());
        return shop;
    }

    public ShopResponseDto toDto(Shop shop) {
        return new ShopResponseDto(
            shop.getId(),
            shop.getName(),
            shop.getDescription(),
            shop.getCnpj()
        );
    }
}
