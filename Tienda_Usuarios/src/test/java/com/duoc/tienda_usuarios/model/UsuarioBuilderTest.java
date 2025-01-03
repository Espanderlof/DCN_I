package com.duoc.tienda_usuarios.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioBuilderTest {

    @Test
    @DisplayName("verifica la construccion de un usuario usando el builder")
    void buildUsuarioTest() {
        // When
        Usuario usuario = UsuarioBuilder.builder()
            .id(1L)
            .nombre("Juan")
            .apellido("Perez")
            .email("juan.perez@mail.com")
            .password("password123")
            .role(Role.CLIENTE)
            .telefono("+56912345678")
            .direccion("Av. Siempre Viva 123")
            .build();

        // Then
        assertEquals(1L, usuario.getId());
        assertEquals("Juan", usuario.getNombre());
        assertEquals("Perez", usuario.getApellido());
        assertEquals("juan.perez@mail.com", usuario.getEmail());
        assertEquals("password123", usuario.getPassword());
        assertEquals(Role.CLIENTE, usuario.getRole());
        assertEquals("+56912345678", usuario.getTelefono());
        assertEquals("Av. Siempre Viva 123", usuario.getDireccion());
    }
}