package com.IntegradorII.GestionResiduos.repository;

import com.IntegradorII.GestionResiduos.entity.AtencionIncidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtencionIncidenciaRepository extends JpaRepository<AtencionIncidencia, Integer> {

    // Paso 1: Buscar todas las asignaciones hechas a un operario específico
    List<AtencionIncidencia> findByOperarioID(Integer operarioID);

    // Paso 2 (opcional): Buscar una asignación específica por reporte
    AtencionIncidencia findByReporteID(Integer reporteID);
}