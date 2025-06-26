




package com.IntegradorII.GestionResiduos.controller;

import com.IntegradorII.GestionResiduos.entity.Direccion;
import com.IntegradorII.GestionResiduos.entity.Usuario;

import com.IntegradorII.GestionResiduos.security.SesionService;
import com.IntegradorII.GestionResiduos.security.JwtUtil;

import com.IntegradorII.GestionResiduos.repository.DireccionRepository;
import com.IntegradorII.GestionResiduos.repository.UsuarioRepository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import jakarta.servlet.http.HttpServletRequest;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/direcciones")
public class DireccionController {

    @Autowired
    private SesionService sesionService;

    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    private DireccionRepository direccionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;
    private java.util.Collections Collections;

    // Registrar dirección desde pestaña de direcciones
    @PostMapping("/registrar")
    @Transactional
    public ResponseEntity<?> registrarDireccion(@RequestBody Direccion direccion) {

        System.out.println("Iniciando registro de dirección para usuario ID: " + direccion.getUsuarioID());

        try {
            // Validaciones manuales con mensajes personalizados
            if (direccion.getUsuarioID() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El ID del usuario es obligatorio.");
            }

            if (direccion.getDistrito() == null || direccion.getDistrito().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El distrito no puede estar vacío.");
            }

            if (direccion.getDireccion() == null || direccion.getDireccion().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("La dirección no puede estar vacía.");
            }

            if (direccion.getLatitud() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("La latitud es obligatoria.");
            }

            if (direccion.getLongitud() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("La longitud es obligatoria.");
            }

            if (direccion.getPrincipal() == null) {
                direccion.setPrincipal(false);
            }

            Direccion guardada = direccionRepository.save(direccion);
            entityManager.flush();

            return ResponseEntity.ok(guardada);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar dirección: " + e.getMessage());
        }
    }



    // -----------------------------------------------------------------------------
// Endpoint protegido: Listar todas las direcciones (sólo para administrador)
// -----------------------------------------------------------------------------
    @GetMapping("/listar")
    public ResponseEntity<?> listarDirecciones(HttpServletRequest request) {
        try {
            // Paso 1: Validar token desde el backend
            ResponseEntity<?> respuesta = sesionService.verificarToken(request);
            if (respuesta != null) return respuesta;

            // Paso 2: Extraer el token del header
            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no proporcionado");
            }

            String jwt = token.substring(7);
            String email = jwtUtil.obtenerUsername(jwt);

            // Paso 3: Buscar usuario autenticado
            Usuario usuario = usuarioRepository.findByEmail(email);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }

            // Paso 4: Validar si es administrador
            if (!"Administrador".equals(usuario.getRol())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No autorizado para ver todas las direcciones");
            }

            // Paso 5: Devolver todas las direcciones
            List<Direccion> direcciones = direccionRepository.findAll();
            return ResponseEntity.ok(direcciones);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al listar direcciones: " + e.getMessage());
        }
    }



    // -----------------------------------------------------------------------------
// Endpoint protegido: Listar direcciones de un usuario específico
// - El ADMINISTRADOR puede consultar cualquier usuario.
// - Los demás (Ciudadano, Supervisor, Operario) solo pueden consultar su propio ID.
// - Si es Administrador, devuelve lista vacía por diseño.
// -----------------------------------------------------------------------------
    @GetMapping("/listar/{usuarioID}")
    public ResponseEntity<?> listarPorUsuario(@PathVariable Integer usuarioID, HttpServletRequest request) {
        try {
            // Paso 1: Verificar token
            ResponseEntity<?> respuesta = sesionService.verificarToken(request);
            if (respuesta != null) return respuesta;

            // Paso 2: Extraer token y obtener datos del usuario autenticado
            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no proporcionado");
            }

            String jwt = token.substring(7);
            String email = jwtUtil.obtenerUsername(jwt);
            Usuario usuarioAuth = usuarioRepository.findByEmail(email);

            if (usuarioAuth == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario autenticado no encontrado");
            }

            // Paso 3: Verificar permisos según rol
            if (!"Administrador".equals(usuarioAuth.getRol()) && !usuarioAuth.getUsuarioID().equals(usuarioID)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No autorizado para acceder a estas direcciones");
            }

            // Paso 4: Si el usuario objetivo no existe, error
            Usuario usuarioConsultado = usuarioRepository.findById(usuarioID).orElse(null);
            if (usuarioConsultado == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }

            // Paso 5: Si el usuario objetivo es administrador, devolver lista vacía
            if ("Administrador".equalsIgnoreCase(usuarioConsultado.getRol())) {
                return ResponseEntity.ok(Collections.emptyList());
            }

            // Paso 6: Buscar y devolver las direcciones del usuario
            List<Direccion> direcciones = direccionRepository.findByUsuarioID(usuarioID);
            return ResponseEntity.ok(direcciones);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al listar direcciones por usuario: " + e.getMessage());
        }
    }
}