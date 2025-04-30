
package com.IntegradorII.GestionResiduos.controller;

import com.IntegradorII.GestionResiduos.entity.Usuario;
import com.IntegradorII.GestionResiduos.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping("/registrar")
    @Transactional
    public Usuario registrarUsuario(@RequestBody Usuario usuario) {
        System.out.println("Usuario recibido: " + usuario);
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        entityManager.flush();

        System.out.println("Usuario guardado: "+usuarioGuardado);
        return usuarioGuardado;
    }

    @GetMapping("/listar")
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }
}
