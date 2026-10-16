package com.shop.guaridaludica_torneos.service;

import com.shop.guaridaludica_torneos.model.Jugador;
import com.shop.guaridaludica_torneos.repository.JugadorRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
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

    // Obtener ranking público (Por ahora devuelve la lista general; puedes
    // ordenarla por puntos/victorias más adelante)
    public List<Jugador> obtenerRankingPublico() {
        // Ejemplo si en el futuro añades puntuación: return
        // jugadorRepository.findAllByOrderByPuntosDesc();
        //jugadorRepository.findByRolNot("ROLE_ADMIN");
        return jugadorRepository.findByRolNot("ROLE_ADMIN");
    }

    public void eliminarJugador(Long id) {
        jugadorRepository.deleteById(id);
    }

    @Transactional
    public void registrarJugadorPublico(Jugador jugador) {
        // Validamos expresamente que el usuario haya escrito una contraseña
        if (jugador.getPassword() == null || jugador.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria para el registro público.");
        }
        // Ciframos la contraseña que el usuario introdujo por su cuenta
        jugador.setPassword(passwordEncoder.encode(jugador.getPassword()));
        if (jugador.getRol() == null || jugador.getRol().isEmpty()) {
            jugador.setRol("ROLE_USER");
        }
        jugadorRepository.save(jugador);
    }

    @Transactional
    public String guardarOActualizarJugadorPorAdmin(Jugador jugadorForm) {
        // 1. Buscamos si ya existe por email
        Optional<Jugador> existente = jugadorRepository.findByEmail(jugadorForm.getEmail());

        // 2. Generamos la clave temporal
        String passwordTemporal = generarPasswordAleatoria();

        Jugador jugadorASalvar;
        if (existente.isPresent()) {
            jugadorASalvar = existente.get();
            jugadorASalvar.setNombre(jugadorForm.getNombre());
            // Si el apodo estaba vacío, le asignamos el nombre por defecto
            if (jugadorASalvar.getApodo() == null || jugadorASalvar.getApodo().isEmpty()) {
                jugadorASalvar.setApodo(jugadorForm.getNombre());
            }
        } else {
            jugadorASalvar = new Jugador();
            jugadorASalvar.setEmail(jugadorForm.getEmail());
            jugadorASalvar.setNombre(jugadorForm.getNombre());
            // Asignamos el nombre como apodo inicial por defecto para el admin
            jugadorASalvar.setApodo(jugadorForm.getNombre());
            jugadorASalvar.setRol("ROLE_USER");
        }

        jugadorASalvar.setPassword(passwordEncoder.encode(passwordTemporal));
        jugadorRepository.save(jugadorASalvar);

        return passwordTemporal;
    }

    // Método auxiliar privado para generar claves aleatorias seguras
    private String generarPasswordAleatoria() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(8); // Genera una clave de 8 caracteres

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(caracteres.length());
            sb.append(caracteres.charAt(index));
        }

        return sb.toString();
    }

    // Actualizar datos básicos y rol desde el panel de admin
    public void actualizarJugadorAdmin(Long id, String nombre, String apodo, String email, String rol) {
        Jugador jugador = jugadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));

        jugador.setNombre(nombre);
        jugador.setApodo(apodo);
        jugador.setEmail(email);
        jugador.setRol(rol); // Ej: "ROLE_ADMIN" o "ROLE_USER"

        jugadorRepository.save(jugador);
    }

    // Opcional: Resetear contraseña de emergencia por el admin
    public void resetearPassword(Long id, String nuevaPassword) {
        Jugador jugador = jugadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));

        jugador.setPassword(passwordEncoder.encode(nuevaPassword));
        jugadorRepository.save(jugador);
    }
}