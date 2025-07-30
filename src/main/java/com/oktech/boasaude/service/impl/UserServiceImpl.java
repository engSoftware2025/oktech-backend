package com.oktech.boasaude.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.oktech.boasaude.dto.CreateUserDto;

import com.oktech.boasaude.entity.User;
import com.oktech.boasaude.repository.UserRepository;
import com.oktech.boasaude.service.UserService;

/**
 * Implementação do serviço de usuário.
 * Fornece métodos para operações relacionadas a usuários.
 * 
 * @author Arlindo Neto
 * @version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * Repositório de usuários para operações CRUD.
     */
    private final UserRepository userRepository;
    /**
     * Injetando o repositório de usuários e o codificador de senhas.
     * 
     * @param userRepository  Repositório de usuários para operações CRUD.
     * @param passwordEncoder Codificador de senhas para segurança.
     */
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, @Autowired PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cria um novo usuário com os dados fornecidos.
     * Verifica se o email e o CPF já existem antes de criar o usuário.
     * 
     * @param createUserDto DTO com os dados do usuário a ser criado.
     * @return O usuário criado.
     */
    @Override
    public User createUser(CreateUserDto createUserDto) {
        if (userRepository.existsByEmail(createUserDto.email())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (userRepository.existsByCpf(createUserDto.cpf())) {
            throw new IllegalArgumentException("CPF already exists");
        }
        User user = new User(createUserDto);
        user.setPassword(passwordEncoder.encode(createUserDto.password())); // Encode the password

        return userRepository.save(user);

    }

    /**
     * Busca um usuário pelo ID.
     * 
     * @param id ID do usuário a ser buscado.
     * @return O usuário encontrado ou null se não existir.
     */
    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Busca um usuário pelo email.
     * 
     * @param email Email do usuário a ser buscado.
     * @return O usuário encontrado ou null se não existir.
     */
    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * Atualiza um usuário existente.
     * 
     * @param id   ID do usuário a ser atualizado.
     * @param user Objeto User com os novos dados.
     * @return O usuário atualizado ou null se não existir.
     */
    @Override
    public User updateUser(UUID id, User user) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setCpf(user.getCpf());
            existingUser.setPassword(user.getPassword());
            return userRepository.save(existingUser);
        }
        return null; // or throw an exception
    }

    /**
     * Deleta um usuário pelo ID.
     * Realiza uma exclusão lógica, desativando o usuário.
     * 
     * @param id ID do usuário a ser deletado.
     */

    @Override
    public void deleteUser(UUID id) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setActive(false); // Soft delete
            userRepository.save(existingUser);
        }
    }

    /**
     * Busca um usuário pelo email.
     * 
     * @param email Email do usuário a ser buscado.
     * @return O usuário encontrado ou null se não existir.
     */
    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
