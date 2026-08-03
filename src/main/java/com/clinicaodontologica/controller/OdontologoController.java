package com.clinicaodontologica.controller;

import com.clinicaodontologica.model.Odontologo;
import com.clinicaodontologica.service.OdontologoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller MVC de odontologos: listado, alta, edicion y eliminacion.
 */
@Controller
@RequestMapping("/odontologos")
@RequiredArgsConstructor
public class OdontologoController {

    private final OdontologoService odontologoService;

    /**
     * Lista odontologos, opcionalmente filtrados por busqueda.
     *
     * @param busqueda texto de busqueda (opcional)
     * @param model modelo de la vista
     * @return vista de listado de odontologos
     */
    @GetMapping
    public String listar(@RequestParam(required = false) String busqueda, Model model) {
        model.addAttribute("odontologos", odontologoService.buscar(busqueda));
        model.addAttribute("busqueda", busqueda);
        return "odontologo/list";
    }

    /**
     * Muestra el formulario de alta de odontologo.
     *
     * @param model modelo de la vista
     * @return vista de formulario de odontologo
     */
    @GetMapping("/nuevo")
    public String formulario(Model model) {
        model.addAttribute("odontologo", new Odontologo());
        return "odontologo/form";
    }

    /**
     * Crea un odontologo desde el formulario.
     *
     * @param odontologo datos del odontologo
     * @return redireccion al listado
     */
    @PostMapping("/nuevo")
    public String crear(@ModelAttribute Odontologo odontologo) {
        odontologoService.crear(odontologo);
        return "redirect:/odontologos";
    }

    /**
     * Muestra el formulario de edicion de un odontologo.
     *
     * @param id id del odontologo
     * @param model modelo de la vista
     * @return vista de formulario de odontologo
     */
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        model.addAttribute("odontologo", odontologoService.obtener(id));
        return "odontologo/form";
    }

    /**
     * Actualiza un odontologo desde el formulario.
     *
     * @param id id del odontologo
     * @param odontologo datos actualizados
     * @return redireccion al listado
     */
    @PostMapping("/editar/{id}")
    public String actualizar(@PathVariable Integer id, @ModelAttribute Odontologo odontologo,
                             RedirectAttributes redirectAttributes) {
        try {
            odontologo.setId(id);
            odontologoService.actualizar(odontologo);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/odontologos";
    }

    /**
     * Elimina un odontologo.
     *
     * @param id id del odontologo
     * @return redireccion al listado
     */
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        odontologoService.eliminar(id);
        return "redirect:/odontologos";
    }
}
