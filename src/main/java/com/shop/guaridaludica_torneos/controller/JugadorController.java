package com.shop.guaridaludica_torneos.controller;

import com.shop.guaridaludica_torneos.model.Jugador;
import com.shop.guaridaludica_torneos.service.JugadorService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
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

    // Ruta inteligente central: Adapta la vista según el rol o estado de
    // autenticación
    @GetMapping
    public String gestionarAccesoJugadores(Authentication authentication, Model model) {

        // CASO A: Administrador -> Panel de gestión
        if (authentication != null
                && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

            model.addAttribute("jugadores", jugadorService.listarJugadores());
            model.addAttribute("template", "jugadores/lista"); // Indicamos qué vista va dentro del layout
            return "layout"; // O el nombre de tu plantilla maestra general
        }

        // CASO B: Usuario logueado (ROLE_USER) -> Su perfil personal
        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getName().equals("anonymousUser")) {

            String emailActual = authentication.getName();
            Jugador jugadorLogado = jugadorService.obtenerJugadorPorEmail(emailActual)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            model.addAttribute("jugador", jugadorLogado);
            model.addAttribute("template", "jugadores/perfil");
            return "layout";
        }

        // CASO C: Visitante anónimo -> Ranking público
        model.addAttribute("ranking", jugadorService.obtenerRankingPublico());
        model.addAttribute("template", "jugadores/ranking_publico");
        return "layout";
    }

    // Mostrar formulario para registrar un nuevo jugador
    @GetMapping("/nuevo")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("jugador", new Jugador());
        model.addAttribute("template", "jugadores/formulario"); // Apunta a tu vista de formulario
        return "layout"; // Nombre de tu plantilla maestra general
    }

    // Guardar el jugador (procesar formulario con validaciones)
    @PostMapping
    public String guardarJugador(@Valid @ModelAttribute Jugador jugador, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("template", "jugadores/formulario");
            return "layout";
        }

        try {
            // Llamamos al método inteligente y guardamos la contraseña temporal generada
            String passwordTemporal = jugadorService.guardarOActualizarJugadorPorAdmin(jugador);

            redirectAttributes.addFlashAttribute("mensaje",
                    "¡Jugador registrado o actualizado con éxito! Su contraseña temporal de acceso es: <strong>"
                            + passwordTemporal + "</strong> (indícale que la cambie al entrar).");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error al registrar el jugador (es posible que el email o el apodo ya existan).");
            model.addAttribute("template", "jugadores/formulario");
            return "layout";
        }

        return "redirect:/jugadores";
    }

    // El método de eliminar hace un redirect, por lo que se queda como está (no
    // renderiza vista directa)
    @GetMapping("/eliminar/{id}")
    public String eliminarJugador(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            jugadorService.eliminarJugador(id);
            redirectAttributes.addFlashAttribute("mensaje", "Jugador eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "No se puede eliminar el jugador porque tiene torneos o participaciones asociadas.");
        }
        return "redirect:/jugadores";
    }
}