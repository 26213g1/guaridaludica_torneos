package com.shop.guaridaludica.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.guaridaludica.model.Direccion;
import com.shop.guaridaludica.model.Usuario;

public interface DireccionRepository extends JpaRepository<Direccion, Long> {
    List<Direccion> findByUsuarioId(Long usuarioId);

    Optional<Direccion> findByUsuarioIdAndEsPrincipalTrue(Long usuarioId);

    // Método para comprobar si ya existe una dirección idéntica para ese usuario
    boolean existsByUsuarioAndCalleAndNumeroAndCiudadAndCodigoPostal(
        Usuario usuario, 
        String calle, 
        String numero, 
        String ciudad, 
        String codigoPostal
    );
}
