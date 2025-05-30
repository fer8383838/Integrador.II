


package com.IntegradorII.GestionResiduos.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

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
    @Column(name = "FechaRegistro", insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    // Getters y Setters

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
}
