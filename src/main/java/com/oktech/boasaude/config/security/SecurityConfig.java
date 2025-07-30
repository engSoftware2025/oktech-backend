package com.oktech.boasaude.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author Arlindo Neto
 *         Configuração de segurança da aplicação.
 *         Define as regras de autenticação e autorização.
 * 
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /**
     * Configura o filtro de segurança HTTP.
     * Permite acesso a endpoints de autenticação e exige autenticação para outros
     * endpoints.
     *
     * @param http a configuração de segurança HTTP
     * @return o filtro de segurança configurado
     * @throws Exception se ocorrer um erro ao configurar a segurança
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // Configurações de segurança
                                                                                         // HTTP
        http
                .cors(Customizer.withDefaults()) // se precisar
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v1/auth/**", "/actuator/**").permitAll() // Permite acesso a endpoints de
                                                                                    // autenticação e do Actuator
                        .requestMatchers("/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/v1/produtor/**").hasAnyRole("PRODUCTOR", "ADMIN")
                        .anyRequest().authenticated());// Exige autenticação para qualquer outra requisição
        return http.build();
    }

    /**
     * Cria um bean de PasswordEncoder para codificação de senhas.
     * Utiliza BCryptPasswordEncoder para segurança.
     *
     * @return o PasswordEncoder configurado
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Usando BCrypt para codificação de senhas
    }
}
