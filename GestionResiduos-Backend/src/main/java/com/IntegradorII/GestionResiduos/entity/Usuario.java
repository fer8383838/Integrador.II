






package com.IntegradorII.GestionResiduos.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Usuarios")
public class Usuario {

    @JsonProperty("usuarioID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UsuarioID")
    private Integer usuarioID;

    @JsonProperty("nombre")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, message = "El nombre debe tener al menos 2 caracteres")
    @Column(name = "Nombre")
    private String nombre;

    @JsonProperty("apellido")
    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, message = "El apellido debe tener al menos 2 caracteres")
    @Column(name = "Apellido")
    private String apellido;

    @JsonProperty("dni")
    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 dígitos")
    @Column(name = "DNI")
    private String dni;

    @JsonProperty("email")
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    @Column(name = "Email")
    private String email;

    @JsonProperty("claveHash")
    @NotBlank(message = "La contraseña es obligatoria")
    @Column(name = "ClaveHash")
    private String claveHash;

    @JsonProperty("telefono")
    @Pattern(regexp = "9\\d{8}", message = "El teléfono debe tener 9 dígitos y empezar con 9")
    @Column(name = "Telefono")
    private String telefono;

    @JsonProperty("rol")
    @NotBlank(message = "El rol es obligatorio")
    @Column(name = "Rol")
    private String rol;

    @JsonProperty("activo")
    @Column(name = "Activo")
    private Boolean activo;

    @JsonProperty("fechaRegistro")
    @Column(name = "FechaRegistro", insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;


    @Column(name = "ZonaID")
    @JsonProperty("zonaID")
    private Integer zonaID;


    // Getters y Setters...

    public Integer getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(Integer usuarioID) {
        this.usuarioID = usuarioID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClaveHash() {
        return claveHash;
    }

    public void setClaveHash(String claveHash) {
        this.claveHash = claveHash;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getZonaID() {
        return zonaID;
    }

    public void setZonaID(Integer zonaID) {
        this.zonaID = zonaID;
    }
}