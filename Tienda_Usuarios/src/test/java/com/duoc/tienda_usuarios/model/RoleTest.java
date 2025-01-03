package com.duoc.tienda_usuarios.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    @DisplayName("verifica que existen todos los roles esperados")
    void enumValuesTest() {
        // When
        Role[] roles = Role.values();

        // Then
        assertEquals(2, roles.length);
        assertTrue(containsRole(roles, "ADMIN"));
        assertTrue(containsRole(roles, "CLIENTE"));
    }

    @Test
    @DisplayName("verifica la conversion de string a role")
    void enumValueOfTest() {
        // When & Then
        assertEquals(Role.ADMIN, Role.valueOf("ADMIN"));
        assertEquals(Role.CLIENTE, Role.valueOf("CLIENTE"));
    }

    @Test
    @DisplayName("verifica que lanza excepcion para roles invalidos")
    void invalidRoleTest() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            Role.valueOf("ROLE_INVALID");
        });
    }

    private boolean containsRole(Role[] roles, String roleName) {
        for (Role role : roles) {
            if (role.name().equals(roleName)) {
                return true;
            }
        }
        return false;
    }
}