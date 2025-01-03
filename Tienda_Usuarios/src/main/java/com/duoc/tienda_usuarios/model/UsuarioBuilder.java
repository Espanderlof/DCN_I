package com.duoc.tienda_usuarios.model;

public class UsuarioBuilder {
    private final Usuario usuario;

    private UsuarioBuilder() {
        usuario = new Usuario();
    }

    public static UsuarioBuilder builder() {
        return new UsuarioBuilder();
    }

    public UsuarioBuilder id(Long id) {
        usuario.setId(id);
        return this;
    }

    public UsuarioBuilder nombre(String nombre) {
        usuario.setNombre(nombre);
        return this;
    }

    public UsuarioBuilder apellido(String apellido) {
        usuario.setApellido(apellido);
        return this;
    }

    public UsuarioBuilder email(String email) {
        usuario.setEmail(email);
        return this;
    }

    public UsuarioBuilder password(String password) {
        usuario.setPassword(password);
        return this;
    }

    public UsuarioBuilder role(Role role) {
        usuario.setRole(role);
        return this;
    }

    public UsuarioBuilder telefono(String telefono) {
        usuario.setTelefono(telefono);
        return this;
    }

    public UsuarioBuilder direccion(String direccion) {
        usuario.setDireccion(direccion);
        return this;
    }

    public Usuario build() {
        return usuario;
    }
}