package com.shop.guaridaludica_torneos.controller;

import com.shop.guaridaludica_torneos.model.Juego;
import com.shop.guaridaludica_torneos.service.JuegoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/juegos")
public class JuegoController {

    private final JuegoService juegoService;

    public JuegoController(JuegoService juegoService) {
        this.juegoService = juegoService;
    }

    // Listar todos los juegos (Vista HTML)
    @GetMapping
    public String listarJuegos(Model model) {
        model.addAttribute("juegos", juegoService.listarJuegos());
        return "juegos/lista"; // Apunta a templates/juegos/lista.html
    }

    // Mostrar formulario para crear un nuevo juego
    @GetMapping("/nuevo")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("juego", new Juego());
        return "juegos/formulario"; // Apunta a templates/juegos/formulario.html
    }

    // Guardar el juego (procesar formulario con imagen)
    @PostMapping
    public String guardarJuego(
            @Valid @ModelAttribute Juego juego,
            BindingResult bindingResult,
            @RequestParam(value = "fileImagen", required = false) MultipartFile imagen,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "juegos/formulario";
        }

        try {
            juegoService.guardarJuego(juego, imagen);
            redirectAttributes.addFlashAttribute("mensaje", "¡Juego guardado con éxito!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error al subir la imagen: " + e.getMessage());
        }

        return "redirect:/juegos";
    }

    // Eliminar un juego
    @GetMapping("/eliminar/{id}")
    public String eliminarJuego(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            juegoService.eliminarJuego(id);
            redirectAttributes.addFlashAttribute("mensaje", "Juego eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar el juego porque está asociado a algún torneo.");
        }
        return "redirect:/juegos";
    }
}