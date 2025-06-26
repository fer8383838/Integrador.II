




package com.IntegradorII.GestionResiduos.controller;

import com.IntegradorII.GestionResiduos.dto.ReporteParaTablaDTO;
import com.IntegradorII.GestionResiduos.dto.UsuarioConDireccionDTO;
import com.IntegradorII.GestionResiduos.dto.UsuarioParaTablaDTO;
import com.IntegradorII.GestionResiduos.entity.Direccion;
import com.IntegradorII.GestionResiduos.entity.Usuario;

import com.IntegradorII.GestionResiduos.entity.Zona;
import com.IntegradorII.GestionResiduos.entity.Reporte;

import com.IntegradorII.GestionResiduos.repository.DireccionRepository;
import com.IntegradorII.GestionResiduos.repository.UsuarioRepository;
import com.IntegradorII.GestionResiduos.repository.ZonaRepository;

import com.IntegradorII.GestionResiduos.security.JwtUtil;
import com.IntegradorII.GestionResiduos.security.SesionService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.HashMap;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;



@RestController

@RequestMapping("/usuarios")

public class UsuarioController {

    // Repositorio para operaciones CRUD sobre la entidad Usuario
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Repositorio para operaciones CRUD sobre la entidad Direccion
    @Autowired
    private DireccionRepository direccionRepository;

    // Utilizado para encriptar y verificar contraseñas
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Componente para generar y validar tokens JWT
    @Autowired
    private JwtUtil jwtUtil;

    // Permite ejecutar consultas personalizadas con JPA si se necesitara
    @PersistenceContext
    private EntityManager entityManager;

    // Servicio para gestionar la invalidez de tokens (logout y verificación de sesión)
    @Autowired
    private SesionService sesionService;


    @Autowired
    private ZonaRepository zonaRepository;




    @GetMapping("/listar")
    public ResponseEntity<?> listarUsuarios(HttpServletRequest request) {
        try {
            // Paso 1: Validar token desde el backend
            ResponseEntity<?> respuesta = sesionService.verificarToken(request);
            if (respuesta != null) return respuesta;

            // Paso 2: Extraer token JWT del header Authorization
            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no proporcionado");
            }

            String jwt = token.substring(7);
            String email = jwtUtil.obtenerUsername(jwt);

            // Paso 3: Verificar si el usuario existe
            Usuario usuarioLogueado = usuarioRepository.findByEmail(email);
            if (usuarioLogueado == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }

            // Paso 4: Verificar si es administrador
            if (!usuarioLogueado.getRol().equals("Administrador")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No autorizado para listar usuarios");
            }

            // Paso 5: Obtener todos los usuarios
            List<Usuario> usuarios = usuarioRepository.findAll();

            // Paso 6: Convertir cada usuario al DTO
            List<UsuarioParaTablaDTO> usuariosDTO = usuarios.stream().map(u -> {
                UsuarioParaTablaDTO dto = new UsuarioParaTablaDTO();
                dto.setUsuarioID(u.getUsuarioID());
                dto.setNombre(u.getNombre());
                dto.setApellido(u.getApellido());
                dto.setDni(u.getDni());
                dto.setEmail(u.getEmail());
                dto.setTelefono(u.getTelefono());
                dto.setRol(u.getRol());
                dto.setActivo(u.getActivo());
                dto.setFechaRegistro(u.getFechaRegistro());

                // Obtener nombre de zona si tiene (puedes omitir esto si no aplica)
                if (u.getZonaID() != null) {
                    zonaRepository.findById(u.getZonaID()).ifPresent(zona -> {
                        dto.setNombreZona(zona.getNombre());
                    });
                }

                return dto;
            }).collect(Collectors.toList());

            // Paso 7: Retornar lista de usuarios DTO
            return ResponseEntity.ok(usuariosDTO);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al listar usuarios: " + e.getMessage());
        }
    }








    // -----------------------------------------------------------------------------
