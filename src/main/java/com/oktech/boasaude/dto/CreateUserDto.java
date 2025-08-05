package com.oktech.boasaude.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para criar um novo usuário.
 * Contém os campos necessários para o registro de um usuário.
 * 
 * @author Arlindo Neto
 * @version 1.0
 */

public record CreateUserDto(
        @NotBlank(message = "Name is required") String name,
        @NotBlank(message = "Email is required") String email,
        @NotBlank(message = "CPF is required") String cpf,
        @NotBlank(message = "Phone is required") String phone,
        @NotBlank(message = "Password is required") String password) {
}
