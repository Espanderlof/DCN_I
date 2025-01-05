package com.duoc.tienda_usuarios.controller;

import com.duoc.tienda_usuarios.dto.AzureUserDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;

@RestController
@RequestMapping("/api/usuario_azure")
@CrossOrigin(origins = "*")
public class AzureUsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(AzureUsuarioController.class);

    @GetMapping("/test")
    public String test() {
        return "El endpoint está funcionando correctamente";
    }

    @GetMapping("/perfil")
    public AzureUserDTO obtenerPerfil(@AuthenticationPrincipal Jwt jwt) {
        logger.info("Token recibido y validado correctamente");
        
        // Log de todos los claims disponibles para debugging
        logger.debug("Claims disponibles en el token:");
        jwt.getClaims().forEach((key, value) -> 
            logger.debug("{}: {}", key, value));
        
        AzureUserDTO usuario = new AzureUserDTO();
        
        // Campos básicos del usuario
        usuario.setId(jwt.getClaimAsString("oid")); // o "sub"
        usuario.setNombre(jwt.getClaimAsString("given_name"));
        usuario.setApellidos(jwt.getClaimAsString("family_name"));
        
        // Correo electrónico (puede venir como array o string)
        Object emailsClaim = jwt.getClaim("emails");
        if (emailsClaim instanceof String[]) {
            usuario.setEmail(((String[]) emailsClaim)[0]);
        } else if (emailsClaim instanceof java.util.List) {
            @SuppressWarnings("unchecked")
            java.util.List<String> emails = (java.util.List<String>) emailsClaim;
            if (!emails.isEmpty()) {
                usuario.setEmail(emails.get(0));
            }
        } else if (emailsClaim instanceof String) {
            usuario.setEmail((String) emailsClaim);
        }
        
        // Campos personalizados
        usuario.setTelefono(jwt.getClaimAsString("extension_Telefono"));
        usuario.setDireccion(jwt.getClaimAsString("extension_Direccion"));
        
        // Campos adicionales
        usuario.setNombreParaMostrar(jwt.getClaimAsString("name"));
        usuario.setAccessToken(jwt.getTokenValue());
        usuario.setTfp(jwt.getClaimAsString("tfp"));
        usuario.setVersion(jwt.getClaimAsString("ver"));
        
        logger.info("Datos del usuario extraídos del token: {}", usuario);
        return usuario;
    }

    @GetMapping("/claims")
    public Object verClaims(@AuthenticationPrincipal Jwt jwt) {
        // Endpoint para debugging que muestra todos los claims disponibles
        return jwt.getClaims();
    }
}