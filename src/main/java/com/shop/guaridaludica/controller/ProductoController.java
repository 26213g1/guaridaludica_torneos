package com.shop.guaridaludica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shop.guaridaludica.model.Producto;
import com.shop.guaridaludica.repository.ProductoRepository;

import org.springframework.ui.Model;

@Controller
public class ProductoController {
    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping("/producto/{id}")
    public String verDetalleProducto(@PathVariable Long id, Model model) {
        Producto producto = productoRepository.findById(id).orElse(null);
        
       if (producto != null) {
        // Fuerza la carga de la colección de imágenes
        producto.getImagenes().size(); 
        model.addAttribute("producto", producto);
        return "detalle";
    }
    return "redirect:/";
    }
}
