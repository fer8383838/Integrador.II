package com.IntegradorII.GestionResiduos.repository;

import com.IntegradorII.GestionResiduos.entity.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZonaRepository extends JpaRepository<Zona, Integer> {

}