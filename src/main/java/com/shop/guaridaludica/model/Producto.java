package com.shop.guaridaludica.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;

/**
 * Producto
 */
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer stock;

    /*@OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY) //Nota: orphanRemoval = true es clave: si borras un producto, se borrarán sus imágenes asociadas automáticamente.
    private List<Imagen> imagenes;*/

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Imagen> imagenes = new ArrayList<>();

    @Column(name = "min_jugadores")
    private Integer minJugadores;

    @Column(name = "max_jugadores")
    private Integer maxJugadores;

    @ManyToMany
    @JoinTable(
        name = "producto_categoria",
        joinColumns = @JoinColumn(name = "producto_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<Categoria> categorias = new HashSet<>();

    // Constructores, Getters y Setters
    public Producto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Integer getMinJugadores() { return minJugadores; }
    public void setMinJugadores(Integer minJugadores) { this.minJugadores = minJugadores; }

    public Integer getMaxJugadores() { return maxJugadores; }
    public void setMaxJugadores(Integer maxJugadores) { this.maxJugadores = maxJugadores; }

    public Set<Categoria> getCategorias() { return categorias; }
    public void setCategorias(Set<Categoria> categorias) { this.categorias = categorias; }

    public List<Imagen> getImagenes() { return imagenes; }
    public void setImagenes(List<Imagen> imagenes) { this.imagenes = imagenes; }

    // Método auxiliar para obtener la URL de la imagen principal directamente en las vistas
    public String getImagenPrincipalUrl() {
        if (imagenes != null && !imagenes.isEmpty()) {
            for (Imagen img : imagenes) {
                if (img.isEsPrincipal()) {
                    return img.getUrl();
                }
            }
            // Si ninguna está marcada como principal por lo que sea, devolvemos la primera de la lista
            return imagenes.get(0).getUrl();
        }
        return "/images/default.jpg"; // Imagen por defecto si no hay ninguna
    }

    // Método auxiliar para devolver las imágenes ordenadas con la principal en la primera posición
    public List<Imagen> getImagenesOrdenadas() {
        if (imagenes == null) {
            return new ArrayList<>();
        }
        List<Imagen> ordenadas = new ArrayList<>(imagenes);
        ordenadas.sort((a, b) -> Boolean.compare(b.isEsPrincipal(), a.isEsPrincipal()));
        return ordenadas;
    }
    
    // Método helper obligatorio para asociar imágenes correctamente en memoria
    public void agregarImagen(Imagen imagen) {
        imagenes.add(imagen);
        imagen.setProducto(this);
    }
}
