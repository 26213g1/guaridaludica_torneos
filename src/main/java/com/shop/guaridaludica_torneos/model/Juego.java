package com.shop.guaridaludica_torneos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "juegos")
public class Juego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del juego es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, unique = true)
    private String nombre;

    @NotNull(message = "El número mínimo de jugadores es obligatorio")
    @Min(value = 1, message = "Debe haber al menos 1 jugador")
    @Column(name = "min_jugadores")
    private Integer minJugadores;

    @Min(value = 1, message = "El número máximo de jugadores debe ser al menos 1")
    @Column(name = "max_jugadores")
    private Integer maxJugadores;

    @Column(name = "duracion_minutos")
    private Integer duracionMinutos;

    private String categoria;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    // Constructor con parámetros
    public Juego(String nombre, Integer minJugadores, Integer maxJugadores, Integer duracionMinutos, String categoria, String imagenUrl, String descripcion) {
        this.nombre = nombre;
        this.minJugadores = minJugadores;
        this.maxJugadores = maxJugadores;
        this.duracionMinutos = duracionMinutos;
        this.categoria = categoria;
        this.imagenUrl = imagenUrl;
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Juego{id=" + id + ", nombre='" + nombre + "', minJugadores=" + minJugadores
                + ", maxJugadores=" + maxJugadores + ", duracionMinutos=" + duracionMinutos + ", categoria='" + categoria + "'}";
    }
}