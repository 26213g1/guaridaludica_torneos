package com.shop.guaridaludica_torneos.repository;

import com.shop.guaridaludica_torneos.model.Participacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipacionRepository extends JpaRepository<Participacion, Long> {
    List<Participacion> findByTorneoId(Long torneoId);
    List<Participacion> findByJugadorId(Long jugadorId);
    boolean existsByTorneoIdAndJugadorId(Long torneoId, Long jugadorId);
}