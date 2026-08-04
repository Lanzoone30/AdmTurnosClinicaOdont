package com.clinicaodontologica.repository;

import com.clinicaodontologica.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio de acceso a datos para {@link Paciente}.
 */
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {

    /**
     * Cuenta los pacientes dados de alta en un rango de fechas.
     *
     * @param desde fecha inicial (inclusive)
     * @param hasta fecha final (inclusive)
     * @return cantidad de pacientes nuevos en el rango
     */
    long countByFechaAltaBetween(LocalDate desde, LocalDate hasta);

    /**
     * Busca pacientes por nombre o apellido (coincidencia parcial, sin
     * distinguir mayusculas).
     *
     * @param nombre fragmento del nombre a buscar
     * @param apellido fragmento del apellido a buscar
     * @return pacientes que coinciden con el nombre o el apellido
     */
    List<Paciente> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
            String nombre, String apellido);

    /**
     * Busca un paciente por su DNI exacto.
     *
     * @param dni DNI del paciente
     * @return el paciente con ese DNI, o vacio si no existe
     */
    Optional<Paciente> findByDni(String dni);

    /**
     * Busca un paciente por su nombre de usuario.
     *
     * @param nombreUsuario nombre de usuario del paciente
     * @return el paciente asociado, o vacio si no existe
     */
    Optional<Paciente> findByUsuarioNombreUsuario(String nombreUsuario);

    /**
     * Obtiene los ultimos pacientes registrados.
     *
     * @return lista de los ultimos 5 pacientes
     */
    List<Paciente> findTop5ByOrderByIdDesc();
}
