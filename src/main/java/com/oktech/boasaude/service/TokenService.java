package com.oktech.boasaude.service;

import java.util.UUID;

public interface TokenService {
    /**
     * Gera um token JWT para o usuário com base no ID do usuário.
     *
     * @param userId o ID do usuário para o qual o token será gerado
     * @return o token JWT gerado
     */
    String generateToken(String name);

    /**
     * Valida o token JWT fornecido.
     *
     * @param token o token JWT a ser validado
     * @return true se o token for válido, false caso contrário
     */
    String validateToken(String token);

    /**
     * Extracts the user ID from the JWT token.
     * 
     * @param token the JWT token from which the user ID will be extracted
     * @return the user ID as UUID, or null if extraction fails
     */
    public UUID getUserIdFromToken(String token);

}
