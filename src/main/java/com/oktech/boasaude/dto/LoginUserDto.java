package com.oktech.boasaude.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para login de usuário.
 * Contém os campos necessários para a autenticação de um usuário.
 * 
 * @author Arlindo Neto
 * @version 1.0
 */
public record LoginUserDto(
        @NotBlank(message = "Email is required") String email,
        @NotBlank(message = "Password is required") @NotNull(message = "Password is required") String password) {

}
