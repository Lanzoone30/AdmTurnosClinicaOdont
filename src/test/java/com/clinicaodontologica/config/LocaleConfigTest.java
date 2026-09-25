package com.clinicaodontologica.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests unitarios de {@link LocaleConfig}: idioma por defecto y cambio de idioma
 * en la sesion.
 */
@DisplayName("LocaleConfig")
class LocaleConfigTest {

    private final LocaleResolver resolver = new LocaleConfig().localeResolver();

    @Test
    @DisplayName("resolveLocale: devuelve es por defecto")
    void resolveLocale_PorDefectoEsEspanol() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();

        // when
        Locale locale = resolver.resolveLocale(request);

        // then
        assertThat(locale.getLanguage()).isEqualTo("es");
    }

    @Test
    @DisplayName("setLocale: guarda el idioma pedido en la sesion")
    void setLocale_GuardaEnSesion() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.getSession();

        // when
        resolver.setLocale(request, null, Locale.forLanguageTag("en"));

        // then
        assertThat(resolver.resolveLocale(request).getLanguage()).isEqualTo("en");
    }
}
