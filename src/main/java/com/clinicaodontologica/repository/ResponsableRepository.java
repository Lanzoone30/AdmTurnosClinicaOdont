package com.clinicaodontologica.repository;

import com.clinicaodontologica.model.Responsable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de acceso a datos para {@link Responsable}.
 */
public interface ResponsableRepository extends JpaRepository<Responsable, Integer> {
}
