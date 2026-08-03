package com.clinicaodontologica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller de autenticacion: muestra el formulario de login.
 */
@Controller
public class AuthController {

    /**
     * Muestra la pagina de login.
     *
     * @return vista login
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
