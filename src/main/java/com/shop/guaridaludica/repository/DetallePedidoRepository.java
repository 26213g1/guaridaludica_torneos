package com.shop.guaridaludica.repository;

import com.shop.guaridaludica.model.DetallePedido;
import com.shop.guaridaludica.model.Pedido;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    // Spring Data JPA implementará automáticamente los métodos save, findAll, findById, etc.
    List<DetallePedido> findByPedido(Pedido pedido);
}