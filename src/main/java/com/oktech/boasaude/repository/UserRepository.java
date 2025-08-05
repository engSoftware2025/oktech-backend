package com.oktech.boasaude.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oktech.boasaude.entity.User;
import com.oktech.boasaude.entity.UserRole;

/**
 * Repositório para operações CRUD com a entidade User.
 * Fornece métodos para buscar usuários por email, CPF e papel.
 * 
 * @author Arlindo Neto
 * @version 1.0
 */
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByCpf(String cpf);

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    List<User> findAllByRole(UserRole role);

}
