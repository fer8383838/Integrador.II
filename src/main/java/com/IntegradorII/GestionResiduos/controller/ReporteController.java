



/**
 * ReporteController.java
 *
 * Controlador REST encargado de gestionar los reportes de residuos sólidos en el sistema.
 * Este controlador maneja 3 funciones clave:
 *   1. Registrar un nuevo reporte desde el frontend (generalmente ciudadanos).
 *   2. Listar todos los reportes (modo administrador).
 *   3. Listar los reportes por usuario específico (modo ciudadano u operario).
 *
 * DIFERENCIA CLAVE:
 * Este archivo trabaja directamente con la entidad Reporte.java para guardar en base de datos.
 * En futuras versiones se puede adaptar a recibir un ReporteDTO si se requiere más control.
 */

package com.IntegradorII.GestionResiduos.controller;

import com.IntegradorII.GestionResiduos.entity.*;


import com.IntegradorII.GestionResiduos.repository.*;

import com.IntegradorII.GestionResiduos.security.*;

import com.IntegradorII.GestionResiduos.dto.ReporteDTO;
import com.IntegradorII.GestionResiduos.dto.ReporteParaTablaDTO;



import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;


import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;





@RestController
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private SesionService sesionService;


    @PersistenceContext
    private EntityManager entityManager;



    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private DireccionRepository direccionRepository;
    @Autowired
    private ZonaRepository zonaRepository;
    @Autowired
    private TipoResiduoRepository tipoResiduoRepository;

    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private UsuarioController usuarioController;

    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    private UbicacionGPSRepository ubicacionGPSRepository;

    @Autowired
    private AtencionIncidenciaRepository atencionIncidenciaRepository;




    @GetMapping("/listar")
    public ResponseEntity<?> listarReportes(HttpServletRequest request) {
        try {
            // Paso 1: Validamos el token llamando al endpoint /usuarios/info-rol-actual
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", request.getHeader("Authorization")); // Se pasa el token al header
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> respuesta = restTemplate.exchange(
                    "http://localhost:8080/usuarios/info-rol-actual",
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            if (!respuesta.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o expirado.");
            }

            // Paso 2: Extraemos los datos del token
            Map<String, Object> datos = respuesta.getBody();
            String rol = (String) datos.get("rol");
            Integer usuarioID = (Integer) datos.get("usuarioID");

            // Paso 3: Obtenemos todos los reportes
            List<Reporte> reportes = reporteRepository.findAll();
            List<ReporteParaTablaDTO> listaDTO = new ArrayList<>();

            // Paso 4: Iteramos sobre los reportes para aplicar filtros por rol y mapear el DTO
            for (Reporte reporte : reportes) {

                // Filtro para Ciudadano: solo ve sus propios reportes
                if (rol.equals("Ciudadano") && !reporte.getUsuarioID().equals(usuarioID)) {
                    continue;
                }

                // Filtro para Supervisor: solo ve reportes de su zona
                if (rol.equals("Supervisor")) {
                    Usuario supervisor = usuarioRepository.findById(usuarioID).orElse(null);
                    if (supervisor == null || supervisor.getZonaID() == null) {
                        continue;
                    }
                    if (!reporte.getZonaID().equals(supervisor.getZonaID())) {
                        continue;
                    }
                }

                // Filtro para Operario: solo ve reportes asignados a él (AtencionIncidencias)
                if (rol.equals("Operario")) {
                    boolean asignado = atencionIncidenciaRepository.existsByReporteIDAndOperarioID(reporte.getReporteID(), usuarioID);
                    if (!asignado) {
                        continue;
                    }
                }

                // Paso 5: Creamos el DTO con los datos del reporte
                ReporteParaTablaDTO dto = new ReporteParaTablaDTO();
                dto.setReporteID(reporte.getReporteID());
                dto.setDescripcion(reporte.getDescripcion());
                dto.setEstado(reporte.getEstado());
                dto.setFechaReporte(reporte.getFechaReporte());
                dto.setFotoURL(reporte.getFotoURL());

                // Paso 6: Agregamos las coordenadas desde la tabla UbicacionGPS
                UbicacionGPS ubicacion = ubicacionGPSRepository.findByReporteID(reporte.getReporteID());
                if (ubicacion != null) {
                    dto.setLatitud(ubicacion.getLatitud());
                    dto.setLongitud(ubicacion.getLongitud());
                }

                // Paso 7: Datos relacionados por ID (usuario, tipo, zona)
                Usuario usuario = usuarioRepository.findById(reporte.getUsuarioID()).orElse(null);
                TipoResiduo tipo = tipoResiduoRepository.findById(reporte.getTipoID()).orElse(null);
                Zona zona = zonaRepository.findById(reporte.getZonaID()).orElse(null);

                dto.setNombreUsuario(usuario != null ? usuario.getNombre() + " " + usuario.getApellido() : "Desconocido");
                dto.setDireccion("No registrada"); // Ya no se usa DireccionID
                dto.setTipoResiduo(tipo != null ? tipo.getNombre() : "N/D");
                dto.setZona(zona != null ? zona.getNombre() : "N/D");

                // Paso 8: Agregamos el DTO a la lista final
                listaDTO.add(dto);
            }

            // Paso 9: Devolvemos la lista completa
            return ResponseEntity.ok(listaDTO);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al listar reportes: " + e.getMessage());
        }
    }









    /**
     * Endpoint para registrar un nuevo reporte de residuos.
     * Valida campos obligatorios y completa campos faltantes con valores por defecto.
     */
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarReporte(@RequestBody ReporteDTO dto, HttpServletRequest request) {
        try {
            // Paso 1: Extraer token del encabezado Authorization
            String token = request.getHeader("Authorization");

            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Token no proporcionado");
            }

            // Paso 2: Validar el token y obtener el correo
            String jwt = token.substring(7);
            String email;
            try {
                email = jwtUtil.obtenerUsername(jwt);
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Token inválido");
            }

            // Paso 3: Verificar si el usuario autenticado existe
            Usuario usuarioAuth = usuarioRepository.findByEmail(email);
            if (usuarioAuth == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Usuario autenticado no encontrado");
            }

            // Paso 4: Solo los roles "Ciudadano" y "Administrador" pueden registrar reportes
            String rol = usuarioAuth.getRol();
            if (!rol.equals("Ciudadano") && !rol.equals("Administrador")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Solo Ciudadanos o Administradores pueden registrar reportes");
            }

            // Paso 5: Crear el nuevo objeto Reporte
            Reporte nuevo = new Reporte();
            nuevo.setUsuarioID(dto.getUsuarioID());
            nuevo.setTipoID(dto.getTipoID());
            nuevo.setZonaID(dto.getZonaID());
            nuevo.setDescripcion(dto.getDescripcion());
            nuevo.setFotoURL(dto.getFotoURL());

            // Paso 6: Registrar fecha y estado (si vienen, o usar valores por defecto)
            nuevo.setFechaReporte(dto.getFechaReporte() != null ? dto.getFechaReporte() : LocalDateTime.now());
            nuevo.setEstado(dto.getEstado() != null ? dto.getEstado() : "Pendiente");

            // Paso 7: Guardar el nuevo reporte en la base de datos
            reporteRepository.save(nuevo);


            // Paso D: Guardar coordenadas GPS en tabla auxiliar UbicacionesGPS
            UbicacionGPS ubicacionGPS = new UbicacionGPS();
            ubicacionGPS.setReporteID(nuevo.getReporteID()); // Usa el ID del reporte recién guardado
            ubicacionGPS.setLatitud(dto.getLatitud());         // Latitud recibida desde el frontend
            ubicacionGPS.setLongitud(dto.getLongitud());       // Longitud recibida desde el frontend

            ubicacionGPSRepository.save(ubicacionGPS); // Guarda en la nueva tabla auxiliar


            // Paso 8: Retornar éxito
            return ResponseEntity.ok("Reporte registrado correctamente");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar reporte: " + e.getMessage());
        }
    }





    /**
     * Endpoint para listar los reportes hechos por un usuario específico.
     * Se usa cuando el usuario logueado ve su historial de reportes.
     */
    @GetMapping("/usuario/{id}")
    public ResponseEntity<?> listarPorUsuario(@PathVariable Integer id) {
        try {
            List<Reporte> reportes = reporteRepository.findByUsuarioID(id);

            if (reportes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontraron reportes para el usuario con ID " + id);
            }

            return ResponseEntity.ok(reportes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar reportes del usuario: " + e.getMessage());
        }
    }
}