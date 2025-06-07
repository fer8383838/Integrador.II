

/**
 * Reporte.java (Entidad JPA)
 *
 * Esta clase representa la tabla "Reportes" en la base de datos.
 * Se utiliza para guardar y recuperar información real sobre los reportes de residuos.
 * La clase incluye anotaciones JPA para persistencia, y @JsonProperty para el intercambio con el frontend.
 *
 * DIFERENCIA CLAVE:
 * Esta clase se usa directamente con la base de datos (mediante JPA).
 * No se debe confundir con ReporteDTO, que solo se usa para recibir datos del formulario del frontend.
 */

package com.IntegradorII.GestionResiduos.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Reportes")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReporteID")
    @JsonProperty("reporteID")
    private Integer reporteID;

    @Column(name = "UsuarioID", nullable = false)
    @JsonProperty("usuarioID")
    private Integer usuarioID;

    @Column(name = "TipoID", nullable = false)
    @JsonProperty("tipoID")
    private Integer tipoID;

    @Column(name = "ZonaID", nullable = false)
    @JsonProperty("zonaID")
    private Integer zonaID;

    @Column(name = "Descripcion", length = 500)
    @JsonProperty("descripcion")
    private String descripcion;

    @Column(name = "FotoURL", length = 255)
    @JsonProperty("fotoURL")
    private String fotoURL;

    @Column(name = "FechaReporte")
    @JsonProperty("fechaReporte")
    private LocalDateTime fechaReporte;

    @Column(name = "Estado", length = 20)
    @JsonProperty("estado")
    private String estado;


    // Métodos GET y SET estándar para JPA y JSON

    public Integer getReporteID() {
        return reporteID;
    }

    public void setReporteID(Integer reporteID) {
        this.reporteID = reporteID;
    }

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

}