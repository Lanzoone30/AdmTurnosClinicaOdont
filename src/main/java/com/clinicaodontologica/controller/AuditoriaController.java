package com.clinicaodontologica.controller;

import com.clinicaodontologica.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller del historial de auditoria, accesible solo para ADMIN.
 */
@Controller
@RequestMapping("/auditoria")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    /**
     * Muestra el historial de auditoria.
     *
     * @param model modelo de la vista
     * @return vista de auditoria
     */
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("registros", auditoriaService.listar());
        return "auditoria/list";
    }
}
