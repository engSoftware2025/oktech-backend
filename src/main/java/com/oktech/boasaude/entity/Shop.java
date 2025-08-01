package com.oktech.boasaude.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.oktech.boasaude.dto.ShopCreateRequestDto;


/**
 * Entidade que representa uma loja do sistema.
 * Contém informações como nome, descrição, CNPJ e proprietário.
 * A loja pode ter vários produtos associados a ela.
 * Implementa UserDetails para integração com o Spring Security.
 * @author Lucas Ouro
 * @version 1.0
 */

@Entity
@Table(name = "shops")
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
@Setter
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // Identificador único da loja

    private String name; // Nome da loja

    private String description; // Descrição 

    @Column(nullable = true, unique = true)
    private String cnpj; // CNPJ da loja, único e não nulo

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner; // ID do Proprietário da loja

    // Timestamps for creation and last update
    @CreatedDate
    private LocalDateTime createdAt;
    // Timestamp when the user was created
    @LastModifiedDate
    private LocalDateTime updatedAt;


    public Shop(ShopCreateRequestDto dto, User owner) {
        this.name = dto.name();
        this.description = dto.description();
        this.cnpj = dto.cnpj();
        this.owner = owner;
    }
}
