package com.duoc.tienda_usuarios.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    private Validator validator;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        usuario = new Usuario();
    }

    @Test
    @DisplayName("verifica la creacion de un usuario valido")
    void createValidUsuarioTest() {
        // When
        usuario.setNombre("Juan");
        usuario.setApellido("Perez");
        usuario.setEmail("juan.perez@mail.com");
        usuario.setPassword("password123");
        usuario.setRole(Role.CLIENTE);
        usuario.setTelefono("+56912345678");
        usuario.setDireccion("Av. Siempre Viva 123");

        // Then
        var violations = validator.validate(usuario);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("verifica las validaciones de campos obligatorios")
    void validateRequiredFieldsTest() {
        // When
        var violations = validator.validate(usuario);

        // Then
        assertEquals(5, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("nombre es obligatorio")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("apellido es obligatorio")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("email es obligatorio")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("contraseña es obligatoria")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("rol es obligatorio")));
    }

    @Test
    @DisplayName("verifica la validacion del formato de email")
    void validateEmailFormatTest() {
        // When
        usuario.setEmail("correo_invalido");
        var violations = validator.validate(usuario);

        // Then
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("formato del email no es válido")));
    }

    @Test
    @DisplayName("verifica la validacion del formato de telefono")
    void validatePhoneFormatTest() {
        // When
        usuario.setTelefono("123");
        var violations = validator.validate(usuario);

        // Then
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Número de teléfono inválido")));
    }

    @Test
    @DisplayName("verifica que se establecen las fechas al crear")
    void onCreateTest() {
        // When
        usuario.onCreate();

        // Then
        assertNotNull(usuario.getFechaCreacion());
        assertNotNull(usuario.getUltimaActualizacion());
    }

    @Test
    @DisplayName("verifica que se actualiza la fecha al modificar")
    void onUpdateTest() {
        // Given
        usuario.onCreate();
        LocalDateTime primeraActualizacion = usuario.getUltimaActualizacion();

        // When
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        usuario.onUpdate();

        // Then
        assertTrue(usuario.getUltimaActualizacion().isAfter(primeraActualizacion));
    }
}