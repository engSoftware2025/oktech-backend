package com.oktech.boasaude.repository;

import com.oktech.boasaude.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;      
import java.util.Optional;

import java.util.UUID;

/**
 * Repositório para operações CRUD com a entidade Shop.
 * Fornece métodos para buscar lojas por CNPJ, proprietário e nome.
 * 
 * @author Lucas Ouro
 * @version 1.0
 */

public interface ShopRepository extends JpaRepository<Shop, UUID> {
    
    Optional<Shop> findByCnpj(String cnpj); // Busca loja por CNPJ
    
    List<Shop> findByOwner(UUID ownerId); // Busca lojas por ID do proprietário
    
    boolean existsByCnpj(String cnpj); // Verifica se já existe uma loja com o mesmo CNPJ
    
    List<Shop> findAllByNameContainingIgnoreCase(String name); // Busca lojas por nome, ignorando maiúsculas e minúsculas
    
}
