package com.shop.guaridaludica_torneos.controller;

import com.shop.guaridaludica_torneos.model.Jugador;
import com.shop.guaridaludica_torneos.model.Participacion;
import com.shop.guaridaludica_torneos.service.JugadorService;
import com.shop.guaridaludica_torneos.service.ParticipacionService;

import jakarta.validation.Valid;

import java.util.List;

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
    private final ParticipacionService participacionService;

    public JugadorController(JugadorService jugadorService, ParticipacionService participacionService) {
        this.jugadorService = jugadorService;
        this.participacionService = participacionService;
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

       // CASO B (Opcional): Si hay un usuario logueado normal, pasamos sus datos por si 
        // queremos saludarle en la cabecera del ranking o mostrarle un acceso directo.
        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getName().equals("anonymousUser")) {
            String emailActual = authentication.getName();
            jugadorService.obtenerJugadorPorEmail(emailActual).ifPresent(jugadorLogado -> {
                model.addAttribute("jugador", jugadorLogado);
            });
        }

        // CASO C: Tanto anónimos como usuarios logueados (ROLE_USER) ven el ranking público y el Top 3
        model.addAttribute("ranking", jugadorService.obtenerRankingPublico());
        model.addAttribute("template", "jugadores/ranking_publico");
        return "layout";
    }

    // Ruta exclusiva para que el usuario logueado vea y edite sus propios datos
    @GetMapping("/perfil")
    public String verMiPerfil(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated() 
                || authentication.getName().equals("anonymousUser")) {
            return "redirect:/login";
        }

        String emailActual = authentication.getName();
        Jugador jugadorLogado = jugadorService.obtenerJugadorPorEmail(emailActual)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtenemos la lista de participaciones del jugador logueado
        List<Participacion> participaciones = participacionService.listarPorJugador(jugadorLogado.getId());

        model.addAttribute("jugadorlogado", jugadorLogado);
        model.addAttribute("participaciones", participaciones); // 👈 La pasamos a la vista
        model.addAttribute("template", "jugadores/perfil"); // Vista con el formulario de edición personal
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

    // Endpoint específico para el Ranking Público (Accesible por TODOS, incluido ADMIN)
    @GetMapping("/ranking")
    public String verRankingPublico(Authentication authentication, Model model) {
        // Cargar datos del jugador si está autenticado (para la cabecera / accesos rápidos)
        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getName().equals("anonymousUser")) {
            String emailActual = authentication.getName();
            jugadorService.obtenerJugadorPorEmail(emailActual).ifPresent(jugadorLogado -> {
                model.addAttribute("jugador", jugadorLogado);
            });
        }

        model.addAttribute("ranking", jugadorService.obtenerRankingPublico());
        model.addAttribute("template", "jugadores/ranking_publico");
        return "layout";
    }
}