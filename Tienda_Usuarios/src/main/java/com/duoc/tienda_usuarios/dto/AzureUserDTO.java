package com.duoc.tienda_usuarios.dto;

import lombok.Data;

@Data
public class AzureUserDTO {
    // ✓ Campos marcados en Azure
    private String id;                    // Id. de objeto del usuario
    private String nombre;                // Nombre
    private String apellidos;             // Apellidos
    private String email;                 // Direcciones de correo electrónico
    private String telefono;              // Teléfono (Personalizado)
    private String direccion;             // Direccion (Personalizado)
    private String nombreParaMostrar;     // Nombre para mostrar
    private String accessToken;           // Token de acceso del proveedor de identidades
    
    // Metadatos adicionales
    private String tfp;                   // Política B2C usada
    private String version;               // Versión del token
}