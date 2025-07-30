package com.oktech.boasaude.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Entidade que representa um usuário do sistema.
 * Contém informações como nome, email, CPF, senha, provedor de autenticação e papel do usuário.
 * Implementa UserDetails para integração com o Spring Security.
 * @author Arlindo Neto
 * @version 1.0
 */

@Entity(name = "User")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // Unique identifier for the user

    private String name; // Full name of the user

    private String email; // Unique email address

    private String cpf; // Brazilian CPF (Cadastro de Pessoas Físicas)

    private String password; // Encrypted password

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider; // Possible values: LOCAL, GOOGLE, FACEBOOK

    private String providerId; // ID from the external provider (if applicable)

    @Enumerated(EnumType.STRING)
    private UserRole role; // Possible values: USER, ADMIN, PRODUCTOR

    private String phone; // Phone number of the user

    private boolean isActive; // Indicates if the user account is active
    // Timestamps for creation and last update
    @CreatedDate
    private LocalDateTime createdAt;
    // Timestamp when the user was created
    @LastModifiedDate
    private LocalDateTime updatedAt;


    /**
     * Construtor para criar um usuário com o papel de USUÁRIO.
     */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.role != null
                ? this.role.getAuthorities()
                : List.of(new SimpleGrantedAuthority("ROLE_USER")); // fallback
    }
    
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