// Endpoint para obtener la información principal de un usuario por su ID.
// - El ADMINISTRADOR puede consultar cualquier ID.
// - Los demás usuarios (Ciudadano, Operario, Supervisor) solo pueden consultar su propio ID.
// - Se devuelve un DTO con los datos del usuario y su dirección principal.
// - Si el usuario tiene asignada una zona (no es Ciudadano), se incluye zonaID y nombreZona.
// - Si el usuario es Supervisor, también se incluye la lista de Operarios a su cargo.
// -----------------------------------------------------------------------------
    @GetMapping("/obtener-principal/{id}")
    public ResponseEntity<?> obtenerPrincipal(@PathVariable Integer id, HttpServletRequest request) {
        try {
            // Paso 1: Verificar si el token es válido
            ResponseEntity<?> respuesta = sesionService.verificarToken(request);
            if (respuesta != null) return respuesta;

            // Paso 2: Extraer token y datos del usuario autenticado
            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no proporcionado");
            }
            String jwt = token.substring(7);
            String rol = jwtUtil.obtenerRol(jwt);
            String email = jwtUtil.obtenerUsername(jwt);

            // Paso 3: Verificar existencia del usuario autenticado
            Usuario usuarioAuth = usuarioRepository.findByEmail(email);
            if (usuarioAuth == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario autenticado no encontrado");
            }

            // Paso 4: Validar que el usuario autenticado tiene permiso para consultar el ID solicitado
            if (!rol.equals("Administrador") && !usuarioAuth.getUsuarioID().equals(id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No autorizado para acceder a este usuario");
            }

            // Paso 5: Buscar al usuario solicitado por ID
            Usuario usuario = usuarioRepository.findById(id).orElse(null);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }

            // Paso 6: Buscar la dirección principal del usuario
            Direccion direccion = direccionRepository.findByUsuarioID(id).stream()
                    .filter(Direccion::getPrincipal)
                    .findFirst()
                    .orElse(null);

            // Paso 7: Construir el DTO con los datos del usuario
            UsuarioConDireccionDTO dto = new UsuarioConDireccionDTO();
            dto.setUsuarioID(usuario.getUsuarioID());
            dto.setNombre(usuario.getNombre());
            dto.setApellido(usuario.getApellido());
            dto.setDni(usuario.getDni());
            dto.setEmail(usuario.getEmail());
            dto.setTelefono(usuario.getTelefono());
            dto.setRol(usuario.getRol());
            dto.setClaveHash(null); // Por seguridad no se retorna

            // Paso 8: Si tiene dirección principal, se añade al DTO
            if (direccion != null) {
                dto.setDistrito(direccion.getDistrito());
                dto.setDireccion(direccion.getDireccion());
                dto.setLatitud(direccion.getLatitud());
                dto.setLongitud(direccion.getLongitud());
                dto.setPrincipal(direccion.getPrincipal());
            }

            // Paso 9: Añadir zonaID y nombreZona si existe
            if (usuario.getZonaID() != null) {
                dto.setZonaID(usuario.getZonaID());
                zonaRepository.findById(usuario.getZonaID())
                        .ifPresent(zona -> dto.setNombreZona(zona.getNombre()));
            }

            // Paso 10: Si es Supervisor, agregar los operarios de su zona
            if ("Supervisor".equals(usuario.getRol()) && usuario.getZonaID() != null) {
                List<Usuario> operarios = usuarioRepository.findByRolAndZonaID("Operario", usuario.getZonaID());

                if (operarios != null && !operarios.isEmpty()) {
                    List<Map<String, Object>> listaOperarios = operarios.stream().map(op -> {
                        Map<String, Object> o = new HashMap<>();
                        o.put("usuarioID", op.getUsuarioID());
                        o.put("nombre", op.getNombre());
                        o.put("apellido", op.getApellido());
                        o.put("dni", op.getDni());
                        o.put("email", op.getEmail());
                        o.put("telefono", op.getTelefono());
                        o.put("rol", op.getRol());
                        return o;
                    }).collect(Collectors.toList());

                    dto.setOperariosACargo(listaOperarios);
                } else {
                    dto.setOperariosACargo(Collections.emptyList());
                }
            }


            System.out.println(dto.getOperariosACargo());

            // Paso 11: Responder con el DTO completo
            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener usuario: " + e.getMessage());
        }
    }





    // -----------------------------------------------------------------------------
