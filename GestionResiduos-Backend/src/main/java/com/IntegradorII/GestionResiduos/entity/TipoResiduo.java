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
@Table(name = "TiposResiduo")
public class TipoResiduo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TipoID")
    @JsonProperty("tipoID")
    private Integer tipoID;

    @Column(name = "Nombre", nullable = false)
    @JsonProperty("nombre")
    private String nombre;

    @Column(name = "Clasificacion")
    @JsonProperty("clasificacion")
    private String clasificacion;

    @Column(name = "Descripcion")
    @JsonProperty("descripcion")
    private String descripcion;
}