package com.shop.guaridaludica.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KeepAliveService {

    // Se ejecuta cada 14 minutos (840,000 milisegundos)
    @Scheduled(fixedRate = 840000)
    public void mantenerDespierta() {
        try {
            String url = "https://guaridaludica.onrender.com/guaridaludica/"; // Pon tu URL pública aquí
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getForObject(url, String.class);
            System.out.println("Petición de mantenimiento enviada para mantener la app despierta.");
        } catch (Exception e) {
            System.err.println("Error al intentar mantener la app despierta: " + e.getMessage());
        }
    }
}