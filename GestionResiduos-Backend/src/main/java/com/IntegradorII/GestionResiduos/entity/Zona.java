package com.IntegradorII.GestionResiduos.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Zonas")
public class Zona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ZonaID")
    @JsonProperty("zonaID")
    private Integer zonaID;

    @Column(name = "Nombre", nullable = false)
    @JsonProperty("nombre")
    private String nombre;

    @Column(name = "Descripcion")
    @JsonProperty("descripcion")
    private String descripcion;

    @Column(name = "Distrito")
    @JsonProperty("distrito")
    private String distrito;

    @Column(name = "Activa")
    @JsonProperty("activa")
    private Boolean activa;
}