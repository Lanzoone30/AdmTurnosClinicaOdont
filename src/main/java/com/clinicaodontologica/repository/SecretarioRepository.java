package com.clinicaodontologica.repository;

import com.clinicaodontologica.model.Secretario;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de acceso a datos para {@link Secretario}.
 */
public interface SecretarioRepository extends JpaRepository<Secretario, Integer> {
}
