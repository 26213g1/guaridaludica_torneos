package com.shop.guaridaludica_torneos.repository;

import com.shop.guaridaludica_torneos.model.Juego;
import org.springframework.data.jpa.repository.JpaRepository;


public interface JuegoRepository extends JpaRepository <Juego, Long> {
    // Puedes agregar métodos específicos para la entidad Juego si es necesario
}
