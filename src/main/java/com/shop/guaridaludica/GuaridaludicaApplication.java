package com.shop.guaridaludica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.shop.guaridaludica.config.ConfiguracionApp;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(ConfiguracionApp.class) // <--- OBLIGATORIO SI FALLA LA INYECCIÓN AUTOMÁTICA
public class GuaridaludicaApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuaridaludicaApplication.class, args);
	}

}
