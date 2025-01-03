package com.duoc.tienda_productos.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class ActualizarStockDTOTest {

    @Test
    @DisplayName("verifica la creacion y manipulacion de actualizar stock dto")
    void actualizarStockDTOTest() {
        // Given
        ActualizarStockDTO dto = new ActualizarStockDTO();

        // When
        dto.setCantidad(5);

        // Then
        assertEquals(5, dto.getCantidad());
    }

    @Test
    @DisplayName("verifica que el campo cantidad puede ser nulo")
    void nullCantidadTest() {
        // Given
        ActualizarStockDTO dto = new ActualizarStockDTO();

        // Then
        assertNull(dto.getCantidad());
    }

    @Test
    @DisplayName("verifica que se pueden establecer cantidades negativas")
    void negativeCantidadTest() {
        // Given
        ActualizarStockDTO dto = new ActualizarStockDTO();

        // When
        dto.setCantidad(-3);

        // Then
        assertEquals(-3, dto.getCantidad());
    }
}