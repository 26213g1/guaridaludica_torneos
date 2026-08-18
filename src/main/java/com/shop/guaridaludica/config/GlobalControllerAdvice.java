package com.shop.guaridaludica.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.shop.guaridaludica.carrito.Carrito;

@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    private ConfiguracionApp configuracionApp;

    @Autowired
    private Carrito carrito;
    
    @ModelAttribute("appConfig")
    public ConfiguracionApp globalConfig() {
        return configuracionApp;
    }

    @ModelAttribute
    public void globalAttributes(Model model) {
        // Sumamos la cantidad de todos los items que hay en el carrito de la sesión
        int totalItems = carrito.getItems().stream()
                .mapToInt(item -> item.getCantidad()) // Asumiendo que tu item tiene un método getCantidad()
                .sum();
        
        model.addAttribute("cantidadCarrito", totalItems);
    }
}
