package com.IntegradorII.GestionResiduos.repository;

import com.IntegradorII.GestionResiduos.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}