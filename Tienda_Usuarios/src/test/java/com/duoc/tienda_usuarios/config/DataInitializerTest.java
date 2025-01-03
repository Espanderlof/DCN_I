package com.duoc.tienda_usuarios.config;

import com.duoc.tienda_usuarios.model.Usuario;
import com.duoc.tienda_usuarios.model.Role;
import com.duoc.tienda_usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private DataInitializer dataInitializer;

    @Test
    @DisplayName("verifica la inicializacion cuando no hay usuarios")
    void initializeEmptyDatabaseTest() throws Exception {
        // Given
        when(usuarioRepository.count()).thenReturn(0L);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        // When
        dataInitializer.run();

        // Then
        verify(usuarioRepository, times(1)).count();
        verify(usuarioRepository, times(3)).save(any(Usuario.class));
        
        // Verificar que se guardó un admin y dos clientes
        verify(usuarioRepository, times(3)).save(argThat(usuario -> 
            (usuario.getRole() == Role.ADMIN && usuario.getEmail().equals("admin@tienda.com")) ||
            (usuario.getRole() == Role.CLIENTE && 
                (usuario.getEmail().equals("juan@email.com") || 
                 usuario.getEmail().equals("maria@email.com")))
        ));
    }

    @Test
    @DisplayName("verifica que no se inicializa cuando ya existen usuarios")
    void skipInitializationTest() throws Exception {
        // Given
        when(usuarioRepository.count()).thenReturn(3L);

        // When
        dataInitializer.run();

        // Then
        verify(usuarioRepository, times(1)).count();
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("verifica los datos del usuario administrador")
    void adminUserDataTest() throws Exception {
        // Given
        when(usuarioRepository.count()).thenReturn(0L);

        // When
        dataInitializer.run();

        // Then
        verify(usuarioRepository).save(argThat(usuario -> 
            usuario.getRole() == Role.ADMIN &&
            usuario.getEmail().equals("admin@tienda.com") &&
            usuario.getNombre().equals("Admin") &&
            usuario.getApellido().equals("Sistema") &&
            usuario.getTelefono().equals("+56912345678") &&
            usuario.getDireccion().equals("Oficina Central")
        ));
    }

    @Test
    @DisplayName("verifica los datos de los usuarios cliente")
    void clienteUsersDataTest() throws Exception {
        // Given
        when(usuarioRepository.count()).thenReturn(0L);

        // When
        dataInitializer.run();

        // Then
        verify(usuarioRepository).save(argThat(usuario -> 
            usuario.getRole() == Role.CLIENTE &&
            usuario.getEmail().equals("juan@email.com") &&
            usuario.getNombre().equals("Juan") &&
            usuario.getApellido().equals("Pérez")
        ));

        verify(usuarioRepository).save(argThat(usuario -> 
            usuario.getRole() == Role.CLIENTE &&
            usuario.getEmail().equals("maria@email.com") &&
            usuario.getNombre().equals("María") &&
            usuario.getApellido().equals("González")
        ));
    }

    @Test
    @DisplayName("verifica el manejo de excepciones durante la inicializacion")
    void handleExceptionDuringInitializationTest() throws Exception {
        // Given
        when(usuarioRepository.count()).thenReturn(0L);
        when(usuarioRepository.save(any(Usuario.class)))
            .thenThrow(new RuntimeException("Error de base de datos"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            dataInitializer.run();
        });
        
        // Verificar que se intentó guardar al menos un usuario
        verify(usuarioRepository).save(any(Usuario.class));
    }
}