package com.shop.guaridaludica_torneos.controller;

import com.shop.guaridaludica_torneos.model.Torneo;
import com.shop.guaridaludica_torneos.service.CloudinaryService;
import com.shop.guaridaludica_torneos.service.JuegoService;
import com.shop.guaridaludica_torneos.service.TorneoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/torneos")
public class TorneoController {
    private static final Logger log = LoggerFactory.getLogger(TorneoController.class);

    private final TorneoService torneoService;
    private final JuegoService juegoService;
    private final CloudinaryService cloudinaryService;

    public TorneoController(TorneoService torneoService, JuegoService juegoService,
            CloudinaryService cloudinaryService) {
        this.torneoService = torneoService;
        this.juegoService = juegoService;
        this.cloudinaryService = cloudinaryService;
    }

    // Listar todos los torneos (Vista HTML)
    @GetMapping
    public String listarTorneos(Model model) {
        model.addAttribute("torneos", torneoService.listarTorneos());
        model.addAttribute("template", "torneos/lista"); // <-- Le dice al layout qué vista incrustar
        return "layout"; // <-- Nombre de tu archivo layout.html maestro
    }

    // Ver detalle de un torneo (Público)
    @GetMapping("/{id}")
    public String verDetalleTorneo(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Torneo torneo = torneoService.obtenerTorneoPorId(id)
                    .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));
            model.addAttribute("torneo", torneo);
            model.addAttribute("template", "torneos/detalle");
            return "layout";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "El torneo seleccionado no existe.");
            return "redirect:/torneos";
        }
    }

    // Mostrar formulario para crear un nuevo torneo
    @GetMapping("/nuevo")
    public String mostrarFormularioCreacion(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        // Comprobación limpia usando el método auxiliar
        String redirect = verificarAdmin(session, redirectAttributes);
        if (redirect != null)
            return redirect;

        model.addAttribute("torneo", new Torneo());
        model.addAttribute("juegos", juegoService.listarJuegos()); // Para el desplegable de selección de juegos
        model.addAttribute("template", "torneos/formulario"); // <-- Lo mismo para el formulario
        return "layout";
    }

    // Mostrar formulario para editar un torneo existente (Solo Admin)
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model, HttpSession session,
            RedirectAttributes redirectAttributes) {
        // Comprobación limpia usando el método auxiliar
        String redirect = verificarAdmin(session, redirectAttributes);
        if (redirect != null)
            return redirect;

        try {
            Torneo torneo = torneoService.obtenerTorneoPorId(id)
                    .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));
            model.addAttribute("torneo", torneo);
            model.addAttribute("juegos", juegoService.listarJuegos());
            model.addAttribute("template", "torneos/formulario"); // Reutilizamos el mismo formulario
            return "layout";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "El torneo a editar no existe.");
            return "redirect:/torneos";
        }
    }

    // Guardar el torneo (procesar formulario con imagen)
    @PostMapping
    public String guardarTorneo(
        @Valid @ModelAttribute Torneo torneo, 
        BindingResult bindingResult,
        @RequestParam(value = "fileImagen", required = false) MultipartFile imagen, 
        Model model,
        HttpSession session, 
        RedirectAttributes redirectAttributes) {

        log.info("--> Intentando guardar un torneo. ID recibido: {}", torneo.getId());
        // Comprobación limpia usando el método auxiliar
        String redirect = verificarAdmin(session, redirectAttributes);
        if (redirect != null)
            return redirect;

        if (bindingResult.hasErrors()) {
            log.warn("--> El formulario tiene errores de validación: {}", bindingResult.getAllErrors());
            model.addAttribute("juegos", juegoService.listarJuegos());
            model.addAttribute("template", "torneos/formulario"); // <-- ¡Esto es lo que mantiene el diseño maestro!
            return "layout"; // <-- Debe devolver "layout", nunca "torneos
        }
        try {
            // Si se ha seleccionado un archivo nuevo para la imagen
            if (imagen != null && !imagen.isEmpty()) {
                log.info("--> Subiendo nueva imagen a Cloudinary...");
                String urlImagen = cloudinaryService.uploadFile(imagen); // Tu lógica de subida a Cloudinary
                torneo.setImagenUrl(urlImagen);
            } else if (torneo.getId() != null) {
                // Si estamos editando (tiene ID) pero NO se subió archivo, conservamos la
                // imagen que ya tenía
                Torneo torneoActual = torneoService.obtenerTorneoPorId(torneo.getId())
                        .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));
                torneo.setImagenUrl(torneoActual.getImagenUrl());
            }

            boolean esNuevo = (torneo.getId() == null);
            torneoService.guardarTorneo(torneo, imagen);
            log.info("--> ¡Torneo guardado con éxito en la BBDD!");
            String mensaje = esNuevo ? "¡Torneo creado con éxito!" : "¡Torneo actualizado con éxito!";
            redirectAttributes.addFlashAttribute("mensaje", mensaje);
        } catch (IOException e) {
            //redirectAttributes.addFlashAttribute("error", "Error al subir la imagen: " + e.getMessage());
            log.error("--> ERROR CRÍTICO al subir la imagen o guardar: {}", e.getMessage(), e);
            model.addAttribute("error", "Error al subir la imagen: " + e.getMessage());
            model.addAttribute("juegos", juegoService.listarJuegos());
            model.addAttribute("template", "torneos/formulario");
            return "layout"; // Devuelve el formulario para que no pierda lo que escribió
        }

        return "redirect:/torneos";
    }

    // Eliminar un torneo
    @GetMapping("/eliminar/{id}")
    public String eliminarTorneo(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        // Comprobación limpia usando el método auxiliar
        String redirect = verificarAdmin(session, redirectAttributes);
        if (redirect != null)
            return redirect;

        torneoService.eliminarTorneo(id);
        redirectAttributes.addFlashAttribute("mensaje", "Torneo eliminado correctamente.");
        return "redirect:/torneos";
    }

    private String verificarAdmin(HttpSession session, RedirectAttributes redirectAttributes) {
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (isAdmin == null || !isAdmin) {
            redirectAttributes.addFlashAttribute("error", "Acceso denegado. Se requiere rol de administrador.");
            return "redirect:/torneos";
        }
        return null; // Retorna null si todo está bien y es admin
    }

}