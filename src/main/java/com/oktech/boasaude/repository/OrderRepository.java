package com.oktech.boasaude.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oktech.boasaude.entity.Order;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    // Additional query methods can be defined here if needed
    
}
