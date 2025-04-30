package com.IntegradorII.GestionResiduos.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Usuarios")
public class Usuario {

    @JsonProperty("usuarioID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UsuarioID")
    private Integer usuarioID;

    @JsonProperty("nombre")
    @Column(name = "Nombre")
    private String nombre;

    @JsonProperty("apellido")
    @Column(name = "Apellido")
    private String apellido;

    @JsonProperty("dni")
    @Column(name = "DNI")
    private String dni;

    @JsonProperty("email")
    @Column(name = "Email")
    private String email;

    @JsonProperty("claveHash")
    @Column(name = "ClaveHash")
    private String claveHash;

    @JsonProperty("telefono")
    @Column(name = "Telefono")
    private String telefono;

    @JsonProperty("rol")
    @Column(name = "Rol")
    private String rol;

    @JsonProperty("activo")
    @Column(name = "Activo")
    private Boolean activo;

    @JsonProperty("fechaRegistro")
    @Column(name = "FechaRegistro",
    insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;
}

