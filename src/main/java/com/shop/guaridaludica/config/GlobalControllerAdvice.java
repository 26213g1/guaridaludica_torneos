package com.shop.guaridaludica.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.security.Principal;
import java.time.Year;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.util.WebUtils;

import com.shop.guaridaludica.carrito.Carrito;
import com.shop.guaridaludica.model.Usuario;
import com.shop.guaridaludica.repository.UsuarioRepository;

@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    private ConfiguracionApp configuracionApp;
    @Autowired
    private UsuarioRepository  usuarioRepository;

    @Autowired(required = false)
    @Qualifier("contactoConfiguracion")
    private Object contactoConfiguracion;

    @Autowired
    private Carrito carrito;
    
    @ModelAttribute("appConfig")
    public ConfiguracionApp globalConfig() {
        return configuracionApp;
    }

    @ModelAttribute("anioActual")
    public int poblarAnioActual() {
        return Year.now().getValue();
    }

    @ModelAttribute("contactoConfiguracion")
    public Object poblarContacto() {
        return contactoConfiguracion;
    }

    @ModelAttribute
    public void globalAttributes(HttpServletRequest request, Model model) {
        // 1. REVISIÓN DE COOKIE PENDIENTE: Si hay una cookie de producto pendiente 
        // y el carrito de sesión aún no lo tiene, podemos volcarlo o simularlo para la vista.
        Cookie cookie = WebUtils.getCookie(request, "productoPendienteId");
        if (cookie != null && cookie.getValue() != null && !cookie.getValue().isEmpty()) {
            try {
                Long productoId = Long.valueOf(cookie.getValue());
                // Si el carrito está vacío pero hay cookie, aseguramos al menos 1 ítem visualmente
                // o incluso podríamos añadirlo automáticamente al carrito de sesión aquí mismo:
                if (carrito.getItems().isEmpty()) {
                    carrito.agregarProducto(productoId);
                    /* productoRepository.findById(productoId).ifPresent(producto -> {
                        carrito.agregarProducto(producto); // Añade el producto al carrito de sesión de forma transparente
                    }); */
                }
            } catch (Exception e) {
                // Manejar posible error de parseo del ID
            }
        }

        // 2. Sumamos la cantidad de todos los ítems que hay en el carrito de la sesión
        int totalItems = carrito.getItems().stream()
                .mapToInt(item -> item.getCantidad())
                .sum();
        
        model.addAttribute("cantidadCarrito", totalItems);
    }

    @ModelAttribute
    public void addGlobalAttributes(Model model, Principal principal) {
        if (principal != null) {
            Optional<Usuario> usuario = usuarioRepository.findByAlias(principal.getName());
            model.addAttribute("usuario", usuario.get());
        } else {
            model.addAttribute("usuario", null);
        }
    }
}
