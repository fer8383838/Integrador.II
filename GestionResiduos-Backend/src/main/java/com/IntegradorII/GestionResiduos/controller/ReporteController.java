

package com.IntegradorII.GestionResiduos.controller;

import com.IntegradorII.GestionResiduos.entity.Reporte;
import com.IntegradorII.GestionResiduos.repository.ReporteRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteRepository reporteRepository;

    @PersistenceContext
    private EntityManager entityManager;

    // Registrar reporte
    @PostMapping("/registrar")
    @Transactional
    public Reporte registrarReporte(@RequestBody Reporte reporte) {
        if (reporte.getFechaReporte() == null) {
            reporte.setFechaReporte(LocalDateTime.now());
        }
        if (reporte.getEstado() == null || reporte.getEstado().isBlank()) {
            reporte.setEstado("Pendiente");
        }
        Reporte guardado = reporteRepository.save(reporte);
        entityManager.flush();
        return guardado;
    }

    // Listar todos los reportes (modo admin)
    @GetMapping("/listar")
    public List<Reporte> listarReportes() {
        return reporteRepository.findAll();
    }

    // Listar reportes por usuarioID (modo usuario)
    @GetMapping("/usuario/{id}")
    public List<Reporte> listarPorUsuario(@PathVariable Integer id) {
        return reporteRepository.findByUsuarioID(id);
    }
}