// Endpoint para registrar un nuevo usuario con su dirección principal
// - Si no hay token, solo se permite registrar ciudadanos
// - Si hay token, solo los administradores pueden registrar operarios o supervisores
// - También se guarda zonaID si corresponde (para operarios y supervisores)
// -----------------------------------------------------------------------------
    @PostMapping("/registrar-completo")
    public ResponseEntity<?> registrarUsuarioConDireccion(
            @RequestBody UsuarioConDireccionDTO dto, HttpServletRequest request) {
        try {
            // Paso 1: Extraer token y rol solicitado
            String token = request.getHeader("Authorization");
            String rolSolicitado = dto.getRol();

            // Paso 2: Si no hay token, solo se permite registrar ciudadanos
            if (token == null || !token.startsWith("Bearer ")) {
                if (!"Ciudadano".equals(rolSolicitado)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body("Solo se permite registrar ciudadanos públicamente");
                }
            } else {
                // Paso 3: Validar autenticidad del token y obtener email
                String jwt = token.substring(7);
                String email;
                try {
                    email = jwtUtil.obtenerUsername(jwt);
                } catch (Exception ex) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body("Token inválido");
                }

                // Paso 4: Verificar que el usuario autenticado existe
                Usuario usuarioAuth = usuarioRepository.findByEmail(email);
                if (usuarioAuth == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Usuario autenticado no encontrado");
                }

                // Paso 5: Solo administradores pueden registrar operarios y supervisores
                if (!"Administrador".equals(usuarioAuth.getRol())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body("No autorizado para registrar este tipo de usuario");
                }

                // Paso 6: Incluso un administrador no puede registrar otro administrador
                if ("Administrador".equals(rolSolicitado)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body("No se permite registrar nuevos administradores");
                }
            }

            // Paso 7: Crear y guardar el nuevo usuario
            Usuario nuevo = new Usuario();
            nuevo.setNombre(dto.getNombre());
            nuevo.setApellido(dto.getApellido());
            nuevo.setDni(dto.getDni());
            nuevo.setEmail(dto.getEmail());
            nuevo.setClaveHash(passwordEncoder.encode(dto.getClaveHash()));
            nuevo.setTelefono(dto.getTelefono());
            nuevo.setRol(rolSolicitado);
            nuevo.setActivo(true);
            nuevo.setFechaRegistro(LocalDateTime.now());

            // Paso 8: Si no es ciudadano, guardar también zonaID
            if (!"Ciudadano".equals(rolSolicitado)) {
                nuevo.setZonaID(dto.getZonaID());
            }

            usuarioRepository.save(nuevo);

            // Paso 9: Crear y guardar la dirección principal
            Direccion direccion = new Direccion();
            direccion.setUsuarioID(nuevo.getUsuarioID());
            direccion.setDistrito("Comas");
            direccion.setDireccion(dto.getDireccion());
            direccion.setLatitud(dto.getLatitud());
            direccion.setLongitud(dto.getLongitud());
            direccion.setPrincipal(dto.getPrincipal());

            direccionRepository.save(direccion);

            // Paso 10: Retornar mensaje de éxito
            return ResponseEntity.ok("Usuario registrado correctamente");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar usuario: " + e.getMessage());
        }
    }






    // Endpoint para autenticar a un usuario con email y contraseña
// Si las credenciales son válidas, genera un token JWT y devuelve los datos esenciales del usuario
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String clave = request.get("clave");

        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email no registrado");
        }

        if (!passwordEncoder.matches(clave, usuario.getClaveHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
        }

        // Generar el token JWT con email y rol
        String token = jwtUtil.generarToken(usuario.getEmail(), usuario.getRol());

        // Preparar y retornar la respuesta con los datos necesarios del usuario
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("usuarioID", usuario.getUsuarioID());
        respuesta.put("nombre", usuario.getNombre());
        respuesta.put("rol", usuario.getRol());
        respuesta.put("activo", usuario.getActivo());
        respuesta.put("token", token);

        return ResponseEntity.ok(respuesta);
    }


    // Endpoint para cerrar sesión del usuario actual
// Marca el token como inválido para que ya no pueda ser reutilizado
    @PostMapping("/cerrar-sesion")
    public ResponseEntity<?> cerrarSesion(HttpServletRequest request) {
        try {

            ResponseEntity<?> respuesta = sesionService.verificarToken(request);
            if (respuesta != null) return respuesta;

            String token = request.getHeader("Authorization");

            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token no proporcionado");
            }

            String jwt = token.substring(7); // quitar "Bearer "

            // Llama al servicio de sesión para invalidar el token actual
            sesionService.invalidarToken(jwt);

            return ResponseEntity.ok("Sesión cerrada correctamente");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al cerrar sesión");
        }
    }




    // Endpoint que informa al frontend qué componentes debe mostrar según el rol del usuario autenticado
