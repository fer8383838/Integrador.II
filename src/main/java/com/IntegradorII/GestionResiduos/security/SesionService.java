



// -----------------------------------------------------------------------------
// ARCHIVO: SesionService.java
// FUNCIÓN: Este servicio centraliza toda la lógica relacionada con la validez del token JWT.
// Permite verificar si un token está expirado, fue invalidado (por logout), y si debe rechazarse
// una solicitud. También extrae el rol desde el token para validación de permisos.
// Este servicio se utiliza en controladores para proteger endpoints de forma segura.
// -----------------------------------------------------------------------------

package com.IntegradorII.GestionResiduos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@Service
public class SesionService {

    // Paso 1: Lista en memoria de tokens que han sido invalidados por logout
    private Set<String> tokensInvalidos = new HashSet<>();

    // Paso 2: Inyectamos JwtUtil para validar tokens, verificar expiración y extraer datos
    @Autowired
    private JwtUtil jwtUtil;

    // Paso 3: Este método se llama cuando el usuario cierra sesión (logout)
    // Agrega el token actual a la lista negra (tokens invalidados)
    public void invalidarToken(String token) {
        tokensInvalidos.add(token);
    }

    // Paso 4: Verifica si el token ya ha sido invalidado (logout)
    public boolean estaInactivo(String token) {
        return tokensInvalidos.contains(token);
    }

    // Paso 5: Combina las validaciones más importantes del token:
    // - Si el token está expirado
    // - Si el token fue invalidado por logout
    public boolean tokenNoValido(String token) {
        return jwtUtil.tokenExpirado(token) || estaInactivo(token);
    }

    // Paso 6: Este método se llama desde cualquier controlador protegido
    // Verifica si hay un token válido. Si no lo hay, retorna una respuesta 401 (no autorizado)
    // Si todo está bien, retorna null para que el controlador continúe con su lógica normal
    public ResponseEntity<?> verificarToken(HttpServletRequest request) {

        // Paso 6.1: Obtener el header Authorization de la solicitud HTTP
        String token = request.getHeader("Authorization");

        // Paso 6.2: Validar que exista y tenga el formato correcto
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no proporcionado");
        }

        // Paso 6.3: Extraer el JWT eliminando el prefijo 'Bearer '
        String jwt = token.substring(7);

        // Paso 6.4: Verificar si el token está vencido o ha sido invalidado
        if (tokenNoValido(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o expirado");
        }

        // Paso 6.5: Todo está correcto, se permite continuar
        return null;
    }

    // ---------------------------------------------------------------------------
    // Método: obtenerRolDesdeToken
    // Descripción: Extrae y devuelve el rol del usuario a partir del token JWT
    // utilizado en la cabecera Authorization de una solicitud HTTP.
    // Se usa para validar permisos y roles en los endpoints del backend.
    // ---------------------------------------------------------------------------
    public String obtenerRolDesdeToken(HttpServletRequest request) {

        // Paso 7.1: Obtener el header Authorization que contiene el JWT
        String token = request.getHeader("Authorization");

        // Paso 7.2: Validar que tenga el formato correcto
        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }

        // Paso 7.3: Extraer solo el JWT
        String jwt = token.substring(7);

        // Paso 7.4: Usar JwtUtil para extraer el rol contenido en el token
        return jwtUtil.obtenerRol(jwt);
    }
}