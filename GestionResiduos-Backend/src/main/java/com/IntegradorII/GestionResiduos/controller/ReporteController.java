package com.IntegradorII.GestionResiduos.controller;

import com.IntegradorII.GestionResiduos.entity.Reporte;
import com.IntegradorII.GestionResiduos.repository.ReporteRepository;
import com.IntegradorII.GestionResiduos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reportes")

public class ReporteController {

    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/registrar/{idUsuario}")
    public Reporte registrarReporte(@PathVariable Long idUsuario, @RequestBody Reporte reporte) {
        reporte.setUsuario(usuarioRepository.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuario no encontrado")));
        return reporteRepository.save(reporte);
    }

    @GetMapping("/listar")
    public List<Reporte> listarReportes() {
        return reporteRepository.findAll();
    }
}
