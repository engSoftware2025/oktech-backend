package com.oktech.boasaude.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * DTO para criar uma nova loja.
 * Contém os campos necessários para o registro de uma loja.
 * 
 * @author Lucas do Ouro
 * @version 1.0
 */

public record ShopCreateRequestDto(
        @NotBlank(message = "O nome da loja é obrigatorio") String name,

        @NotBlank(message = "A descrição da loja é obrigatoria") String description,

        @NotBlank(message = "O CNPJ da loja é obrigatorio") @Pattern(regexp = "^(\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}|\\d{14})$", message = "CNPJ inválido. Deve estar no formato XX.XXX.XXX/XXXX-XX ou XXXXXXXXXXXXXXX") String cnpj) {
}