package com.oktech.boasaude.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.oktech.boasaude.dto.CreateUserDto;
import com.oktech.boasaude.entity.User;
import com.oktech.boasaude.repository.UserRepository;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testCreateUserWithSuccess() {
        CreateUserDto createUserDto = new CreateUserDto(
                "John Doe",
                "test@test.com",
                "12345678901",
                "1234567890",
                "password123");

        // Mockando para dizer que email e cpf ainda não existem
        Mockito.when(userRepository.existsByEmail(createUserDto.email())).thenReturn(false);
        Mockito.when(userRepository.existsByCpf(createUserDto.cpf())).thenReturn(false);

        // Mock para o save, simulando que o banco vai retornar o usuário salvo
        User savedUser = new User(createUserDto);
        savedUser.setPassword("encodedPassword"); // Simule a senha codificada

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(savedUser);

        User result = userService.createUser(createUserDto);

        assertNotNull(result);
        assertEquals(createUserDto.name(), result.getName());
        assertEquals(createUserDto.email(), result.getEmail());
        assertEquals(createUserDto.cpf(), result.getCpf());
        assertEquals(createUserDto.phone(), result.getPhone());
        assertNotNull(result.getPassword());
    }

    @Test
    void testCreateUserWithExistingEmail() {
        CreateUserDto createUserDto = new CreateUserDto(
                "Jane Doe",
                "test@test.com",
                "12345678901",
                "1234567890",
                "password123");

        Mockito.when(userRepository.existsByEmail(createUserDto.email())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(createUserDto);
        });

        assertEquals("Email already exists", exception.getMessage());

        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
    }

    @Test
    void testCreateUserWithExistingCpf() {
        CreateUserDto createUserDto = new CreateUserDto(
                "Jane Doe",
                "test@test.com",
                "12345678901",
                "1234567890",
                "password123");

        Mockito.when(userRepository.existsByEmail(createUserDto.email())).thenReturn(false);
        Mockito.when(userRepository.existsByCpf(createUserDto.cpf())).thenReturn(true);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(createUserDto);
        });

        assertEquals("CPF already exists", exception.getMessage());
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
    }

}
