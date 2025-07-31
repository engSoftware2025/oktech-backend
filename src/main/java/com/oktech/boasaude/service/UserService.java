package com.oktech.boasaude.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oktech.boasaude.dto.CreateUserDto;
import com.oktech.boasaude.entity.User;

/**
 * Interface para o serviço de usuário.
 * Define os métodos para operações relacionadas a usuários.
 * 
 * @author Arlindo Neto
 * @version 1.0
 */
public interface UserService {
    User createUser(CreateUserDto createUserDto);

    User getUserById(UUID id);

    Page<User> getAllUsers(Pageable pageable);

    User updateUser(UUID id, User user);

    void deleteUser(UUID id);

    User getUserByEmail(String email);
}
