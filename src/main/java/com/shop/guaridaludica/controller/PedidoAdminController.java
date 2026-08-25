package com.shop.guaridaludica.controller;

import com.shop.guaridaludica.model.Pedido;
import com.shop.guaridaludica.repository.PedidoRepository;
import com.shop.guaridaludica.repository.ProductoRepository;
import com.shop.guaridaludica.model.DetallePedido;
import com.shop.guaridaludica.model.Producto;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;

@Controller
@RequestMapping("/admin/pedidos")
public class PedidoAdminController {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // Inyectamos el mismo tamaño de paginación de administración que usas en productos
    @Value("${shop.pagination.admin-size}")
    private int adminSize;

    // Listar todos los pedidos de la tienda para el admin
    @GetMapping
    public String listarPedidosAdmin(@RequestParam(required = false) String query,
            @RequestParam(required = false) Pedido.EstadoPedido estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, adminSize);

        // Llamamos al repositorio con los filtros ampliados (incluyendo fechas)
        // Nota: Asegúrate de actualizar también tu query/método en PedidoRepository para recibir fechaInicio y fechaFin si usas una consulta personalizada.
        Page<Pedido> pedidosPage = pedidoRepository.filtrarPedidos(estado, query, fechaInicio, fechaFin, pageable);
        //Page<Pedido> pedidosPage = pedidoRepository.filtrarPedidos(estado, query, pageable);
        
        model.addAttribute("pedidos", pedidosPage.getContent());
        model.addAttribute("currentPage", pedidosPage.getNumber());
        model.addAttribute("totalPages", pedidosPage.getTotalPages());
        model.addAttribute("hasPrevious", pedidosPage.hasPrevious());
        model.addAttribute("hasNext", pedidosPage.hasNext());

        // Parámetros para mantener los filtros seleccionados en la vista HTML
        model.addAttribute("query", query);
        model.addAttribute("estadoSeleccionado", estado);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("estadosDisponibles", Pedido.EstadoPedido.values()); // Para poblar el select de estados
        
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

        return "redirect:/admin/pedidos";
    }
}
