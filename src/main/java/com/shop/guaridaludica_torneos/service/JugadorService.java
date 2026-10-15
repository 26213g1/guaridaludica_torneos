package com.shop.guaridaludica_torneos.service;

import com.shop.guaridaludica_torneos.model.Jugador;
import com.shop.guaridaludica_torneos.repository.JugadorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JugadorService {

    private final JugadorRepository jugadorRepository;
    private final PasswordEncoder passwordEncoder;

    public JugadorService(JugadorRepository jugadorRepository, PasswordEncoder passwordEncoder) {
        this.jugadorRepository = jugadorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Listar todos los jugadores
    public List<Jugador> listarJugadores() {
        return jugadorRepository.findAll();
    }

    // Buscar jugador por ID
    public Optional<Jugador> obtenerJugadorPorId(Long id) {
        return jugadorRepository.findById(id);
    }

    // Buscar jugador por email
    public Optional<Jugador> obtenerJugadorPorEmail(String email) {
        return jugadorRepository.findByEmail(email);
    }

    // Guardar o registrar un jugador cifrando su contraseña obligatoriamente
    public Jugador guardarJugador(Jugador jugador) {
        // Si la contraseña viene en texto plano (por ejemplo, en un registro nuevo o cambio), la ciframos.
        // O si ya está cifrada (empieza por $2a$), podríamos evitar volver a cifrarla,
        // pero asegurarnos de que siempre pase por el encoder si es nueva.
        if (jugador.getPassword() != null && !jugador.getPassword().startsWith("$2a$")) {
            jugador.setPassword(passwordEncoder.encode(jugador.getPassword()));
        }

        // Si no se le ha asignado rol por defecto, le ponemos ROLE_USER
        if (jugador.getRol() == null || jugador.getRol().isEmpty()) {
            jugador.setRol("ROLE_USER");
        }

        return jugadorRepository.save(jugador);
    }

    public void eliminarJugador(Long id) {
        jugadorRepository.deleteById(id);
    }
}