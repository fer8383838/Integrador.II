package com.IntegradorII.GestionResiduos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;



import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableMethodSecurity
public class GestionResiduosApplication {

	public static void main(String[] args) {

		SpringApplication.run(GestionResiduosApplication.class, args);

		// Encriptar directamente desde aquí
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String rawPassword = "12";
		String encrypted = encoder.encode(rawPassword);

		System.out.println("Contraseña '12' encriptada: " + encrypted);


	}

}
