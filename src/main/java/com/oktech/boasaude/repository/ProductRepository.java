package com.oktech.boasaude.repository;

import com.oktech.boasaude.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repositório para operações CRUD com a entidade Product.
 * Fornece métodos para buscar produtos por loja, nome e categoria.
 * 
 * @author João Martins
 * @version 1.0
 */

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findAllByShopId(UUID shopId, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Product> findByCategory(String category, Pageable pageable);

    Page<Product> findByShopIdAndNameContainingIgnoreCase(UUID shopId, String name, Pageable pageable);

}
