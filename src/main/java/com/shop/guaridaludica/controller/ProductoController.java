package com.shop.guaridaludica.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.shop.guaridaludica.config.ConfiguracionApp;
import com.shop.guaridaludica.model.Producto;
import com.shop.guaridaludica.model.Usuario;
import com.shop.guaridaludica.repository.ProductoRepository;
import com.shop.guaridaludica.repository.UsuarioRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import org.springframework.security.core.Authentication;

@Controller
public class ProductoController {
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private ConfiguracionApp configuracionApp;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/producto/{id}")
    public String verDetalleProducto(@PathVariable Long id, Model model) {
        Producto producto = productoRepository.findById(id).orElse(null);
        
       if (producto != null) {
        // Fuerza la carga de la colección de imágenes
        producto.getImagenes().size(); 
        model.addAttribute("producto", producto);
        model.addAttribute("diasUltimasUnidades", configuracionApp.getMarcarUltimasUnidades());

        return "detalle";
    }
    return "redirect:/";
    }

    @PostMapping("/favorito/toggle/{id}")
    public String toggleFavorito(@PathVariable Long id, Authentication authentication, HttpServletRequest request) {
        String alias = authentication.getName();
        
        // Si no está autenticado, lo mandamos al login
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login"; 
        }

        // 1. Obtener el usuario autenticado actual
        Usuario usuario = usuarioRepository.findByAlias(alias).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));;
        
        // 2. Buscar el producto                                                           
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        // 3. Comprobar si ya está en favoritos y alternar (Toggle)
        if (usuario.getFavoritos().contains(producto)) {
            usuario.getFavoritos().remove(producto); // Lo quita si ya lo tenía
        } else {
            usuario.getFavoritos().add(producto);    // Lo añade si no lo tenía
        }
        
        // 4. Guardar el usuario (JPA actualiza la tabla intermedia solo)
        usuarioRepository.save(usuario);
        
        // 5. Redirigir de vuelta a la página donde estaba el usuario
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}
