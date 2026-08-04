package com.clinicaodontologica.model;

/**
 * Roles de usuario del sistema.
 * <p>El prefijo {@code ROLE_} se agrega automaticamente en
 * {@link com.clinicaodontologica.service.AuthService AuthService}.</p>
 */
public enum Rol {
    ADMIN,
    ODONTOLOGO,
    PACIENTE,
    SECRETARIO
}
