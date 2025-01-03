package com.duoc.tienda_usuarios.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.slf4j.LoggerFactory;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioLoggerTest {

    private ListAppender<ILoggingEvent> listAppender;
    private UsuarioLogger usuarioLogger;

    @BeforeEach
    void setUp() {
        usuarioLogger = UsuarioLogger.getInstance();
        Logger logger = (Logger) LoggerFactory.getLogger(UsuarioLogger.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
        listAppender.list.clear(); // Limpiar logs anteriores
    }

    @Test
    @DisplayName("verifica el log de creacion de usuario")
    void logCreacionTest() {
        // When
        usuarioLogger.logCreacion("test@email.com");

        // Then
        ILoggingEvent logEvent = listAppender.list.get(0);
        assertEquals(Level.INFO, logEvent.getLevel());
        String formattedMessage = logEvent.getFormattedMessage();
        assertTrue(formattedMessage.contains("Usuario creado"));
        assertTrue(formattedMessage.contains("Email: test@email.com"));
        assertTimeStampFormat(formattedMessage);
    }

    @Test
    @DisplayName("verifica el log de actualizacion de usuario")
    void logActualizacionTest() {
        // When
        usuarioLogger.logActualizacion(1L);

        // Then
        ILoggingEvent logEvent = listAppender.list.get(0);
        assertEquals(Level.INFO, logEvent.getLevel());
        String formattedMessage = logEvent.getFormattedMessage();
        assertTrue(formattedMessage.contains("Usuario actualizado"));
        assertTrue(formattedMessage.contains("ID: 1"));
        assertTimeStampFormat(formattedMessage);
    }

    @Test
    @DisplayName("verifica el log de eliminacion de usuario")
    void logEliminacionTest() {
        // When
        usuarioLogger.logEliminacion(1L);

        // Then
        ILoggingEvent logEvent = listAppender.list.get(0);
        assertEquals(Level.INFO, logEvent.getLevel());
        String formattedMessage = logEvent.getFormattedMessage();
        assertTrue(formattedMessage.contains("Usuario eliminado"));
        assertTrue(formattedMessage.contains("ID: 1"));
        assertTimeStampFormat(formattedMessage);
    }

    @Test
    @DisplayName("verifica el log de login exitoso")
    void logLoginExitosoTest() {
        // When
        usuarioLogger.logLogin("test@email.com", true);

        // Then
        ILoggingEvent logEvent = listAppender.list.get(0);
        assertEquals(Level.INFO, logEvent.getLevel());
        String formattedMessage = logEvent.getFormattedMessage();
        assertTrue(formattedMessage.contains("Login exitoso"));
        assertTrue(formattedMessage.contains("Email: test@email.com"));
        assertTimeStampFormat(formattedMessage);
    }

    @Test
    @DisplayName("verifica el log de login fallido")
    void logLoginFallidoTest() {
        // When
        usuarioLogger.logLogin("test@email.com", false);

        // Then
        ILoggingEvent logEvent = listAppender.list.get(0);
        assertEquals(Level.WARN, logEvent.getLevel());
        String formattedMessage = logEvent.getFormattedMessage();
        assertTrue(formattedMessage.contains("Intento de login fallido"));
        assertTrue(formattedMessage.contains("Email: test@email.com"));
        assertTimeStampFormat(formattedMessage);
    }

    @Test
    @DisplayName("verifica el log de error")
    void logErrorTest() {
        // When
        usuarioLogger.logError("TEST", "mensaje de error");

        // Then
        ILoggingEvent logEvent = listAppender.list.get(0);
        assertEquals(Level.ERROR, logEvent.getLevel());
        String formattedMessage = logEvent.getFormattedMessage();
        assertTrue(formattedMessage.contains("Error en operación TEST"));
        assertTrue(formattedMessage.contains("Mensaje: mensaje de error"));
        assertTimeStampFormat(formattedMessage);
    }

    private void assertTimeStampFormat(String message) {
        assertTrue(message.contains("Timestamp:"));
        assertTrue(message.matches(".*\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.*"));
    }

    @Test
    @DisplayName("verifica que el singleton retorna la misma instancia")
    void singletonTest() {
        // When
        UsuarioLogger instance1 = UsuarioLogger.getInstance();
        UsuarioLogger instance2 = UsuarioLogger.getInstance();

        // Then
        assertSame(instance1, instance2);
    }
}