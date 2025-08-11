package com.oktech.boasaude.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oktech.boasaude.dto.ShopCreateRequestDto;
import com.oktech.boasaude.dto.ShopResponseDto;
import com.oktech.boasaude.entity.User;
import com.oktech.boasaude.service.ProductService;
import com.oktech.boasaude.service.ShopService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class ShopControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private ShopService shopService;

    @Mock
    private ProductService productService;

    @Test
    void testCreateShop_ComSucesso() throws Exception {
        // Arrange
        ShopCreateRequestDto createDto = new ShopCreateRequestDto("Loja de Teste", "Desc", "11.222.333/0001-44");
        ShopResponseDto responseDto = new ShopResponseDto(UUID.randomUUID(), createDto.name(), createDto.description(), createDto.cnpj());
        
        when(shopService.createShop(any(User.class), any(ShopCreateRequestDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/v1/shops/create")
                        .with(user(new User())) // Simula um usuário autenticado
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk()) // Seu controller retorna 200 OK
                .andExpect(jsonPath("$.name").value("Loja de Teste"));
    }
    
    @Test
    void testCreateShop_Falha_NaoAutenticado() throws Exception {
        // Arrange
        ShopCreateRequestDto createDto = new ShopCreateRequestDto("Loja de Teste", "Desc", "11.222.333/0001-44");

        // Act & Assert
        // A chamada é feita SEM o .with(user(...))
        mockMvc.perform(post("/v1/shops/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isUnauthorized()); // Espera 401, conforme sua lógica no controller
    }

    @Test
    void testGetShop_ComSucesso_UsuarioAutenticado() throws Exception {
        // Arrange
        ShopResponseDto responseDto = new ShopResponseDto(UUID.randomUUID(), "Minha Loja", "Desc", "22.333.444/0001-55");
        when(shopService.getShopbyuser(any(User.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(get("/v1/shops")
                        .with(user(new User())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Minha Loja"));
    }
    
    @Test
    void testListgetAllShops_RotaPublica_ComSucesso() throws Exception {
        // Arrange
        ShopResponseDto shopDto = new ShopResponseDto(UUID.randomUUID(), "Loja Pública", "Desc", "33.444.555/0001-66");
        Page<ShopResponseDto> pageResponse = new PageImpl<>(Collections.singletonList(shopDto));
        
        when(shopService.getAllShops(any(Pageable.class))).thenReturn(pageResponse);
        
        // Act & Assert
        mockMvc.perform(get("/v1/shops/all")) // Chamada sem autenticação
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Loja Pública"));
    }

    @Test
    void testUpdateShop_ComSucesso() throws Exception {
        // Arrange
        UUID shopId = UUID.randomUUID();
        ShopCreateRequestDto updateDto = new ShopCreateRequestDto("Nome Atualizado", "Desc Atualizada", "11.222.333/0001-44");
        ShopResponseDto responseDto = new ShopResponseDto(shopId, updateDto.name(), updateDto.description(), updateDto.cnpj());

        when(shopService.updateShop(any(UUID.class), any(ShopCreateRequestDto.class), any(User.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(put("/v1/shops/{id}", shopId)
                        .with(user(new User()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nome Atualizado"));
    }
    
    @Test
    void testDeleteShop_ComSucesso() throws Exception {
        // Arrange
        UUID shopId = UUID.randomUUID();
        // O método deleteShop retorna void, então não precisamos de um "when(...).thenReturn(...)"
        // Apenas garantimos que nenhuma exceção seja lançada
        doNothing().when(shopService).deleteShop(any(UUID.class), any(User.class));

        // Act & Assert
        mockMvc.perform(delete("/v1/shops/{id}", shopId)
                        .with(user(new User())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Shop deleted"));
    }
}