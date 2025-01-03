package com.duoc.tienda_usuarios.service;

import com.duoc.tienda_usuarios.model.Usuario;
import com.duoc.tienda_usuarios.repository.UsuarioRepository;
import com.duoc.tienda_usuarios.util.UsuarioLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    private final UsuarioLogger logger = UsuarioLogger.getInstance();

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario crearUsuario(Usuario usuario) {
        try {
            if (usuarioRepository.existsByEmail(usuario.getEmail())) {
                logger.logError("creación", "Email ya registrado: " + usuario.getEmail());
                throw new RuntimeException("El email ya está registrado");
            }
            Usuario nuevoUsuario = usuarioRepository.save(usuario);
            logger.logCreacion(usuario.getEmail());
            return nuevoUsuario;
        } catch (Exception e) {
            logger.logError("creación", e.getMessage());
            throw e;
        }
    }

    public Optional<Usuario> actualizarUsuario(Long id, Usuario usuarioActualizado) {
        try {
            return usuarioRepository.findById(id)
                .map(usuarioExistente -> {
                    if (usuarioActualizado.getNombre() != null) {
                        usuarioExistente.setNombre(usuarioActualizado.getNombre());
                    }
                    if (usuarioActualizado.getApellido() != null) {
                        usuarioExistente.setApellido(usuarioActualizado.getApellido());
                    }
                    if (usuarioActualizado.getTelefono() != null) {
                        usuarioExistente.setTelefono(usuarioActualizado.getTelefono());
                    }
                    if (usuarioActualizado.getDireccion() != null) {
                        usuarioExistente.setDireccion(usuarioActualizado.getDireccion());
                    }
                    Usuario updated = usuarioRepository.save(usuarioExistente);
                    logger.logActualizacion(id);
                    return updated;
                });
        } catch (Exception e) {
            logger.logError("actualización", e.getMessage());
            throw e;
        }
    }


    public void eliminarUsuario(Long id) {
        try {
            usuarioRepository.deleteById(id);
            logger.logEliminacion(id);
        } catch (Exception e) {
            logger.logError("eliminación", e.getMessage());
            throw e;
        }
    }

    public Optional<Usuario> login(String email, String password) {
        try {
            Optional<Usuario> usuario = usuarioRepository.findByEmail(email)
                .filter(u -> u.getPassword().equals(password));
            
            logger.logLogin(email, usuario.isPresent());
            return usuario;
        } catch (Exception e) {
            logger.logError("login", e.getMessage());
            throw e;
        }
    }

    public Optional<Boolean> resetPassword(Long id, String newPassword) {
        try {
            return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setPassword(newPassword);
                    usuarioRepository.save(usuario);
                    logger.logActualizacion(id);
                    return true;
                });
        } catch (Exception e) {
            logger.logError("reset password", e.getMessage());
            throw e;
        }
    }
}