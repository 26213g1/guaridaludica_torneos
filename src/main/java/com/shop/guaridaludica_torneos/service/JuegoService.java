package com.shop.guaridaludica_torneos.service;

import com.shop.guaridaludica_torneos.model.Juego;
import com.shop.guaridaludica_torneos.repository.JuegoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JuegoService {

    private final JuegoRepository juegoRepository;

    public JuegoService(JuegoRepository juegoRepository) {
        this.juegoRepository = juegoRepository;
    }

    public List<Juego> listarTodos() {
        return juegoRepository.findAll();
    }

    public Juego guardarJuego(Juego juego) {
        return juegoRepository.save(juego);
    }
}