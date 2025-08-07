package com.oktech.boasaude.entity;

import java.beans.Transient;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Order")
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
/**
 * Entity that represents an order in the system.
 * Contains information such as the user who placed the order and the items in
 * the order.
 * 
 * @author Arlindo Neto
 * @version 1.0
 */
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY) // Many-to-one relationship with User
    @JoinColumn(name = "user_id", nullable = false) // Foreign key to the User entity
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;
    // Timestamps for creation and last update

    @CreatedDate
    private LocalDateTime createdAt;

    // Timestamp when the user was created
    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * Calculates the total price of the order by summing the total prices of all
     * order items.
     * 
     * @return Total price as BigInteger.
     */
    @Transient
    public BigInteger getTotalPrice() {
        return items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigInteger.ZERO, BigInteger::add);
    }
}
