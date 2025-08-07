package com.oktech.boasaude.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oktech.boasaude.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
    // Additional query methods can be defined here if needed
}
