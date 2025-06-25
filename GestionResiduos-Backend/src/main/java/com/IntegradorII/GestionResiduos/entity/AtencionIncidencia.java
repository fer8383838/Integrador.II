package com.IntegradorII.GestionResiduos.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa la tabla AtencionIncidencias.
 * Registra la asignación de un reporte a un operario.
 */
@Entity
@Table(name = "AtencionIncidencias")
public class AtencionIncidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AtencionID")
    private Integer atencionID;

    @Column(name = "ReporteID", nullable = false)
    private Integer reporteID;

    @Column(name = "OperarioID", nullable = false)
    private Integer operarioID;

    @Column(name = "FechaAsignacion", insertable = false, updatable = false)
    private LocalDateTime fechaAsignacion;

    @Column(name = "FechaAtencion")
    private LocalDateTime fechaAtencion;

    @Column(name = "Comentario", length = 255)
    private String comentario;

    @Column(name = "Estado", length = 20)
    private String estado = "Asignado";

    // Constructor vacío obligatorio para JPA
    public AtencionIncidencia() {
    }


    // Getters y Setters

    public Integer getAtencionID() {
        return atencionID;
    }

    public void setAtencionID(Integer atencionID) {
        this.atencionID = atencionID;
    }

    public Integer getReporteID() {
        return reporteID;
    }

    public void setReporteID(Integer reporteID) {
        this.reporteID = reporteID;
    }

    public Integer getOperarioID() {
        return operarioID;
    }

    public void setOperarioID(Integer operarioID) {
        this.operarioID = operarioID;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public LocalDateTime getFechaAtencion() {
        return fechaAtencion;
    }

    public void setFechaAtencion(LocalDateTime fechaAtencion) {
        this.fechaAtencion = fechaAtencion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}