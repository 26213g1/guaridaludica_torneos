package com.shop.guaridaludica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shop.guaridaludica.carrito.Carrito;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private Carrito carrito; // Spring inyecta automáticamente el carrito de la sesión

    @GetMapping
    public String verCarrito(Model model) {
        model.addAttribute("items", carrito.getItems());
        model.addAttribute("total", carrito.getTotal());
        return "carrito"; // Nombre de tu template HTML
    }

    @PostMapping("/agregar")
    public String agregar(@RequestParam Long id) {
        carrito.agregarProducto(id);
        return "redirect:/carrito";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Long id) {
        // Asumiendo que tu clase Carrito tiene un método eliminarProducto(Long id)
        // Si no lo tienes, puedes crearlo en la clase Carrito
        carrito.eliminarProducto(id); 
        return "redirect:/carrito";
    }

    @PostMapping("/actualizar")
    public String actualizarCantidad(@RequestParam Long id, @RequestParam int cantidad) {
        // Llamamos al método de la clase Carrito en sesión
        carrito.actualizarCantidad(id, cantidad);
        
        // Recargamos la vista del carrito para que se reflejen los nuevos subtotales y el total
        return "redirect:/carrito";
    }
}