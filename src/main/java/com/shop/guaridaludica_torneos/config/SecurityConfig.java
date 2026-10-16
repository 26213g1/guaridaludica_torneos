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
                        // 1. Rutas públicas que cualquiera puede ver (sin login)
                        .requestMatchers("/", "/juegos", "/juegos/**", "/torneos", "/torneos/**",
                                "/login", "/registro", "/css/**", "/js/**", "/images/**").permitAll()

                        // 2. Rutas exclusivas para el rol ADMIN (Gestión de juegos y jugadores)
                        .requestMatchers("/juegos/nuevo", "/juegos/guardar", "/juegos/editar/**", "/juegos/eliminar/**").hasRole("ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/jugadores/nuevo", "/jugadores/guardar", "/jugadores/eliminar/**").hasRole("ADMIN")

                        // 3. Cualquier otra petición requiere al menos estar autenticado
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
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