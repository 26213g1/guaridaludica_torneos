package com.shop.guaridaludica.repository;

import com.shop.guaridaludica.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByAlias(String alias);

    // Paginación personalizada
    Page<Usuario> findAll(Pageable pageable);
    
    // Si queremos buscar por nombre o email con filtros
    Page<Usuario> findByNombreContainingIgnoreCaseOrEmailContainingIgnoreCase(String nombre, String email, Pageable pageable);
}