package com.oktech.boasaude.service.impl;

import com.oktech.boasaude.dto.ShopCreateRequestDto;
import com.oktech.boasaude.dto.ShopResponseDto;
import com.oktech.boasaude.entity.Shop;
import com.oktech.boasaude.entity.User;
import com.oktech.boasaude.entity.UserRole;
import com.oktech.boasaude.repository.ShopRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ShopServiceImplTest {

    @Mock
    private ShopRepository shopRepository;

    @Mock
    private UserServiceImpl userService; // Mock da implementação, conforme seu código

    @InjectMocks
    private ShopServiceImpl shopService;

    // --- Testes para createShop ---

    @Test
    void testCreateShop_ComSucesso_EAtualizaRole() {
        // Arrange
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setRole(UserRole.USER); // Usuário comum

        ShopCreateRequestDto createDto = new ShopCreateRequestDto("Loja Nova", "Desc", "11.222.333/0001-44");
        
        // Simula que o usuário não tem loja
        Mockito.when(shopRepository.findByOwnerId(user.getId())).thenReturn(Optional.empty());
        // Simula a verificação de nome (mesmo sendo falha, precisamos mockar para o teste passar)
        Mockito.when(shopRepository.findAllByNameContainingIgnoreCase(user.getName())).thenReturn(Collections.emptyList());

        Shop savedShop = new Shop(createDto, user);
        Mockito.when(shopRepository.save(Mockito.any(Shop.class))).thenReturn(savedShop);

        // Act
        ShopResponseDto result = shopService.createShop(user, createDto);

        // Assert
        assertNotNull(result);
        assertEquals(createDto.name(), result.name());
        // Verifica que o método para atualizar a role foi chamado para um usuário não-admin
        Mockito.verify(userService, Mockito.times(1)).updateUserRole(user.getId(), UserRole.PRODUCTOR);
        Mockito.verify(shopRepository, Mockito.times(1)).save(Mockito.any(Shop.class));
    }

    @Test
    void testCreateShop_ComSucesso_UsuarioAdminNaoAtualizaRole() {
        // Arrange
        User adminUser = new User();
        adminUser.setId(UUID.randomUUID());
        adminUser.setRole(UserRole.ADMIN); // Usuário é ADMIN

        ShopCreateRequestDto createDto = new ShopCreateRequestDto("Loja do Admin", "Desc Admin", "22.333.444/0001-55");

        Mockito.when(shopRepository.findByOwnerId(adminUser.getId())).thenReturn(Optional.empty());
        Mockito.when(shopRepository.findAllByNameContainingIgnoreCase(adminUser.getName())).thenReturn(Collections.emptyList());

        Shop savedShop = new Shop(createDto, adminUser);
        Mockito.when(shopRepository.save(Mockito.any(Shop.class))).thenReturn(savedShop);

        // Act
        shopService.createShop(adminUser, createDto);

        // Assert
        // Verifica que o método para atualizar a role NÃO foi chamado para um admin
        Mockito.verify(userService, Mockito.never()).updateUserRole(Mockito.any(), Mockito.any());
    }
    
    @Test
    void testCreateShop_Falha_UsuarioJaPossuiLoja() {
        // Arrange
        User user = new User();
        user.setId(UUID.randomUUID());
        ShopCreateRequestDto createDto = new ShopCreateRequestDto("Loja Teste", "Descrição Teste", "11.222.333/0001-44");

        // Simula que o usuário já possui uma loja
        Mockito.when(shopRepository.findByOwnerId(user.getId())).thenReturn(Optional.of(new Shop()));
        Mockito.when(shopRepository.findAllByNameContainingIgnoreCase(user.getName())).thenReturn(Collections.emptyList());


        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            shopService.createShop(user, createDto);
        });

        assertEquals("Usuário já possui uma loja associada.", exception.getMessage());
        Mockito.verify(shopRepository, Mockito.never()).save(Mockito.any(Shop.class));
    }
    
    // --- Testes para deleteShop ---

    @Test
    void testDeleteShop_ComSucesso() {
        // Arrange
        User owner = new User();
        owner.setId(UUID.randomUUID());
        Shop shop = new Shop();
        shop.setOwner(owner);
        UUID shopId = UUID.randomUUID();

        Mockito.when(shopRepository.findById(shopId)).thenReturn(Optional.of(shop));

        // Act
        shopService.deleteShop(shopId, owner);

        // Assert
        Mockito.verify(shopRepository, Mockito.times(1)).delete(shop);
    }
    
    @Test
    void testDeleteShop_Falha_UsuarioNaoEhProprietario() {
        // Arrange
        User owner = new User();
        owner.setId(UUID.randomUUID());
        User otherUser = new User();
        otherUser.setId(UUID.randomUUID());
        
        Shop shop = new Shop();
        shop.setOwner(owner);
        UUID shopId = UUID.randomUUID();

        Mockito.when(shopRepository.findById(shopId)).thenReturn(Optional.of(shop));

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> {
            // Tenta deletar a loja com um usuário diferente
            shopService.deleteShop(shopId, otherUser);
        });
        
        Mockito.verify(shopRepository, Mockito.never()).delete(Mockito.any(Shop.class));
    }

    // --- Testes para updateShop ---

    @Test
    void testUpdateShop_ComSucesso() {
        // Arrange
        User owner = new User();
        owner.setId(UUID.randomUUID());
        Shop existingShop = new Shop();
        existingShop.setId(UUID.randomUUID());
        existingShop.setOwner(owner);
        
        ShopCreateRequestDto updateDto = new ShopCreateRequestDto("Loja Teste", "Descrição Teste", "99.888.777/0001-66");

        Mockito.when(shopRepository.findById(existingShop.getId())).thenReturn(Optional.of(existingShop));
        Mockito.when(shopRepository.findAll()).thenReturn(List.of(existingShop)); // Simula a lista para a checagem de CNPJ
        Mockito.when(shopRepository.save(Mockito.any(Shop.class))).thenReturn(existingShop);

        // Act
        ShopResponseDto result = shopService.updateShop(existingShop.getId(), updateDto, owner);

        // Assert
        assertNotNull(result);
        assertEquals("Nome Atualizado", result.name());
        assertEquals("99.888.777/0001-66", result.cnpj());
        Mockito.verify(shopRepository, Mockito.times(1)).save(existingShop);
    }
    
    @Test
    void testUpdateShop_Falha_CnpjDuplicadoEmOutraLoja() {
        // Arrange
        User owner = new User();
        owner.setId(UUID.randomUUID());
        
        Shop shopToUpdate = new Shop();
        shopToUpdate.setId(UUID.randomUUID());
        shopToUpdate.setOwner(owner);
        
        Shop otherShopWithSameCnpj = new Shop();
        otherShopWithSameCnpj.setId(UUID.randomUUID());
        otherShopWithSameCnpj.setCnpj("99.888.777/0001-66");
        
        ShopCreateRequestDto updateDto = new ShopCreateRequestDto("Loja Teste", "Descrição Teste", "99.888.777/0001-66");
        
        Mockito.when(shopRepository.findById(shopToUpdate.getId())).thenReturn(Optional.of(shopToUpdate));
        // Simula a lista para a checagem de CNPJ, retornando a outra loja com o mesmo CNPJ
        Mockito.when(shopRepository.findAll()).thenReturn(List.of(otherShopWithSameCnpj));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            shopService.updateShop(shopToUpdate.getId(), updateDto, owner);
        });
        
        assertEquals("CNPJ já cadastrado.", exception.getMessage());
        Mockito.verify(shopRepository, Mockito.never()).save(Mockito.any(Shop.class));
    }
}