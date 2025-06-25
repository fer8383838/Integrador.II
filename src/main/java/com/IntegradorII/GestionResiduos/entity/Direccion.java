

package com.IntegradorII.GestionResiduos.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "Direcciones")
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DireccionID")
    @JsonProperty("direccionID")
    private Integer direccionID;

    @Column(name = "UsuarioID", nullable = false)
    @JsonProperty("usuarioID")
    private Integer usuarioID;

    @Column(name = "Distrito")
    @JsonProperty("distrito")
    private String distrito;

    @Column(name = "Direccion")
    @JsonProperty("direccion")
    private String direccion;

    @Column(name = "Latitud")
    @JsonProperty("latitud")
    private Double latitud;

    @Column(name = "Longitud")
    @JsonProperty("longitud")
    private Double longitud;

    @Column(name = "Principal")
    @JsonProperty("principal")
    private Boolean principal;

    // Getters y Setters

    public Integer getDireccionID() {
        return direccionID;
    }

    public void setDireccionID(Integer direccionID) {
        this.direccionID = direccionID;
    }

    public Integer getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(Integer usuarioID) {
        this.usuarioID = usuarioID;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public Boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }
}