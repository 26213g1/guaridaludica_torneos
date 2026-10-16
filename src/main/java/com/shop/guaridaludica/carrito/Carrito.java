package com.shop.guaridaludica.carrito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.annotation.SessionScope;

import com.shop.guaridaludica.repository.ProductoRepository;

import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@SessionScope // ¡Magia! Esto hace que cada usuario tenga su carrito independiente
public class Carrito {
    @Autowired
    private ProductoRepository productoRepository;


    private List<CarritoItem> items = new ArrayList<>();

    public void agregarProducto(Long productoId) {
        CarritoItem itemExistente = items.stream().filter(item -> item.getProductoId().equals(productoId)).findFirst()
                .orElse(null);

        if (itemExistente != null) {
            itemExistente.setCantidad(itemExistente.getCantidad() + 1);
        } else {
            productoRepository.findById(productoId).ifPresent(producto -> {
                items.add(new CarritoItem(producto.getId(), producto.getNombre(), producto.getPrecio(), 1));
            });
        }
    }

    public List<CarritoItem> getItems() {
        return items;
    }

    public BigDecimal getTotal() {
        return items.stream().map(item -> item.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void vaciar() {
        items.clear();
        // Si tienes un atributo explícito para el total, recárgalo a cero:
        // this.total = BigDecimal.ZERO;
    }

    public void eliminarProducto(Long productoId) {
        // Buscamos y eliminamos el ítem cuyo producto coincida con el ID recibido
        items.removeIf(item -> item.getProductoId().equals(productoId));
    }

    public void actualizarCantidad(Long productoId, int nuevaCantidad) {
        for (CarritoItem item : items) {
            if (item.getProductoId().equals(productoId)) {
                if (nuevaCantidad > 0) {
                    item.setCantidad(nuevaCantidad);
                } else {
                    // Si ponen 0 o negativo, podemos eliminarlo directamente
                    items.remove(item);
                }
                break;
            }
        }
    }

    @GetMapping
    public String verCarrito(Model model, HttpSession session) {
        // Limpiamos el atributo de sesión por si acaso, pero NO volvemos a sumar nada
        session.removeAttribute("agregarProductoAlIniciar");

        model.addAttribute("items", this.getItems());
        model.addAttribute("total", this.getTotal());
        return "carrito";
    }
}
