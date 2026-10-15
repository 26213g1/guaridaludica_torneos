package com.shop.guaridaludica_torneos.component;

import com.shop.guaridaludica_torneos.model.Juego;
import com.shop.guaridaludica_torneos.model.Jugador;
import com.shop.guaridaludica_torneos.model.Torneo;
import com.shop.guaridaludica_torneos.repository.JuegoRepository;
import com.shop.guaridaludica_torneos.repository.JugadorRepository;
import com.shop.guaridaludica_torneos.repository.TorneoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final JuegoRepository juegoRepository;
    private final TorneoRepository torneoRepository;
    private final JugadorRepository jugadorRepository;
    private final PasswordEncoder passwordEncoder;
    private final DataSource datasource;


    // Inyectamos ambos repositorios en el constructor
    public DataInitializer(JuegoRepository juegoRepository, TorneoRepository torneoRepository, DataSource dataSource, JugadorRepository jugadorRepository,
                           PasswordEncoder passwordEncoder) {
        this.juegoRepository = juegoRepository;
        this.torneoRepository = torneoRepository;
        this.jugadorRepository = jugadorRepository;
        this.passwordEncoder = passwordEncoder;
        this.datasource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Comprobando la conexión con PostgreSQL...");

        // try-with-resources (S04-1): la Connection se cierra sola al salir del bloque,
        // tanto si todo va bien como si hay una excepción.
        // getConnection() es el momento en que se intenta de verdad llegar al servidor.
        try (Connection conexion = datasource.getConnection()) {

            // DatabaseMetaData describe la base de datos a la que estamos conectados:
            // producto, versión, usuario... Es una forma de comprobar que la conexión funciona de verdad.
            DatabaseMetaData info = conexion.getMetaData();

            System.out.println("CONEXIÓN CORRECTA");
            System.out.println("  Base de datos: " + info.getDatabaseProductName()
                    + " " + info.getDatabaseProductVersion());   // → PostgreSQL 17.x (la versión que hayas instalado)
            System.out.println("  Usuario:       " + info.getUserName());  // → postgres

        } catch (Exception e) {
            // Si algo falla (dirección mal escrita, contraseña incorrecta, red que no llega al servidor),
            // getConnection() lanza una excepción. La capturamos para escribir el motivo
            // en lugar de dejar que el programa termine con un error largo.
            System.out.println("NO SE HA PODIDO CONECTAR");
            System.out.println("  Motivo: " + e.getMessage());
        }

        // 0. NUEVO: Inicializar Usuario Administrador si la tabla de jugadores está vacía
        if (jugadorRepository.count() == 0) {
            Jugador admin = new Jugador();
            admin.setNombre("Administrador");
            admin.setApodo("AdminGuarida");
            admin.setEmail("admin@guaridaludica.com");
            // Ciframos obligatoriamente la contraseña con BCrypt
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRol("ROLE_ADMIN");
            admin.setPuntosElo(1200);

            jugadorRepository.save(admin);
            System.out.println(">> ¡Usuario Administrador creado con éxito en Postgres!");
            System.out.println("   - Email: admin@guaridaludica.com");
            System.out.println("   - Contraseña: admin123");
        }

        // 1. Inicializar Juegos si la tabla está vacía
        if (juegoRepository.count() == 0) {
            Juego j1 = new Juego("Catan", 3, 4, 60, "Estrategia / Gestión", null, "Descripcion 1");
            juegoRepository.save(j1);

            Juego j2 = new Juego("Carcassonne", 2, 5, 65, "Loseta / Familiar", null, "Descripcion 2");
            juegoRepository.save(j2);

            Juego j3 = new Juego("7 Wonders", 2, 7, 66, "Draft / Cartas", null, "Descripcion 3");
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