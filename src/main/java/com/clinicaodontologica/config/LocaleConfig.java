package com.clinicaodontologica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;
import java.util.Set;

/**
 * Configuracion de internacionalizacion. El idioma vive en la sesion (es un dato
 * del usuario, no del navegador) y por defecto es español.
 *
 * <p>El cambio de idioma lo maneja {@code IdiomaController}, que valida el valor
 * contra {@link #IDIOMAS_SOPORTADOS} y persiste la preferencia del usuario.</p>
 */
@Configuration
public class LocaleConfig {

    /** Idiomas soportados por la interfaz. */
    public static final Locale ESPANOL = Locale.forLanguageTag("es");
    public static final Locale INGLES = Locale.forLanguageTag("en");
    public static final Set<String> IDIOMAS_SOPORTADOS = Set.of("es", "en");

    /**
     * Resuelve el locale desde la sesion, con español por defecto.
     *
     * @return el resolver de locale por sesion
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocaleFunction(request -> ESPANOL);
        return resolver;
    }
}
