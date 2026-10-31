package com.shop.guaridaludica_torneos.repository;

import com.shop.guaridaludica_torneos.model.Participacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipacionRepository extends JpaRepository<Participacion, Long> {
    boolean existsByTorneoIdAndJugadorId(Long torneoId, Long jugadorId);

    // Buscar todas las participaciones de un torneo específico
    List<Participacion> findByTorneoId(Long torneoId);

    // Buscar las participaciones de un jugador (su historial)
    List<Participacion> findByJugadorId(Long jugadorId);

    // Comprobar si un jugador ya está inscrito en un torneo concreto
    Optional<Participacion> findByTorneoIdAndJugadorId(Long torneoId, Long jugadorId);

    // Contar cuántos inscritos hay en un torneo para validar el límite de plazas
    long countByTorneoId(Long torneoId);

    Optional<Participacion> findByTorneoIdAndJugadorEmail(Long torneoId, String emailJugador);

    @Query("SELECT p FROM Participacion p JOIN p.jugador j WHERE " +
           "LOWER(j.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(j.nombre) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Participacion> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT p FROM Participacion p JOIN p.jugador j WHERE " +
           "p.torneo.id = :torneoId AND (" +
           "LOWER(j.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(j.nombre) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Participacion> findByTorneoIdAndKeyword(@Param("torneoId") Long torneoId, @Param("keyword") String keyword);

}