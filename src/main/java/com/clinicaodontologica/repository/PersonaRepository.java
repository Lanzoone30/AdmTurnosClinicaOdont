package com.clinicaodontologica.repository;

import com.clinicaodontologica.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de acceso a datos para {@link Persona}.
 */
public interface PersonaRepository extends JpaRepository<Persona, Integer> {
}
