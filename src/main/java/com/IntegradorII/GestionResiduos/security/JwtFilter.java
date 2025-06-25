



// -----------------------------------------------------------------------------
// ARCHIVO: JwtFilter.java
// FUNCIÓN: Este filtro se ejecuta una sola vez por solicitud y valida el token JWT
// que viene en la cabecera "Authorization". Si el token es válido, extrae el email
// y lo registra en el contexto de seguridad de Spring (SecurityContextHolder).
// Esto permite proteger endpoints REST y controlar el acceso según el token.
// Este filtro es necesario cuando se usa Spring Security con autenticación JWT.
// -----------------------------------------------------------------------------

package com.IntegradorII.GestionResiduos.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    // Paso 1: Inyección del componente que gestiona la lógica JWT (generar, validar, obtener datos)
    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // Paso 2: Este método se ejecuta automáticamente en cada solicitud entrante
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Paso 3: Obtener el encabezado Authorization
        String authHeader = request.getHeader("Authorization");

        // Paso 4: Verificar que el encabezado no sea nulo y que empiece con 'Bearer '
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            // Paso 5: Extraer el token quitando el prefijo "Bearer "
            String token = authHeader.substring(7);

            // Paso 6: Validar el token con el método de JwtUtil
            if (jwtUtil.validarToken(token)) {

                // Paso 7: Obtener el email del usuario desde el token
                String email = jwtUtil.obtenerUsername(token);

                // Paso 8: Crear el objeto de autenticación sin credenciales (null) y sin roles
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());

                // Paso 9: Establecer detalles adicionales como IP, sesión, etc.
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Paso 10: Registrar la autenticación en el contexto de seguridad de Spring
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Paso 11: Continuar con el siguiente filtro en la cadena
        chain.doFilter(request, response);
    }
}