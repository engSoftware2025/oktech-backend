package com.oktech.boasaude.entity;

import jakarta.persistence.Transient;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private List<OrderItem> items = new ArrayList<>(); // List of items in the order
    // Timestamps for creation and last update

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

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

    /**
     * Constructor to create an Order with a user.
     * 
     * @param user The user who placed the order.
     */
    public Order(User user) {
        this.user = user;
        this.status = OrderStatus.PENDING; // Default status when creating a new order
    }

    /**
     * Updates the status of the order.
     * 
     * @param status The new status to set for the order.
     */
    public void updateStatus(OrderStatus status) {
        this.status = status;
    }

}
