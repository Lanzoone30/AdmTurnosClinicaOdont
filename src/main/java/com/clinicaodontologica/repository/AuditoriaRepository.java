package com.clinicaodontologica.repository;

import com.clinicaodontologica.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio de acceso a datos para {@link Auditoria}.
 */
public interface AuditoriaRepository extends JpaRepository<Auditoria, Integer> {

    /**
     * Obtiene los ultimos registros de auditoria, mas recientes primero.
     *
     * @return lista de los ultimos 100 registros
     */
    List<Auditoria> findTop100ByOrderByFechaDesc();
}
