package com.IntegradorII.GestionResiduos.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Reportes")
public class Reporte {

    @JsonProperty("reporteID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reporteID;

    @JsonProperty("usuarioID")
    private Integer usuarioID;

    @JsonProperty("direccionID")
    private Integer direccionID;

    @JsonProperty("tipoID")
    private Integer tipoID;

    @JsonProperty("zonaID")
    private Integer zonaID;

    @JsonProperty("descripcion")
    private String descripcion;

    @JsonProperty("fotoURL")
    private String fotoURL;

    @JsonProperty("fechaReporte")
    private LocalDateTime fechaReporte;

    @JsonProperty("estado")
    private String estado;
}
