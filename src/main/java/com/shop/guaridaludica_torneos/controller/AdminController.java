package com.shop.guaridaludica_torneos.controller;

import com.shop.guaridaludica_torneos.repository.JugadorRepository;
import com.shop.guaridaludica_torneos.repository.TorneoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final JugadorRepository jugadorRepository;
    private final TorneoRepository torneoRepository;

    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model) {
        // Métricas clave (KPIs)
        model.addAttribute("totalJugadores", jugadorRepository.count());
        model.addAttribute("torneosAbiertos", torneoRepository.countByEstado("ABIERTO"));
        model.addAttribute("torneosEnCurso", torneoRepository.countByEstado("EN_CURSO"));
        model.addAttribute("torneosFinalizados", torneoRepository.countByEstado("FINALIZADO"));

        // Indicamos qué plantilla interna cargará el layout principal en la columna derecha
        model.addAttribute("template", "admin/dashboard");

        return "layout"; // Retorna tu layout maestro
    }
}