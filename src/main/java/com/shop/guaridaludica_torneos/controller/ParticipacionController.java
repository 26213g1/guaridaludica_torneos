package com.shop.guaridaludica_torneos.controller;

import com.shop.guaridaludica_torneos.model.Jugador;
import com.shop.guaridaludica_torneos.model.Participacion;
import com.shop.guaridaludica_torneos.model.Torneo;
import com.shop.guaridaludica_torneos.service.JugadorService;
import com.shop.guaridaludica_torneos.service.ParticipacionService;
import com.shop.guaridaludica_torneos.service.TorneoService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/participaciones")
public class ParticipacionController {

    private final ParticipacionService participacionService;
    private final JugadorService jugadorService;
    private final TorneoService torneoService;

    public ParticipacionController(ParticipacionService participacionService,
            JugadorService jugadorService,
            TorneoService torneoService) {
        this.participacionService = participacionService;
        this.jugadorService = jugadorService;
        this.torneoService = torneoService;
    }

    // 1. Inscribir al usuario logueado en un torneo (con control de aforo)
    @GetMapping("/inscribir/{torneoId}")
    public String inscribirseTorneo(@PathVariable Long torneoId,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getName().equals("anonymousUser")) {
            return "redirect:/login";
        }

        try {
            String email = authentication.getName();
            Jugador jugador = jugadorService.obtenerJugadorPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));

            participacionService.inscribirJugador(torneoId, jugador.getId());
            redirectAttributes.addFlashAttribute("mensaje", "¡Te has inscrito al torneo correctamente!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/torneos/" + torneoId;
    }

    // 2. Vista de Admin: Ver participantes de un torneo e introducir puntuaciones
    @GetMapping("/torneo/{torneoId}")
    public String gestionarParticipantesTorneo(@PathVariable Long torneoId, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            Torneo torneo = torneoService.obtenerTorneoPorId(torneoId)
                    .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));

            List<Participacion> participaciones = participacionService.listarPorTorneo(torneoId);

            WrapperParticipaciones wrapper = new WrapperParticipaciones();
            wrapper.setParticipaciones(participaciones);

            model.addAttribute("torneo", torneo);
            model.addAttribute("torneoId", torneoId);
            model.addAttribute("wrapper", wrapper);
            model.addAttribute("template", "participaciones/gestion_torneo");
            return "layout";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "El torneo no existe.");
            return "redirect:/torneos";
        }
    }

    // 3. Guardar puntuaciones provisionales (mientras el torneo está EN_CURSO)
    @PostMapping("/torneo/{torneoId}/guardar-provisionales")
    public String guardarPuntuacionesProvisionales(@PathVariable Long torneoId,
            @ModelAttribute WrapperParticipaciones wrapper,
            RedirectAttributes redirectAttributes) {
        try {
            participacionService.guardarPuntuacionesProvisionales(torneoId, wrapper.getParticipaciones());
            redirectAttributes.addFlashAttribute("mensaje", "Puntuaciones provisionales guardadas correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar las puntuaciones: " + e.getMessage());
        }
        return "redirect:/participaciones/torneo/" + torneoId;
    }

    // 4. Finalizar torneo definitivamente (calcula posiciones, asigna ELO y cambia
    // estado a FINALIZADO)
    @PostMapping("/torneo/{torneoId}/finalizar")
    public String finalizarTorneoDefinitivamente(@PathVariable Long torneoId,
            @ModelAttribute WrapperParticipaciones wrapper,
            RedirectAttributes redirectAttributes) {
        try {
            // Procesa resultados (calcula posiciones, ELO, etc.)
            participacionService.procesarResultadosTorneo(torneoId, wrapper.getParticipaciones());

            // Cambia el estado del torneo a FINALIZADO
            Torneo torneo = torneoService.obtenerTorneoPorId(torneoId)
                    .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));
            torneo.setEstado("FINALIZADO");
            torneoService.guardarTorneo(torneo, null);

            redirectAttributes.addFlashAttribute("mensaje",
                    "¡Torneo finalizado con éxito! Posiciones y ELO calculados.");
            return "redirect:/torneos/" + torneoId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al finalizar el torneo: " + e.getMessage());
            return "redirect:/participaciones/torneo/" + torneoId;
        }
    }

    @GetMapping("")
    public String listarMisParticipaciones(Authentication authentication, Model model) {
        String email = authentication.getName();
        Jugador jugador = jugadorService.obtenerJugadorPorEmail(email).orElse(null);

        List<Participacion> participaciones = (jugador != null)
                ? participacionService.listarPorJugador(jugador.getId())
                : List.of();

        model.addAttribute("participaciones", participaciones);
        model.addAttribute("template", "participaciones/usuario-lista");
        return "layout";
    }

    @GetMapping("/admin/participaciones")
    public String gestionarTodasLasParticipaciones(
            @RequestParam(required = false) Long torneoId,
            @RequestParam(required = false) String keyword,
            Model model) {

        // Llamamos al repositorio con los filtros (si son nulos, traerá todo)
        List<Participacion> participaciones = participacionService.listarParticipaciones(torneoId, keyword);

        model.addAttribute("participaciones", participaciones);
        model.addAttribute("listaTorneos", torneoService.listarTorneos()); // Para poblar el <select> del filtro
        model.addAttribute("template", "participaciones/admin-lista");

        return "layout";
    }

    @PostMapping("/torneo/{id}/desinscribir")
    public String desinscribir(@PathVariable Long id, Authentication authentication,
            RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        try {
            String email = authentication.getName();
            participacionService.desinscribirJugadorDeTorneo(id, email);
            redirectAttributes.addFlashAttribute("mensaje", "Te has desinscrito del torneo correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/jugadores/perfil";
    }
}