package com.shop.guaridaludica_torneos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;

@Entity
@Table(name = "jugadores")
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @NotBlank(message = "El nombre no puede estar en blanco")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El apodo no puede estar en blanco")
    @Size(max = 50, message = "El apodo no puede superar los 50 caracteres")
    @Column(nullable = false, unique = true)
    private String apodo;

    @NotBlank(message = "El email no puede estar en blanco")
    @Email(message = "Debe ser un email válido")
    @Column(nullable = false, unique = true)
    private String email;

    // NUEVO: Campo para la contraseña (cifrada con BCrypt)
    @NotBlank(message = "La contraseña es obligatoria")
    @Column(nullable = false)
    private String password;

    // NUEVO: Campo para el rol (ej: "ROLE_ADMIN", "ROLE_USER")
    @Column(nullable = false)
    private String rol = "ROLE_USER";

    @Column(name = "puntos_elo")
    private Integer puntosElo = 1000;

    // Constructores
    public Jugador() {}

    public Jugador(String nombre, String apodo, String email, String password, String rol, Integer puntosElo) {
        this.nombre = nombre;
        this.apodo = apodo;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.puntosElo = puntosElo;
    }

    // --- GETTERS Y SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApodo() { return apodo; }
    public void setApodo(String apodo) { this.apodo = apodo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public Integer getPuntosElo() { return puntosElo; }
    public void setPuntosElo(Integer puntosElo) { this.puntosElo = puntosElo; }

    // toString() devuelve el texto con el que se imprime el objeto.
    // Sin sobrescribirlo, println() mostraría algo ilegible como com.shop.guaridaludica_torneos.model.Juego.
    @Override
    public String toString() {
        return "Juego{id=" + id + ", nombre='" + nombre + "', Apodo='" + apodo
                + "', Emails=" + email + ", Puntos Elo='" + puntosElo + "'}";
    }
}