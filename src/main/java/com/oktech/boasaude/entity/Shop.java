package com.oktech.boasaude.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;


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
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // Identificador único da loja

    private String name; // Nome da loja

    private String description; // Descrição 

   @Column(nullable = false, unique = true)
    private String cnpj; // CNPJ da loja, único e não nulo

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private UUID owner; // ID do Proprietário da loja

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products; // Produtos disponíveis na loja

}
