package com.shop.guaridaludica_torneos.controller;

import com.shop.guaridaludica_torneos.model.Torneo;
import com.shop.guaridaludica_torneos.service.JuegoService;
import com.shop.guaridaludica_torneos.service.TorneoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/torneos")
public class TorneoController {

    private final TorneoService torneoService;
    private final JuegoService juegoService;

    public TorneoController(TorneoService torneoService, JuegoService juegoService) {
        this.torneoService = torneoService;
        this.juegoService = juegoService;
    }

    // Listar todos los torneos (Vista HTML)
    @GetMapping
    public String listarTorneos(Model model) {
        model.addAttribute("torneos", torneoService.listarTorneos());
        model.addAttribute("template", "torneos/lista"); // <-- Le dice al layout qué vista incrustar
        return "layout"; // <-- Nombre de tu archivo layout.html maestro
    }

    // Mostrar formulario para crear un nuevo torneo
    @GetMapping("/nuevo")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("torneo", new Torneo());
        model.addAttribute("juegos", juegoService.listarJuegos()); // Para el desplegable de selección de juegos
        model.addAttribute("template", "torneos/formulario"); // <-- Lo mismo para el formulario
        return "layout";
    }

    // Guardar el torneo (procesar formulario con imagen)
    @PostMapping
    public String guardarTorneo(
            @Valid @ModelAttribute Torneo torneo,
            BindingResult bindingResult,
            @RequestParam(value = "fileImagen", required = false) MultipartFile imagen,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("juegos", juegoService.listarJuegos());
            return "torneos/formulario";
        }

        try {
            torneoService.guardarTorneo(torneo, imagen);
            redirectAttributes.addFlashAttribute("mensaje", "¡Torneo creado con éxito!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error al subir la imagen: " + e.getMessage());
        }

        return "redirect:/torneos";
    }

    // Eliminar un torneo
    @GetMapping("/eliminar/{id}")
    public String eliminarTorneo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        torneoService.eliminarTorneo(id);
        redirectAttributes.addFlashAttribute("mensaje", "Torneo eliminado correctamente.");
        return "redirect:/torneos";
    }
}