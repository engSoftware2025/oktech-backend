package com.oktech.boasaude.entity;


/**
 * Enumeração que representa os possíveis status de um pedido.
 * 
 * @author João Martins
 * @version 1.0
 */

public enum OrderStatus {
    PENDING("PENDING"),
    CANCELLED("CANCELLED"),
    COMPLETED("COMPLETED");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
