package com.IntegradorII.GestionResiduos.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty; // 👈 IMPORTANTE
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuario")
public class Usuario {

    @JsonProperty("idUsuario")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @JsonProperty("nombre")  // 👈 SOLUCION
    private String nombre;

    @JsonProperty("correo")  // 👈 SOLUCION
    private String correo;

    @JsonProperty("contrasena") // 👈 SOLUCION
    private String contrasena;

    @JsonProperty("rol")  // 👈 SOLUCION
    private String rol;
}
