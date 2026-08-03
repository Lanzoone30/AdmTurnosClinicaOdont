package com.clinicaodontologica.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Secretario de la clinica. Datos personales heredados de {@link Persona},
 * mas el sector donde trabaja y su cuenta de usuario.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Secretario extends Persona {

    private String sector;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
