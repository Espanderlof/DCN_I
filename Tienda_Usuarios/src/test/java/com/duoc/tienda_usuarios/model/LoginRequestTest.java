package com.duoc.tienda_usuarios.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    @Test
    @DisplayName("verifica la creacion y manipulacion de login request")
    void loginRequestTest() {
        // Given
        LoginRequest loginRequest = new LoginRequest();

        // When
        loginRequest.setEmail("test@mail.com");
        loginRequest.setPassword("password123");

        // Then
        assertEquals("test@mail.com", loginRequest.getEmail());
        assertEquals("password123", loginRequest.getPassword());
    }
}