package com.clinicaodontologica.service;

import com.clinicaodontologica.model.EstadoTurno;
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
     * Lista los turnos, opcionalmente filtrados por estado.
     *
     * @param estado estado a filtrar, o {@code null} para todos
     * @return lista de turnos
     */
    @Transactional(readOnly = true)
    public List<Turno> listar(EstadoTurno estado) {
        if (estado == null) {
            return listar();
        }
        return turnoRepository.findByEstado(estado);
    }

    /**
     * Busca turnos por texto en la afeccion. Si el texto es vacio o nulo,
     * devuelve todos.
     *
     * @param texto texto a buscar
     * @return turnos coincidentes
     */
    @Transactional(readOnly = true)
    public List<Turno> buscarPorAfeccion(String texto) {
        if (texto == null || texto.isBlank()) {
            return listar();
        }
        return turnoRepository.findByAfeccionContainingIgnoreCase(texto.trim());
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
     * Lista los turnos de un paciente, mas recientes primero.
     *
     * @param pacienteId id del paciente
     * @return turnos del paciente
     */
    @Transactional(readOnly = true)
    public List<Turno> listarPorPaciente(Integer pacienteId) {
        return turnoRepository.findByPacienteIdOrderByFechaTurnoDescHoraTurnoDesc(pacienteId);
    }

    /**
     * Lista los turnos de un odontologo en un rango de fechas.
     *
     * @param odontologoId id del odontologo
     * @param inicio fecha inicial
     * @param fin fecha final
     * @return turnos del odontologo en el rango
     */
    @Transactional(readOnly = true)
    public List<Turno> listarPorOdontologoYPeriodo(Integer odontologoId, LocalDate inicio, LocalDate fin) {
        return turnoRepository.findByOdontologoIdAndFechaTurnoBetweenOrderByHoraTurno(
                odontologoId, inicio, fin);
    }

    /**
     * Lista los turnos del paciente asociado al nombre de usuario.
     *
     * @param nombreUsuario nombre de usuario del paciente
     * @return turnos del paciente, mas recientes primero
     * @throws IllegalArgumentException si no hay paciente con ese usuario
     */
    @Transactional(readOnly = true)
    public List<Turno> listarPorPacienteLogueado(String nombreUsuario) {
        Paciente paciente = pacienteRepository.findByUsuarioNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No hay paciente vinculado al usuario: " + nombreUsuario));
        return listarPorPaciente(paciente.getId());
    }

    /**
     * Lista los turnos del odontologo asociado al nombre de usuario en un
     * rango de fechas.
     *
     * @param nombreUsuario nombre de usuario del odontologo
     * @param inicio fecha inicial
     * @param fin fecha final
     * @return turnos del odontologo en el rango
     * @throws IllegalArgumentException si no hay odontologo con ese usuario
     */
    @Transactional(readOnly = true)
    public List<Turno> listarAgendaOdontologoLogueado(String nombreUsuario,
                                                      LocalDate inicio, LocalDate fin) {
        Odontologo odontologo = odontologoRepository.findByUsuarioNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No hay odontologo vinculado al usuario: " + nombreUsuario));
        return listarPorOdontologoYPeriodo(odontologo.getId(), inicio, fin);
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
        return crear(fechaTurno, horaTurno, afeccion, odontologoId, pacienteId,
                EstadoTurno.PENDIENTE);
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
     * @param estado estado inicial del turno
     * @return el turno creado
     * @throws IllegalArgumentException si el odontologo ya tiene un turno
     *         a la misma fecha y hora, o si el odontologo/paciente no existe
     */
    @Transactional
    public Turno crear(LocalDate fechaTurno, LocalTime horaTurno, String afeccion,
                       Integer odontologoId, Integer pacienteId, EstadoTurno estado) {
        Odontologo odontologo = odontologoRepository.findById(odontologoId)
                .orElseThrow(() -> new IllegalArgumentException("Odontologo no encontrado: " + odontologoId));
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado: " + pacienteId));
        validarSinChoque(odontologoId, fechaTurno, horaTurno, null);

        Turno turno = new Turno();
        turno.setFechaTurno(fechaTurno);
        turno.setHoraTurno(horaTurno);
        turno.setAfeccion(afeccion);
        turno.setEstado(estado);
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
        return actualizar(id, fechaTurno, horaTurno, afeccion, odontologoId, pacienteId,
                obtener(id).getEstado());
    }

    /**
     * Actualiza un turno incluyendo el estado.
     *
     * @param id id del turno
     * @param fechaTurno nueva fecha
     * @param horaTurno nueva hora
     * @param afeccion nueva afeccion
     * @param odontologoId id del odontologo
     * @param pacienteId id del paciente
     * @param estado nuevo estado del turno
     * @return el turno actualizado
     */
    @Transactional
    public Turno actualizar(Integer id, LocalDate fechaTurno, LocalTime horaTurno,
                            String afeccion, Integer odontologoId, Integer pacienteId,
                            EstadoTurno estado) {
        Turno existente = obtener(id);
        validarSinChoque(odontologoId, fechaTurno, horaTurno, id);
        existente.setFechaTurno(fechaTurno);
        existente.setHoraTurno(horaTurno);
        existente.setAfeccion(afeccion);
        if (estado != null) {
            existente.setEstado(estado);
        }
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
