package com.duoc.tienda_usuarios.service;

import com.duoc.tienda_usuarios.dto.AzureUserDTO;
import com.duoc.tienda_usuarios.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AzureUsuarioService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public AzureUserDTO obtenerPerfilDesdeToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Token no válido");
        }

        // Extraer el token
        String token = authorizationHeader.substring(7);
        
        // Validar y decodificar el token
        return jwtTokenUtil.obtenerInformacionUsuario(token);
    }
}