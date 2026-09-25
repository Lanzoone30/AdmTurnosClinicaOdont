package com.clinicaodontologica.service;

import com.clinicaodontologica.model.DiaSemana;
import com.clinicaodontologica.model.EstadoTurno;
import com.clinicaodontologica.model.Horario;
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
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Servicio de gestion de turnos: CRUD con validacion de choque de horario.
 */
@Service
@RequiredArgsConstructor
public class TurnoService {

    /**
     * Allowed state transitions. Closed states (CANCELADO, REALIZADO,
     * NO_ASISTIO) are terminal, so a turno cannot be reopened.
     */
    private static final Map<EstadoTurno, Set<EstadoTurno>> TRANSICIONES = new EnumMap<>(EstadoTurno.class);

    static {
        TRANSICIONES.put(EstadoTurno.PENDIENTE,
                EnumSet.of(EstadoTurno.CONFIRMADO, EstadoTurno.CANCELADO, EstadoTurno.NO_ASISTIO));
        TRANSICIONES.put(EstadoTurno.CONFIRMADO,
                EnumSet.of(EstadoTurno.REALIZADO, EstadoTurno.CANCELADO, EstadoTurno.NO_ASISTIO));
        TRANSICIONES.put(EstadoTurno.CANCELADO, EnumSet.noneOf(EstadoTurno.class));
        TRANSICIONES.put(EstadoTurno.REALIZADO, EnumSet.noneOf(EstadoTurno.class));
        TRANSICIONES.put(EstadoTurno.NO_ASISTIO, EnumSet.noneOf(EstadoTurno.class));
    }

    private final TurnoRepository turnoRepository;
    private final OdontologoRepository odontologoRepository;
    private final PacienteRepository pacienteRepository;
    private final Mensajes mensajes;

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
                        mensajes.get("error.turno.paciente-sin-vinculo", nombreUsuario)));
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
                        mensajes.get("error.turno.odontologo-sin-vinculo", nombreUsuario)));
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
                .orElseThrow(() -> new IllegalArgumentException(mensajes.get("error.turno.no-encontrado", id)));
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
                .orElseThrow(() -> new IllegalArgumentException(mensajes.get("error.odontologo.no-encontrado", odontologoId)));
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new IllegalArgumentException(mensajes.get("error.paciente.no-encontrado", pacienteId)));
        validarSinChoque(odontologoId, fechaTurno, horaTurno, null);
        validarDentroDeHorario(odontologo, fechaTurno, horaTurno);

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
        Odontologo odontologo = odontologoRepository.findById(odontologoId).orElse(null);
        validarDentroDeHorario(odontologo, fechaTurno, horaTurno);
        existente.setFechaTurno(fechaTurno);
        existente.setHoraTurno(horaTurno);
        existente.setAfeccion(afeccion);
        if (estado != null) {
            existente.setEstado(estado);
        }
        existente.setOdontologo(odontologo);
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
     * Applies a state transition to a turno, enforcing the allowed
     * transitions and the per-state requirements.
     *
     * @param id turno id
     * @param nuevoEstado target state
     * @param motivo cancellation reason, required only for CANCELADO
     * @return the updated turno
     * @throws IllegalArgumentException if the transition is not allowed or a
     *         required value is missing
     */
    @Transactional
    public Turno cambiarEstado(Integer id, EstadoTurno nuevoEstado, String motivo) {
        Turno turno = obtener(id);
        if (nuevoEstado == null) {
            throw new IllegalArgumentException(mensajes.get("error.turno.estado-obligatorio"));
        }
        Set<EstadoTurno> permitidos = TRANSICIONES.getOrDefault(turno.getEstado(), EnumSet.noneOf(EstadoTurno.class));
        if (!permitidos.contains(nuevoEstado)) {
            throw new IllegalArgumentException(
                    mensajes.get("error.turno.transicion-invalida", turno.getEstado(), nuevoEstado));
        }
        if (nuevoEstado == EstadoTurno.CANCELADO) {
            if (motivo == null || motivo.isBlank()) {
                throw new IllegalArgumentException(mensajes.get("error.turno.motivo-obligatorio"));
            }
            turno.setMotivoCancelacion(motivo.trim());
        }
        if (nuevoEstado == EstadoTurno.REALIZADO
                && (turno.getNotaClinica() == null || turno.getNotaClinica().isBlank())) {
            throw new IllegalArgumentException(mensajes.get("error.turno.nota-obligatoria"));
        }
        turno.setEstado(nuevoEstado);
        return turnoRepository.save(turno);
    }

    /**
     * Saves the clinical note for a turno.
     *
     * @param id turno id
     * @param notaClinica diagnosis and treatment performed
     * @return the updated turno
     */
    @Transactional
    public Turno registrarNota(Integer id, String notaClinica) {
        Turno turno = obtener(id);
        turno.setNotaClinica(notaClinica == null ? null : notaClinica.trim());
        return turnoRepository.save(turno);
    }

    /**
     * Rejects a turno that falls outside the dentist's availability for that
     * weekday. Dentists without configured hours are unconstrained.
     *
     * @throws IllegalArgumentException if the turno is outside the schedule
     */
    private void validarDentroDeHorario(Odontologo odontologo, LocalDate fecha, LocalTime hora) {
        if (odontologo == null || odontologo.getHorarios() == null
                || odontologo.getHorarios().isEmpty()) {
            return;
        }
        DiaSemana dia = DiaSemana.from(fecha.getDayOfWeek());
        for (Horario horario : odontologo.getHorarios()) {
            if (dia.equals(horario.getDiaSemana())
                    && horario.getHorarioInicio() != null && horario.getHorarioFin() != null
                    && !hora.isBefore(horario.getHorarioInicio())
                    && !hora.isAfter(horario.getHorarioFin())) {
                return;
            }
        }
        throw new IllegalArgumentException(
                mensajes.get("error.turno.fuera-horario", dia, hora));
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
                    mensajes.get("error.turno.choque", fecha, hora));
        }
    }
}
