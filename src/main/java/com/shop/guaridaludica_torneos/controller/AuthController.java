package com.shop.guaridaludica_torneos.controller;

import com.shop.guaridaludica_torneos.model.Jugador;
import com.shop.guaridaludica_torneos.service.JugadorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final JugadorService jugadorService;

    public AuthController(JugadorService jugadorService) {
        this.jugadorService = jugadorService;
    }

    // Mostrar formulario de registro
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("jugador", new Jugador());
        model.addAttribute("template", "auth/registro"); // Apunta a tu estructura de layout
        return "layout";
    }

    // Procesar el registro del jugador
    @PostMapping("/registro")
    public String registrarJugador(@Valid @ModelAttribute("jugador") Jugador jugador,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {

        // Validar si hay errores en el formulario
        if (bindingResult.hasErrors()) {
            model.addAttribute("template", "auth/registro");
            return "layout";
        }

        try {
            // Guardamos el jugador (el servicio se encarga de cifrar la contraseña y poner el rol USER)
            jugadorService.registrarJugadorPublico(jugador);
            redirectAttributes.addFlashAttribute("exito", "¡Te has registrado con éxito en La Guarida Lúdica! Ya puedes iniciar sesión.");
            return "redirect:/login";
        } catch (Exception e) {
            // Mostramos el mensaje exacto que viene del servicio o de la excepción
            model.addAttribute("error", e.getMessage() != null ? e.getMessage() : "Ocurrió un error en el registro.");
            model.addAttribute("template", "auth/registro");
            return "layout";
        }
    }

    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("template", "auth/login");
        return "layout";
    }
}