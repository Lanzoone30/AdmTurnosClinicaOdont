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
 * Odontologo de la clinica. Datos personales heredados de {@link Persona},
 * mas especialidad, horario de atencion y cuenta de usuario.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Odontologo extends Persona {

    private String especialidad;

    /** Turnos que atiende el odontologo. */
    @OneToMany(mappedBy = "odontologo")
    private List<Turno> turnos = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToOne
    @JoinColumn(name = "horario_id")
    private Horario horario;
}
