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

/**
 * Usuario del sistema con credenciales de acceso y rol.
 *
 * <p>La contrasenia se guarda hasheada con BCrypt; nunca en texto plano.</p>
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    private String nombreUsuario;

    /** Hash BCrypt de la contrasenia. Excluida de toString por seguridad. */
    @ToString.Exclude
    private String contrasenia;

    @Enumerated(EnumType.STRING)
    private Rol rol;
}
