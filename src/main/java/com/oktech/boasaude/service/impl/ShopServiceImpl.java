package com.oktech.boasaude.service.impl;

import com.oktech.boasaude.dto.ShopCreateRequestDto;
import com.oktech.boasaude.dto.ShopResponseDto;
import com.oktech.boasaude.entity.Shop;
import com.oktech.boasaude.entity.User;
import com.oktech.boasaude.repository.ShopRepository;
import com.oktech.boasaude.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;


/** * Implementação do serviço de loja.
 * Fornece métodos para criar e obter lojas associadas a usuários.
 * @author Lucas do Ouro   
 * @version 1.1 - Corrigido o método createShop para verificar se o usuário já possui uma loja associada e validar o CNPJ.
 * @version 1.0 - Implementação inicial do serviço de loja.
 */

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopRepository shopRepository;

    private static final String CNPJ_REGEX = "^(\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}|\\d{14})$"; // Formato XX.XXX.XXX/XXXX-XX ou XXXXXXXXXXXXXXX
    private static final Pattern CNPJ_PATTERN = Pattern.compile(CNPJ_REGEX); // Regex para validar CNPJ

    
    @Override
    public ShopResponseDto createShop(User user, ShopCreateRequestDto dto) {
        // Verifica se o usuário já possui uma loja associada
        if (shopRepository.findAllByNameContainingIgnoreCase(user.getName()).isEmpty()) {
            throw new IllegalArgumentException("Usuário já possui uma loja associada.");
        }
        // Valida o CNPJ
        if (shopRepository.findByOwnerId(user.getId()).isPresent()) {
            throw new IllegalArgumentException("CNPJ já cadastrado.");
        }
        if (!isValidCnpj(dto.cnpj())) {
            throw new IllegalArgumentException("CNPJ inválido.");
        }

        Shop shop = new Shop(dto, user); // Cria uma nova loja com os dados do DTO e o usuário
        
        Shop savedShop = shopRepository.save(shop); // Salva a loja no repositório
    
        return new ShopResponseDto(savedShop); // Retorna a resposta com os dados da loja salva
    }

    // Obtém a loja associada ao usuário
    @Override
    public ShopResponseDto getShopbyuser(User user) {
        return shopRepository.findByOwnerId(user.getId()) // Se a loja for encontrada, converte para ShopResponseDto
                .map(ShopResponseDto::new) // Retorna a loja encontrada como ShopResponseDto
                .orElseThrow(() -> new IllegalArgumentException("Loja não encontrada para o usuário.")); 
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
}
