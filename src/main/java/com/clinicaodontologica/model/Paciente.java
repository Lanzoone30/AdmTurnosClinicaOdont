package com.clinicaodontologica.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Paciente de la clinica. Datos personales heredados de {@link Persona},
 * mas datos de obra social y tipo de sangre.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Paciente extends Persona {

    private boolean tieneOS;

    private String tipoSangre;

    /** Responsable legal del paciente (padre, tutor, etc.). */
    @OneToOne
    @JoinColumn(name = "responsable_id")
    private Responsable responsable;

    /** Turnos asignados al paciente. */
    @OneToMany(mappedBy = "paciente")
    private List<Turno> turnos = new ArrayList<>();
}
