package com.clinicaodontologica.repository;

import com.clinicaodontologica.model.EstadoTurno;
import com.clinicaodontologica.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio de acceso a datos para {@link Turno}.
 */
public interface TurnoRepository extends JpaRepository<Turno, Integer> {

    /**
     * Proyeccion del conteo de turnos por odontologo.
     */
    interface OdontologoConteo {
        String getNombre();
        String getApellido();
        Long getTotal();
    }

    /**
     * Cuenta los turnos en un rango de fechas.
     *
     * @param desde fecha inicial (inclusive)
     * @param hasta fecha final (inclusive)
     * @return cantidad de turnos en el rango
     */
    long countByFechaTurnoBetween(LocalDate desde, LocalDate hasta);

    /**
     * Cuenta los turnos por estado.
     *
     * @param estado estado a contar
     * @return cantidad de turnos con ese estado
     */
    long countByEstado(EstadoTurno estado);

    /**
     * Cuenta los turnos de cada odontologo, ordenados de mayor a menor.
     *
     * @return lista de odontologos con su total de turnos
     */
    @Query("select t.odontologo.nombre as nombre, t.odontologo.apellido as apellido, count(t) as total "
            + "from Turno t group by t.odontologo.id, t.odontologo.nombre, t.odontologo.apellido "
            + "order by total desc")
    List<OdontologoConteo> contarTurnosPorOdontologo();

    /**
     * Busca turnos por estado.
     *
     * @param estado estado de los turnos
     * @return turnos con ese estado
     */
    List<Turno> findByEstado(EstadoTurno estado);

    /**
     * Busca turnos por texto en la afeccion (coincidencia parcial, sin
     * distinguir mayusculas).
     *
     * @param texto texto a buscar en la afeccion
     * @return turnos que coinciden
     */
    List<Turno> findByAfeccionContainingIgnoreCase(String texto);

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
     * Busca los turnos de un paciente ordenados por fecha y hora
     * descendente (mas recientes primero).
     *
     * @param pacienteId id del paciente
     * @return turnos del paciente, mas recientes primero
     */
    List<Turno> findByPacienteIdOrderByFechaTurnoDescHoraTurnoDesc(Integer pacienteId);

    /**
     * Busca los turnos de un odontologo en un rango de fechas, ordenados
     * por hora.
     *
     * @param odontologoId id del odontologo
     * @param inicio fecha inicial del rango
     * @param fin fecha final del rango
     * @return turnos del odontologo en el rango
     */
    List<Turno> findByOdontologoIdAndFechaTurnoBetweenOrderByHoraTurno(
            Integer odontologoId, LocalDate inicio, LocalDate fin);

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
