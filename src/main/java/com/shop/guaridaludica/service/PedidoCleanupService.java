package com.shop.guaridaludica.service;

import com.shop.guaridaludica.model.DetallePedido;
import com.shop.guaridaludica.model.Pedido;
import com.shop.guaridaludica.model.Producto;
import com.shop.guaridaludica.repository.PedidoRepository;
import com.shop.guaridaludica.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoCleanupService {
    
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // Leemos las horas configuradas en el application.yaml (por defecto 24 si no se encuentra)
    @Value("${shop.order.pending-timeout-hours:24}")
    private int pendingTimeoutHours;

    // Se ejecuta cada hora (en milisegundos)
    @Scheduled(fixedRateString = "${shop.order.cleanup-fixed-rate-ms:3600000}") 
    @Transactional
    public void cancelarPedidosPendientesExpirados() {
        // Por ejemplo, si pasan más de 24 horas sin pagar
        LocalDateTime limiteTiempo = LocalDateTime.now().minusHours(pendingTimeoutHours);

        List<Pedido> pedidosPendientes = pedidoRepository.findByEstadoAndFechaPedidoBefore(
            Pedido.EstadoPedido.PENDIENTE_PAGO, limiteTiempo
        );

        for (Pedido pedido : pedidosPendientes) {
            // 1. Cambiamos el estado a cancelado
            pedido.setEstado(Pedido.EstadoPedido.CANCELADO);
            
            // 2. Devolvemos el stock de cada juego de mesa al inventario
            for (DetallePedido detalle : pedido.getDetalles()) {
                Producto producto = detalle.getProducto();
                producto.setStock(producto.getStock() + detalle.getCantidad());
                productoRepository.save(producto);
            }

            pedidoRepository.save(pedido);
            // Opcional: Aquí podrías llamar a tu emailService para notificarle que el pedido caducó.
        }

        if (!pedidosPendientes.isEmpty()) {
            System.out.println("Se han cancelado " + pedidosPendientes.size() + " pedidos pendientes por caducidad.");
        }
    }
}