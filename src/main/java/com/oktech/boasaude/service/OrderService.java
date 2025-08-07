package com.oktech.boasaude.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oktech.boasaude.dto.CreateOrderItemDto;
import com.oktech.boasaude.entity.Order;
import com.oktech.boasaude.entity.User;

import java.util.List;
import java.util.UUID;

/**
 * OrderService é responsável por gerenciar as operações relacionadas aos pedidos.
 * Ele pode incluir funcionalidades como criar pedidos, atualizar status de pedidos,
 * buscar detalhes de um pedido específico, listar pedidos de um usuário, etc.
 * 
 * @author João Martins
 * @version 1.0
 */

public interface OrderService {
    Order createOrder(User currentUser, List<CreateOrderItemDto> orderItems); // Cria um novo pedido associado ao usuário);

    Order updateOrderStatus(UUID orderId, String status, User currentUser); // Atualiza o status de um pedido

    Order getOrderById(UUID orderId, User currentUser);

    Page<Order> getOrdersByUserId(Pageable pageable, User currentUser); // Lista os pedidos de um usuário com paginação

    void deleteOrder(UUID orderId, User currentUser);
}

