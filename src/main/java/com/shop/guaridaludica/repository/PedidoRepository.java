package com.shop.guaridaludica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.guaridaludica.model.Pedido;
import com.shop.guaridaludica.model.Usuario;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    long countByUsuarioId(Long usuarioId);
    
    // Método rápido para saber si existen pedidos asociados a un ID de usuario
    boolean existsByUsuarioId(Long usuarioId);

    // Para listar los pedidos del usuario en su panel, del más nuevo al más antiguo
    List<Pedido> findByUsuarioOrderByFechaPedidoDesc(Usuario usuario);
}