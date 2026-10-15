package com.shop.guaridaludica_torneos.service;

import com.shop.guaridaludica_torneos.model.Jugador;
import com.shop.guaridaludica_torneos.repository.JugadorRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JugadorDetailsService implements UserDetailsService {

    private final JugadorRepository jugadorRepository;

    public JugadorDetailsService(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Jugador jugador = jugadorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No se encontró ningún jugador con el email: " + email));

        // Spring Security requiere el rol sin el prefijo "ROLE_" si se usa .roles(),
        // o con el prefijo si se usa .authorities(). Usamos .roles() limpiándolo por seguridad:
        String rolLimpio = jugador.getRol().startsWith("ROLE_")
                ? jugador.getRol().substring(5)
                : jugador.getRol();

        return User.builder()
                .username(jugador.getEmail())
                .password(jugador.getPassword())
                .roles(rolLimpio)
                .build();
    }
}