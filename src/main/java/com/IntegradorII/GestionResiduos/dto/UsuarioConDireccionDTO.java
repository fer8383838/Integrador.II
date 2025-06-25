


/**
 * ----------------------------------------------------------------------------
 * Archivo: UsuarioConDireccionDTO.java
 * Proyecto: Sistema de Gestión de Residuos Municipales
 * Autor: Fernando Montero (adaptado y comentado por ChatGPT)
 *
 * Descripción:
 *   Este archivo define una clase DTO (Data Transfer Object) que permite
 *   transferir conjuntamente los datos de un usuario junto con su dirección
 *   principal. Está diseñado especialmente para las operaciones donde se
 *   necesita mostrar o registrar ambos elementos al mismo tiempo.
 *
 *   A diferencia de la clase Usuario.java (que representa una entidad pura
 *   de base de datos), este DTO no se mapea directamente a una tabla, sino que
 *   se usa para agrupar datos de distintas fuentes (usuario + dirección) para
 *   ser enviados o recibidos como un solo objeto.
 *
 *   También difiere del UsuarioController.java, que es el responsable de
 *   recibir las solicitudes HTTP y manejar la lógica de control, y del
 *   UsuarioRepository.java, que interactúa directamente con la base de datos.
 *
 *   Además, este DTO puede contener datos adicionales como la lista de
 *   operarios asignados en caso de que el usuario sea un Supervisor.
 * ----------------------------------------------------------------------------
 */

package com.IntegradorII.GestionResiduos.dto;

import jakarta.validation.constraints.*;

import java.util.List;
import com.IntegradorII.GestionResiduos.entity.Usuario;

import java.util.Map;
import java.util.HashMap;

public class UsuarioConDireccionDTO {

    // Identificador único del usuario
    private Integer usuarioID;

    // Nombre del usuario (no puede estar vacío y mínimo 2 caracteres)
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, message = "El nombre debe tener al menos 2 caracteres")
    private String nombre;

    // Apellido del usuario (no puede estar vacío y mínimo 2 caracteres)
    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(min = 2, message = "El apellido debe tener al menos 2 caracteres")
    private String apellido;

    // DNI del usuario (exactamente 8 dígitos)
    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 dígitos")
    private String dni;

    // Correo electrónico válido
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email no es válido")
    private String email;

    // Clave encriptada (hash) para autenticación
    @NotBlank(message = "La contraseña no puede estar vacía")
    private String claveHash;

    // Número de teléfono que debe empezar con 9 y tener 9 dígitos
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "9\\d{8}", message = "El teléfono debe tener 9 dígitos y empezar con 9")
    private String telefono;

    // Rol del usuario (Ciudadano, Operario, Supervisor, Administrador)
    @NotBlank(message = "El rol es obligatorio")
    private String rol;

    // Distrito correspondiente a la dirección principal del usuario
    @NotBlank(message = "El distrito no puede estar vacío")
    private String distrito;

    // Dirección específica del usuario
    @NotBlank(message = "La dirección no puede estar vacía")
    private String direccion;

    // Coordenadas geográficas (latitud), deben estar en el rango válido de -90 a 90
    @NotNull(message = "La latitud es obligatoria")
    @DecimalMin(value = "-90.0", inclusive = true, message = "La latitud mínima es -90.0")
    @DecimalMax(value = "90.0", inclusive = true, message = "La latitud máxima es 90.0")
    private Double latitud;

    // Coordenadas geográficas (longitud), deben estar en el rango válido de -180 a 180
    @NotNull(message = "La longitud es obligatoria")
    @DecimalMin(value = "-180.0", inclusive = true, message = "La longitud mínima es -180.0")
    @DecimalMax(value = "180.0", inclusive = true, message = "La longitud máxima es 180.0")
    private Double longitud;


    // Indica si esta dirección es la principal del usuario
    private Boolean principal;




    private Integer zonaID;

    private String nombreZona;

    private List<Map<String, Object>> operariosACargo;

    //

    // ---------------------- Getters ----------------------

    public Integer getUsuarioID() { return usuarioID; }

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



    public Integer getZonaID() { return zonaID; }

    public String getNombreZona() { return nombreZona; }

    public List<Map<String, Object>> getOperariosACargo() { return operariosACargo; }


    // ---------------------- Setters ----------------------

    public void setUsuarioID(Integer usuarioID) {
        this.usuarioID = usuarioID; }

    public void setNombre(String nombre) {
        this.nombre = nombre; }

    public void setApellido(String apellido) {
        this.apellido = apellido; }

    public void setDni(String dni) {
        this.dni = dni; }

    public void setEmail(String email) {
        this.email = email; }

    public void setClaveHash(String claveHash) {
        this.claveHash = claveHash; }

    public void setTelefono(String telefono) {
        this.telefono = telefono; }

    public void setRol(String rol) {
        this.rol = rol; }

    public void setDistrito(String distrito) {
        this.distrito = distrito; }

    public void setDireccion(String direccion) {
        this.direccion = direccion; }

    public void setLatitud(Double latitud) {
        this.latitud = latitud; }

    public void setLongitud(Double longitud) {
        this.longitud = longitud; }

    public void setPrincipal(Boolean principal) {
        this.principal = principal; }




    public void setZonaID(Integer zonaID) {
        this.zonaID = zonaID; }

    public void setNombreZona(String nombreZona) {
        this.nombreZona = nombreZona; }

    public void setOperariosACargo(List<Map<String, Object>> operariosACargo) {
        this.operariosACargo = operariosACargo; }
}






