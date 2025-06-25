



// ---------------------------------------------------------------------------------------------------
// ARCHIVO: JwtPageInterceptor.java
//
// PROPÓSITO:
// Este interceptor se encarga de proteger el acceso directo a páginas HTML en el sistema.
// Si un usuario intenta abrir una página .html sin tener un token válido en sus cookies,
// será automáticamente redirigido al login.
//
// FUNCIONAMIENTO:
// - Intercepta todas las peticiones a archivos HTML.
// - Permite el acceso libre a login.html.
// - Verifica si existe un token válido en las cookies del navegador.
// - Si no hay token o es inválido, redirige al login.html.
//
// IMPORTANTE:
// Esto refuerza la seguridad del frontend, impidiendo el acceso manual a páginas protegidas,
// aunque no reemplaza la validación de roles que se hace en el backend con @PreAuthorize.
//
// ---------------------------------------------------------------------------------------------------

package com.IntegradorII.GestionResiduos.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.IntegradorII.GestionResiduos.security.JwtUtil;

@Component
public class JwtPageInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    // -------------------------------------------------------
    // Paso 1: Este método se ejecuta ANTES de que se procese una solicitud HTML
    // -------------------------------------------------------
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();

        // Paso 2: Mostrar por consola qué URI se está interceptando
        System.out.println("Interceptando: "+uri);

        // Paso 3: Si se trata de login.html, se permite el acceso sin verificar el token
        if (uri.equals("/login.html")) {
            return true;
        }

        // Paso 4: Solo proteger archivos .html, ignorar otros (como .js, .css, etc.)
        if (uri.endsWith(".html")) {
            String token = null;

            // Paso 5: Se busca el token en las cookies de la solicitud
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {

                    // Paso 6: Mostrar por consola el contenido de cada cookie encontrada
                    System.out.println("Cookie: "+cookie.getName()+" = "+cookie.getValue());

                    // Paso 7: Si la cookie se llama "token", se guarda su valor
                    if ("token".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            } else {
                // Paso 8: Si no hay cookies, también se informa por consola
                System.out.println("No hay cookies en la petición");
            }

            // Paso 9: Si no hay token o el token no es válido, se redirige al login
            if (token == null || !jwtUtil.validarToken(token)) {
                response.sendRedirect("/login.html");
                return false; // Se detiene el procesamiento
            }
        }

        // Paso 10: Si todo está bien, se permite continuar con la solicitud
        return true;
    }

    // -------------------------------------------------------
    // Paso 11: Método que se ejecuta DESPUÉS del controlador pero ANTES de mostrar la vista
    // -------------------------------------------------------
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // No se necesita lógica adicional en este proyecto
    }

    // -------------------------------------------------------
    // Paso 12: Método que se ejecuta DESPUÉS de que la vista ya fue renderizada
    // -------------------------------------------------------
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // No se necesita lógica adicional en este proyecto
    }
}