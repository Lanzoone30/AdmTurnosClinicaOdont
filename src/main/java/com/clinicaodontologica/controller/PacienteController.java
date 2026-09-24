package com.clinicaodontologica.controller;

import com.clinicaodontologica.model.Paciente;
import com.clinicaodontologica.service.PacienteService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller MVC de pacientes: listado, alta, edicion y eliminacion.
 */
@Controller
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    /**
     * Lista pacientes, opcionalmente filtrados por busqueda.
     *
     * @param busqueda texto de busqueda (opcional)
     * @param model modelo de la vista
     * @return vista de listado de pacientes
     */
    @GetMapping
    public String listar(@RequestParam(required = false) String busqueda, Model model) {
        model.addAttribute("pacientes", pacienteService.buscar(busqueda));
        model.addAttribute("busqueda", busqueda);
        return "paciente/list";
    }

    /**
     * Muestra el formulario de alta de paciente.
     *
     * @param model modelo de la vista
     * @return vista de formulario de paciente
     */
    @GetMapping("/nuevo")
    public String formulario(Model model) {
        model.addAttribute("paciente", new Paciente());
        return "paciente/form";
    }

    /**
     * Crea un paciente desde el formulario.
     *
     * @param paciente datos del paciente
     * @return redireccion al listado
     */
    @PostMapping("/nuevo")
    public String crear(@Valid @ModelAttribute Paciente paciente, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("paciente", paciente);
            return "paciente/form";
        }
        try {
            pacienteService.crear(paciente);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "paciente/form";
        }
        return "redirect:/pacientes";
    }

    /**
     * Muestra el formulario de edicion de un paciente.
     *
     * @param id id del paciente
     * @param model modelo de la vista
     * @return vista de formulario de paciente
     */
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        model.addAttribute("paciente", pacienteService.obtener(id));
        return "paciente/form";
    }

    /**
     * Actualiza un paciente desde el formulario.
     *
     * @param paciente datos actualizados
     * @return redireccion al listado
     */
    @PostMapping("/editar/{id}")
    public String actualizar(@PathVariable Integer id, @Valid @ModelAttribute Paciente paciente,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            paciente.setId(id);
            model.addAttribute("paciente", paciente);
            return "paciente/form";
        }
        try {
            paciente.setId(id);
            pacienteService.actualizar(paciente);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("paciente", paciente);
            return "paciente/form";
        }
        return "redirect:/pacientes";
    }

    /**
     * Elimina un paciente.
     *
     * @param id id del paciente
     * @return redireccion al listado
     */
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            pacienteService.eliminar(id);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/pacientes";
    }
}
