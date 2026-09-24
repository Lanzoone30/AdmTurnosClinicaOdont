package com.clinicaodontologica.controller;

import com.clinicaodontologica.model.Rol;
import com.clinicaodontologica.model.Usuario;
import com.clinicaodontologica.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller MVC de usuarios: listado, alta, edicion y eliminacion.
 */
@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Lista todos los usuarios.
     *
     * @param model modelo de la vista
     * @return vista de listado de usuarios
     */
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listar());
        return "usuario/list";
    }

    /**
     * Muestra el formulario de alta de usuario.
     *
     * @param model modelo de la vista
     * @return vista de formulario de usuario
     */
    @GetMapping("/nuevo")
    public String formulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", Rol.values());
        return "usuario/form";
    }

    /**
     * Crea un usuario desde el formulario.
     *
     * @param usuario datos del usuario
     * @return redireccion al listado
     */
    @PostMapping("/nuevo")
    public String crear(@Valid @ModelAttribute Usuario usuario, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("roles", Rol.values());
            return "usuario/form";
        }
        usuarioService.crear(usuario);
        return "redirect:/usuarios";
    }

    /**
     * Muestra el formulario de edicion de un usuario.
     *
     * @param id id del usuario
     * @param model modelo de la vista
     * @return vista de formulario de usuario
     */
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        model.addAttribute("usuario", usuarioService.obtener(id));
        model.addAttribute("roles", Rol.values());
        return "usuario/form";
    }

    /**
     * Actualiza un usuario desde el formulario.
     *
     * @param id id del usuario
     * @param usuario datos actualizados
     * @return redireccion al listado
     */
    @PostMapping("/editar/{id}")
    public String actualizar(@PathVariable Integer id, @Valid @ModelAttribute Usuario usuario,
                             BindingResult result, Model model) {
        usuario.setId(id);
        if (result.hasErrors()) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("roles", Rol.values());
            return "usuario/form";
        }
        try {
            usuarioService.actualizar(usuario);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("usuario", usuario);
            model.addAttribute("roles", Rol.values());
            return "usuario/form";
        }
        return "redirect:/usuarios";
    }

    /**
     * Elimina un usuario.
     *
     * @param id id del usuario
     * @return redireccion al listado
     */
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.eliminar(id);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/usuarios";
    }
}
