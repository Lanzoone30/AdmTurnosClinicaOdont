package com.clinicaodontologica.repository;

import com.clinicaodontologica.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de acceso a datos para {@link Horario}.
 */
public interface HorarioRepository extends JpaRepository<Horario, Integer> {
}
