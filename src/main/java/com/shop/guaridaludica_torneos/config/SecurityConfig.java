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
                http.authorizeHttpRequests(auth -> auth
                                // 1. PRIMERO: Restricciones de Administrador (Juegos, Jugadores y Gestión de
                                // Participaciones)
                                .requestMatchers("/juegos/nuevo", "/juegos/guardar", "/juegos/editar/**",
                                                "/juegos/eliminar/**")
                                .hasRole("ADMIN")
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/jugadores/nuevo", "/jugadores/guardar", "/jugadores/eliminar/**")
                                .hasRole("ADMIN")
                                // Rutas de administración de torneos y gestión/procesamiento de resultados:
                                .requestMatchers("/torneos/nuevo", "/torneos/guardar", "/torneos/editar/**",
                                                "/torneos/eliminar/**")
                                .hasRole("ADMIN")
                                // Permitir que cualquier usuario logueado pueda desinscribirse
                                .requestMatchers("/participaciones/torneo/*/desinscribir").authenticated()
                                .requestMatchers("/participaciones/torneo/**").hasRole("ADMIN") // 👈 Solo ADMIN
                                                                                                // gestiona y puntúa

                                // 2. SEGUNDO: Acciones de usuarios autenticados (Inscribirse a torneos)
                                .requestMatchers("/participaciones/inscribir/**").authenticated() // 👈 Usuario logueado
                                                                                                  // se apunta

                                // 3. TERCERO: Las rutas públicas (Listados y detalles generales de torneos,
                                // juegos y jugadores)
                                .requestMatchers("/", "/juegos", "/juegos/**", "/torneos", "/torneos/{id}",
                                                "/jugadores", "/jugadores/**", "/login", "/registro",
                                                "/css/**", "/js/**", "/images/**", "/api/health",
                                                "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html")
                                .permitAll()

                                // 4. ÚLTIMO: Cualquier otra cosa requiere estar autenticado
                                .anyRequest().authenticated())
                                .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/", true).permitAll())
                                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/logout")
                                                .permitAll());

                                

                return http.securityContext(context -> context.requireExplicitSave(false)).build();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
                return authConfig.getAuthenticationManager();
        }
}