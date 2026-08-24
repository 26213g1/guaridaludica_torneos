package com.shop.guaridaludica.controller;

import com.shop.guaridaludica.model.Direccion;
import com.shop.guaridaludica.model.Usuario;
import com.shop.guaridaludica.repository.DireccionRepository;
import com.shop.guaridaludica.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DireccionRepository direccionRepository; // Asegúrate de tener este repositorio creado

    // 1. Ver el panel de la cuenta y su libreta de direcciones
    @GetMapping
    public String verPerfil(Model model, Authentication authentication) {
        String alias = authentication.getName(); // El email del usuario logueado
        Usuario usuario = usuarioRepository.findByAlias(alias)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        model.addAttribute("usuario", usuario);
        model.addAttribute("nuevaDireccion", new Direccion()); // Objeto vacio para el formulario de añadir
        return "perfil/cuenta"; // Ruta de la vista HTML
    }

    // 2. Guardar una nueva dirección en la agenda del usuario
    @PostMapping("/direcciones/guardar")
    public String guardarDireccion(@ModelAttribute Direccion direccion, Authentication authentication) {
        String alias = authentication.getName();
        Usuario usuario = usuarioRepository.findByAlias(alias)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Usamos tu método helper o asociamos la dirección al usuario
        usuario.addDireccion(direccion);
        usuarioRepository.save(usuario); // Gracias al CascadeType.ALL, guardará la dirección automáticamente

        return "redirect:/perfil";
    }

    // 3. Eliminar una dirección de la agenda
    @PostMapping("/direcciones/eliminar")
    public String eliminarDireccion(@RequestParam Long id, Authentication authentication) {
        // Validamos opcionalmente que la dirección pertenezca al usuario logueado por seguridad
        direccionRepository.findById(id).ifPresent(direccion -> {
            if (direccion.getUsuario().getEmail().equals(authentication.getName())) {
                direccionRepository.delete(direccion);
            }
        });

        return "redirect:/perfil";
    }

    // 4. Actualizar datos personales del usuario
    @PostMapping("/actualizar-datos")
    public String actualizarDatos(@ModelAttribute Usuario usuarioForm, Authentication authentication) {
        String aliasActual = authentication.getName();
        Usuario usuario = usuarioRepository.findByAlias(aliasActual)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizamos los campos permitidos
        usuario.setNombre(usuarioForm.getNombre());
        usuario.setTelefono(usuarioForm.getTelefono());

        usuarioRepository.save(usuario);
        
        // Nota: Si cambias el alias, es posible que tengas que actualizar el contexto de seguridad 
        // para que authentication.getName() no falle en la siguiente recarga, 
        // o puedes evitar que cambien el alias y permitir solo nombre, teléfono y email.

        return "redirect:/perfil";
    }
}