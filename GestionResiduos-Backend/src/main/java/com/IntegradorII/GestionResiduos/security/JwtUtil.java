


// -----------------------------------------------------------------------------
// ARCHIVO: JwtUtil.java
// FUNCIÓN: Esta clase maneja toda la lógica de generación, validación y lectura
// de tokens JWT. Permite generar tokens seguros con firma HMAC SHA256 y extraer
// información como el usuario (email), el rol y la fecha de expiración.
// Es esencial para autenticar y autorizar usuarios en el sistema.
// -----------------------------------------------------------------------------

package com.IntegradorII.GestionResiduos.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Paso 1: Clave secreta para firmar los tokens. Debe tener al menos 32 caracteres.
    private final String SECRET_KEY = "clave-super-secreta-para-el-jwt-que-tenga-al-menos-32-caracteres";

    // Paso 2: Tiempo de expiración del token (1 día = 86400000 ms)
    private final long EXPIRATION_TIME = 86400000;

    // Paso 3: Método para obtener la clave en formato adecuado para firmar el JWT
    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Paso 4: Generar un token JWT a partir del email (username) y rol del usuario
    public String generarToken(String username, String rol) {
        return Jwts.builder()
                .setSubject(username) // Email del usuario
                .claim("rol", rol)    // Rol (Administrador, Ciudadano, etc.)
                .setIssuedAt(new Date()) // Fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Expira en 24h
                .signWith(getKey(), SignatureAlgorithm.HS256) // Firma HMAC SHA256
                .compact();
    }

    // Paso 5: Validar si el token es correcto (estructura válida, firma válida, no expirado)
    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // Paso 6: Verifica si el token está expirado (compara con la hora actual)
    public boolean tokenExpirado(String token) {
        try {
            Date expiracion = Jwts.parserBuilder().setSigningKey(getKey()).build()
                    .parseClaimsJws(token).getBody().getExpiration();
            return expiracion.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    // Paso 7: Extraer el "subject" del token, que en este sistema es el email del usuario
    public String obtenerUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    // Paso 8: Extraer el rol del usuario desde el campo personalizado "rol" en el token
    public String obtenerRol(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build()
                .parseClaimsJws(token).getBody().get("rol", String.class);
    }
}