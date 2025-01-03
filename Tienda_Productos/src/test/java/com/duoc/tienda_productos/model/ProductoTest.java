package com.duoc.tienda_productos.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ProductoTest {
    
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("verifica la creacion de un producto valido")
    void createValidProductoTest() {
        // Given
        Producto producto = new Producto();
        producto.setNombre("laptop gaming");
        producto.setDescripcion("laptop para juegos");
        producto.setPrecio(new BigDecimal("999.99"));
        producto.setStock(10);
        producto.setCategoria("electronica");
        producto.setImagenUrl("http://ejemplo.com/imagen.jpg");
        producto.setActivo(true);

        // When
        var violations = validator.validate(producto);

        // Then
        assertTrue(violations.isEmpty());
        assertEquals("laptop gaming", producto.getNombre());
        assertEquals(new BigDecimal("999.99"), producto.getPrecio());
    }

    @Test
    @DisplayName("verifica las validaciones de campos obligatorios")
    void validateRequiredFieldsTest() {
        // Given
        Producto producto = new Producto();

        // When
        var violations = validator.validate(producto);

        // Then
        assertEquals(4, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("nombre es obligatorio")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("descripción es obligatoria")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("precio es obligatorio")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("categoría es obligatoria")));
    }

    @Test
    @DisplayName("verifica la validacion de precio minimo")
    void validateMinPriceTest() {
        // Given
        Producto producto = new Producto();
        producto.setNombre("producto");
        producto.setDescripcion("descripcion");
        producto.setPrecio(new BigDecimal("-1.0"));
        producto.setCategoria("categoria");

        // When
        var violations = validator.validate(producto);

        // Then
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("precio debe ser mayor que 0")));
    }

    @Test
    @DisplayName("verifica que el stock no puede ser negativo")
    void validateNegativeStockTest() {
        // Given
        Producto producto = new Producto();
        producto.setStock(-1);

        // When
        var violations = validator.validate(producto);

        // Then
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("stock no puede ser negativo")));
    }

    @Test
    @DisplayName("verifica que se establecen las fechas al crear")
    void onCreateTest() {
        // Given
        Producto producto = new Producto();

        // When
        producto.onCreate();

        // Then
        assertNotNull(producto.getFechaCreacion());
        assertNotNull(producto.getUltimaActualizacion());
    }

    @Test
    @DisplayName("verifica que se actualiza la fecha al modificar")
    void onUpdateTest() {
        // Given
        Producto producto = new Producto();
        producto.onCreate();
        LocalDateTime primeraActualizacion = producto.getUltimaActualizacion();

        // When
        try {
            Thread.sleep(1); // Asegura diferencia en timestamps
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        producto.onUpdate();

        // Then
        assertTrue(producto.getUltimaActualizacion().isAfter(primeraActualizacion));
    }
}