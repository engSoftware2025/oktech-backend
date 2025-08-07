package com.oktech.boasaude.entity;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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

@Entity(name = "OrderItem")
@Table(name = "order_items")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
/**
 * Entity that represents an item in an order.
 * Contains information such as the product, quantity, and timestamps for
 * creation and last update.
 * 
 * @author Arlindo Neto
 * @version 1.0
 */
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY) // Many-to-one relationship with Order
    @JoinColumn(name = "order_id", nullable = false) // Foreign key to the Order entity
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY) // Many-to-one relationship with Product
    @JoinColumn(name = "product_id", nullable = false) // Foreign key to the Product entity
    private Product product;

    private Integer quantity;

    // Timestamps for creation and last update
    @CreatedDate
    private LocalDateTime createdAt;

    // Timestamp when the user was created
    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * Calculates the total price of the order item based on the product price and
     * quantity.
     * 
     * @return Total price as BigInteger.
     */
    public BigInteger getTotalPrice() {
        return BigInteger.valueOf(product.getPrice().intValue() * quantity);
    }

}
