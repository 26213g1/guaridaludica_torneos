package com.shop.guaridaludica.config;

import com.shop.guaridaludica.model.Usuario;
import com.shop.guaridaludica.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.findByEmail("admin@guarida.com").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setAlias("Admin");
                admin.setNombre("Administrador");
                admin.setEmail("admin@guarida.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRoles(Set.of("ROLE_ADMIN"));
                usuarioRepository.save(admin);
            }
        };
    }
}
