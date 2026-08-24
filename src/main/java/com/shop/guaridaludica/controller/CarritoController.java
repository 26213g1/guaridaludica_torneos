package com.shop.guaridaludica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shop.guaridaludica.carrito.Carrito;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private Carrito carrito; // Spring inyecta automáticamente el carrito de la sesión

    @GetMapping
    public String verCarrito(Model model, 
                            @CookieValue(name = "productoPendienteId", required = false) String productoIdStr,
                            HttpServletResponse response) {
        
        // Si el usuario acaba de loguearse y traía la cookie con el producto pendiente
        if (productoIdStr != null) {
            try {
                Long productoId = Long.parseLong(productoIdStr);
                carrito.agregarProducto(productoId);
            } catch (NumberFormatException e) {
                // Por si acaso el valor no fuera un número válido
            }

            // Destruimos la cookie para que no se vuelva a procesar en el futuro
            Cookie cookie = new Cookie("productoPendienteId", null);
            cookie.setMaxAge(0); // Caduca inmediatamente
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        model.addAttribute("items", carrito.getItems());
        model.addAttribute("total", carrito.getTotal());
        return "carrito";
    }

    @PostMapping("/agregar")
    public String agregar(@RequestParam Long id, Authentication authentication, HttpServletResponse response) {
        // Comprobamos si el usuario es anónimo (no ha hecho login)
        boolean esAnonimo = (authentication == null || 
                         !authentication.isAuthenticated() || 
                         authentication.getPrincipal().equals("anonymousUser"));

        if (esAnonimo) {
            // Creamos una cookie con el ID del producto pendiente
            Cookie cookie = new Cookie("productoPendienteId", String.valueOf(id));
            cookie.setMaxAge(600); // Dura 10 minutos (suficiente para loguearse o registrarse)
            cookie.setPath("/");   // Disponible para toda la web
            response.addCookie(cookie);

            return "redirect:/login";
        }

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