package com.clinicaodontologica.controller;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;

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
                .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                .findFirst()
                .orElse("");
    }

    /**
     * Agrega el codigo de idioma activo ("es" o "en") al modelo de cada vista.
     *
     * @return codigo de idioma de la peticion actual
     */
    @ModelAttribute("idiomaActual")
    public String idiomaActual() {
        return LocaleContextHolder.getLocale().getLanguage();
    }

    /**
     * Agrega la ruta de la peticion actual (ej: "/pacientes/5") al modelo, para
     * que el menu marque el item activo con {@code aria-current="page"}.
     *
     * @param request peticion actual
     * @return ruta solicitada, o "/" si no esta disponible
     */
    @ModelAttribute("rutaActual")
    public String rutaActual(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
