package com.shop.guaridaludica_torneos.repository;

import com.shop.guaridaludica_torneos.model.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JugadorRepository extends JpaRepository<Jugador, Long> {
    Optional<Jugador> findByEmail(String email);
    Optional<Jugador> findByApodo(String apodo);
    // Ejemplo en JugadorRepository
    List<Jugador> findByRol(String rol);
    // O mejor aún, buscar los que NO sean admin:
    List<Jugador> findByRolNot(String rol);
}