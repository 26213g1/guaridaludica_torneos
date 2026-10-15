package com.shop.guaridaludica_torneos.controller;

import com.shop.guaridaludica_torneos.model.Juego;
import com.shop.guaridaludica_torneos.model.Torneo;
import com.shop.guaridaludica_torneos.service.JuegoService;
import jakarta.servlet.http.HttpSession;
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
        model.addAttribute("template", "juegos/lista");
        return "layout";
    }

    // Ver detalle de un torneo (Público)
    @GetMapping("/{id}")
    public String verDetalleJuego(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Juego juego = juegoService.obtenerJuegoPorId(id)
                    .orElseThrow(() -> new RuntimeException("Juego no encontrado"));
            model.addAttribute("juego", juego);
            model.addAttribute("template", "juegos/detalle");
            return "layout";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "El juego seleccionado no existe.");
            return "redirect:/juegos";
        }
    }

    // Mostrar formulario para crear un nuevo juego
    @GetMapping("/nuevo")
    public String mostrarFormularioCreacion(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String redirect = verificarAdmin(session, redirectAttributes);
        if (redirect != null) return redirect;

        model.addAttribute("juego", new Juego());
        model.addAttribute("template", "juegos/formulario"); // <-- Usando el layout maestro
        return "layout";
    }

    // Mostrar formulario para editar un juego existente
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String redirect = verificarAdmin(session, redirectAttributes);
        if (redirect != null) return redirect;

        Juego juego = juegoService.obtenerJuegoPorId(id)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado"));

        model.addAttribute("juego", juego);
        model.addAttribute("template", "juegos/formulario"); // Reutilizamos la misma vista de formulario
        return "layout";
    }

    // Guardar o actualizar el juego (procesar formulario con imagen)
    @PostMapping
    public String guardarJuego(
            @Valid @ModelAttribute Juego juego,
            BindingResult bindingResult,
            @RequestParam(value = "fileImagen", required = false) MultipartFile imagen,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        String redirect = verificarAdmin(session, redirectAttributes);
        if (redirect != null) return redirect;

        if (bindingResult.hasErrors()) {
            model.addAttribute("template", "juegos/formulario"); // Mantiene el layout si hay errores
            return "layout";
        }

        try {
            boolean esNuevo = (juego.getId() == null);
            juegoService.guardarJuego(juego, imagen);
            String mensaje = esNuevo ? "¡Juego creado con éxito!" : "¡Juego actualizado con éxito!";
            redirectAttributes.addFlashAttribute("mensaje", mensaje);
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error al subir la imagen: " + e.getMessage());
        }

        return "redirect:/juegos";
    }

    // Eliminar un juego
    @GetMapping("/eliminar/{id}")
    public String eliminarJuego(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        String redirect = verificarAdmin(session, redirectAttributes);
        if (redirect != null) return redirect;

        try {
            juegoService.eliminarJuego(id);
            redirectAttributes.addFlashAttribute("mensaje", "Juego eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar el juego porque está asociado a algún torneo.");
        }
        return "redirect:/juegos";
    }

    // Método auxiliar para verificar si es administrador
    private String verificarAdmin(HttpSession session, RedirectAttributes redirectAttributes) {
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (isAdmin == null || !isAdmin) {
            redirectAttributes.addFlashAttribute("error", "Acceso denegado. Se requiere rol de administrador.");
            return "redirect:/juegos";
        }
        return null; // Retorna null si todo está bien y es admin
    }
}