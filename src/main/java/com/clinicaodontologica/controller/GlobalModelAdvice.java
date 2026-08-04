package com.clinicaodontologica.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;

/**
 * Advice global de modelos de vista: expone el usuario autenticado y su
 * rol a todas las plantillas Thymeleaf como {@code usuarioLogueado} y
 * {@code rolUsuario}.
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

    /**
     * Agrega el rol del usuario autenticado (sin prefijo ROLE_) al modelo.
     *
     * @param authentication autenticacion actual, o {@code null} si no hay
     * @return rol del usuario (ej: "ADMIN"), o cadena vacia si no hay sesion
     */
    @ModelAttribute("rolUsuario")
    public String rolUsuario(Authentication authentication) {
        if (authentication == null) {
            return "";
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> authority.replace("ROLE_", ""))
                .findFirst()
                .orElse("");
    }
}
