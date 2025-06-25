




// -------------------------------------------------------------------------
// CLASE: WebMvcConfig
// PROPÓSITO: Aplica un interceptor que verifica si hay token antes de acceder a cualquier página HTML.
// IMPORTANCIA: Aunque el backend ya valida seguridad con JWT en endpoints, esto protege las páginas HTML (frontend) del acceso no autorizado.
// Se usa especialmente para evitar que un usuario sin login vea archivos como usuarios.html, reportes.html, etc.
// -------------------------------------------------------------------------

package com.IntegradorII.GestionResiduos.config;

import com.IntegradorII.GestionResiduos.interceptor.JwtPageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Marca como configuración Web MVC
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtPageInterceptor jwtPageInterceptor; // Interceptor personalizado que revisa el token en las páginas

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtPageInterceptor)
                .addPathPatterns("//*.html")       // Aplica a todas las páginas HTML
                .excludePathPatterns("/login.html", "registro-ciudadano.html"); // Exceptúa el login
    }
}