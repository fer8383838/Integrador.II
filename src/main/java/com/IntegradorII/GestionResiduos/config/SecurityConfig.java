



// -------------------------------------------------------------------------
// CLASE: SecurityConfig
// PROPÓSITO: Configura toda la seguridad del backend con Spring Security
// IMPORTANCIA: Define qué rutas son públicas, cuáles requieren token JWT, desactiva CSRF, y agrega filtros personalizados como JwtFilter.
// También habilita la codificación segura de contraseñas y el AuthenticationManager.
// -------------------------------------------------------------------------

package com.IntegradorII.GestionResiduos.config;

import com.IntegradorII.GestionResiduos.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // Marca esta clase como configuración de seguridad
@EnableMethodSecurity(prePostEnabled = true) // Habilita anotaciones como @PreAuthorize
public class SecurityConfig {

    private final JwtFilter jwtFilter; // Filtro que analiza y valida el JWT

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // -------------------------------------------------------------------------
    // PASO 1: Configura el filtro de seguridad para proteger endpoints
    // -------------------------------------------------------------------------
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())         // Desactiva protección CSRF (no es necesaria para APIs REST)
                .cors(cors -> {})                    // Activa CORS (lo configura CorsConfig)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",                    // Ruta raíz
                                "/index.html",          // Página principal
                                "/login.html",          // Login frontend
                                "/usuarios.html",       // Admin usuarios
                                "/direcciones.html",
                                "/reportes.html",
                                "/zonas/listar",
                                "/favicon.ico",         // Ícono
                                "/js/**",               // Archivos JS
                                "/css/**",              // Archivos CSS
                                "/assets/**",           // Otros recursos estáticos
                                "/usuarios/login",      // Login backend
                                "/usuarios/registrar-completo", // Registro desde frontend por administrador
                                "/usuarios/registrar-ciudadano-publico",
                                "/registro-ciudadano.html",
                                "/usuarios/info-rol-actual"
                        ).permitAll() // Estas rutas no requieren autenticación
                        .anyRequest().authenticated()  // Tod.o lo demás requiere JWT
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Agrega filtro JWT antes del filtro de login por defecto
                .build();
    }

    // -------------------------------------------------------------------------
    // PASO 2: Codificador de contraseñas usando BCrypt
    // -------------------------------------------------------------------------
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // -------------------------------------------------------------------------
    // PASO 3: AuthenticationManager necesario para procesar login manualmente
    // -------------------------------------------------------------------------
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}