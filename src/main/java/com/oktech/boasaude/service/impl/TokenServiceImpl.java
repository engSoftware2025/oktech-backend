package com.oktech.boasaude.service.impl;

import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.oktech.boasaude.service.TokenService;
import com.oktech.boasaude.service.UserService;

@Service
public class TokenServiceImpl implements TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);

    @org.springframework.beans.factory.annotation.Value("${jwt.secret}")
    private String secret;

    private final UserService userService;

    public TokenServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String generateToken(String email) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            var user = userService.getUserByEmail(email);

            if (user == null) {
                throw new IllegalArgumentException("User not found with email: " + email);
            }

            var userId = user.getId();

            String token = JWT.create()
                    .withIssuer("oktech")
                    .withSubject(user.getUsername())
                    .withClaim("userId", userId.toString())
                    .withClaim("role", user.getAuthorities().toString())
                    .withExpiresAt(getExpirationTime())
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException exception) {
            logger.error("Error creating JWT token", exception);
            return null;
        }
    }

    @Override
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            
            JWT.require(algorithm)
                    .withIssuer("oktech")
                    .build()
                    .verify(token);
            
            return token;
        } catch (JWTVerificationException e) {
            logger.error("Error validating JWT token", e);
            return null;
        }
    }

    @Override
    public UUID getUserIdFromToken(String token) {
        try {
            String validatedToken = this.validateToken(token);

            if (validatedToken == null) {
                return null;
            }

            var decodedJWT = JWT.decode(validatedToken);
            String userIdStr = decodedJWT.getClaim("userId").asString();
            return UUID.fromString(userIdStr);
        } catch (Exception e) {
            logger.error("Error extracting userId from JWT token", e);
            return null;
        }
    }

    private Instant getExpirationTime() {
        return Instant.now().plusSeconds(3600); // 1 hour
    }
}
