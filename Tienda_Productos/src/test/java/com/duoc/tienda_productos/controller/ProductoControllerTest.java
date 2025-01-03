package com.duoc.tienda_productos.controller;

import com.duoc.tienda_productos.model.Producto;
import com.duoc.tienda_productos.service.ProductoService;
import com.duoc.tienda_productos.dto.ProductoDTO;
import com.duoc.tienda_productos.dto.ActualizarStockDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductoService productoService;

    private Producto crearProductoPrueba() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto Test");
        producto.setDescripcion("Descripción test");
        producto.setPrecio(new BigDecimal("100.00"));
        producto.setStock(10);
        producto.setCategoria("Test");
        producto.setActivo(true);
        return producto;
    }

    @Test
    @DisplayName("verifica la obtencion de todos los productos")
    void obtenerTodosLosProductosTest() throws Exception {
        // Given
        when(productoService.obtenerTodosLosProductos())
            .thenReturn(Arrays.asList(crearProductoPrueba()));

        // When & Then
        mockMvc.perform(get("/api/productos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nombre").value("Producto Test"))
            .andExpect(jsonPath("$[0].activo").value(true));
    }

    @Test
    @DisplayName("verifica la obtencion de un producto por id")
    void obtenerProductoPorIdTest() throws Exception {
        // Given
        when(productoService.obtenerProductoPorId(1L))
            .thenReturn(Optional.of(crearProductoPrueba()));

        // When & Then
        mockMvc.perform(get("/api/productos/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nombre").value("Producto Test"));
    }

    @Test
    @DisplayName("verifica la creacion de un nuevo producto")
    void crearProductoTest() throws Exception {
        // Given
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setNombre("Producto Test");
        productoDTO.setDescripcion("Descripción test");
        productoDTO.setPrecio(new BigDecimal("100.00"));
        productoDTO.setStock(10);
        productoDTO.setCategoria("Test");

        when(productoService.crearProducto(any(ProductoDTO.class)))
            .thenReturn(crearProductoPrueba());

        // When & Then
        mockMvc.perform(post("/api/productos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productoDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Producto Test"));
    }

    @Test
    @DisplayName("verifica la actualizacion de stock de un producto")
    void actualizarStockTest() throws Exception {
        // Given
        ActualizarStockDTO stockDTO = new ActualizarStockDTO();
        stockDTO.setCantidad(20);

        Producto productoActualizado = crearProductoPrueba();
        productoActualizado.setStock(20);

        when(productoService.actualizarStock(anyLong(), any(Integer.class)))
            .thenReturn(Optional.of(productoActualizado));

        // When & Then
        mockMvc.perform(patch("/api/productos/1/stock")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(stockDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.stock").value(20));
    }

    @Test
    @DisplayName("verifica la eliminacion de un producto")
    void eliminarProductoTest() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/productos/1"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("verifica la busqueda de productos por categoria")
    void buscarPorCategoriaTest() throws Exception {
        // Given
        Producto producto = crearProductoPrueba();
        when(productoService.buscarPorCategoria("Test"))
            .thenReturn(Arrays.asList(producto));

        // When & Then
        mockMvc.perform(get("/api/productos/categoria/Test"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nombre").value("Producto Test"))
            .andExpect(jsonPath("$[0].categoria").value("Test"));
    }

    @Test
    @DisplayName("verifica la busqueda de productos por categoria sin resultados")
    void buscarPorCategoriaSinResultadosTest() throws Exception {
        // Given
        when(productoService.buscarPorCategoria("CategoriaInexistente"))
            .thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/productos/categoria/CategoriaInexistente"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("verifica la busqueda de productos por nombre")
    void buscarPorNombreTest() throws Exception {
        // Given
        Producto producto = crearProductoPrueba();
        when(productoService.buscarPorNombre("Test"))
            .thenReturn(Arrays.asList(producto));

        // When & Then
        mockMvc.perform(get("/api/productos/buscar")
            .param("nombre", "Test"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nombre").value("Producto Test"));
    }

    @Test
    @DisplayName("verifica la busqueda de productos por nombre sin resultados")
    void buscarPorNombreSinResultadosTest() throws Exception {
        // Given
        when(productoService.buscarPorNombre("ProductoInexistente"))
            .thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/productos/buscar")
            .param("nombre", "ProductoInexistente"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("verifica la actualizacion exitosa de un producto")
    void actualizarProductoTest() throws Exception {
        // Given
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setNombre("Producto Actualizado");
        productoDTO.setDescripcion("Nueva descripción");
        productoDTO.setPrecio(new BigDecimal("150.00"));
        productoDTO.setStock(15);
        productoDTO.setCategoria("Test");

        Producto productoActualizado = new Producto();
        productoActualizado.setId(1L);
        productoActualizado.setNombre("Producto Actualizado");
        productoActualizado.setDescripcion("Nueva descripción");
        productoActualizado.setPrecio(new BigDecimal("150.00"));
        productoActualizado.setStock(15);
        productoActualizado.setCategoria("Test");
        productoActualizado.setActivo(true);

        when(productoService.actualizarProducto(eq(1L), any(ProductoDTO.class)))
            .thenReturn(Optional.of(productoActualizado));

        // When & Then
        mockMvc.perform(put("/api/productos/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productoDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Producto Actualizado"))
            .andExpect(jsonPath("$.precio").value(150.00))
            .andExpect(jsonPath("$.stock").value(15));
    }

    @Test
    @DisplayName("verifica la actualizacion de un producto inexistente")
    void actualizarProductoNoEncontradoTest() throws Exception {
        // Given
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setNombre("Producto Actualizado");
        productoDTO.setDescripcion("Nueva descripción");
        productoDTO.setPrecio(new BigDecimal("150.00"));
        productoDTO.setStock(15);
        productoDTO.setCategoria("Test");

        when(productoService.actualizarProducto(eq(999L), any(ProductoDTO.class)))
            .thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(put("/api/productos/999")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productoDTO)))
            .andExpect(status().isNotFound());
    }
}