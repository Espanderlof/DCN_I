package com.duoc.tienda_productos.service;

import com.duoc.tienda_productos.model.Producto;
import com.duoc.tienda_productos.repository.ProductoRepository;
import com.duoc.tienda_productos.dto.ProductoDTO;
import com.duoc.tienda_productos.config.ProductoCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;
    private ProductoDTO productoDTO;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto Test");
        producto.setDescripcion("Descripción test");
        producto.setPrecio(new BigDecimal("100.00"));
        producto.setStock(10);
        producto.setCategoria("Test");
        producto.setActivo(true);

        productoDTO = new ProductoDTO();
        productoDTO.setNombre("Producto Test");
        productoDTO.setDescripcion("Descripción test");
        productoDTO.setPrecio(new BigDecimal("100.00"));
        productoDTO.setStock(10);
        productoDTO.setCategoria("Test");
    }

    @Test
    @DisplayName("verifica la obtencion de todos los productos activos")
    void obtenerTodosLosProductosTest() {
        // Given
        when(productoRepository.findByActivoTrue()).thenReturn(Arrays.asList(producto));

        // When
        List<Producto> productos = productoService.obtenerTodosLosProductos();

        // Then
        assertFalse(productos.isEmpty());
        assertEquals(1, productos.size());
        verify(productoRepository).findByActivoTrue();
    }

    @Test
    @DisplayName("verifica la obtencion de producto por id desde cache")
    void obtenerProductoPorIdFromCacheTest() {
        // Given
        ProductoCache.getInstance().addProducto(producto);
        
        // When
        Optional<Producto> resultado = productoService.obtenerProductoPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verify(productoRepository, never()).findById(any());
    }

    @Test
    @DisplayName("verifica la creacion de un nuevo producto")
    void crearProductoTest() {
        // Given
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        // When
        Producto resultado = productoService.crearProducto(productoDTO);

        // Then
        assertNotNull(resultado);
        assertEquals(productoDTO.getNombre(), resultado.getNombre());
        assertTrue(resultado.getActivo());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    @DisplayName("verifica la actualizacion de un producto existente")
    void actualizarProductoTest() {
        // Given
        Producto productoExistente = new Producto();
        productoExistente.setId(1L);
        productoExistente.setNombre("Producto Test");
        productoExistente.setDescripcion("Descripción test");
        productoExistente.setPrecio(new BigDecimal("100.00"));
        productoExistente.setStock(10);
        productoExistente.setCategoria("Test");
        productoExistente.setActivo(true);
    
        Producto productoActualizado = new Producto();
        productoActualizado.setId(1L);
        productoActualizado.setNombre("Nombre Actualizado");
        productoActualizado.setDescripcion("Descripción test");
        productoActualizado.setPrecio(new BigDecimal("100.00"));
        productoActualizado.setStock(10);
        productoActualizado.setCategoria("Test");
        productoActualizado.setActivo(true);
    
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setNombre("Nombre Actualizado");
        productoDTO.setDescripcion("Descripción test");
        productoDTO.setPrecio(new BigDecimal("100.00"));
        productoDTO.setStock(10);
        productoDTO.setCategoria("Test");
    
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoExistente));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoActualizado);
    
        // When
        Optional<Producto> resultado = productoService.actualizarProducto(1L, productoDTO);
    
        // Then
        assertTrue(resultado.isPresent());
        assertEquals("Nombre Actualizado", resultado.get().getNombre());
        verify(productoRepository).findById(1L);
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    @DisplayName("verifica la actualizacion del stock de un producto")
    void actualizarStockTest() {
        // Given
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        // When
        Optional<Producto> resultado = productoService.actualizarStock(1L, 20);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(20, resultado.get().getStock());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    @DisplayName("verifica la eliminacion de un producto")
    void eliminarProductoTest() {
        // When
        productoService.eliminarProducto(1L);

        // Then
        verify(productoRepository).deleteById(1L);
        assertFalse(ProductoCache.getInstance().getProducto(1L).isPresent());
    }

    @Test
    @DisplayName("verifica la obtencion de producto por id desde base de datos cuando no esta en cache")
    void obtenerProductoPorIdFromDBTest() {
        // Given
        ProductoCache.getInstance().clearCache();
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        // When
        Optional<Producto> resultado = productoService.obtenerProductoPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verify(productoRepository).findById(1L);
        assertTrue(ProductoCache.getInstance().getProducto(1L).isPresent());
    }

    @Test
    @DisplayName("verifica que no se obtiene un producto inactivo por id")
    void obtenerProductoInactivoPorIdTest() {
        // Given
        Producto productoInactivo = new Producto();
        productoInactivo.setId(1L);
        productoInactivo.setNombre("Producto Test");
        productoInactivo.setDescripcion("Descripción test");
        productoInactivo.setPrecio(new BigDecimal("100.00"));
        productoInactivo.setStock(10);
        productoInactivo.setCategoria("Test");
        productoInactivo.setActivo(false);
        
        ProductoCache.getInstance().clearCache();  // Limpiamos el caché primero
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoInactivo));
    
        // When
        Optional<Producto> resultado = productoService.obtenerProductoPorId(1L);
    
        // Then
        assertFalse(resultado.isPresent(), "No debería retornar un producto inactivo");
        verify(productoRepository).findById(1L);
    }

    @Test
    @DisplayName("verifica la busqueda de productos por categoria y actualizacion del cache")
    void buscarPorCategoriaTest() {
        // Given
        List<Producto> productos = Arrays.asList(producto);
        when(productoRepository.findByCategoria("Test")).thenReturn(productos);
        ProductoCache.getInstance().clearCache();

        // When
        List<Producto> resultado = productoService.buscarPorCategoria("Test");

        // Then
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Test", resultado.get(0).getCategoria());
        assertTrue(ProductoCache.getInstance().getProducto(1L).isPresent());
        verify(productoRepository).findByCategoria("Test");
    }

    @Test
    @DisplayName("verifica la busqueda de productos por nombre y actualizacion del cache")
    void buscarPorNombreTest() {
        // Given
        List<Producto> productos = Arrays.asList(producto);
        when(productoRepository.findByNombreContainingIgnoreCase("Test")).thenReturn(productos);
        ProductoCache.getInstance().clearCache();

        // When
        List<Producto> resultado = productoService.buscarPorNombre("Test");

        // Then
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getNombre().contains("Test"));
        assertTrue(ProductoCache.getInstance().getProducto(1L).isPresent());
        verify(productoRepository).findByNombreContainingIgnoreCase("Test");
    }

    @Test
    @DisplayName("verifica que retorna lista vacia cuando no hay productos en la categoria")
    void buscarPorCategoriaVaciaTest() {
        // Given
        when(productoRepository.findByCategoria("CategoriaInexistente")).thenReturn(Collections.emptyList());

        // When
        List<Producto> resultado = productoService.buscarPorCategoria("CategoriaInexistente");

        // Then
        assertTrue(resultado.isEmpty());
        verify(productoRepository).findByCategoria("CategoriaInexistente");
    }

    @Test
    @DisplayName("verifica que retorna lista vacia cuando no hay productos con el nombre")
    void buscarPorNombreVacioTest() {
        // Given
        when(productoRepository.findByNombreContainingIgnoreCase("NombreInexistente")).thenReturn(Collections.emptyList());

        // When
        List<Producto> resultado = productoService.buscarPorNombre("NombreInexistente");

        // Then
        assertTrue(resultado.isEmpty());
        verify(productoRepository).findByNombreContainingIgnoreCase("NombreInexistente");
    }

    @Test
    @DisplayName("verifica actualizacion con algunos campos nulos manteniendo valores existentes")
    void actualizarProductoCamposNulosTest() {
        // Given
        Producto productoExistente = new Producto();
        productoExistente.setId(1L);
        productoExistente.setNombre("Nombre Original");
        productoExistente.setDescripcion("Descripción Original");
        productoExistente.setPrecio(new BigDecimal("100.00"));
        productoExistente.setStock(10);
        productoExistente.setCategoria("Categoría Original");
        productoExistente.setImagenUrl("url-original");
        productoExistente.setActivo(true);

        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setNombre("Nuevo Nombre");
        productoDTO.setDescripcion(null);  // Mantener descripción original
        productoDTO.setPrecio(null);       // Mantener precio original
        productoDTO.setStock(20);          // Actualizar stock
        productoDTO.setCategoria(null);    // Mantener categoría original
        productoDTO.setImagenUrl(null);    // Mantener imagen original

        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoExistente));
        when(productoRepository.save(any(Producto.class))).thenAnswer(i -> i.getArgument(0));

        // When
        Optional<Producto> resultado = productoService.actualizarProducto(1L, productoDTO);

        // Then
        assertTrue(resultado.isPresent());
        Producto productoActualizado = resultado.get();
        assertEquals("Nuevo Nombre", productoActualizado.getNombre());
        assertEquals("Descripción Original", productoActualizado.getDescripcion());
        assertEquals(new BigDecimal("100.00"), productoActualizado.getPrecio());
        assertEquals(20, productoActualizado.getStock());
        assertEquals("Categoría Original", productoActualizado.getCategoria());
        assertEquals("url-original", productoActualizado.getImagenUrl());
    }

    @Test
    @DisplayName("verifica actualizacion con todos los campos nuevos")
    void actualizarProductoTodosCamposNuevosTest() {
        // Given
        Producto productoExistente = new Producto();
        productoExistente.setId(1L);
        productoExistente.setNombre("Nombre Original");
        productoExistente.setDescripcion("Descripción Original");
        productoExistente.setPrecio(new BigDecimal("100.00"));
        productoExistente.setStock(10);
        productoExistente.setCategoria("Categoría Original");
        productoExistente.setImagenUrl("url-original");
        productoExistente.setActivo(true);

        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setNombre("Nuevo Nombre");
        productoDTO.setDescripcion("Nueva Descripción");
        productoDTO.setPrecio(new BigDecimal("150.00"));
        productoDTO.setStock(20);
        productoDTO.setCategoria("Nueva Categoría");
        productoDTO.setImagenUrl("nueva-url");

        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoExistente));
        when(productoRepository.save(any(Producto.class))).thenAnswer(i -> i.getArgument(0));

        // When
        Optional<Producto> resultado = productoService.actualizarProducto(1L, productoDTO);

        // Then
        assertTrue(resultado.isPresent());
        Producto productoActualizado = resultado.get();
        assertEquals("Nuevo Nombre", productoActualizado.getNombre());
        assertEquals("Nueva Descripción", productoActualizado.getDescripcion());
        assertEquals(new BigDecimal("150.00"), productoActualizado.getPrecio());
        assertEquals(20, productoActualizado.getStock());
        assertEquals("Nueva Categoría", productoActualizado.getCategoria());
        assertEquals("nueva-url", productoActualizado.getImagenUrl());
    }

    @Test
    @DisplayName("verifica actualizacion con todos los campos nulos mantiene valores originales")
    void actualizarProductoTodosCamposNulosTest() {
        // Given
        Producto productoExistente = new Producto();
        productoExistente.setId(1L);
        productoExistente.setNombre("Nombre Original");
        productoExistente.setDescripcion("Descripción Original");
        productoExistente.setPrecio(new BigDecimal("100.00"));
        productoExistente.setStock(10);
        productoExistente.setCategoria("Categoría Original");
        productoExistente.setImagenUrl("url-original");
        productoExistente.setActivo(true);

        ProductoDTO productoDTO = new ProductoDTO();
        // Todos los campos son null

        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoExistente));
        when(productoRepository.save(any(Producto.class))).thenAnswer(i -> i.getArgument(0));

        // When
        Optional<Producto> resultado = productoService.actualizarProducto(1L, productoDTO);

        // Then
        assertTrue(resultado.isPresent());
        Producto productoActualizado = resultado.get();
        assertEquals("Nombre Original", productoActualizado.getNombre());
        assertEquals("Descripción Original", productoActualizado.getDescripcion());
        assertEquals(new BigDecimal("100.00"), productoActualizado.getPrecio());
        assertEquals(10, productoActualizado.getStock());
        assertEquals("Categoría Original", productoActualizado.getCategoria());
        assertEquals("url-original", productoActualizado.getImagenUrl());
    }
}