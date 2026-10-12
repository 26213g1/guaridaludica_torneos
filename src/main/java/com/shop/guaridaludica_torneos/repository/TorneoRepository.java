package com.shop.guaridaludica_torneos.repository;

import com.shop.guaridaludica_torneos.model.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TorneoRepository extends JpaRepository<Torneo, Long> {
    List<Torneo> findByEstado(String estado);
    List<Torneo> findByJuegoId(Long juegoId);
}