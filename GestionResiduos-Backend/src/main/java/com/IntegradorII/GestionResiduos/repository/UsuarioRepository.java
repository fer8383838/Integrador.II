package com.IntegradorII.GestionResiduos.repository;

import com.IntegradorII.GestionResiduos.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Usuario findByDni(String in);
    Usuario findByEmail(String email);


    List<Usuario> findByRol(String rol);
    List<Usuario> findByRolAndZonaID(String rol, Integer zonaID);

}

