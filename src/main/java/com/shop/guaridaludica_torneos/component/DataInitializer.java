package com.shop.guaridaludica_torneos.component;

import com.shop.guaridaludica_torneos.model.Juego;
import com.shop.guaridaludica_torneos.repository.JuegoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    //@Autowired
    private JuegoRepository juegoRepository;

    public  DataInitializer(JuegoRepository juegoRepository) {
        this.juegoRepository = juegoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Solo insertamos datos de prueba si la tabla está vacía
        if (juegoRepository.count() == 0) {
            Juego j1 = new Juego("Catan", 3, 4, "Estrategia / Gestión", null);
            juegoRepository.save(j1);

            Juego j2 = new Juego("Carcassonne", 2, 5, "Loseta / Familiar", null);
            juegoRepository.save(j2);

            Juego j3 = new Juego("7 Wonders", 2, 7, "Draft / Cartas", null);
            juegoRepository.save(j3);

            System.out.println(">> ¡Datos de prueba de juegos insertados correctamente en Aiven!");
        }
    }
}