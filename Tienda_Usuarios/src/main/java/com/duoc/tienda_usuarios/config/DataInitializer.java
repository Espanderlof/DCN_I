package com.duoc.tienda_usuarios.config;

import com.duoc.tienda_usuarios.model.Role;
import com.duoc.tienda_usuarios.model.Usuario;
import com.duoc.tienda_usuarios.model.UsuarioBuilder;
import com.duoc.tienda_usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            System.out.println("Inicializando datos de usuarios...");
            
            // Crear usuario administrador
            Usuario admin = UsuarioBuilder.builder()
                .nombre("Admin")
                .apellido("Sistema")
                .email("admin@tienda.com")
                .password("admin123")
                .role(Role.ADMIN)
                .telefono("+56912345678")
                .direccion("Oficina Central")
                .build();
            usuarioRepository.save(admin);
            System.out.println("Usuario administrador creado");

            // Crear primer cliente de prueba
            Usuario cliente1 = UsuarioBuilder.builder()
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan@email.com")
                .password("cliente123")
                .role(Role.CLIENTE)
                .telefono("+56987654321")
                .direccion("Av. Principal 123")
                .build();
            usuarioRepository.save(cliente1);
            System.out.println("Cliente 1 creado");

            // Crear segundo cliente de prueba
            Usuario cliente2 = UsuarioBuilder.builder()
                .nombre("María")
                .apellido("González")
                .email("maria@email.com")
                .password("cliente456")
                .role(Role.CLIENTE)
                .telefono("+56976543210")
                .direccion("Calle Secundaria 456")
                .build();
            usuarioRepository.save(cliente2);
            System.out.println("Cliente 2 creado");

            System.out.println("Inicialización de datos completada!");
        } else {
            System.out.println("La base de datos ya contiene usuarios. No se requiere inicialización.");
        }
    }
}