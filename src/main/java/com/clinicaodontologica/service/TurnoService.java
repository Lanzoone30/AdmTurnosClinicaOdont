package com.clinicaodontologica.service;

import com.clinicaodontologica.model.Odontologo;
import com.clinicaodontologica.model.Paciente;
import com.clinicaodontologica.model.Turno;
import com.clinicaodontologica.repository.OdontologoRepository;
import com.clinicaodontologica.repository.PacienteRepository;
import com.clinicaodontologica.repository.TurnoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Servicio de gestion de turnos: CRUD con validacion de choque de horario.
 */
@Service
@RequiredArgsConstructor
public class TurnoService {

    private final TurnoRepository turnoRepository;
    private final OdontologoRepository odontologoRepository;
    private final PacienteRepository pacienteRepository;

    /**
     * Lista todos los turnos.
     *
     * @return lista de turnos
     */
    @Transactional(readOnly = true)
    public List<Turno> listar() {
        return turnoRepository.findAll();
    }

    /**
     * Lista los turnos de un odontologo en una fecha.
     *
     * @param odontologoId id del odontologo
     * @param fecha fecha a consultar, o {@code null} para todos
     * @return turnos del odontologo
     */
    @Transactional(readOnly = true)
    public List<Turno> listarPorOdontologoYFecha(Integer odontologoId, LocalDate fecha) {
        if (odontologoId == null) {
            return listar();
        }
        if (fecha == null) {
            return turnoRepository.findAll().stream()
                    .filter(t -> t.getOdontologo() != null
                            && odontologoId.equals(t.getOdontologo().getId()))
                    .toList();
        }
        return turnoRepository.findByOdontologoIdAndFechaTurno(odontologoId, fecha);
    }

    /**
     * Busca un turno por su id.
     *
     * @param id id del turno
     * @return el turno encontrado
     * @throws IllegalArgumentException si el turno no existe
     */
    @Transactional(readOnly = true)
    public Turno obtener(Integer id) {
        return turnoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Turno no encontrado: " + id));
    }

    /**
     * Crea un turno validando que el odontologo no tenga otro turno en la
     * misma fecha y hora.
     *
     * @param fechaTurno fecha de la cita
     * @param horaTurno hora de la cita
     * @param afeccion afeccion del paciente
     * @param odontologoId id del odontologo
     * @param pacienteId id del paciente
     * @return el turno creado
     * @throws IllegalArgumentException si el odontologo ya tiene un turno
     *         a la misma fecha y hora, o si el odontologo/paciente no existe
     */
    @Transactional
    public Turno crear(LocalDate fechaTurno, LocalTime horaTurno, String afeccion,
                       Integer odontologoId, Integer pacienteId) {
        Odontologo odontologo = odontologoRepository.findById(odontologoId)
                .orElseThrow(() -> new IllegalArgumentException("Odontologo no encontrado: " + odontologoId));
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado: " + pacienteId));
        validarSinChoque(odontologoId, fechaTurno, horaTurno, null);

        Turno turno = new Turno();
        turno.setFechaTurno(fechaTurno);
        turno.setHoraTurno(horaTurno);
        turno.setAfeccion(afeccion);
        turno.setOdontologo(odontologo);
        turno.setPaciente(paciente);
        return turnoRepository.save(turno);
    }

    /**
     * Actualiza un turno. Para las relaciones, recibe los ids de odontologo
     * y paciente y carga las entidades reales.
     *
     * @param id id del turno
     * @param fechaTurno nueva fecha
     * @param horaTurno nueva hora
     * @param afeccion nueva afeccion
     * @param odontologoId id del odontologo
     * @param pacienteId id del paciente
     * @return el turno actualizado
     */
    @Transactional
    public Turno actualizar(Integer id, LocalDate fechaTurno, LocalTime horaTurno,
                            String afeccion, Integer odontologoId, Integer pacienteId) {
        Turno existente = obtener(id);
        validarSinChoque(odontologoId, fechaTurno, horaTurno, id);
        existente.setFechaTurno(fechaTurno);
        existente.setHoraTurno(horaTurno);
        existente.setAfeccion(afeccion);
        existente.setOdontologo(odontologoRepository.findById(odontologoId).orElse(null));
        existente.setPaciente(pacienteRepository.findById(pacienteId).orElse(null));
        return turnoRepository.save(existente);
    }

    /**
     * Elimina un turno por su id.
     *
     * @param id id del turno a eliminar
     */
    @Transactional
    public void eliminar(Integer id) {
        turnoRepository.deleteById(id);
    }

    /**
     * Verifica que el odontologo no tenga otro turno en la misma fecha y
     * hora. {@code turnoExcluido} permite ignorar el propio turno al
     * editar.
     *
     * @param odontologoId id del odontologo
     * @param fecha fecha del turno
     * @param hora hora del turno
     * @param turnoExcluido id del turno que se esta editando, o {@code null}
     * @throws IllegalArgumentException si existe un turno en conflicto
     */
    private void validarSinChoque(Integer odontologoId, LocalDate fecha, LocalTime hora,
                                  Integer turnoExcluido) {
        boolean choque = turnoRepository.findByOdontologoIdAndFechaTurno(odontologoId, fecha)
                .stream()
                .anyMatch(t -> t.getHoraTurno().equals(hora)
                        && !t.getId().equals(turnoExcluido));
        if (choque) {
            throw new IllegalArgumentException(
                    "El odontologo ya tiene un turno el " + fecha + " a las " + hora);
        }
    }
}
