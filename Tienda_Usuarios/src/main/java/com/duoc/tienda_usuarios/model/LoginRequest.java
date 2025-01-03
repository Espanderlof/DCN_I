package com.duoc.tienda_usuarios.model;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}