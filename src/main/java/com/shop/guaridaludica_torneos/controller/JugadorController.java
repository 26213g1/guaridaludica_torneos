package com.shop.guaridaludica_torneos.controller;

import com.shop.guaridaludica_torneos.model.Jugador;
import com.shop.guaridaludica_torneos.service.JugadorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/jugadores")
public class JugadorController {

    private final JugadorService jugadorService;

    public JugadorController(JugadorService jugadorService) {
        this.jugadorService = jugadorService;
    }

    // Listar todos los jugadores (Vista HTML)
    @GetMapping
    public String listarJugadores(Model model) {
        model.addAttribute("jugadores", jugadorService.listarJugadores());
        return "jugadores/lista"; // Apunta a templates/jugadores/lista.html
    }

    // Mostrar formulario para registrar un nuevo jugador
    @GetMapping("/nuevo")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("jugador", new Jugador());
        return "jugadores/formulario"; // Apunta a templates/jugadores/formulario.html
    }

    // Guardar el jugador (procesar formulario con validaciones)
    @PostMapping
    public String guardarJugador(
            @Valid @ModelAttribute Jugador jugador,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "jugadores/formulario";
        }

        try {
            jugadorService.guardarJugador(jugador);
            redirectAttributes.addFlashAttribute("mensaje", "¡Jugador registrado con éxito!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar el jugador (es posible que el email o el apodo ya existan).");
            return "jugadores/formulario";
        }

        return "redirect:/jugadores";
    }

    // Eliminar un jugador
    @GetMapping("/eliminar/{id}")
    public String eliminarJugador(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            jugadorService.eliminarJugador(id);
            redirectAttributes.addFlashAttribute("mensaje", "Jugador eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar el jugador porque tiene torneos o participaciones asociadas.");
        }
        return "redirect:/jugadores";
    }
}