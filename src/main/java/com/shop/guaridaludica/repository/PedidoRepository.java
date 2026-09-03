package com.shop.guaridaludica.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    // 🕒 Para buscar pedidos pendientes anteriores a una fecha límite (ideal para la tarea de caducidad)
    List<Pedido> findByEstadoAndFechaPedidoBefore(Pedido.EstadoPedido estado, LocalDateTime fechaLimite);

    // Consulta unificada para filtrar por estado, búsqueda de texto y rango de fechas
    @Query("SELECT p FROM Pedido p WHERE " +
           "(:estado IS NULL OR p.estado = :estado) AND " +
           "(:fechaInicio IS NULL OR CAST(p.fechaPedido AS LocalDate) >= :fechaInicio) AND " +
           "(:fechaFin IS NULL OR CAST(p.fechaPedido AS LocalDate) <= :fechaFin) AND " +
           "(:query IS NULL OR STR(p.id) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.usuario.email) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.usuario.nombre) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Pedido> filtrarPedidos(
            @Param("estado") Pedido.EstadoPedido estado,
            @Param("query") String query,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            Pageable pageable
    );

    // Método mágico de Spring Data JPA para contar por estado
    long countByEstado(Pedido.EstadoPedido estado);
}