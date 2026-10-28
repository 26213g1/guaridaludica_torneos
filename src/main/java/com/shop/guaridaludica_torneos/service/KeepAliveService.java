package com.shop.guaridaludica_torneos.service;

import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Profile("prod") // <-- Solo se carga y se ejecuta cuando el perfil activo es "prod"
@Component
public class KeepAliveService {

    // Se ejecuta cada 14 minutos (840,000 milisegundos)
    //@Scheduled(fixedRate = 840000)
    // Se ejecuta cada 14 minutos (minutos 0, 14, 28 y 42), exclusivamente entre las 09:00 y las 23:00, todos los días.
    @Scheduled(cron = "0 0,14,28,42 10-20 * * *")
    public void mantenerDespierta() {
        try {
            String url = "https://guaridaludica-torneos.onrender.com/"; // Pon tu URL pública aquí
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getForObject(url, String.class);
            System.out.println("Petición de mantenimiento enviada para mantener la app despierta.");
        } catch (Exception e) {
            System.err.println("Error al intentar mantener la app despierta: " + e.getMessage());
        }
    }
}