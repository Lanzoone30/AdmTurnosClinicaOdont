package com.clinicaodontologica.repository;

import com.clinicaodontologica.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio de acceso a datos para {@link Paciente}.
 */
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {

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
     * Obtiene los ultimos pacientes registrados.
     *
     * @return lista de los ultimos 5 pacientes
     */
    List<Paciente> findTop5ByOrderByIdDesc();
}
