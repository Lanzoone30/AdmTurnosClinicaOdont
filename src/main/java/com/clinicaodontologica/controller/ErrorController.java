package com.clinicaodontologica.controller;

import com.clinicaodontologica.service.Mensajes;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Pagina de error: expone el codigo de estado real de la peticion y un aviso
 * (titulo y texto) acorde al estado, en el idioma activo.
 *
 * <p>Reemplaza al {@code BasicErrorController} de Spring Boot para que un 403
 * o un 500 no se muestren con el aviso de un 404.</p>
 */
@Controller
@RequestMapping("/error")
@RequiredArgsConstructor
public class ErrorController implements org.springframework.boot.webmvc.error.ErrorController {

    private final Mensajes mensajes;

    /**
     * Renderiza la pagina de error segun el estado de la peticion.
     *
     * @param request peticion con el estado del error
     * @param model modelo de la vista
     * @return vista de error
     */
    @GetMapping
    public String error(HttpServletRequest request, Model model) {
        Object atributo = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int codigo = atributo instanceof Integer valor ? valor : HttpStatus.INTERNAL_SERVER_ERROR.value();

        String claveTitulo = switch (codigo) {
            case 403 -> "error.acceso-denegado";
            case 500 -> "error.servidor";
            default -> "error.titulo";
        };
        String claveTexto = switch (codigo) {
            case 403 -> "error.acceso-denegado-texto";
            case 500 -> "error.servidor-texto";
            default -> "error.texto";
        };

        model.addAttribute("codigo", codigo);
        model.addAttribute("errorTitulo", mensajes.get(claveTitulo));
        model.addAttribute("errorTexto", mensajes.get(claveTexto));
        return "error";
    }
}
