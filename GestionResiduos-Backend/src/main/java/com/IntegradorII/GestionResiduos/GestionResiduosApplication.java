package com.IntegradorII.GestionResiduos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class GestionResiduosApplication {

	public static void main(String[] args) {

		SpringApplication.run(GestionResiduosApplication.class, args);


	}

}
