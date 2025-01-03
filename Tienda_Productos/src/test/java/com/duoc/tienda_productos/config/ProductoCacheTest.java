package com.duoc.tienda_productos.config;

import com.duoc.tienda_productos.model.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class ProductoCacheTest {

    private ProductoCache productoCache;
    private Producto producto;

    @BeforeEach
    void setUp() {
        productoCache = ProductoCache.getInstance();
        productoCache.clearCache();

        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Laptop Test");
        producto.setDescripcion("Descripción test");
        producto.setPrecio(new BigDecimal("100.00"));
        producto.setStock(10);
        producto.setCategoria("Computadores");
        producto.setActivo(true);
    }

    @Test
    @DisplayName("verifica que el singleton retorna la misma instancia")
    void singletonInstanceTest() {
        // When
        ProductoCache instance1 = ProductoCache.getInstance();
        ProductoCache instance2 = ProductoCache.getInstance();

        // Then
        assertSame(instance1, instance2);
    }

    @Test
    @DisplayName("verifica la adicion y obtencion de productos del cache")
    void addAndGetProductoTest() {
        // When
        productoCache.addProducto(producto);
        Optional<Producto> cachedProducto = productoCache.getProducto(1L);

        // Then
        assertTrue(cachedProducto.isPresent());
        assertEquals(producto.getNombre(), cachedProducto.get().getNombre());
    }

    @Test
    @DisplayName("verifica la busqueda de productos por categoria")
    void getProductosPorCategoriaTest() {
        // Given
        productoCache.addProducto(producto);
        
        // When
        List<Producto> productos = productoCache.getProductosPorCategoria("Computadores");

        // Then
        assertFalse(productos.isEmpty());
        assertEquals("Computadores", productos.get(0).getCategoria());
    }

    @Test
    @DisplayName("verifica la busqueda de productos por nombre")
    void getProductosPorNombreTest() {
        // Given
        productoCache.addProducto(producto);
        
        // When
        List<Producto> productos = productoCache.getProductosPorNombre("laptop");

        // Then
        assertFalse(productos.isEmpty());
        assertTrue(productos.get(0).getNombre().toLowerCase().contains("laptop"));
    }

    @Test
    @DisplayName("verifica la eliminacion de productos del cache")
    void removeProductoTest() {
        // Given
        productoCache.addProducto(producto);
        
        // When
        productoCache.removeProducto(1L);
        Optional<Producto> removedProducto = productoCache.getProducto(1L);

        // Then
        assertTrue(removedProducto.isEmpty());
    }

    @Test
    @DisplayName("verifica el limite de tamaño del cache")
    void cacheSizeLimitTest() {
        // Given
        for (int i = 0; i < 101; i++) {
            Producto p = new Producto();
            p.setId((long) i);
            productoCache.addProducto(p);
        }

        // Then
        assertTrue(productoCache.getCacheSize() <= 100);
    }

    @Test
    @DisplayName("verifica que se puede limpiar el cache completamente")
    void clearCacheTest() {
        // Given
        productoCache.addProducto(producto);
        
        // When
        productoCache.clearCache();

        // Then
        assertEquals(0, productoCache.getCacheSize());
    }

    @Test
    @DisplayName("verifica la obtencion de todos los productos en cache")
    void getAllCachedProductsTest() {
        // Given
        productoCache.addProducto(producto);
        
        // When
        var allProducts = productoCache.getAllCachedProducts();

        // Then
        assertEquals(1, allProducts.size());
        assertTrue(allProducts.containsKey(1L));
    }
}