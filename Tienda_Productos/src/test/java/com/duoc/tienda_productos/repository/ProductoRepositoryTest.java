package com.duoc.tienda_productos.repository;

import com.duoc.tienda_productos.model.Producto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ProductoRepositoryTest {

    @Autowired
    private ProductoRepository productoRepository;

    @Test
    @DisplayName("verifica la busqueda de productos activos")
    void findByActivoTrueTest() {
        // Given
        Producto productoActivo = new Producto();
        productoActivo.setNombre("Producto Test");
        productoActivo.setDescripcion("Descripción test");
        productoActivo.setPrecio(new BigDecimal("100.00"));
        productoActivo.setStock(10);
        productoActivo.setCategoria("Test");
        productoActivo.setActivo(true);
        productoRepository.save(productoActivo);

        // When
        List<Producto> productosActivos = productoRepository.findByActivoTrue();

        // Then
        assertFalse(productosActivos.isEmpty());
        assertTrue(productosActivos.stream()
                .allMatch(Producto::getActivo));
    }

    @Test
    @DisplayName("verifica la busqueda de productos por categoria")
    void findByCategoriaTest() {
        // Given
        String categoriaTest = "electronica";
        Producto producto = new Producto();
        producto.setNombre("Laptop Test");
        producto.setDescripcion("Descripción test");
        producto.setPrecio(new BigDecimal("100.00"));
        producto.setStock(10);
        producto.setCategoria(categoriaTest);
        producto.setActivo(true);
        productoRepository.save(producto);

        // When
        List<Producto> productos = productoRepository.findByCategoria(categoriaTest);

        // Then
        assertFalse(productos.isEmpty());
        assertEquals(categoriaTest, productos.get(0).getCategoria());
    }

    @Test
    @DisplayName("verifica la busqueda de productos por nombre")
    void findByNombreContainingIgnoreCaseTest() {
        // Given
        Producto producto = new Producto();
        producto.setNombre("Laptop Gaming");
        producto.setDescripcion("Descripción test");
        producto.setPrecio(new BigDecimal("100.00"));
        producto.setStock(10);
        producto.setCategoria("Test");
        producto.setActivo(true);
        productoRepository.save(producto);

        // When
        List<Producto> productos = productoRepository.findByNombreContainingIgnoreCase("laptop");

        // Then
        assertFalse(productos.isEmpty());
        assertTrue(productos.get(0).getNombre().toLowerCase()
                .contains("laptop"));
    }
}