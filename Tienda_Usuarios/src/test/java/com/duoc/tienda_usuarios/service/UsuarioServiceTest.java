package com.duoc.tienda_usuarios.service;

import com.duoc.tienda_usuarios.model.Usuario;
import com.duoc.tienda_usuarios.model.Role;
import com.duoc.tienda_usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Test");
        usuario.setApellido("Usuario");
        usuario.setEmail("test@ejemplo.com");
        usuario.setPassword("password123");
        usuario.setRole(Role.CLIENTE);
        usuario.setTelefono("+56912345678");
        usuario.setDireccion("Dirección de prueba");
    }

    @Test
    @DisplayName("verifica la obtencion de todos los usuarios")
    void obtenerTodosLosUsuariosTest() {
        // Given
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario));

        // When
        var usuarios = usuarioService.obtenerTodosLosUsuarios();

        // Then
        assertFalse(usuarios.isEmpty());
        assertEquals(1, usuarios.size());
        verify(usuarioRepository).findAll();
    }

    @Test
    @DisplayName("verifica la obtencion de usuario por id")
    void obtenerUsuarioPorIdTest() {
        // Given
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // When
        var resultado = usuarioService.obtenerUsuarioPorId(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("test@ejemplo.com", resultado.get().getEmail());
    }

    @Test
    @DisplayName("verifica la creacion exitosa de usuario")
    void crearUsuarioExitosoTest() {
        // Given
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // When
        Usuario resultado = usuarioService.crearUsuario(usuario);

        // Then
        assertNotNull(resultado);
        assertEquals(usuario.getEmail(), resultado.getEmail());
        verify(usuarioRepository).existsByEmail(usuario.getEmail());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("verifica que no se puede crear usuario con email duplicado")
    void crearUsuarioEmailDuplicadoTest() {
        // Given
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            usuarioService.crearUsuario(usuario);
        });
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("verifica la actualizacion exitosa de usuario")
    void actualizarUsuarioExitosoTest() {
        // Given
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre("Nuevo Nombre");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // When
        var resultado = usuarioService.actualizarUsuario(1L, usuarioActualizado);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("Nuevo Nombre", resultado.get().getNombre());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("verifica el login exitoso")
    void loginExitosoTest() {
        // Given
        when(usuarioRepository.findByEmail(usuario.getEmail()))
            .thenReturn(Optional.of(usuario));

        // When
        var resultado = usuarioService.login(usuario.getEmail(), "password123");

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(usuario.getEmail(), resultado.get().getEmail());
    }

    @Test
    @DisplayName("verifica el login fallido por contraseña incorrecta")
    void loginFallidoTest() {
        // Given
        when(usuarioRepository.findByEmail(usuario.getEmail()))
            .thenReturn(Optional.of(usuario));

        // When
        var resultado = usuarioService.login(usuario.getEmail(), "wrongpassword");

        // Then
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("verifica el reset de password exitoso")
    void resetPasswordExitosoTest() {
        // Given
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // When
        var resultado = usuarioService.resetPassword(1L, "newpassword123");

        // Then
        assertTrue(resultado.isPresent());
        assertTrue(resultado.get());
        assertEquals("newpassword123", usuario.getPassword());
    }

    @Test
    @DisplayName("verifica la eliminacion exitosa de usuario")
    void eliminarUsuarioTest() {
        // When
        usuarioService.eliminarUsuario(1L);

        // Then
        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    @DisplayName("verifica el manejo de excepcion al eliminar usuario")
    void eliminarUsuarioExceptionTest() {
        // Given
        doThrow(new RuntimeException("Error al eliminar")).when(usuarioRepository).deleteById(1L);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            usuarioService.eliminarUsuario(1L);
        });
    }

    @Test
    @DisplayName("verifica que se lanza excepción al crear usuario con error inesperado")
    void crearUsuarioErrorInesperadoTest() {
        // Given
        when(usuarioRepository.existsByEmail(anyString())).thenThrow(new RuntimeException("Error inesperado"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            usuarioService.crearUsuario(usuario);
        });
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("verifica la actualización de campos individuales de usuario")
    void actualizarCamposIndividualesDeUsuarioTest() {
        // Given
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre("Nuevo Nombre");
        usuarioActualizado.setApellido("Nuevo Apellido");
        usuarioActualizado.setTelefono("987654321");
        usuarioActualizado.setDireccion("Nueva Dirección");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // When
        var resultado = usuarioService.actualizarUsuario(1L, usuarioActualizado);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("Nuevo Nombre", resultado.get().getNombre());
        assertEquals("Nuevo Apellido", resultado.get().getApellido());
        assertEquals("987654321", resultado.get().getTelefono());
        assertEquals("Nueva Dirección", resultado.get().getDireccion());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("verifica que se lanza excepción al resetear password con error inesperado")
    void resetPasswordErrorInesperadoTest() {
        // Given
        when(usuarioRepository.findById(1L)).thenThrow(new RuntimeException("Error inesperado"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            usuarioService.resetPassword(1L, "newpassword123");
        });
    }

    @Test
    @DisplayName("verifica que se lanza excepción al actualizar usuario no encontrado")
    void actualizarUsuarioNoEncontradoTest() {
        // Given
        Usuario usuarioActualizado = new Usuario();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());
    
        // When & Then
        var resultado = usuarioService.actualizarUsuario(1L, usuarioActualizado);
        assertTrue(resultado.isEmpty());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }    

    @Test
    @DisplayName("verifica que se lanza excepción al actualizar usuario con error inesperado")
    void actualizarUsuarioErrorInesperadoTest() {
        // Given
        Usuario usuarioActualizado = new Usuario();
        when(usuarioRepository.findById(1L)).thenThrow(new RuntimeException("Error inesperado"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            usuarioService.actualizarUsuario(1L, usuarioActualizado);
        });
    }

    @Test
    @DisplayName("verifica el login fallido por email no encontrado")
    void loginEmailNoEncontradoTest() {
        // Given
        when(usuarioRepository.findByEmail(usuario.getEmail()))
            .thenReturn(Optional.empty());

        // When
        var resultado = usuarioService.login(usuario.getEmail(), "password123");

        // Then
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("verifica que se lanza excepción al hacer login con error inesperado")
    void loginErrorInesperadoTest() {
        // Given
        when(usuarioRepository.findByEmail(usuario.getEmail()))
            .thenThrow(new RuntimeException("Error inesperado"));
    
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            usuarioService.login(usuario.getEmail(), "password123");
        });
    }    
}