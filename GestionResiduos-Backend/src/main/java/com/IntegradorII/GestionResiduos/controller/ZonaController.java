package com.IntegradorII.GestionResiduos.controller;

import com.IntegradorII.GestionResiduos.entity.Zona;
import com.IntegradorII.GestionResiduos.repository.ZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zonas")
public class ZonaController {

    @Autowired
    private ZonaRepository zonaRepository;

    @GetMapping("/listar")
    public List<Zona> listarZonas() {
        return zonaRepository.findAll();
    }
}