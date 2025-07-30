package com.oktech.boasaude.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oktech.boasaude.dto.CreateUserDto;
import com.oktech.boasaude.dto.LoginUserDto;
import com.oktech.boasaude.service.TokenService;
import com.oktech.boasaude.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
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

    private final AuthenticationManager manager;

    private final TokenService tokenService;
    /**
     * Construtor que recebe o UserService.
     * 
     * @param userService UserService para manipulação de usuários.
     */
    public AuthController(UserService userService, AuthenticationManager manager, TokenService tokenService) {
        this.userService = userService;
        this.manager = manager;
        this.tokenService = tokenService;
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

@PostMapping("login")
public ResponseEntity<String> loginUser(@RequestBody @Valid LoginUserDto loginUserDto) {
    try {
        var authenticationToken = new UsernamePasswordAuthenticationToken(
            loginUserDto.email(), loginUserDto.password());

        var authentication = manager.authenticate(authenticationToken);

        String token = tokenService.generateToken(authentication.getName());
        return ResponseEntity.ok(token);
    } catch (AuthenticationException ex) {
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}
}
