package com.IntegradorII.GestionResiduos.exception;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerarHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String claveOriginal = "12"; // Cambia esto por la clave que quieras
        String hash = encoder.encode(claveOriginal);
        System.out.println("Hash generado: " + hash);
    }
}