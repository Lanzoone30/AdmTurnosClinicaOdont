package com.clinicaodontologica.repository;

import com.clinicaodontologica.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio de acceso a datos para {@link Turno}.
 */
public interface TurnoRepository extends JpaRepository<Turno, Integer> {

    /**
     * Busca los turnos de un odontologo en una fecha.
     *
     * @param odontologoId id del odontologo
     * @param fecha fecha de la cita
     * @return turnos del odontologo en esa fecha
     */
    List<Turno> findByOdontologoIdAndFechaTurno(Integer odontologoId, LocalDate fecha);

    /**
     * Busca los turnos de un paciente.
     *
     * @param pacienteId id del paciente
     * @return turnos del paciente
     */
    List<Turno> findByPacienteId(Integer pacienteId);

    /**
     * Cuenta los turnos en una fecha especifica.
     *
     * @param fecha fecha a contar
     * @return cantidad de turnos en esa fecha
     */
    long countByFechaTurno(LocalDate fecha);

    /**
     * Obtiene los proximos turnos (ordenados por fecha descendente).
     *
     * @return lista de los proximos turnos
     */
    List<Turno> findTop5ByOrderByFechaTurnoDescHoraTurnoDesc();
}
