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
        return "torneos/lista"; // Apunta a una plantilla templates/torneos/lista.html
    }

    // Mostrar formulario para crear un nuevo torneo
    @GetMapping("/nuevo")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("torneo", new Torneo());
        model.addAttribute("juegos", juegoService.listarJuegos()); // Para el desplegable de selección de juegos
        return "torneos/formulario"; // Apunta a templates/torneos/formulario.html
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