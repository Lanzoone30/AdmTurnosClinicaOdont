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

    /** Nombre de la obra social (solo si {@code tieneOS} es true). */
    private String nombreOS;

    /** Numero de afiliado en la obra social. */
    private String numeroAfiliado;

    private String tipoSangre;

    /** Responsable legal del paciente (padre, tutor, etc.). */
    @OneToOne
    @JoinColumn(name = "responsable_id")
    private Responsable responsable;

    /** Cuenta de usuario del paciente (portal de autogestion). */
    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    /** Turnos asignados al paciente. */
    @OneToMany(mappedBy = "paciente")
    private List<Turno> turnos = new ArrayList<>();
}
