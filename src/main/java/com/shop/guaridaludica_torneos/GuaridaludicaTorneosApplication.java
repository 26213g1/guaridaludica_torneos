package com.shop.guaridaludica_torneos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GuaridaludicaTorneosApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuaridaludicaTorneosApplication.class, args);
	}

}
