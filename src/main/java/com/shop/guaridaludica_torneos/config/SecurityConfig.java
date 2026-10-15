package com.shop.guaridaludica_torneos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas que cualquiera puede ver
                        .requestMatchers("/", "/juegos", "/juegos/**", "/torneos", "/torneos/**",
                                "/jugadores", "/jugadores/**",
                                "/login", "/registro", "/css/**", "/js/**", "/images/**").permitAll()

                        // Rutas exclusivas para el rol ADMIN (gestión de juegos)
                        .requestMatchers("/juegos/nuevo", "/juegos/guardar", "/juegos/editar/**", "/juegos/eliminar/**").hasRole("ADMIN")

                        // Cualquier otra petición requiere estar autenticado
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // <--- Aquí le decimos qué URL usar para mostrar nuestra plantilla
                        .defaultSuccessUrl("/juegos", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/juegos?logout")
                        .permitAll()
                );

        return http.securityContext(context -> context.requireExplicitSave(false)).build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}