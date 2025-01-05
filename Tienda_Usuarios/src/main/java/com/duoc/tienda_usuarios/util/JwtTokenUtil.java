package com.duoc.tienda_usuarios.util;

import com.duoc.tienda_usuarios.dto.AzureUserDTO;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtTokenUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

    public AzureUserDTO obtenerInformacionUsuario(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            
            AzureUserDTO usuario = new AzureUserDTO();
            
            // Solo los campos marcados con ✓ en Azure
            usuario.setId(obtenerClaim(jwt, "oid")); // Id. de objeto del usuario
            usuario.setNombre(obtenerClaim(jwt, "given_name")); // Nombre
            usuario.setApellidos(obtenerClaim(jwt, "family_name")); // Apellidos
            usuario.setEmail(obtenerClaim(jwt, "emails")); // Direcciones de correo electrónico
            usuario.setTelefono(obtenerClaim(jwt, "extension_Telefono")); // Teléfono
            usuario.setDireccion(obtenerClaim(jwt, "extension_Direccion")); // Dirección
            usuario.setNombreParaMostrar(obtenerClaim(jwt, "name")); // Nombre para mostrar
            usuario.setAccessToken(token); // Token de acceso
            
            return usuario;
        } catch (Exception e) {
            logger.error("Error al decodificar el token", e);
            throw new RuntimeException("Error al decodificar el token", e);
        }
    }

    private String obtenerClaim(DecodedJWT jwt, String claim) {
        try {
            return jwt.getClaim(claim).asString();
        } catch (Exception e) {
            logger.debug("Claim {} no encontrado en el token", claim);
            return null;
        }
    }
}