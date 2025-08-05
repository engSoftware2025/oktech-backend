package com.oktech.boasaude.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oktech.boasaude.dto.CreateProductDto;
import com.oktech.boasaude.entity.Product;
import com.oktech.boasaude.entity.User;

/**
 * ProductService é responsável por gerenciar as operações relacionadas aos produtos.
 * Ele pode incluir funcionalidades como listar produtos, buscar detalhes de um produto específico,
 * criar novos produtos, atualizar informações de produtos existentes, etc.
 * @author João Martins
 * @version 1.0 
 */


public interface ProductService {
    
    Product createProduct(CreateProductDto createProductDto, UUID shopId, User currentUser);

    Product getProductById(UUID id);

    Page<Product> getAllProducts(Pageable pageable);

    Product updateProduct(UUID id, CreateProductDto product, User currentUser);

    void deleteProduct(UUID id, User currentUser);

}
