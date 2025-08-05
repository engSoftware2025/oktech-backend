package com.oktech.boasaude.config.security;

import java.io.IOException;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.oktech.boasaude.service.TokenService;
import com.oktech.boasaude.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserAuthenticationFilter.class);

    public UserAuthenticationFilter(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            try {
                UUID userId = tokenService.getUserIdFromToken(tokenJWT);
                // logger.info("Token JWT recuperado: {}", tokenJWT); // Removed to avoid
                // logging sensitive token information
                if (userId == null) {
                    logger.warn("Token inválido ou expirado (userId nulo)");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido ou expirado (userId nulo)");
                    return;
                }

                var user = userService.getUserById(userId);
                logger.debug("User retrieved: {}", user != null ? user.getUsername() : "null");
                if (user == null) {
                    logger.warn("Usuário não encontrado com ID: {}", userId);
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuário não encontrado");
                    return;
                }

                var authentication = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());
                logger.debug("Authenticated user: {}", user.getUsername());

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                logger.error("Erro ao autenticar token", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Erro ao autenticar token");
                return;
            }
        }
        // logger.debug("Continuing filter to next step");
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7).trim();
        }
        return null;
    }

}
