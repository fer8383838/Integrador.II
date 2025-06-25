package com.IntegradorII.GestionResiduos.controller;

import com.IntegradorII.GestionResiduos.entity.TipoResiduo;
import com.IntegradorII.GestionResiduos.repository.TipoResiduoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipos-residuo")
public class TipoResiduoController {

    @Autowired
    private TipoResiduoRepository tipoResiduoRepository;

    /*@GetMapping("/listar")
    public List<TipoResiduo> listarTipos() {
        return tipoResiduoRepository.findAll();
    }*/

    @GetMapping("/listar")
    public List<TipoResiduo> listarTipos() {
        System.out.println("Entró al controlador");
        List<TipoResiduo> tipos = tipoResiduoRepository.findAll();
        System.out.println("Cantidad de registros: " + tipos.size());
        for (TipoResiduo t : tipos) {
            System.out.println("Tipo: " + t);
        }
        return tipos;
    }
}