package com.duoc.tienda_usuarios.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class PasswordResetDTOTest {

    @Test
    @DisplayName("verifica la creacion y manipulacion de password reset dto")
    void passwordResetDTOTest() {
        // Given
        PasswordResetDTO dto = new PasswordResetDTO();

        // When
        dto.setNewPassword("nuevaContraseña123");

        // Then
        assertEquals("nuevaContraseña123", dto.getNewPassword());
    }

    @Test
    @DisplayName("verifica que el password puede ser nulo")
    void nullPasswordTest() {
        // Given
        PasswordResetDTO dto = new PasswordResetDTO();

        // Then
        assertNull(dto.getNewPassword());
    }
}