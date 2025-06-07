




// -------------------------------------------------------------------------
// CLASE: CorsConfig
// PROPÓSITO: Habilita y configura CORS (Cross-Origin Resource Sharing)
// IMPORTANCIA: Permite que el frontend (HTML, JS) pueda comunicarse con el backend desde dominios distintos (por ejemplo desde archivos locales o un servidor externo).
// En este caso, se permite el acceso desde cualquier origen (*), lo cual está bien para desarrollo, pero debe restringirse en producción.
// -------------------------------------------------------------------------

package com.IntegradorII.GestionResiduos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration  // Marca esta clase como una configuración de Spring Boot
public class CorsConfig {

    @Bean  // Define un bean para configurar CORS de forma global
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Permite que cualquier origen acceda a cualquier endpoint del backend
                // Se aceptan los métodos GET, POST, PUT y DELETE
                registry.addMapping("/")
                        .allowedOrigins("*") // IMPORTANTE: Solo para desarrollo. En producción se debe restringir a orígenes específicos.
                        .allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }
}