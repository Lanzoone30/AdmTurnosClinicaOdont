package com.clinicaodontologica.model;

import jakarta.persistence.CascadeType;
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
    @ToString.Exclude
    @OneToMany(mappedBy = "odontologo")
    private List<Turno> turnos = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    /** Weekly availability, one entry per attended weekday. */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "odontologo_id")
    private List<Horario> horarios = new ArrayList<>();
}
