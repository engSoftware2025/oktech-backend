package com.oktech.boasaude.dto;

import com.oktech.boasaude.entity.User;
import com.oktech.boasaude.entity.UserRole;

/**
 * DTO para resposta de usuário.
 * Contém os campos necessários para a representação de um usuário na API.
 * 
 * @author Arlindo Neto
 * @version 1.0
 */
public record UserResponseDto(
        String id,
        String name,
        String email,
        String cpf,
        UserRole role) {

    public UserResponseDto(User user) {
        this(
                user.getId().toString(),
                user.getName(),
                user.getEmail(),
                user.getCpf(),
                user.getRole());
    }

}
