package com.shop.guaridaludica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.shop.guaridaludica.dto.RegistroUsuarioDto;
import com.shop.guaridaludica.service.UsuarioService;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuarioDto", new RegistroUsuarioDto());
        return "registro"; // Nombre de la vista HTML
    }

    @PostMapping("/registro")
    public String registrarUsuario(@Valid @ModelAttribute("usuarioDto") RegistroUsuarioDto usuarioDto,
                                   BindingResult result, 
                                   Model model) {
        if (result.hasErrors()) {
            /*// Esto te imprimirá en la consola de Eclipse/IntelliJ qué campo ha fallado y por qué
            result.getFieldErrors().forEach(error -> 
                System.out.println("CAMPO FALLIDO: " + error.getField() + " - MENSAJE: " + error.getDefaultMessage())
            );*/
            return "registro";
        }

        try {
            usuarioService.registrarCliente(usuarioDto);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "registro";
        }

        return "redirect:/login?registrado=true";
    }
}