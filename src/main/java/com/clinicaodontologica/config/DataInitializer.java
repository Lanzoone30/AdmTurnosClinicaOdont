package com.clinicaodontologica.config;

import com.clinicaodontologica.model.Usuario;
import com.clinicaodontologica.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Inicializador de datos: crea el usuario admin por defecto en el primer
 * arranque, cuando la tabla de usuarios esta vacia.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Ejecuta la inicializacion al arrancar la aplicacion.
     *
     * @param args argumentos de linea de comandos
     */
    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario();
            admin.setNombreUsuario("admin");
            admin.setContrasenia(passwordEncoder.encode("admin"));
            admin.setRol("ADMIN");
            usuarioRepository.save(admin);
            log.info("Usuario por defecto creado: admin / admin (cambielo en produccion)");
        }
    }
}
