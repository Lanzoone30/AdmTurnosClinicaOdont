package com.clinicaodontologica.model;

import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Responsable legal de un {@link Paciente}. Datos personales heredados
 * de {@link Persona}, mas el tipo de vinculo (padre, tutor, etc.).
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Responsable extends Persona {

    private String tipoResp;
}
