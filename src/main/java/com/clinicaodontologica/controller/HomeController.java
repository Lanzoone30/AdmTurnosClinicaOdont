package com.clinicaodontologica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller de la pagina de inicio.
 */
@Controller
public class HomeController {

    /**
     * Muestra la pagina principal tras el login.
     *
     * @return vista index
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
