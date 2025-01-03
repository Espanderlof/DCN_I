package com.duoc.tienda_productos.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ProductoDTOTest {

    @Test
    @DisplayName("verifica la creacion y manipulacion de producto dto")
    void productoDTOTest() {
        // Given
        ProductoDTO dto = new ProductoDTO();

        // When
        dto.setNombre("laptop gaming");
        dto.setDescripcion("laptop para juegos");
        dto.setPrecio(new BigDecimal("999.99"));
        dto.setStock(10);
        dto.setCategoria("electronica");
        dto.setImagenUrl("http://ejemplo.com/imagen.jpg");

        // Then
        assertEquals("laptop gaming", dto.getNombre());
        assertEquals("laptop para juegos", dto.getDescripcion());
        assertEquals(new BigDecimal("999.99"), dto.getPrecio());
        assertEquals(10, dto.getStock());
        assertEquals("electronica", dto.getCategoria());
        assertEquals("http://ejemplo.com/imagen.jpg", dto.getImagenUrl());
    }

    @Test
    @DisplayName("verifica que los campos pueden ser nulos")
    void nullFieldsTest() {
        // Given
        ProductoDTO dto = new ProductoDTO();

        // Then
        assertNull(dto.getNombre());
        assertNull(dto.getDescripcion());
        assertNull(dto.getPrecio());
        assertNull(dto.getStock());
        assertNull(dto.getCategoria());
        assertNull(dto.getImagenUrl());
    }
}