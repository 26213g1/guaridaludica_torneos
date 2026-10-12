package com.shop.guaridaludica_torneos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
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
    private Integer minJugadores;

    @Min(value = 1, message = "El número máximo de jugadores debe ser al menos 1")
    private Integer maxJugadores;
    private String categoria;

    @Column(name = "imagen_url")
    private String imagenUrl;


    // Constructor vacío obligatorio para JPA
    // public Juego() {
    // }

    // Constructor con parámetros (opcional)
    public Juego(String nombre, Integer minJugadores, Integer maxJugadores, String categoria, String imagenUrl) {
        this.nombre = nombre;
        this.minJugadores = minJugadores;
        this.maxJugadores = maxJugadores;
        this.categoria = categoria;
        this.imagenUrl = imagenUrl;
    }

      public String getImagenUrl() {
        return imagenUrl;
    }

    //getters y setters
    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Integer getMinJugadores() {
        return minJugadores;
    }
    public void setMinJugadores(Integer minJugadores) {
        this.minJugadores = minJugadores;
    }
    public Integer getMaxJugadores() {
        return maxJugadores;
    }
    public void setMaxJugadores(Integer maxJugadores) {
        this.maxJugadores = maxJugadores;
    }
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

}