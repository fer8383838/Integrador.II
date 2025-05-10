




package com.IntegradorII.GestionResiduos.controller;

import com.IntegradorII.GestionResiduos.dto.UsuarioConDireccionesDTO;
import com.IntegradorII.GestionResiduos.entity.Direccion;
import com.IntegradorII.GestionResiduos.entity.Usuario;
import com.IntegradorII.GestionResiduos.repository.DireccionRepository;
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

    @Autowired
    private DireccionRepository direccionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping("/registrar")
    @Transactional
    public Usuario registrarUsuario(@RequestBody Usuario usuario) {
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        entityManager.flush();
        return usuarioGuardado;
    }

    @PostMapping("/registrar-completo")
    @Transactional
    public Usuario registrarUsuarioCompleto(@RequestBody UsuarioConDireccionesDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setDni(dto.getDni());
        usuario.setEmail(dto.getEmail());
        usuario.setClaveHash(dto.getClaveHash());
        usuario.setTelefono(dto.getTelefono());
        usuario.setRol(dto.getRol());
        usuario.setActivo(true);
        Usuario guardado = usuarioRepository.save(usuario);
        entityManager.flush();

        for (Direccion d : dto.getDirecciones()) {
            d.setUsuarioID(guardado.getUsuarioID());
            if (d.getPrincipal() == null) d.setPrincipal(true);
            direccionRepository.save(d);
        }

        return guardado;
    }

    @GetMapping("/listar")
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/obtener-con-direcciones/{id}")
    public UsuarioConDireccionesDTO obtenerConDirecciones(@PathVariable Integer id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) return null;

        UsuarioConDireccionesDTO dto = new UsuarioConDireccionesDTO();
        dto.setUsuarioID(usuario.getUsuarioID());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setDni(usuario.getDni());
        dto.setEmail(usuario.getEmail());
        dto.setTelefono(usuario.getTelefono());
        dto.setRol(usuario.getRol());
        dto.setActivo(usuario.getActivo());
        dto.setDirecciones(direccionRepository.findByUsuarioID(id));

        return dto;
    }
}