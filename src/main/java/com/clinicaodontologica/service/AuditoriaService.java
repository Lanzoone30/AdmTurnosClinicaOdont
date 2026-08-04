package com.clinicaodontologica.service;

import com.clinicaodontologica.model.Auditoria;
import com.clinicaodontologica.model.TipoAccion;
import com.clinicaodontologica.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de registro y consulta de auditoria.
 */
@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;

    /**
     * Registra una operacion en la auditoria.
     *
     * @param entidad nombre de la entidad afectada
     * @param accion tipo de accion realizada
     * @param detalle resumen del cambio
     */
    @Transactional
    public void registrar(String entidad, TipoAccion accion, String detalle) {
        Auditoria registro = new Auditoria();
        registro.setFecha(LocalDateTime.now());
        registro.setUsuario(usuarioActual());
        registro.setEntidad(entidad);
        registro.setAccion(accion);
        registro.setDetalle(detalle);
        auditoriaRepository.save(registro);
    }

    /**
     * Obtiene los ultimos registros de auditoria.
     *
     * @return lista de los ultimos 100 registros
     */
    @Transactional(readOnly = true)
    public List<Auditoria> listar() {
        return auditoriaRepository.findTop100ByOrderByFechaDesc();
    }

    /**
     * Obtiene el nombre del usuario autenticado, o "anonimo" si no hay
     * sesion.
     *
     * @return nombre del usuario actual
     */
    private String usuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth == null ? "anonimo" : auth.getName();
    }
}
