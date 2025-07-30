package com.oktech.boasaude.service.impl;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.oktech.boasaude.service.TokenService;
import com.oktech.boasaude.service.UserService;

@Service
public class TokenServiceImpl implements TokenService {

    private final String secret = "your-secret";

    private final UserService userService;

    public TokenServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String generateToken(String email) {
        try {
            // Ensure you have a secret key defined somewhere securely
            Algorithm algorithm = Algorithm.HMAC256(secret);

            var user = userService.getUserByEmail(email);

            if (user == null) {
                throw new IllegalArgumentException("User not found with ID: " + email);
            }

            var userId = user.getId(); // Assuming User has a getId() method that returns UUID

            // Generate the token, including the userId as a claim

            String token = JWT.create()
                    .withIssuer("oktech")
                    .withSubject(user.getUsername()) // ou userId.toString(), escolha um só
                    .withClaim("userId", userId.toString())
                    .withClaim("role", user.getAuthorities().toString())
                    .withExpiresAt(getExpirationTime())
                    .withIssuedAt(Instant.now())
                    .sign(algorithm);
            return token;

        } catch (JWTCreationException exception) {
            // Log or handle the exception
            exception.printStackTrace();
            return null; // Or throw an exception if needed
        }
    }

    @Override
    public String validateToken(String token) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            JWT.require(algorithm)
                    .withIssuer("oktech") // Ensure the issuer matches
                    .build()
                    .verify(token); // Verify the token
            return token; // Token is valid
        } catch (Exception e) {
            // Log or handle the exception
            e.printStackTrace();
            return null; // Token is invalid
        }
    }

    /**
     * Extracts the user ID from the token.
     *
     * @param token the JWT token
     * @return the user ID as a UUID, or null if extraction fails
     */
    @Override
    public UUID getUserIdFromToken(String token) {
        try {
            String optionalToken = this.validateToken(token);

            if (optionalToken == null) {
                return null; // Token is invalid
            }

            var decodedJWT = JWT.decode(optionalToken);
            String userIdStr = decodedJWT.getClaim("userId").asString();
            return UUID.fromString(userIdStr); // Convert the userId string to UUID
        } catch (Exception e) {
            // Log or handle the exception
            e.printStackTrace();
            return null; // Or throw an exception if needed
        }
    }

    private Instant getExpirationTime() {
        // Set the expiration time for the token (e.g., 1 hour from now)
        return Instant.now().plusSeconds(3600); // 1 hour
    }

}
