
package com.IntegradorII.GestionResiduos.repository;

import com.IntegradorII.GestionResiduos.entity.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Integer> {
    List<Direccion> findByUsuarioID(Integer usuarioID);
}