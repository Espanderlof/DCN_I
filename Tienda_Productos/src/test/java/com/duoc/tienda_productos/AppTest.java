package com.duoc.tienda_productos;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

class AppTest {
    
    @Test
    @DisplayName("verifica que la aplicacion spring boot inicia correctamente")
    void mainTest() {
        try (var mockedSpringApplication = mockStatic(SpringApplication.class)) {
            // Given
            String[] args = {};
            
            // When
            App.main(args);
            
            // Then
            mockedSpringApplication.verify(
                () -> SpringApplication.run(eq(App.class), any(String[].class))
            );
        }
    }

    @Test
    @DisplayName("verifica que se puede crear una instancia de la clase app")
    void constructorTest() {
        // When
        App app = new App();
        
        // Then
        assertNotNull(app, "la instancia de app no deberia ser null");
    }
}