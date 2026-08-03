package com.clinicaodontologica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicacion ClinicaOdontologica.
 *
 * <p>Arranca el contexto de Spring Boot con el servidor embebido (Tomcat)
 * y las dependencias de web, JPA, Thymeleaf y Security.</p>
 */
@SpringBootApplication
public class ClinicaOdontologicaApplication {

    /**
     * Metodo principal: inicia la aplicacion.
     *
     * @param args argumentos de linea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(ClinicaOdontologicaApplication.class, args);
    }
}
