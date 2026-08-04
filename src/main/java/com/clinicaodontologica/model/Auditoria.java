package com.clinicaodontologica.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Registro de auditoria de operaciones sobre entidades del sistema.
 *
 * <p>Registra quien realizo la operacion, sobre que entidad, que accion
 * y cuando. Se alimenta via AOP en los servicios.</p>
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Auditoria implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    private LocalDateTime fecha;

    /** Nombre del usuario que realizo la operacion. */
    private String usuario;

    /** Nombre de la entidad afectada (ej: "Turno"). */
    private String entidad;

    @Enumerated(EnumType.STRING)
    private TipoAccion accion;

    /** Resumen del cambio (ej: "Turno #3 -> estado CONFIRMADO"). */
    private String detalle;
}
