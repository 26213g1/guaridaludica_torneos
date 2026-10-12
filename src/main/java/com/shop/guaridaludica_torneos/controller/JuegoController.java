package com.shop.guaridaludica_torneos.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

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
    public List<Juego> listarJuegos() {
        return juegoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Juego> obtenerJuegoPorId(@PathVariable Long id) {
        return juegoService.obtenerJuegoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crearJuego(@Valid @ModelAttribute Juego juego, 
        @RequestParam(value = "imagen", required = false) MultipartFile imagen) {
        
            try {
            Juego nuevoJuego = juegoService.guardarJuego(juego, imagen);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoJuego);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al subir la imagen a Cloudinary: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarJuego(@PathVariable Long id) {
        if (juegoService.obtenerJuegoPorId(id).isPresent()) {
            juegoService.eliminarJuego(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}