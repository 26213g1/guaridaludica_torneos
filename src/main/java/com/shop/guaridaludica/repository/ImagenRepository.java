package com.shop.guaridaludica.repository;

import com.shop.guaridaludica.model.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ImagenRepository extends JpaRepository<Imagen, Long> {
    // Buscar la imagen principal de un producto
    Optional<Imagen> findByProductoIdAndEsPrincipalTrue(Long productoId);
}