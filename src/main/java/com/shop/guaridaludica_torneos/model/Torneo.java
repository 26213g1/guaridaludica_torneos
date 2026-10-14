package com.shop.guaridaludica_torneos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "torneos")
public class Torneo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del torneo es obligatorio")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @NotNull(message = "La fecha del torneo es obligatoria")
    @Future(message = "La fecha del torneo debe ser en el futuro")
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime fecha;

    @NotNull(message = "Debe asociarse un juego al torneo")
    @ManyToOne
    @JoinColumn(name = "juego_id", nullable = false)
    private Juego juego;

    @NotNull(message = "El número máximo de participantes es obligatorio")
    @Min(value = 2, message = "Un torneo debe permitir al menos 2 participantes")
    @Column(name = "max_participantes", nullable = false)
    private Integer maxParticipantes;

    @Column(nullable = false)
    private String estado = "ABIERTO"; // ABIERTO, EN_CURSO, FINALIZADO, CANCELADO

    @Min(value = 1, message = "La duración en minutos debe ser al menos 1")
    @Column(name = "duracion_real_minutos")
    private Integer duracionRealMinutos;

    @Column(columnDefinition = "TEXT")
    private String comentarios;

    @Column(name = "imagen_url")
    private String imagenUrl;

    // Constructor vacío obligatorio para JPA
    public Torneo() {
    }

    public Torneo(String nombre, String descripcion, LocalDateTime fecha, Juego juego, Integer maxParticipantes, String estado, Integer duracionRealMinutos, String comentarios, String imagenUrl) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.juego = juego;
        this.maxParticipantes = maxParticipantes;
        if (estado != null) {
            this.estado = estado;
        }
        this.duracionRealMinutos = duracionRealMinutos;
        this.comentarios = comentarios;
        this.imagenUrl = imagenUrl;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Juego getJuego() { return juego; }
    public void setJuego(Juego juego) { this.juego = juego; }

    public Integer getMaxParticipantes() { return maxParticipantes; }
    public void setMaxParticipantes(Integer maxParticipantes) { this.maxParticipantes = maxParticipantes; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Integer getDuracionRealMinutos() { return duracionRealMinutos; }
    public void setDuracionRealMinutos(Integer duracionRealMinutos) { this.duracionRealMinutos = duracionRealMinutos; }

    public String getComentarios() { return comentarios; }
    public void setComentarios(String comentarios) { this.comentarios = comentarios; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    // toString() devuelve el texto con el que se imprime el objeto.
    // Sin sobrescribirlo, println() mostraría algo ilegible como com.shop.guaridaludica_torneos.model.Juego.
    @Override
    public String toString() {
        return "Torneo{id=" + id + ", nombre='" + nombre + "', Descripcion='" + descripcion
                + "', Fecha=" + fecha + ", Juego='" + juego
                + "', Max Participantes=" + maxParticipantes + ", Estado='" + estado
                + "', Duracion real minutos=" + duracionRealMinutos + ", Comentarios='" + comentarios+ "'}";


    }
}