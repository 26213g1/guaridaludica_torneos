package com.shop.guaridaludica.carrito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.shop.guaridaludica.repository.ProductoRepository;

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
}
