package com.clinicaodontologica.controller;

import com.clinicaodontologica.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller del perfil del usuario logueado: datos propios y cambio
 * de contrasenia.
 */
@Controller
@RequestMapping("/mi-perfil")
@RequiredArgsConstructor
public class PerfilController {

    private final UsuarioService usuarioService;

    /**
     * Muestra el perfil del usuario logueado.
     *
     * @param authentication autenticacion actual
     * @param model modelo de la vista
     * @return vista de perfil
     */
    @GetMapping
    public String perfil(Authentication authentication, Model model) {
        model.addAttribute("usuario", usuarioService.obtenerPorNombre(authentication.getName()));
        return "usuario/perfil";
    }

    /**
     * Cambia la contrasenia del usuario logueado.
     *
     * @param authentication autenticacion actual
     * @param contraseniaActual contrasenia actual
     * @param contraseniaNueva nueva contrasenia
     * @param redirectAttributes atributos de redireccion
     * @return redireccion al perfil
     */
    @PostMapping("/cambiar-contrasena")
    public String cambiarContrasenia(Authentication authentication,
                                     @RequestParam String contraseniaActual,
                                     @RequestParam String contraseniaNueva,
                                     RedirectAttributes redirectAttributes) {
        try {
            usuarioService.cambiarContrasenia(authentication.getName(),
                    contraseniaActual, contraseniaNueva);
            redirectAttributes.addFlashAttribute("exito", "Contrasenia actualizada correctamente");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/mi-perfil";
    }
}
