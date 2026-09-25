package com.clinicaodontologica.controller;

import com.clinicaodontologica.config.LocaleConfig;
import com.clinicaodontologica.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import java.net.URI;
import java.util.Locale;

/**
 * Cambia el idioma de la interfaz desde la UI. Funciona sin JavaScript: valida
 * el idioma pedido, lo persiste en el usuario autenticado y recarga la vista
 * desde la que se hizo el cambio.
 */
@Controller
@RequiredArgsConstructor
public class IdiomaController {

    private final UsuarioService usuarioService;
    private final LocaleResolver localeResolver;

    /**
     * Aplica el idioma pedido y vuelve a la pagina anterior.
     *
     * @param lang codigo de idioma ("es" o "en")
     * @param authentication usuario autenticado, o {@code null} si no hay sesion
     * @param request peticion actual (para resolver sesion y referer)
     * @return redireccion a la ruta de origen, o al inicio
     */
    @GetMapping("/idioma")
    public String cambiarIdioma(@RequestParam("lang") String lang,
                                Authentication authentication,
                                HttpServletRequest request) {
        if (LocaleConfig.IDIOMAS_SOPORTADOS.contains(lang)) {
            localeResolver.setLocale(request, null, Locale.forLanguageTag(lang));
            if (authentication != null) {
                usuarioService.cambiarIdioma(authentication.getName(), lang);
            }
        }
        return "redirect:" + rutaDeOrigen(request);
    }

    /** Devuelve solo la ruta del referer si es del mismo origen, o "/" en caso contrario. */
    private String rutaDeOrigen(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer == null) {
            return "/";
        }
        try {
            URI uri = URI.create(referer);
            if (uri.getHost() == null || !uri.getHost().equals(request.getServerName())) {
                return "/";
            }
            String path = uri.getRawPath();
            if (path == null || path.isBlank() || path.startsWith("//")) {
                return "/";
            }
            String query = uri.getRawQuery();
            return query == null ? path : path + "?" + query;
        } catch (IllegalArgumentException e) {
            return "/";
        }
    }
}
