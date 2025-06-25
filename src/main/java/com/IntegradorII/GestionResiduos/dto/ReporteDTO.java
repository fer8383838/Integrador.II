/*
 * ---------------------------------------------------------------
 * ARCHIVO: ReporteDTO.java
 * ---------------------------------------------------------------
 * FUNCIÓN PRINCIPAL:
 * Este DTO (Data Transfer Object) se utiliza para recibir y transportar
 * los datos necesarios para registrar un nuevo reporte desde el frontend
 * hacia el backend en el sistema de gestión de residuos.
 *
 * DIFERENCIAS CON OTROS ARCHIVOS RELACIONADOS:
 * - A diferencia de la clase entidad Reporte.java, este DTO no mapea
 *   directamente la base de datos, sino que sirve como objeto temporal
 *   para recolectar datos personalizados del formulario.
 *
 * - En comparación con ReporteController.java, este archivo no contiene
 *   lógica de control ni endpoints, solo estructura de datos.
 *
 * - A diferencia del ReporteRepository.java, aquí no hay consultas
 *   a base de datos ni lógica JPA.
 *
 * - Esta clase se usa comúnmente en los métodos @PostMapping("/registrar")
 *   del controlador para recibir la solicitud del frontend.
 *
 * ÚLTIMA ACTUALIZACIÓN: se agregaron los campos latitud y longitud
 * para capturar la ubicación geográfica desde el formulario (usando Leaflet).
 */

package com.IntegradorII.GestionResiduos.dto;

import java.time.LocalDateTime;

// ------------------------------------------------------------
// Esta clase sirve como DTO (Data Transfer Object) para
// transportar datos del reporte entre el frontend y el backend
// sin exponer directamente la entidad Reporte.java.
// ------------------------------------------------------------
public class ReporteDTO {

    // ID del usuario que reporta (obligatorio)
    private Integer usuarioID;

    // ID del tipo de residuo seleccionado (orgánico, reciclable, etc.)
    private Integer tipoID;

    // ID de la zona donde ocurrió el reporte (se elige si aplica)
    private Integer zonaID;

    // Descripción textual que el usuario deja en el reporte
    private String descripcion;

    // URL de la imagen enviada (si se habilita captura o carga de foto)
    private String fotoURL;

    // Fecha y hora del momento en que se creó el reporte
    private LocalDateTime fechaReporte;

    // Estado inicial del reporte (Ejemplo: "Pendiente")
    private String estado;

    // Latitud exacta del lugar donde ocurrió el incidente
    private Double latitud;

    // Longitud exacta del lugar donde ocurrió el incidente
    private Double longitud;

    // ------------------------------------------------------------
    // MÉTODOS GETTER Y SETTER - Acceso a cada campo del DTO
    // ------------------------------------------------------------

    public Integer getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(Integer usuarioID) {
        this.usuarioID = usuarioID;
    }


    public Integer getTipoID() {
        return tipoID;
    }

    public void setTipoID(Integer tipoID) {
        this.tipoID = tipoID;
    }

    public Integer getZonaID() {
        return zonaID;
    }

    public void setZonaID(Integer zonaID) {
        this.zonaID = zonaID;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFotoURL() {
        return fotoURL;
    }

    public void setFotoURL(String fotoURL) {
        this.fotoURL = fotoURL;
    }

    public LocalDateTime getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(LocalDateTime fechaReporte) {
        this.fechaReporte = fechaReporte;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}