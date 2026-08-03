package com.clinicaodontologica.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Persona base de la jerarquia de personas de la clinica.
 *
 * <p>Usa herencia SINGLE_TABLE: todos los subtipos (Paciente, Odontologo,
 * Responsable, Secretario) comparten una unica tabla con la columna
 * discriminadora {@code tipo}.</p>
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Persona implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    private String dni;

    private String nombre;

    private String apellido;

    private String telefono;

    private String direccion;

    /** Fecha de nacimiento en formato {@code yyyy-MM-dd}. */
    @Column(name = "fecha_nac")
    private LocalDate fechaNac;
}
