package com.oktech.boasaude.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.oktech.boasaude.dto.CreateProductDto;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** * Entidade que representa um produto no sistema.
 * Contém informações como nome, descrição, preço, quantidade em estoque e timestamps de criação e atualização.
 * @author João Martins
 * @version 1.0
 */


@Entity(name = "Product")
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY) // Many-to-one relationship with Shop
    @JoinColumn(name = "shop_id", nullable = false) // Foreign key to the Shop entity
    private Shop shop; // A loja a que o produto pertence

    private String name;

    private String description;

    private Integer price;

    private String category;

    private Integer stock;

    // Timestamps for creation and last update
    @CreatedDate
    private LocalDateTime createdAt;

    // Timestamp when the user was created
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Product(CreateProductDto dto, Shop shop) {
        this.shop = shop;
        this.name = dto.name();
        this.description = dto.description();
        this.price = dto.price();
        this.category = dto.category();
        this.stock = dto.stock();
    }
}
