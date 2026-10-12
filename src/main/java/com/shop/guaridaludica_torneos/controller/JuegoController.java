package com.shop.guaridaludica_torneos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import com.shop.guaridaludica_torneos.model.Juego;
import com.shop.guaridaludica_torneos.service.JuegoService;

@RestController
@RequestMapping("/api/juegos")
public class JuegoController {

    private final JuegoService juegoService;

    public JuegoController(JuegoService juegoService) {
        this.juegoService = juegoService;
    }

    @GetMapping
    public List<Juego> obtenerJuegos() {
        return juegoService.listarTodos();
    }

    @PostMapping
    public ResponseEntity<Juego> crearJuego(@Valid @RequestBody Juego juego) {
        Juego nuevoJuego = juegoService.guardarJuego(juego);
        return ResponseEntity.ok(nuevoJuego);
    }
}