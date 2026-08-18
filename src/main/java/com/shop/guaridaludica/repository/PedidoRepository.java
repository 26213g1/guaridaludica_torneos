package com.shop.guaridaludica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.guaridaludica.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    long countByUsuarioId(Long usuarioId);
    
    // Método rápido para saber si existen pedidos asociados a un ID de usuario
    boolean existsByUsuarioId(Long usuarioId);
}