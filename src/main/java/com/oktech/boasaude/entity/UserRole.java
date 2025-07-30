package com.oktech.boasaude.entity;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Enum para definir os papéis de usuário no sistema.
 * Cada papel possui uma lista de autoridades associadas.
 * 
 * @author Arlindo Neto
 * @version 1.0
 */
public enum UserRole {
    ADMIN(List.of("ROLE_ADMIN", "ROLE_USER", "ROLE_PRODUCTOR")),
    PRODUCTOR(List.of("ROLE_PRODUCTOR", "ROLE_USER")),
    USER(List.of("ROLE_USER"));

    /** 
     * Lista de autoridades associadas ao papel.
     */
    private final List<String> roles;

    /**
     * Construtor para inicializar o papel com suas autoridades.
     * 
     * @param roles Lista de autoridades associadas ao papel.
     */
    UserRole(List<String> roles) {
        this.roles = roles;
    }

    /**
     * Retorna a lista de autoridades como GrantedAuthority.
     * 
     * @return Lista de GrantedAuthority associadas ao papel.
     */

    public List<GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}