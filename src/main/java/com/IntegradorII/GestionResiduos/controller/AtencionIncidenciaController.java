package com.IntegradorII.GestionResiduos.controller;

import com.IntegradorII.GestionResiduos.entity.AtencionIncidencia;
import com.IntegradorII.GestionResiduos.entity.Reporte;
import com.IntegradorII.GestionResiduos.entity.Usuario;

import com.IntegradorII.GestionResiduos.repository.AtencionIncidenciaRepository;
import com.IntegradorII.GestionResiduos.repository.ReporteRepository;
import com.IntegradorII.GestionResiduos.repository.UsuarioRepository;

import com.IntegradorII.GestionResiduos.security.JwtUtil;
import com.IntegradorII.GestionResiduos.security.SesionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/atencion")
public class AtencionIncidenciaController {

    @Autowired
    private AtencionIncidenciaRepository atencionRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private ReporteRepository reporteRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private SesionService sesionService;

    // Endpoint para asignar un reporte a un operario
    @PostMapping("/asignar")
    public ResponseEntity<?> asignarReporte(@RequestBody Map<String, Integer> body, HttpServletRequest request) {
        try {
            // Paso 1: Verificar token
            ResponseEntity<?> respuesta = sesionService.verificarToken(request);
            if (respuesta != null) return respuesta;

            // Paso 2: Obtener IDs
            Integer reporteID = body.get("reporteID");
            Integer operarioID = body.get("operarioID");

            if (reporteID == null || operarioID == null) {
                return ResponseEntity.badRequest().body("Faltan datos requeridos");
            }

            // Paso 3: Validar que el usuario que asigna sea un supervisor
            String token = request.getHeader("Authorization").substring(7);
            String email = jwtUtil.obtenerUsername(token);
            Usuario supervisor = usuarioRepo.findByEmail(email);

            if (!"Supervisor".equals(supervisor.getRol())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo supervisores pueden asignar reportes");
            }

            // Paso 4: Verificar que el reporte y operario existan
            Optional<Reporte> reporteOpt = reporteRepo.findById(reporteID);
            Optional<Usuario> operarioOpt = usuarioRepo.findById(operarioID);

            if (reporteOpt.isEmpty() || operarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reporte u operario no encontrado");
            }

            Usuario operario = operarioOpt.get();

            // Paso 5: Verificar que el operario sea de la misma zona del supervisor
            if (!Objects.equals(operario.getZonaID(), supervisor.getZonaID())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("El operario no pertenece a tu zona");
            }

            // Paso 6: Registrar la asignación
            AtencionIncidencia atencion = new AtencionIncidencia();
            atencion.setReporteID(reporteID);
            atencion.setOperarioID(operarioID);
            atencion.setFechaAsignacion(LocalDateTime.now());
            atencion.setEstado("Asignado");

            atencionRepo.save(atencion);


            //Con esto el estado cambia a asignado
            Reporte reporte = reporteOpt.get();
            //Obtenemos el objetoReporte desde el Optional
            reporte.setEstado("Asignado");
            //Cambiamos el estado a "Asignado"
            reporteRepo.save(reporte);
            //Guardamos el cambio en la base de datos

            return ResponseEntity.ok("Reporte asignado correctamente al operario");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al asignar reporte");
        }
    }





    // AtencionIncidenciaController.java
    @PostMapping("/atender")
    public ResponseEntity<?> atenderReporte(@RequestBody Map<String,Integer> body,
                                            HttpServletRequest request) {
        try {
            // 1️⃣ Validar token
            ResponseEntity<?> verif = sesionService.verificarToken(request);
            if (verif != null) return verif;

            Integer reporteID  = body.get("reporteID");
            if (reporteID == null) {
                return ResponseEntity.badRequest().body("Falta reporteID");
            }

            // 2️⃣ Obtener operario autenticado
            String jwt   = request.getHeader("Authorization").substring(7);
            String email = jwtUtil.obtenerUsername(jwt);
            Usuario operario = usuarioRepo.findByEmail(email);

            if (!"Operario".equals(operario.getRol())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Solo operarios pueden finalizar reportes");
            }

            // 3️⃣ Verificar que la incidencia exista y esté asignada a este operario
            AtencionIncidencia ati = atencionRepo.findByReporteIDAndOperarioID(reporteID, operario.getUsuarioID())
                    .orElse(null);

            if (ati == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Este reporte no está asignado a ti");
            }
            if ("Atendido".equals(ati.getEstado())) {
                return ResponseEntity.badRequest().body("Ya estaba atendido");
            }

            // 4️⃣ Actualizar estado y fecha
            ati.setEstado("Finalizado");
            ati.setFechaAtencion(LocalDateTime.now());
            atencionRepo.save(ati);

            // 5️⃣ Actualizar tabla Reportes
            Reporte rep = reporteRepo.findById(reporteID).orElse(null);
            if (rep != null) {
                rep.setEstado("Atendido");
                reporteRepo.save(rep);
            }

            return ResponseEntity.ok("Reporte atendido correctamente");
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al atender reporte");
        }
    }



    // Paso 2: Obtener todos los reportes asignados a un operario
    @GetMapping("/asignados/{operarioID}")
    public ResponseEntity<?> obtenerReportesAsignados(@PathVariable Integer operarioID, HttpServletRequest request) {
        try {
            // Paso 1: Verificar token
            ResponseEntity<?> respuesta = sesionService.verificarToken(request);
            if (respuesta != null) return respuesta;

            // Paso 2: Validar que el usuario sea un Operario
            String rol = sesionService.obtenerRolDesdeToken(request);
            if (!"Operario".equals(rol)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo operarios pueden acceder a esta información");
            }

            // Paso 3: Buscar todas las asignaciones de ese operario
            List<AtencionIncidencia> asignaciones = atencionRepo.findByOperarioID(operarioID);

            // Paso 4: Preparar la lista de IDs de reportes
            List<Integer> idsReportes = new ArrayList<>();
            for (AtencionIncidencia asignacion : asignaciones) {
                idsReportes.add(asignacion.getReporteID());
            }

            // Paso 5: Buscar los reportes correspondientes
            List<Reporte> reportes = new ArrayList<>();
            for (Integer id : idsReportes) {
                Optional<Reporte> reporteOpt = reporteRepo.findById(id);
                if (reporteOpt.isPresent()) {
                    reportes.add(reporteOpt.get());
                }
            }

            return ResponseEntity.ok(reportes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener reportes asignados");
        }
    }
}