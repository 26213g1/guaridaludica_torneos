package com.shop.guaridaludica_torneos.controller;

import com.shop.guaridaludica_torneos.model.Jugador;
import com.shop.guaridaludica_torneos.repository.JugadorRepository;
import com.shop.guaridaludica_torneos.service.JugadorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/jugadores")
public class AdminJugadorController {

    private final JugadorRepository jugadorRepository;
    private final JugadorService jugadorService;

    public AdminJugadorController(JugadorRepository jugadorRepository, JugadorService jugadorService) {
        this.jugadorRepository = jugadorRepository;
        this.jugadorService = jugadorService;
    }

    // Listado de jugadores para el Administrador
    @GetMapping
    public String listarJugadoresAdmin(Model model) {
        model.addAttribute("jugadores", jugadorRepository.findAll());
        model.addAttribute("template", "jugadores/lista"); // Ruta de tu fragmento interno
        return "layout"; // Tu plantilla principal compartida
    }

    // Formulario de edición
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Jugador jugador = jugadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));
        model.addAttribute("jugador", jugador);
        model.addAttribute("template", "admin/jugadores/editar"); // Fragmento interno de edición
        return "layout";
    }

    // Procesar actualización
    @PostMapping("/editar/{id}")
    public String actualizarJugador(@PathVariable Long id,
                                    @ModelAttribute Jugador jugadorForm,
                                    RedirectAttributes redirectAttributes) {
        try {
            jugadorService.actualizarJugadorAdmin(
                    id,
                    jugadorForm.getNombre(),
                    jugadorForm.getApodo(),
                    jugadorForm.getEmail(),
                    jugadorForm.getRol()
            );
            redirectAttributes.addFlashAttribute("mensaje", "Jugador actualizado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
        }
        return "redirect:/admin/jugadores";
    }
}
