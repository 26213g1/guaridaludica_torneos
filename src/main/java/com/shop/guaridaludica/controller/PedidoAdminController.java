package com.shop.guaridaludica.controller;

import com.shop.guaridaludica.model.Pedido;
import com.shop.guaridaludica.repository.PedidoRepository;
import com.shop.guaridaludica.repository.ProductoRepository;
import com.shop.guaridaludica.model.DetallePedido;
import com.shop.guaridaludica.model.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/pedidos")
public class PedidoAdminController {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // Listar todos los pedidos de la tienda para el admin
    @GetMapping
    public String listarPedidosAdmin(Model model) {
        List<Pedido> pedidos = pedidoRepository.findAll(); // O ordenados por fecha desc
        model.addAttribute("pedidos", pedidos);
        return "admin/pedidos/listar"; // Vista HTML en templates/admin/pedidos.html
    }

    // Cambiar estado o cancelar pedido desde el admin
    @PostMapping("/actualizar-estado/{id}")
    public String cambiarEstadoPedido(@PathVariable Long id, @RequestParam Pedido.EstadoPedido nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Si el admin decide cancelar un pedido que no estaba cancelado, devolvemos el stock
        if (nuevoEstado == Pedido.EstadoPedido.CANCELADO && pedido.getEstado() != Pedido.EstadoPedido.CANCELADO) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                Producto producto = detalle.getProducto();
                producto.setStock(producto.getStock() + detalle.getCantidad());
                productoRepository.save(producto);
            }
        }

        pedido.setEstado(nuevoEstado);
        pedidoRepository.save(pedido);

        return "redirect:/admin/pedidos/listar";
    }
}
