package com.shop.guaridaludica_torneos.service;

import com.shop.guaridaludica_torneos.model.Participacion;
import com.shop.guaridaludica_torneos.repository.ParticipacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipacionService {

    private final ParticipacionRepository participacionRepository;

    public ParticipacionService(ParticipacionRepository participacionRepository) {
        this.participacionRepository = participacionRepository;
    }

    public List<Participacion> listarParticipaciones() {
        return participacionRepository.findAll();
    }

    public List<Participacion> listarPorTorneo(Long torneoId) {
        return participacionRepository.findByTorneoId(torneoId);
    }

    public List<Participacion> listarPorJugador(Long jugadorId) {
        return participacionRepository.findByJugadorId(jugadorId);
    }

    public Participacion guardarParticipacion(Participacion participacion) {
        // Validación opcional para evitar duplicados en el mismo torneo
        boolean yaInscrito = participacionRepository.existsByTorneoIdAndJugadorId(
                participacion.getTorneo().getId(),
                participacion.getJugador().getId()
        );

        if (yaInscrito && participacion.getId() == null) {
            throw new IllegalArgumentException("El jugador ya está inscrito en este torneo.");
        }

        return participacionRepository.save(participacion);
    }

    public void eliminarParticipacion(Long id) {
        participacionRepository.deleteById(id);
    }
}