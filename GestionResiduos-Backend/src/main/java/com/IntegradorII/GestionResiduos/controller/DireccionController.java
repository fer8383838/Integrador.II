

package com.IntegradorII.GestionResiduos.controller;

import com.IntegradorII.GestionResiduos.entity.Direccion;
import com.IntegradorII.GestionResiduos.repository.DireccionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/direcciones")
public class DireccionController {

    @Autowired
    private DireccionRepository direccionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    // Registrar dirección desde pestaña de direcciones
    @PostMapping("/registrar")
    @Transactional
    public Direccion registrarDireccion(@RequestBody Direccion direccion) {
        if (direccion.getPrincipal() == null) {
            direccion.setPrincipal(false);
        }
        Direccion guardada = direccionRepository.save(direccion);
        entityManager.flush();
        return guardada;
    }

    // Listar todas las direcciones
    @GetMapping("/listar")
    public List<Direccion> listarDirecciones() {
        return direccionRepository.findAll();
    }

    // Listar direcciones por usuario específico (simula login)
    @GetMapping("/listar/{usuarioID}")
    public List<Direccion> listarPorUsuario(@PathVariable Integer usuarioID) {
        return direccionRepository.findByUsuarioID(usuarioID);
    }
}