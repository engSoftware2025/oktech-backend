package com.oktech.boasaude.dto;

import java.util.List;

import com.oktech.boasaude.entity.Order;


public record OrderResponseDto(
    String id,
    String status,
    List<OrderItemResponseDto> items
) {
    /**
     * Construtor para criar um ResponseOrderDto a partir de uma lista de ResponseOrderItemDto.
     * @param id O ID do pedido.
     * @param status O status do pedido.
     * @param items A lista de itens do pedido.
     */
    public OrderResponseDto(Order order) {
        this(
            order.getId().toString(),
            order.getStatus().name(),
            order.getItems().stream().map(OrderItemResponseDto::new).toList()
        );
    }
}

