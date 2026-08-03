package com.clinicaodontologica.controller;

import com.clinicaodontologica.service.OdontologoService;
import com.clinicaodontologica.service.PacienteService;
import com.clinicaodontologica.service.TurnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Controller MVC de turnos: listado, alta, edicion y eliminacion.
 */
@Controller
@RequestMapping("/turnos")
@RequiredArgsConstructor
public class TurnoController {

    private final TurnoService turnoService;
    private final OdontologoService odontologoService;
    private final PacienteService pacienteService;

    /**
     * Lista los turnos.
     *
     * @param model modelo de la vista
     * @return vista de listado de turnos
     */
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("turnos", turnoService.listar());
        return "turno/list";
    }

    /**
     * Muestra el formulario de alta de turno con odontologos y pacientes
     * disponibles.
     *
     * @param model modelo de la vista
     * @return vista de formulario de turno
     */
    @GetMapping("/nuevo")
    public String formulario(Model model) {
        model.addAttribute("odontologos", odontologoService.listar());
        model.addAttribute("pacientes", pacienteService.listar());
        return "turno/form";
    }

    /**
     * Crea un turno desde el formulario.
     *
     * @param fechaTurno fecha de la cita
     * @param horaTurno hora de la cita
     * @param afeccion afeccion del paciente
     * @param odontologoId id del odontologo
     * @param pacienteId id del paciente
     * @param redirectAttributes atributos de redireccion
     * @return redireccion al listado
     */
    @PostMapping("/nuevo")
    public String crear(@RequestParam LocalDate fechaTurno,
                        @RequestParam LocalTime horaTurno,
                        @RequestParam String afeccion,
                        @RequestParam Integer odontologoId,
                        @RequestParam Integer pacienteId,
                        RedirectAttributes redirectAttributes) {
        try {
            turnoService.crear(fechaTurno, horaTurno, afeccion, odontologoId, pacienteId);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/turnos";
    }

    /**
     * Muestra el formulario de edicion de un turno.
     *
     * @param id id del turno
     * @param model modelo de la vista
     * @return vista de formulario de turno
     */
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        model.addAttribute("turno", turnoService.obtener(id));
        model.addAttribute("odontologos", odontologoService.listar());
        model.addAttribute("pacientes", pacienteService.listar());
        return "turno/form";
    }

    /**
     * Actualiza un turno desde el formulario.
     *
     * @param id id del turno
     * @param fechaTurno fecha de la cita
     * @param horaTurno hora de la cita
     * @param afeccion afeccion del paciente
     * @param odontologoId id del odontologo
     * @param pacienteId id del paciente
     * @param redirectAttributes atributos de redireccion
     * @return redireccion al listado
     */
    @PostMapping("/editar/{id}")
    public String actualizar(@PathVariable Integer id,
                             @RequestParam LocalDate fechaTurno,
                             @RequestParam LocalTime horaTurno,
                             @RequestParam String afeccion,
                             @RequestParam Integer odontologoId,
                             @RequestParam Integer pacienteId,
                             RedirectAttributes redirectAttributes) {
        try {
            turnoService.actualizar(id, fechaTurno, horaTurno, afeccion, odontologoId, pacienteId);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/turnos";
    }

    /**
     * Elimina un turno.
     *
     * @param id id del turno
     * @return redireccion al listado
     */
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        turnoService.eliminar(id);
        return "redirect:/turnos";
    }
}
