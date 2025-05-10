package com.IntegradorII.GestionResiduos.dto;

import java.time.LocalDateTime;

public class ReporteDTO {
    private Integer usuarioID;
    private Integer direccionID;
    private Integer tipoID;
    private Integer zonaID;
    private String descripcion;
    private String fotoURL;
    private LocalDateTime fechaReporte;
    private String estado;

    // Getters y setters
    public Integer getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(Integer usuarioID) {
        this.usuarioID = usuarioID;
    }

    public Integer getDireccionID() {
        return direccionID;
    }

    public void setDireccionID(Integer direccionID) {
        this.direccionID = direccionID;
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
}
