

package com.IntegradorII.GestionResiduos.dto;

public class UsuarioConDireccionDTO {
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String claveHash;
    private String telefono;
    private String rol;

    private String distrito;
    private String direccion;
    private Double latitud;
    private Double longitud;
    private Boolean principal;

    // Getters
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getDni() { return dni; }
    public String getEmail() { return email; }
    public String getClaveHash() { return claveHash; }
    public String getTelefono() { return telefono; }
    public String getRol() { return rol; }
    public String getDistrito() { return distrito; }
    public String getDireccion() { return direccion; }
    public Double getLatitud() { return latitud; }
    public Double getLongitud() { return longitud; }
    public Boolean getPrincipal() { return principal; }

    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setDni(String dni) { this.dni = dni; }
    public void setEmail(String email) { this.email = email; }
    public void setClaveHash(String claveHash) { this.claveHash = claveHash; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setRol(String rol) { this.rol = rol; }
    public void setDistrito(String distrito) { this.distrito = distrito; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }
    public void setPrincipal(Boolean principal) { this.principal = principal; }
}