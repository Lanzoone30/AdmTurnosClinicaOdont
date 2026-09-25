package com.clinicaodontologica.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * Resuelve mensajes de negocio desde el bundle de traducciones usando el
 * idioma de la peticion actual.
 *
 * <p>Los services lanzan excepciones con claves (por ejemplo
 * {@code error.paciente.dni-duplicado}); {@link #get(String, Object...)} las
 * traduce al idioma activo.</p>
 */
@Component
@RequiredArgsConstructor
public class Mensajes {

    private final MessageSource messageSource;

    /**
     * Resuelve una clave en el idioma de la peticion.
     *
     * @param clave clave del bundle
     * @param args argumentos de la clave (puede ir vacio)
     * @return el mensaje traducido
     */
    public String get(String clave, Object... args) {
        return messageSource.getMessage(clave, args, LocaleContextHolder.getLocale());
    }
}
