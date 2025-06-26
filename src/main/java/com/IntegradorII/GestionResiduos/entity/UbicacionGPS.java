package com.IntegradorII.GestionResiduos.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "UbicacionesGPS")
public class UbicacionGPS {

    // Paso 1: Mapeamos correctamente el nombre de la columna primaria
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UbicacionID") // 🔧 aquí estaba el error
    private Integer id;

    @Column(name = "reporteID")
    private Integer reporteID;

    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "longitud")
    private Double longitud;

    // Getters y setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReporteID() {
        return reporteID;
    }

    public void setReporteID(Integer reporteID) {
        this.reporteID = reporteID;
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