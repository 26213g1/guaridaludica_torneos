package com.shop.guaridaludica.repository;

import com.shop.guaridaludica.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Spring Data JPA nos ofrece automáticamente métodos como findAll(), save(), findById(), deleteById()
}

