package com.shop.guaridaludica.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alias", nullable = false, unique = true) // 'unique = true' es buena práctica para nicks
    private String alias;
    
    @Column(nullable = true, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    // Podríamos manejar roles con un Set o un Enum (ej. "ROLE_ADMIN", "ROLE_CLIENTE")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "rol")
    private Set<String> roles = new HashSet<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Direccion> direcciones = new ArrayList<>();

    @Column(name = "telefono", length = 20)
    private String telefono;

    @ManyToMany
    @JoinTable(
        name = "usuario_favoritos",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "producto_id"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "producto_id"})
    )
    private Set<Producto> favoritos = new HashSet<>();

    public Set<Producto> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(Set<Producto> favoritos) {
        this.favoritos = favoritos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public List<Direccion> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(List<Direccion> direcciones) {
        this.direcciones = direcciones;
    }

    // Helper para mantener sincronizada la relación
    public void addDireccion(Direccion direccion) {
        direcciones.add(direccion);
        direccion.setUsuario(this);
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    // public boolean tienePedidos() {
    //     return this.pedidos != null && !this.pedidos.isEmpty();
    // }

}