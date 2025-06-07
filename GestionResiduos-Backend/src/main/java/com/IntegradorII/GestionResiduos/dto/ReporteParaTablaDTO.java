package com.IntegradorII.GestionResiduos.dto;

import java.time.LocalDateTime;

public class ReporteParaTablaDTO {

    // ID único del reporte
    private Integer reporteID;

    // Descripción del problema reportado
    private String descripcion;

    // Estado del reporte (ej: Pendiente, Atendido)
    private String estado;

    // Fecha y hora en que se registró el reporte
    private LocalDateTime fechaReporte;

    // URL de la imagen asociada
    private String fotoURL;

    // Coordenadas geográficas del punto reportado
    private Double latitud;
    private Double longitud;

    // Datos relacionados obtenidos por ID (relaciones manuales, no ManyToOne)
    private String nombreUsuario;
    private String direccion;
    private String tipoResiduo;
    private String zona;

    // ==========================
    // Getters y Setters
    // ==========================

    public Integer getReporteID() {
        return reporteID;
    }

    public void setReporteID(Integer reporteID) {
        this.reporteID = reporteID;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(LocalDateTime fechaReporte) {
        this.fechaReporte = fechaReporte;
    }

    public String getFotoURL() {
        return fotoURL;
    }

    public void setFotoURL(String fotoURL) {
        this.fotoURL = fotoURL;
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

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipoResiduo() {
        return tipoResiduo;
    }

    public void setTipoResiduo(String tipoResiduo) {
        this.tipoResiduo = tipoResiduo;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }
}