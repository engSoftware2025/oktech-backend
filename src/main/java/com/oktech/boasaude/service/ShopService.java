package com.oktech.boasaude.service;

import com.oktech.boasaude.dto.ShopResponseDto;
import com.oktech.boasaude.entity.User;
import com.oktech.boasaude.dto.ShopCreateRequestDto;

public interface ShopService {

    ShopResponseDto createShop(User user, ShopCreateRequestDto  dto); // Cria uma nova loja associada ao usuário

    ShopResponseDto getShopbyuser(User user); // Obtém a loja associada ao usuário

    boolean isValidCnpj(String cnpj); // Valida o CNPJ da loja
}