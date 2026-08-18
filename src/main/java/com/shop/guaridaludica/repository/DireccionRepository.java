package com.shop.guaridaludica.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.guaridaludica.model.Direccion;

public interface DireccionRepository extends JpaRepository<Direccion, Long> {
    List<Direccion> findByUsuarioId(Long usuarioId);

    Optional<Direccion> findByUsuarioIdAndEsPrincipalTrue(Long usuarioId);
}
