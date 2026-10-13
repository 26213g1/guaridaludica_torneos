package com.shop.guaridaludica_torneos.component;

import com.shop.guaridaludica_torneos.model.Juego;
import com.shop.guaridaludica_torneos.model.Torneo;
import com.shop.guaridaludica_torneos.repository.JuegoRepository;
import com.shop.guaridaludica_torneos.repository.TorneoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final JuegoRepository juegoRepository;
    private final TorneoRepository torneoRepository;

    // Inyectamos ambos repositorios en el constructor
    public DataInitializer(JuegoRepository juegoRepository, TorneoRepository torneoRepository) {
        this.juegoRepository = juegoRepository;
        this.torneoRepository = torneoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Inicializar Juegos si la tabla está vacía
        if (juegoRepository.count() == 0) {
            Juego j1 = new Juego("Catan", 3, 4, "Estrategia / Gestión", null);
            juegoRepository.save(j1);

            Juego j2 = new Juego("Carcassonne", 2, 5, "Loseta / Familiar", null);
            juegoRepository.save(j2);

            Juego j3 = new Juego("7 Wonders", 2, 7, "Draft / Cartas", null);
            juegoRepository.save(j3);

            System.out.println(">> ¡Datos de prueba de juegos insertados correctamente en Postgres!");
        }

        // 2. Inicializar Torneos si la tabla está vacía
        if (torneoRepository.count() == 0) {
            List<Juego> juegos = juegoRepository.findAll();
            
            if (!juegos.isEmpty()) {
                Juego catan = juegos.get(0);
                Juego carcasonne = juegos.size() > 1 ? juegos.get(1) : catan;

                Torneo torneo1 = new Torneo(
                    "I Gran Torneo de " + catan.getNombre(),
                    "Ven a demostrar tus habilidades comerciales y de expansión en este torneo oficial.",
                    LocalDateTime.now().plusDays(5),
                    catan,
                    8,
                    "ABIERTO",
                    120,
                    "Normas estándar de la edición básica.",
                    catan.getImagenUrl()
                );

                Torneo torneo2 = new Torneo(
                    "Liga Nocturna: " + carcasonne.getNombre(),
                    "Un torneo rápido para estrategas de las losetas.",
                    LocalDateTime.now().plusDays(10),
                    carcasonne,
                    4,
                    "ABIERTO",
                    90,
                    "Plazas limitadas. ¡Premio especial para el ganador!",
                    carcasonne.getImagenUrl()
                );

                torneoRepository.saveAll(List.of(torneo1, torneo2));
                System.out.println(">> ¡Datos de prueba de torneos insertados correctamente en Postgres!");
            }
        }
    }
}