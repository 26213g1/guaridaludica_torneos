package com.shop.guaridaludica_torneos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "participaciones")
public class Participacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Relación con Torneo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "torneo_id", nullable = false)
    private Torneo torneo;

    // Relación con Jugador
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jugador_id", nullable = false)
    private Jugador jugador;

    @Column(name = "puntuacion")
    private Integer puntuacion = 0;

    @Column(name = "es_ganador")
    private Boolean esGanador = false;

    @Column(name = "posicion")
    private Integer posicion;

    @Column(name = "estado_inscripcion")
    private String estadoInscripcion = "INSCRITO"; // Ej: INSCRITO, CONFIRMADO, CANCELADO

    public Participacion(Torneo torneo, Jugador jugador) {
        this.torneo = torneo;
        this.jugador = jugador;
    }

    @Override
    public String toString() {
        return "Participacion{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", torneoId=" + (torneo != null ? torneo.getId() : null) +
                ", jugadorId=" + (jugador != null ? jugador.getId() : null) +
                ", puntuacion=" + puntuacion +
                ", esGanador=" + esGanador +
                ", posicion=" + posicion +
                ", estadoInscripcion='" + estadoInscripcion + '\'' +
                '}';
    }
}