package com.IntegradorII.GestionResiduos.controller;

import com.IntegradorII.GestionResiduos.entity.Reporte;
import com.IntegradorII.GestionResiduos.repository.ReporteRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteRepository reporteRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping("/registrar")
    @Transactional
    public Reporte registrarReporte(@RequestBody Reporte reporte) {
        System.out.println("Reporte recibido: " + reporte);
        Reporte guardado = reporteRepository.save(reporte);
        entityManager.flush(); // asegura el guardado inmediato
        return guardado;
    }

    @GetMapping("/listar")
    public List<Reporte> listarReportes() {
        return reporteRepository.findAll();
    }
}
