package com.shop.guaridaludica_torneos.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
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

    // Constructores
    public Participacion() {}

    public Participacion(Torneo torneo, Jugador jugador) {
        this.torneo = torneo;
        this.jugador = jugador;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public Boolean getEsGanador() {
        return esGanador;
    }

    public void setEsGanador(Boolean esGanador) {
        this.esGanador = esGanador;
    }

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }

    public String getEstadoInscripcion() {
        return estadoInscripcion;
    }

    public void setEstadoInscripcion(String estadoInscripcion) {
        this.estadoInscripcion = estadoInscripcion;
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