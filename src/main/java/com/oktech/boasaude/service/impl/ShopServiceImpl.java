package com.oktech.boasaude.service.impl;

import com.oktech.boasaude.dto.ShopCreateRequestDto;
import com.oktech.boasaude.dto.ShopResponseDto;
import com.oktech.boasaude.entity.Shop;
import com.oktech.boasaude.entity.User;
import com.oktech.boasaude.entity.UserRole;
import com.oktech.boasaude.repository.ShopRepository;
import com.oktech.boasaude.service.ShopService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * * Implementação do serviço de loja.
 * Fornece métodos para criar e obter lojas associadas a usuários.
 * 
 * @author Lucas do Ouro
 * @version 1.1 - Corrigido o método createShop para verificar se o usuário já
 *          possui uma loja associada e validar o CNPJ.
 * @version 1.0 - Implementação inicial do serviço de loja.
 * @version 1.2 - Adicionado método para validar CNPJ e corrigido o método de
 *          atualização de loja.
 * @version 1.3 - Adicionado método para deletar loja e corrigido o método de
 *          atualização de loja para verificar se o usuário é o proprietário.
 * @version 1.4 - Adicionado método para obter todas as lojas com paginação.
 */

@Service
public class ShopServiceImpl implements ShopService {

    private ShopRepository shopRepository;

    private UserServiceImpl userService;

    private static final String CNPJ_REGEX = "^(\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}|\\d{14})$"; // Formato
                                                                                                   // XX.XXX.XXX/XXXX-XX
                                                                                                   // ou XXXXXXXXXXXXXXX
    private static final Pattern CNPJ_PATTERN = Pattern.compile(CNPJ_REGEX); // Regex para validar CNPJ

    public ShopServiceImpl(ShopRepository shopRepository, UserServiceImpl userService) {
        this.shopRepository = shopRepository;
        this.userService = userService;
    }

    @Override
    public ShopResponseDto createShop(User user, ShopCreateRequestDto dto) {
        // Verifica se o usuário já possui uma loja associada
        if (!shopRepository.findAllByNameContainingIgnoreCase(user.getName()).isEmpty()) {
            throw new IllegalArgumentException("Usuário já possui uma loja associada.");
        }
        // Valida se o usuário já possui uma loja
        if (shopRepository.findByOwnerId(user.getId()).isPresent()) {
            throw new IllegalArgumentException("Usuário já possui uma loja associada.");
        }
        if (!isValidCnpj(dto.cnpj())) {
            throw new IllegalArgumentException("CNPJ inválido.");
        }

        if (user.getRole() != UserRole.ADMIN) {
            userService.updateUserRole(user.getId(), UserRole.PRODUCTOR);
        }

        Shop shop = new Shop(dto, user); // Cria uma nova loja com os dados do DTO e o usuário

        Shop savedShop = shopRepository.save(shop); // Salva a loja no repositório

        return new ShopResponseDto(savedShop); // Retorna a resposta com os dados da loja salva
    }

    // Obtém a loja pelo ID
    @Override
    public ShopResponseDto getShopById(UUID id) { 
        return shopRepository.findById(id)
            .map(shopEncontrada -> new ShopResponseDto(shopEncontrada)) 
            .orElseThrow(() -> new EntityNotFoundException("Shop not found with id: " + id));
    }
    

    // Obtém a loja associada ao usuário
    @Override
    public ShopResponseDto getShopbyuser(User user) {
        return shopRepository.findByOwnerId(user.getId()) // Se a loja for encontrada, converte para ShopResponseDto
                .map(ShopResponseDto::new) // Retorna a loja encontrada como ShopResponseDto
                .orElseThrow(() -> new IllegalArgumentException("Loja não encontrada para o usuário."));
    }

    @Override
    public Page<ShopResponseDto> getAllShops(Pageable pageable) {
        return shopRepository.findAll(pageable)
                .map(shop -> new ShopResponseDto(shop)); // Converte cada loja para ShopResponseDto
    }

    // Valida o CNPJ usando o padrão definido
    @Override
    public boolean isValidCnpj(String cnpj) {
        // Verifica se o CNPJ é nulo ou vazio
        if (cnpj == null || cnpj.isBlank()) {
            return false;
        }
        // Verifica se o CNPJ corresponde ao padrão definido
        return CNPJ_PATTERN.matcher(cnpj).matches();
    }

    @Override
    public void deleteShop(UUID id, User currentUser) {
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Shop not found"));

        if (!shop.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not the owner of this shop");
        }

        shopRepository.delete(shop);
    }

    @Override
    public ShopResponseDto updateShop(UUID id, ShopCreateRequestDto dto, User currentUser) {
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Shop not found."));

        if (!shop.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not the owner of this shop");
        }

        // Valida o CNPJ
        shopRepository.findAll().stream()
            .filter(s -> s.getCnpj().equals(dto.cnpj()) && !s.getId().equals(id))
            .findAny()
            .ifPresent(s -> {
                throw new IllegalArgumentException("CNPJ já cadastrado.");
            });
        if (!isValidCnpj(dto.cnpj())) {
            throw new IllegalArgumentException("CNPJ inválido.");
        }

        shop.setName(dto.name());
        shop.setCnpj(dto.cnpj());
        shop.setDescription(dto.description());

        shopRepository.save(shop);

        return new ShopResponseDto(shop);
    }


}
