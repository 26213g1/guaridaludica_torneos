package com.shop.guaridaludica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.guaridaludica.model.Pedido;
import com.shop.guaridaludica.model.Producto;
import com.shop.guaridaludica.repository.DetallePedidoRepository;
import com.shop.guaridaludica.repository.ProductoRepository;

@Service
public class PedidoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Transactional // Vital para asegurar que si falta stock, todo se revierta
    public void descontarStock(Pedido pedido) {
        // Recorremos los detalles del pedido guardados en la BD
        for (var detalle : detallePedidoRepository.findByPedido(pedido)) {
            Producto producto = detalle.getProducto();
            
            // 1. Validar stock
            if (producto.getStock() < detalle.getCantidad()) {
                throw new RuntimeException("Lo sentimos, ya no hay suficiente stock para el juego: " + producto.getNombre());
            }
            
            // 2. Restar stock
            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepository.save(producto);
        }
    }
}