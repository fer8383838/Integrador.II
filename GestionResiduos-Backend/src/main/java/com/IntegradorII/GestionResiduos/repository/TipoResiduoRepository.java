package com.IntegradorII.GestionResiduos.repository;

import com.IntegradorII.GestionResiduos.entity.TipoResiduo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoResiduoRepository extends JpaRepository<TipoResiduo, Integer> {
}