




package com.IntegradorII.GestionResiduos.controller;

import com.IntegradorII.GestionResiduos.entity.Direccion;
import com.IntegradorII.GestionResiduos.entity.Usuario;

import com.IntegradorII.GestionResiduos.repository.DireccionRepository;
import com.IntegradorII.GestionResiduos.repository.UsuarioRepository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/direcciones")
public class DireccionController {

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

    // Listar todas las direcciones
    @GetMapping("/listar")
    public ResponseEntity<?> listarDirecciones() {
        try {
            List<Direccion> direcciones = direccionRepository.findAll();
            return ResponseEntity.ok(direcciones);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al listar direcciones: " + e.getMessage());
        }
    }

    // Listar direcciones por usuario específico
    @GetMapping("/listar/{usuarioID}")
    public ResponseEntity<?> listarPorUsuario(@PathVariable Integer usuarioID) {
        try {
            // Obtener el usuario
            Usuario usuario = usuarioRepository.findById(usuarioID).orElse(null);

            // Si no existe, devolver error
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Usuario no encontrado");
            }

            // Si es Administrador, devolver lista vacía directamente
            if ("Administrador".equalsIgnoreCase(usuario.getRol())) {
                return ResponseEntity.ok(Collections.emptyList());
            }

            // Buscar direcciones normalmente
            List<Direccion> direcciones = direccionRepository.findByUsuarioID(usuarioID);
            return ResponseEntity.ok(direcciones);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al listar direcciones por usuario: " + e.getMessage());
        }
    }
}