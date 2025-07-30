package com.oktech.boasaude.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oktech.boasaude.dto.CreateUserDto;
import com.oktech.boasaude.dto.LoginUserDto;
import com.oktech.boasaude.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * AuthController recebe as requisições de autenticação e registro de usuários.
 * Ele utiliza o UserService para criar usuários e realizar login.
 * 
 * @author Arlindo Neto
 * @version 1.0
 */

@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    /**
     * UserService é injetado para manipular usuários.
     */

    private final UserService userService;

    /**
     * Construtor que recebe o UserService.
     * 
     * @param userService UserService para manipulação de usuários.
     */
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint para registrar um novo usuário.
     * 
     * @param createUserDto DTO com os dados do usuário a ser criado.
     * @return ResponseEntity com mensagem de sucesso.
     */

    @PostMapping("register")
    public ResponseEntity<String> registerUser(@RequestBody CreateUserDto createUserDto) {
        userService.createUser(createUserDto);
        return ResponseEntity.ok("User registered successfully");
    }

    /**
     * Endpoint para login de usuário.
     * 
     * @param loginUserDto DTO com os dados de login do usuário.
     * @return ResponseEntity com mensagem de sucesso.
     */

    @PostMapping("login")
    public ResponseEntity<String> loginUser(@RequestBody LoginUserDto loginUserDto) {
        return ResponseEntity.ok("User logged in successfully");
    }
}
