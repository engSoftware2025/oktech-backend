package com.oktech.boasaude.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oktech.boasaude.dto.CreateUserDto;
import com.oktech.boasaude.dto.LoginUserDto;
import com.oktech.boasaude.dto.TokenResponse;
import com.oktech.boasaude.service.TokenService;
import com.oktech.boasaude.service.UserService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

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
    public ResponseEntity<String> registerUser(@RequestBody @Valid CreateUserDto createUserDto) {
    try
    {    
        logger.info("Registering user: {}", createUserDto);
        
        userService.createUser(createUserDto);

        logger.info("User registered successfully: {}", createUserDto.email());
        return ResponseEntity.ok("User registered successfully");
    }
    catch (Exception ex) {
        logger.error("Error registering user: {}", createUserDto.email(), ex);
        return ResponseEntity.status(500).body("Error registering user");
    }
}

@PostMapping("login")
public ResponseEntity<TokenResponse> loginUser(@RequestBody @Valid LoginUserDto loginUserDto) {
    logger.info("User login attempt: {}", loginUserDto.email());
    try {
        var authenticationToken = new UsernamePasswordAuthenticationToken(
            loginUserDto.email(), loginUserDto.password());

        var authentication = manager.authenticate(authenticationToken);

        String token = tokenService.generateToken(authentication.getName());
        return ResponseEntity.ok(new TokenResponse(token));
    } catch (AuthenticationException ex) {
        logger.error("Authentication failed for user: {}", loginUserDto.email(), ex);
        return ResponseEntity.status(401).body(new TokenResponse("Invalid credentials"));
    }
}
}
