package com.oktech.boasaude.service;

import java.util.UUID;

import com.oktech.boasaude.dto.LoginUserDto;


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
     * Extrai o ID do usuário do token JWT.
     * * @param token o token JWT do qual o ID do usuário será extraído
     * @return o ID do usuário como UUID, ou null se a extração falhar
     */
    public UUID getUserIdFromToken(String token);
    
    

}
