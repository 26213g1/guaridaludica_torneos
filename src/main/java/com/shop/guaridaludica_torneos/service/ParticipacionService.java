package com.shop.guaridaludica_torneos.service;

import com.shop.guaridaludica_torneos.model.Jugador;
import com.shop.guaridaludica_torneos.model.Participacion;
import com.shop.guaridaludica_torneos.model.Torneo;
import com.shop.guaridaludica_torneos.repository.JugadorRepository;
import com.shop.guaridaludica_torneos.repository.ParticipacionRepository;
import com.shop.guaridaludica_torneos.repository.TorneoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipacionService {

    private final ParticipacionRepository participacionRepository;
    private final TorneoRepository torneoRepository;
    private final JugadorRepository jugadorRepository;

    public ParticipacionService(ParticipacionRepository participacionRepository,
            TorneoRepository torneoRepository,
            JugadorRepository jugadorRepository) {
        this.participacionRepository = participacionRepository;
        this.torneoRepository = torneoRepository;
        this.jugadorRepository = jugadorRepository;
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
        boolean yaInscrito = participacionRepository.existsByTorneoIdAndJugadorId(
                participacion.getTorneo().getId(),
                participacion.getJugador().getId());

        if (yaInscrito && participacion.getId() == null) {
            throw new IllegalArgumentException("El jugador ya está inscrito en este torneo.");
        }

        return participacionRepository.save(participacion);
    }

    public void eliminarParticipacion(Long id) {
        participacionRepository.deleteById(id);
    }

    // 1. Inscribir a un usuario en un torneo controlando el aforo máximo
    @Transactional
    public void inscribirJugador(Long torneoId, Long jugadorId) {
        Torneo torneo = torneoRepository.findById(torneoId)
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));

        // VALIDACIÓN NUEVA: Si el torneo ya no está ABIERTO, no se permite la inscripción
        if (!"ABIERTO".equalsIgnoreCase(torneo.getEstado())) {
            throw new IllegalStateException("No te puedes inscribir. El torneo ya ha comenzado o ha finalizado.");
        }

        Jugador jugador = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));

        boolean yaInscrito = participacionRepository.findByTorneoIdAndJugadorId(torneoId, jugadorId).isPresent();
        if (yaInscrito) {
            throw new IllegalStateException("El jugador " + jugador.getApodo() + " ya está inscrito en este torneo.");
        }

        long inscritosActuales = participacionRepository.countByTorneoId(torneoId);
        if (inscritosActuales >= torneo.getMaxParticipantes()) {
            throw new IllegalStateException("Lo sentimos, el torneo ha alcanzado el límite máximo de participantes.");
        }

        Participacion participacion = new Participacion();
        participacion.setTorneo(torneo);
        participacion.setJugador(jugador);
        participacion.setEstadoInscripcion("INSCRITO");

        participacionRepository.save(participacion);
    }

    // 2. Guardar puntuaciones de forma provisional (sin tocar ELO ni posiciones definitivas)
    @Transactional
    public void guardarPuntuacionesProvisionales(Long torneoId, List<Participacion> resultadosForm) {
        List<Participacion> participaciones = participacionRepository.findByTorneoId(torneoId);

        for (Participacion pDb : participaciones) {
            for (Participacion pForm : resultadosForm) {
                if (pDb.getId().equals(pForm.getId())) {
                    pDb.setPuntuacion(pForm.getPuntuacion());
                }
            }
        }

        participacionRepository.saveAll(participaciones);
    }

    // 3. Procesar resultados definitivos del torneo (Admin finaliza, calcula posiciones, ganador y ELO)
    @Transactional
    public void procesarResultadosTorneo(Long torneoId, List<Participacion> resultadosForm) {
        List<Participacion> participaciones = participacionRepository.findByTorneoId(torneoId);

        for (Participacion pDb : participaciones) {
            for (Participacion pForm : resultadosForm) {
                if (pDb.getId().equals(pForm.getId())) {
                    pDb.setPuntuacion(pForm.getPuntuacion());
                }
            }
        }

        participaciones.sort((p1, p2) -> {
            Integer p1Score = p1.getPuntuacion() != null ? p1.getPuntuacion() : 0;
            Integer p2Score = p2.getPuntuacion() != null ? p2.getPuntuacion() : 0;
            return p2Score.compareTo(p1Score);
        });

        int posicion = 1;
        for (Participacion p : participaciones) {
            p.setPosicion(posicion);

            if (posicion == 1) {
                p.setEsGanador(true);
                Jugador j = p.getJugador();
                j.setPuntosElo(j.getPuntosElo() + 25);
                jugadorRepository.save(j);
            } else {
                p.setEsGanador(false);
            }

            posicion++;
        }

        participacionRepository.saveAll(participaciones);
    }

    public void desinscribirJugadorDeTorneo(Long torneoId, String emailJugador) {
        // Buscamos el torneo y validamos que esté abierto
        Torneo torneo = torneoRepository.findById(torneoId)
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));
                
        if (!"ABIERTO".equals(torneo.getEstado())) {
            throw new RuntimeException("No te puedes desinscribir de un torneo que ya ha comenzado o ha finalizado.");
        }

        // 1. Buscamos la participación en la base de datos
        Optional<Participacion> participacion = participacionRepository.findByTorneoIdAndJugadorEmail(torneoId, emailJugador);

        // 2. Si existe, la borramos
        if (participacion.isPresent()) {
            participacionRepository.deleteById(participacion.get().getId());
        }
    }
}