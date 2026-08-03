package com.clinicaodontologica.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Advice global de modelos de vista: expone el usuario autenticado a
 * todas las plantillas Thymeleaf como {@code usuarioLogueado}.
 */
@ControllerAdvice
public class GlobalModelAdvice {

    /**
     * Agrega el nombre del usuario autenticado al modelo de cada vista.
     *
     * @param authentication autenticacion actual, o {@code null} si no hay
     * @return nombre del usuario, o cadena vacia si no hay sesion
     */
    @ModelAttribute("usuarioLogueado")
    public String usuarioLogueado(Authentication authentication) {
        return authentication == null ? "" : authentication.getName();
    }
}
