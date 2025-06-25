package com.IntegradorII.GestionResiduos.repository;


import com.IntegradorII.GestionResiduos.entity.TipoResiduo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface TipoResiduoRepository extends JpaRepository<TipoResiduo, Integer> {

    // Paso 1: Buscar un tipo de residuo por su nombre exacto (opcional)
    Optional<TipoResiduo> findByNombre(String nombre);

    // Paso 2: Obtener todos los tipos de residuo activos (si tienes un campo activo)

    // Paso 3: Buscar por categoría si más adelante agregas esa lógica
    // List<TipoResiduo> findByCategoria(String categoria);

}