package com.IntegradorII.GestionResiduos.repository;

import com.IntegradorII.GestionResiduos.entity.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Integer> {
    List<Reporte> findByUsuarioID(Integer usuarioID);
    List<Reporte> findByZonaID(Integer zonaID);

    @Override
    List<Reporte> findAll();
}