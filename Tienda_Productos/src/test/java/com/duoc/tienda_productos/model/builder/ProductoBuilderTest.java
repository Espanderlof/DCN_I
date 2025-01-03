package com.duoc.tienda_productos.model.builder;

import com.duoc.tienda_productos.model.Producto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ProductoBuilderTest {

    @Test
    @DisplayName("verifica la construccion de un producto usando el builder")
    void buildProductoTest() {
        // When
        Producto producto = new ProductoBuilder()
            .conNombre("laptop gaming")
            .conDescripcion("laptop para juegos")
            .conPrecio(new BigDecimal("999.99"))
            .conStock(10)
            .conCategoria("electronica")
            .conImagenUrl("http://ejemplo.com/imagen.jpg")
            .activo(true)
            .build();

        // Then
        assertEquals("laptop gaming", producto.getNombre());
        assertEquals("laptop para juegos", producto.getDescripcion());
        assertEquals(new BigDecimal("999.99"), producto.getPrecio());
        assertEquals(10, producto.getStock());
        assertEquals("electronica", producto.getCategoria());
        assertEquals("http://ejemplo.com/imagen.jpg", producto.getImagenUrl());
        assertTrue(producto.getActivo());
        assertNotNull(producto.getFechaCreacion());
        assertNotNull(producto.getUltimaActualizacion());
    }
}