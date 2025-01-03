package com.duoc.tienda_usuarios.controller;

import com.duoc.tienda_usuarios.model.Usuario;
import com.duoc.tienda_usuarios.model.Role;
import com.duoc.tienda_usuarios.model.LoginRequest;
import com.duoc.tienda_usuarios.dto.PasswordResetDTO;
import com.duoc.tienda_usuarios.dto.UsuarioActualizacionDTO;
import com.duoc.tienda_usuarios.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    private Usuario createTestUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Test");
        usuario.setApellido("Usuario");
        usuario.setEmail("test@ejemplo.com");
        usuario.setPassword("password123");
        usuario.setRole(Role.CLIENTE);
        usuario.setTelefono("+56912345678");
        usuario.setDireccion("Dirección de prueba");
        return usuario;
    }

    @Test
    @DisplayName("verifica la obtencion de todos los usuarios")
    void obtenerTodosLosUsuariosTest() throws Exception {
        // Given
        when(usuarioService.obtenerTodosLosUsuarios())
            .thenReturn(Arrays.asList(createTestUsuario()));

        // When & Then
        mockMvc.perform(get("/api/usuarios"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].email").value("test@ejemplo.com"));
    }

    @Test
    @DisplayName("verifica la obtencion de usuario por id")
    void obtenerUsuarioPorIdTest() throws Exception {
        // Given
        when(usuarioService.obtenerUsuarioPorId(1L))
            .thenReturn(Optional.of(createTestUsuario()));

        // When & Then
        mockMvc.perform(get("/api/usuarios/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("test@ejemplo.com"));
    }

    @Test
    @DisplayName("verifica la obtencion de usuario inexistente")
    void obtenerUsuarioInexistenteTest() throws Exception {
        // Given
        when(usuarioService.obtenerUsuarioPorId(99L))
            .thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/usuarios/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("verifica la creacion exitosa de usuario")
    void crearUsuarioTest() throws Exception {
        // Given
        Usuario usuario = createTestUsuario();
        when(usuarioService.crearUsuario(any(Usuario.class)))
            .thenReturn(usuario);

        // When & Then
        mockMvc.perform(post("/api/usuarios")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(usuario)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("test@ejemplo.com"));
    }

    @Test
    @DisplayName("verifica la actualizacion exitosa de usuario")
    void actualizarUsuarioTest() throws Exception {
        // Given
        Usuario usuarioActualizado = createTestUsuario();
        usuarioActualizado.setNombre("Nombre Actualizado");
        UsuarioActualizacionDTO dto = new UsuarioActualizacionDTO();
        dto.setNombre("Nombre Actualizado");

        when(usuarioService.actualizarUsuario(eq(1L), any(Usuario.class)))
            .thenReturn(Optional.of(usuarioActualizado));

        // When & Then
        mockMvc.perform(put("/api/usuarios/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Nombre Actualizado"));
    }

    @Test
    @DisplayName("verifica la eliminacion exitosa de usuario")
    void eliminarUsuarioTest() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/usuarios/1"))
            .andExpect(status().isOk());

        verify(usuarioService).eliminarUsuario(1L);
    }

    @Test
    @DisplayName("verifica el login exitoso")
    void loginExitosoTest() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@ejemplo.com");
        loginRequest.setPassword("password123");

        when(usuarioService.login("test@ejemplo.com", "password123"))
            .thenReturn(Optional.of(createTestUsuario()));

        // When & Then
        mockMvc.perform(post("/api/usuarios/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("test@ejemplo.com"));
    }

    @Test
    @DisplayName("verifica el login fallido")
    void loginFallidoTest() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@ejemplo.com");
        loginRequest.setPassword("wrongpassword");

        when(usuarioService.login("test@ejemplo.com", "wrongpassword"))
            .thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(post("/api/usuarios/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("verifica el reset de password exitoso")
    void resetPasswordTest() throws Exception {
        // Given
        PasswordResetDTO passwordDTO = new PasswordResetDTO();
        passwordDTO.setNewPassword("newpassword123");

        when(usuarioService.resetPassword(1L, "newpassword123"))
            .thenReturn(Optional.of(true));

        // When & Then
        mockMvc.perform(put("/api/usuarios/1/reset-password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(passwordDTO)))
            .andExpect(status().isOk());
    }
}