package com.shop.guaridaludica.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shop.guaridaludica.model.Pedido;
import com.shop.guaridaludica.model.Usuario;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    long countByUsuarioId(Long usuarioId);
    
    // Método rápido para saber si existen pedidos asociados a un ID de usuario
    boolean existsByUsuarioId(Long usuarioId);

    // Para listar los pedidos del usuario en su panel, del más nuevo al más antiguo
    List<Pedido> findByUsuarioOrderByFechaPedidoDesc(Usuario usuario);

    // Consulta unificada para filtrar por estado y/o búsqueda de texto (en el email del usuario, nombre o ID)
    @Query("SELECT p FROM Pedido p WHERE " +
           "(:estado IS NULL OR p.estado = :estado) AND " +
           "(:query IS NULL OR STR(p.id) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.usuario.email) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.usuario.nombre) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Pedido> filtrarPedidos(
            @Param("estado") Pedido.EstadoPedido estado,
            @Param("query") String query,
            Pageable pageable
    );
}