package com.duoc.tienda_usuarios.dto;

import lombok.Data;

@Data
public class UsuarioActualizacionDTO {
    private String nombre;
    private String apellido;
    private String telefono;
    private String direccion;
}