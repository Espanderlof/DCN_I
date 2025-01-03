package com.duoc.tienda_usuarios.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UsuarioLogger {
    private static UsuarioLogger instance;
    private final Logger logger;
    private final DateTimeFormatter formatter;

    private UsuarioLogger() {
        this.logger = LoggerFactory.getLogger(UsuarioLogger.class);
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    public static synchronized UsuarioLogger getInstance() {
        if (instance == null) {
            instance = new UsuarioLogger();
        }
        return instance;
    }

    public void logCreacion(String email) {
        logger.info("Usuario creado - Email: {} - Timestamp: {}", 
            email, LocalDateTime.now().format(formatter));
    }

    public void logActualizacion(Long id) {
        logger.info("Usuario actualizado - ID: {} - Timestamp: {}", 
            id, LocalDateTime.now().format(formatter));
    }

    public void logEliminacion(Long id) {
        logger.info("Usuario eliminado - ID: {} - Timestamp: {}", 
            id, LocalDateTime.now().format(formatter));
    }

    public void logLogin(String email, boolean exitoso) {
        if (exitoso) {
            logger.info("Login exitoso - Email: {} - Timestamp: {}", 
                email, LocalDateTime.now().format(formatter));
        } else {
            logger.warn("Intento de login fallido - Email: {} - Timestamp: {}", 
                email, LocalDateTime.now().format(formatter));
        }
    }

    public void logError(String operacion, String mensaje) {
        logger.error("Error en operación {} - Mensaje: {} - Timestamp: {}", 
            operacion, mensaje, LocalDateTime.now().format(formatter));
    }
}