// Se utiliza para personalizar la interfaz de usuario en tiempo de ejecución
    @GetMapping("/info-rol-actual")
    public ResponseEntity<?> obtenerInfoRol(HttpServletRequest request) {
        try {
            ResponseEntity<?> respuesta = sesionService.verificarToken(request);
            if (respuesta != null) return respuesta;

            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no proporcionado");
            }

            String jwt = token.substring(7);
            String email = jwtUtil.obtenerUsername(jwt);
            Usuario usuario = usuarioRepository.findByEmail(email);

            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }

            String rol = usuario.getRol();



            // Configurar qué partes del frontend deben mostrarse según el rol
            boolean mostrarFormulario = rol.equals("Administrador");
            boolean mostrarBuscar = rol.equals("Administrador");
            boolean mostrarBotonVerTodos = rol.equals("Administrador");
            boolean mostrarTabla = rol.equals("Administrador");

            if (rol.equals("Ciudadano") || rol.equals("Operario") || rol.equals("Supervisor")) {
                mostrarTabla = true;
            }

            // Usamos otro nombre distinto a 'respuesta' para evitar conflicto de nombres
            Map<String, Object> datos = new HashMap<>();
            datos.put("usuarioID", usuario.getUsuarioID());
            datos.put("nombre", usuario.getNombre());
            datos.put("rol", usuario.getRol());
            datos.put("mostrarFormularioRegistro", mostrarFormulario);
            datos.put("mostrarBuscarPorID", mostrarBuscar);
            datos.put("mostrarBotonVerTodos", mostrarBotonVerTodos);
            datos.put("mostrarTablaUsuarios", mostrarTabla);



            datos.put("zonaID", usuario.getZonaID());
            Zona zona = zonaRepository.findById(usuario.getZonaID()).orElse(null);


            if (zona != null) {
                datos.put("nombreZona", zona.getNombre());

            }


            return ResponseEntity.ok(datos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener info de rol: " + e.getMessage());
        }
    }












    @PostMapping("/registrar-ciudadano-publico")
    @Transactional
    public ResponseEntity<String> registrarCiudadanoPublico(@RequestBody @Valid UsuarioConDireccionDTO dto) {

        // Paso 1: Validar que el rol sea estrictamente "Ciudadano"
        if (!"Ciudadano".equalsIgnoreCase(dto.getRol())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Solo se permite registro de ciudadanos.");
        }

        // Paso 2: Validar si ya existe un usuario con el mismo email
        Usuario usuarioPorEmail = usuarioRepository.findByEmail(dto.getEmail());
        if (usuarioPorEmail != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ya existe un usuario con ese email.");
        }

        // Paso 3: Validar si ya existe un usuario con el mismo DNI
        Usuario usuarioPorDni = usuarioRepository.findByDni(dto.getDni());
        if (usuarioPorDni != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ya existe un usuario con ese DNI.");
        }

        // Paso 4: Validar si la zona seleccionada existe
        if (dto.getZonaID() == null || !zonaRepository.existsById(dto.getZonaID())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La zona seleccionada no existe.");
        }

        // Paso 5: Crear objeto Usuario con datos del DTO
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setDni(dto.getDni());
        usuario.setEmail(dto.getEmail());
        usuario.setClaveHash(passwordEncoder.encode(dto.getClaveHash()));
        usuario.setTelefono(dto.getTelefono());
        usuario.setRol("Ciudadano");
        usuario.setZonaID(dto.getZonaID());
        usuario.setActivo(true);
        usuario.setFechaRegistro(LocalDateTime.now());

        // Paso 6: Guardar usuario en la base de datos
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // Paso 7: Crear dirección principal para este usuario
        Direccion direccion = new Direccion();
        direccion.setUsuarioID(usuarioGuardado.getUsuarioID());
        direccion.setDistrito(dto.getDistrito());
        direccion.setDireccion(dto.getDireccion());
        direccion.setLatitud(dto.getLatitud());
        direccion.setLongitud(dto.getLongitud());
        direccion.setPrincipal(true);

        direccionRepository.save(direccion);

        // Paso 8: Retornar éxito
        return ResponseEntity.ok("Ciudadano registrado correctamente.");
    }




    //  ---   Asignacion de reportes a operarios   ------------------------------------------------
    @GetMapping("/operarios-por-zona/{zonaId}")
    public ResponseEntity<?> operariosPorZona(@PathVariable Integer zonaId,
                                              HttpServletRequest request) {

        // 1. validar token (sólo SUPERVISOR o ADMIN)
        ResponseEntity<?> check = sesionService.verificarToken(request);
        if (check != null) return check;

        String email = jwtUtil.obtenerUsername(request.getHeader("Authorization").substring(7));
        Usuario auth = usuarioRepository.findByEmail(email);
        if (auth == null) return ResponseEntity.status(404).body("Usuario no encontrado");

        if (!auth.getRol().equals("Administrador") && !auth.getRol().equals("Supervisor"))
            return ResponseEntity.status(403).body("Sin permiso");

        // 2. si es supervisor, sólo su propia zona
        if (auth.getRol().equals("Supervisor") && !zonaId.equals(auth.getZonaID()))
            return ResponseEntity.status(403).body("Zona no autorizada");

        // 3. devolver lista simple de operarios
        List<Map<String,Object>> lista = usuarioRepository
                .findByRolAndZonaID("Operario", zonaId)
                .stream()
                .map(op -> {
                    Map<String,Object> m = new HashMap<>();
                    m.put("operarioID", op.getUsuarioID());
                    m.put("nombre", op.getNombre() + " " + op.getApellido());
                    return m;
                }).toList();

        return ResponseEntity.ok(lista);
    }



}