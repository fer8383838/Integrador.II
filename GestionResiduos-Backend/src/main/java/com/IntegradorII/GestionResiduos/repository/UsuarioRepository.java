package com.IntegradorII.GestionResiduos.repository;

import com.IntegradorII.GestionResiduos.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

}

