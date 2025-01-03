package com.duoc.tienda_usuarios.controller;

import com.duoc.tienda_usuarios.dto.PasswordResetDTO;
import com.duoc.tienda_usuarios.dto.UsuarioActualizacionDTO;
import com.duoc.tienda_usuarios.model.LoginRequest;
import com.duoc.tienda_usuarios.model.Usuario;
import com.duoc.tienda_usuarios.model.UsuarioBuilder;
import com.duoc.tienda_usuarios.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioService.obtenerTodosLosUsuarios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        return usuarioService.obtenerUsuarioPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@Valid @RequestBody Usuario usuarioRequest) {
        Usuario usuario = UsuarioBuilder.builder()
            .nombre(usuarioRequest.getNombre())
            .apellido(usuarioRequest.getApellido())
            .email(usuarioRequest.getEmail())
            .password(usuarioRequest.getPassword())
            .role(usuarioRequest.getRole())
            .telefono(usuarioRequest.getTelefono())
            .direccion(usuarioRequest.getDireccion())
            .build();
            
        Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);
        return ResponseEntity.ok(nuevoUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioActualizacionDTO usuarioDTO) {
        
        Usuario usuarioParaActualizar = UsuarioBuilder.builder()
            .nombre(usuarioDTO.getNombre())
            .apellido(usuarioDTO.getApellido())
            .telefono(usuarioDTO.getTelefono())
            .direccion(usuarioDTO.getDireccion())
            .build();

        return usuarioService.actualizarUsuario(id, usuarioParaActualizar)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody LoginRequest loginRequest) {
        return usuarioService.login(loginRequest.getEmail(), loginRequest.getPassword())
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/reset-password")
    public ResponseEntity<Void> resetPassword(@PathVariable Long id, @RequestBody PasswordResetDTO passwordDTO) {
        return usuarioService.resetPassword(id, passwordDTO.getNewPassword())
            .map(result -> ResponseEntity.ok().<Void>build())
            .orElse(ResponseEntity.notFound().build());
    }
}