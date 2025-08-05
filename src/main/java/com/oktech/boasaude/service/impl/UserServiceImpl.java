package com.oktech.boasaude.service.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.oktech.boasaude.dto.CreateUserDto;

import com.oktech.boasaude.entity.User;
import com.oktech.boasaude.entity.UserRole;
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

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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
            logger.error("Email already exists: {}", createUserDto.email());
            throw new IllegalArgumentException("Email already exists");
        }
        if (userRepository.existsByCpf(createUserDto.cpf())) {
            logger.error("CPF already exists: {}", createUserDto.cpf());
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
        try {
            logger.info("Fetching user by ID: {}", id);

            return userRepository.findById(id).orElse(null);
        } catch (Exception e) {
            logger.error("Error fetching user by ID: {}", id, e);
            throw new IllegalArgumentException("User not found: " + id, e);
        }
    }

    /**
     * Busca todos os usuários com paginação.
     * 
     * @param pageable Objeto Pageable para paginação.
     * @return Uma página de usuários.
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
            logger.info("Updating user with ID: {}", id);
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setCpf(user.getCpf());
            existingUser.setPassword(user.getPassword());
            return userRepository.save(existingUser);
        }
        logger.error("User not found for ID: {}", id);
        throw new IllegalArgumentException("User not found: " + id);
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
        logger.info("Deleting user with ID: {}", id);
        if (existingUser != null) {
            logger.info("User found for ID: {}", id);
            existingUser.setActive(false); // Soft delete
            userRepository.save(existingUser);
        } else {
            logger.error("User not found for ID: {}", id);
            throw new IllegalArgumentException("User not found: " + id);
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
        logger.info("Fetching user by email: {}", email);
        var user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            logger.info("User found: {}", user.get().getEmail());
            return user.get();
        } else {
            logger.warn("User not found for email: {}", email);
            throw new IllegalArgumentException("User not found: " + email);
        }
    }

    /**
     * Atualiza o papel de um usuário.
     * 
     * @param userId   ID do usuário a ser atualizado.
     * @param userRole Novo papel do usuário.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    @Override
    public boolean updateUserRole(UUID userId, UserRole userRole) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            logger.error("User not found with ID: {}", userId);
            throw new IllegalArgumentException("User not found: " + userId);
        }
        user.setRole(userRole);
        userRepository.save(user);
        logger.info("User role updated successfully for ID: {}", userId);
        return true;
    }
}
