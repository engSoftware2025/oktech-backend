package com.oktech.boasaude.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oktech.boasaude.dto.ShopCreateRequestDto;
import com.oktech.boasaude.dto.ShopResponseDto;
import com.oktech.boasaude.entity.User;
import com.oktech.boasaude.service.ShopService;


import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ShopController é responsável por gerenciar as operações relacionadas às lojas.
 * Ele pode incluir funcionalidades como listar lojas, buscar detalhes de uma loja específica,
 * criar novas lojas, atualizar informações de lojas existentes, etc.
 * @author João Martins
 * @version 1.0
 */

@RestController
@RequestMapping("/v1/shops")

public class ShopController {
    private final ShopService shopService;


    private static final Logger logger = LoggerFactory.getLogger(ShopController.class);

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @PostMapping("/create")
    public ResponseEntity<ShopResponseDto> createShop(@Valid @RequestBody ShopCreateRequestDto shopCreateDto, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            logger.warn("User not authenticated");
            return ResponseEntity.status(401).build();
        }

        User user = (User) authentication.getPrincipal();
        ShopResponseDto shopResponse = shopService.createShop(user, shopCreateDto);
        logger.info("Shop created successfully for user ID: {}", user.getId());
        return ResponseEntity.ok(shopResponse);
    }

    @GetMapping()
    public ResponseEntity<ShopResponseDto> getShop(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            logger.warn("User not authenticated");
            return ResponseEntity.status(401).build();
        }

        User user = (User) authentication.getPrincipal();
        ShopResponseDto shopResponse = shopService.getShopbyuser(user);
        logger.info("Shop retrieved successfully for user ID: {}", user.getId());
        return ResponseEntity.ok(shopResponse);     
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteShop(@PathVariable UUID id, Authentication authentication){
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            logger.warn("User not authenticated");
            return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("message", "User not authenticated"));
        }

        User currentUser = (User) authentication.getPrincipal();

        shopService.deleteShop(id, currentUser);
        logger.info("Shop deleted successfully for user ID: {}", currentUser.getId());
        return ResponseEntity.ok(Map.of("message", "Shop deleted"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShopResponseDto> updateShop(@PathVariable UUID id, @Valid @RequestBody ShopCreateRequestDto shopCreateDto, Authentication authentication){
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            logger.warn("User not authenticated");
            return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .build();
        }
        
        User currentUser = (User) authentication.getPrincipal();

        ShopResponseDto shopResponse = shopService.updateShop(id, shopCreateDto, currentUser);
        logger.info("Shop updated successfully for user ID: {}", currentUser.getId());

        return ResponseEntity.ok(shopResponse);
    }

}
