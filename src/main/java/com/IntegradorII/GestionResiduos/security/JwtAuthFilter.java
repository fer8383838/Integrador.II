



// -----------------------------------------------------------------------------
// ARCHIVO: JwtAuthFilter.java
// FUNCIÓN: Este filtro se ejecuta una sola vez por cada solicitud HTTP entrante.
// Su propósito es interceptar las peticiones, extraer el token JWT del header
// "Authorization", validarlo y, si es correcto, establecer los datos del usuario
// autenticado en el contexto de seguridad de Spring (SecurityContextHolder).
// Es esencial para que el sistema sepa qué usuario está autenticado y con qué rol.
// -----------------------------------------------------------------------------

package com.IntegradorII.GestionResiduos.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    // Paso 1: Este método se ejecuta para cada petición HTTP
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Paso 2: Obtener el encabezado "Authorization" del request
        String authHeader = request.getHeader("Authorization");

        // Paso 3: Verificar si el encabezado comienza con "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Paso 4: Extraer solo el token (sin el prefijo "Bearer ")
            String token = authHeader.substring(7);

            // Paso 5: Validar el token (firma, expiración, estructura)
            if (jwtUtil.validarToken(token)) {
                // Paso 6: Obtener el email y el rol del usuario desde el token
                String email = jwtUtil.obtenerUsername(token);
                String rol = jwtUtil.obtenerRol(token);

                // Paso 7: Crear objeto de autenticación con el rol del usuario
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority(rol))
                );

                // Paso 8: Asociar detalles de la petición al objeto de autenticación
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Paso 9: Registrar la autenticación en el contexto de seguridad de Spring
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Paso 10: Continuar con el siguiente filtro de la cadena
        filterChain.doFilter(request, response);
    }
}