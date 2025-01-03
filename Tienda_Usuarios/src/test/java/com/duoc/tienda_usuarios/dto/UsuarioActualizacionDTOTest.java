package com.duoc.tienda_usuarios.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioActualizacionDTOTest {

    @Test
    @DisplayName("verifica la creacion y manipulacion de usuario actualizacion dto")
    void usuarioActualizacionDTOTest() {
        // Given
        UsuarioActualizacionDTO dto = new UsuarioActualizacionDTO();

        // When
        dto.setNombre("Juan");
        dto.setApellido("Pérez");
        dto.setTelefono("+56912345678");
        dto.setDireccion("Av. Siempre Viva 123");

        // Then
        assertEquals("Juan", dto.getNombre());
        assertEquals("Pérez", dto.getApellido());
        assertEquals("+56912345678", dto.getTelefono());
        assertEquals("Av. Siempre Viva 123", dto.getDireccion());
    }

    @Test
    @DisplayName("verifica que todos los campos pueden ser nulos")
    void nullFieldsTest() {
        // Given
        UsuarioActualizacionDTO dto = new UsuarioActualizacionDTO();

        // Then
        assertNull(dto.getNombre());
        assertNull(dto.getApellido());
        assertNull(dto.getTelefono());
        assertNull(dto.getDireccion());
    }

    @Test
    @DisplayName("verifica la actualizacion individual de campos")
    void individualFieldUpdateTest() {
        // Given
        UsuarioActualizacionDTO dto = new UsuarioActualizacionDTO();

        // When
        dto.setNombre("Juan");
        
        // Then
        assertEquals("Juan", dto.getNombre());
        assertNull(dto.getApellido());
        assertNull(dto.getTelefono());
        assertNull(dto.getDireccion());
    }
}