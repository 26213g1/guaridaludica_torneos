package com.shop.guaridaludica.model;

import jakarta.persistence.*;

@Entity
@Table(name = "imagenes")
public class Imagen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url; // Ejemplo: "/images/productos/dado1.jpg"

    @Column(nullable = false)
    private boolean esPrincipal = false; // Nuevo campo para marcar la foto de portada

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    // Constructores vacíos
    public Imagen() {}

    public Imagen(String url, Producto producto) {
        this.url = url;
        this.producto = producto;
    }

    // Getters y Setters
    public boolean isEsPrincipal() {
        return esPrincipal;
    }

    public void setEsPrincipal(boolean esPrincipal) {
        this.esPrincipal = esPrincipal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

}