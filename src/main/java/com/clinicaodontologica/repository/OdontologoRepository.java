package com.clinicaodontologica.repository;

import com.clinicaodontologica.model.Odontologo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio de acceso a datos para {@link Odontologo}.
 */
public interface OdontologoRepository extends JpaRepository<Odontologo, Integer> {

    /**
     * Busca odontologos por nombre o apellido (coincidencia parcial, sin
     * distinguir mayusculas).
     *
     * @param nombre fragmento del nombre a buscar
     * @param apellido fragmento del apellido a buscar
     * @return odontologos que coinciden con el nombre o el apellido
     */
    List<Odontologo> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
            String nombre, String apellido);
}
