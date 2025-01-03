package com.duoc.tienda_productos.config;

import com.duoc.tienda_productos.model.Producto;
import com.duoc.tienda_productos.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private DataInitializer dataInitializer;

    @Test
    @DisplayName("verifica que los datos se inicializan cuando el repositorio esta vacio")
    void initializeDataWhenEmptyTest() throws Exception {
        // Given
        when(productoRepository.count()).thenReturn(0L);
        when(productoRepository.save(any(Producto.class))).thenAnswer(i -> i.getArgument(0));

        // When
        dataInitializer.run();

        // Then
        verify(productoRepository, times(3)).save(any(Producto.class));
    }

    @Test
    @DisplayName("verifica que los datos no se inicializan cuando ya existen productos")
    void skipInitializationWhenNotEmptyTest() throws Exception {
        // Given
        when(productoRepository.count()).thenReturn(5L);

        // When
        dataInitializer.run();

        // Then
        verify(productoRepository, never()).save(any(Producto.class));
    }
}