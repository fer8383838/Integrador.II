package com.IntegradorII.GestionResiduos.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "AtencionIncidencias")
public class AtencionIncidencia {

    // ID primario de esta tabla (autogenerado por la base de datos)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer atencionID;

    // ID del reporte al que se le está haciendo seguimiento
    @Column(name = "reporteID")
    private Integer reporteID;

    // ID del operario que fue asignado para atender el reporte
    @Column(name = "operarioID")
    private Integer operarioID;

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
}