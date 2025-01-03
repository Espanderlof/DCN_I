package com.duoc.tienda_usuarios.repository;

import com.duoc.tienda_usuarios.model.Usuario;
import com.duoc.tienda_usuarios.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import jakarta.persistence.EntityManager;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EntityManager entityManager;

    private Usuario createTestUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Test");
        usuario.setApellido("Usuario");
        usuario.setEmail("test@ejemplo.com");
        usuario.setPassword("password123");
        usuario.setRole(Role.CLIENTE);
        usuario.setTelefono("+56912345678");
        usuario.setDireccion("Dirección de prueba");
        return usuario;
    }

    @BeforeEach
    void setUp() {
        // Limpiar la base de datos antes de cada prueba
        usuarioRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("verifica la busqueda de usuario por email existente")
    void findByEmailExistenteTest() {
        // Given
        Usuario usuario = createTestUsuario();
        entityManager.persist(usuario);
        entityManager.flush();

        // When
        var usuarioEncontrado = usuarioRepository.findByEmail("test@ejemplo.com");

        // Then
        assertTrue(usuarioEncontrado.isPresent());
        assertEquals("test@ejemplo.com", usuarioEncontrado.get().getEmail());
    }

    @Test
    @DisplayName("verifica la busqueda de usuario por email inexistente")
    void findByEmailInexistenteTest() {
        // When
        var resultado = usuarioRepository.findByEmail("noexiste@ejemplo.com");

        // Then
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("verifica la existencia de usuario por email")
    void existsByEmailTest() {
        // Given
        Usuario usuario = createTestUsuario();
        entityManager.persist(usuario);
        entityManager.flush();

        // When
        boolean existe = usuarioRepository.existsByEmail("test@ejemplo.com");
        boolean noExiste = usuarioRepository.existsByEmail("noexiste@ejemplo.com");

        // Then
        assertTrue(existe);
        assertFalse(noExiste);
    }
}