package com.IntegradorII.GestionResiduos.repository;

import com.IntegradorII.GestionResiduos.entity.UbicacionGPS;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UbicacionGPSRepository extends JpaRepository<UbicacionGPS, Integer> {

    UbicacionGPS findByReporteID(Integer reporteID);
}