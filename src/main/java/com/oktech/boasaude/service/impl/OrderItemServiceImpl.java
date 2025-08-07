package com.oktech.boasaude.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.oktech.boasaude.entity.Order;
import com.oktech.boasaude.entity.OrderItem;
import com.oktech.boasaude.entity.Product;
import com.oktech.boasaude.repository.OrderItemRepository;
import com.oktech.boasaude.service.OrderItemService;

/**
 * Implementação do serviço de itens de pedido.
 * Fornece métodos para adicionar, atualizar e excluir itens de pedido.
 * @author João Martins
 * @version 1.0
 */

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    
    private final ProductServiceImpl productServiceImpl;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository, ProductServiceImpl productServiceImpl) {
        this.orderItemRepository = orderItemRepository;
        this.productServiceImpl = productServiceImpl;
    }

    /**
     * Adiciona um item de pedido ao pedido especificado.
     * @param order O pedido ao qual o item será adicionado.
     * @param productId O ID do produto a ser adicionado.
     * @param quantity A quantidade do produto a ser adicionada.
     * @return O item de pedido criado e salvo.
     */
    @Override
    public OrderItem addOrderItem(Order order, UUID productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        Product product = productServiceImpl.getProductById(productId);

        OrderItem orderItem = new OrderItem(order, product, quantity);

        return orderItemRepository.save(orderItem);
    }

    /**
     * Obtém os itens de pedido associados a um pedido específico.
     * @param orderId O ID do pedido cujos itens serão recuperados.
     * @return A lista de itens de pedido associados ao pedido.
     */
    @Override
    public List<OrderItem> getOrderItemsByOrderId(UUID orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

        return orderItems;
    }

    @Override
    public OrderItem updateOrderItem(UUID orderItemId, int quantity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateOrderItem'");
    }

    @Override
    public void deleteOrderItem(UUID orderItemId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteOrderItem'");
    }
    
}
