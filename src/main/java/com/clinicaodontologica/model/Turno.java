package com.clinicaodontologica.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Turno (cita) asignado a un {@link Paciente} con un {@link Odontologo}.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Turno implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    /** Fecha de la cita. */
    private LocalDate fechaTurno;

    /** Hora de la cita. */
    private LocalTime horaTurno;

    private String afeccion;

    /** Estado del ciclo de vida del turno. Por defecto {@code PENDIENTE}. */
    @Enumerated(EnumType.STRING)
    private EstadoTurno estado = EstadoTurno.PENDIENTE;

    /** Diagnosis and treatment performed. Required when set to REALIZADO. */
    @Column(length = 2000)
    private String notaClinica;

    /** Cancellation reason. Required when set to CANCELADO. */
    @Column(length = 500)
    private String motivoCancelacion;

    @ManyToOne
    @JoinColumn(name = "id_odonto")
    private Odontologo odontologo;

    @ManyToOne
    @JoinColumn(name = "id_pacien")
    private Paciente paciente;
}
