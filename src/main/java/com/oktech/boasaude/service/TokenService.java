package com.oktech.boasaude.service;

import java.util.UUID;

import com.auth0.jwt.interfaces.DecodedJWT;

public interface TokenService {
    /**
     * Gera um token JWT para o usuário com base no ID do usuário.
     *
     * @param userId o ID do usuário para o qual o token será gerado
     * @return o token JWT gerado
     */
    String generateToken(String name);
    
    
    /**
     * Decodes the provided JWT token and returns its decoded representation.
     *
     * @param token the JWT token to decode
     * @return the decoded JWT object
     */
    DecodedJWT getDecodedToken(String token);

    /**
     * Extracts the user ID from the JWT token.
     * 
     * @param token the JWT token from which the user ID will be extracted
     * @return the user ID as UUID, or null if extraction fails
     */
    public UUID getUserIdFromToken(String token);

}
