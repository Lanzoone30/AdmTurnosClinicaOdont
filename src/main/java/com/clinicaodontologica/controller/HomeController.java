package com.clinicaodontologica.controller;

import com.clinicaodontologica.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller de la pagina de inicio (dashboard).
 */
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final DashboardService dashboardService;

    /**
     * Muestra el dashboard con metricas del sistema.
     *
     * @param modelo modelo de la vista
     * @return vista index
     */
    @GetMapping("/")
    public String index(Model modelo) {
        modelo.addAttribute("dashboard", dashboardService.obtenerDashboard());
        return "index";
    }
}
