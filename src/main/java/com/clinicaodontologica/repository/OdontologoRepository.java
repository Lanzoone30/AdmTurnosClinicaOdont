package com.clinicaodontologica.repository;

import com.clinicaodontologica.model.Odontologo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de acceso a datos para {@link Odontologo}.
 */
public interface OdontologoRepository extends JpaRepository<Odontologo, Integer> {

    /**
     * Busca odontologos por nombre o apellido (coincidencia parcial, sin
     * distinguir mayusculas).
     *
     * @param nombre fragmento del nombre a buscar
     * @param apellido fragmento del apellido a buscar
     * @return odontologos que coinciden con el nombre o el apellido
     */
    List<Odontologo> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
            String nombre, String apellido);

    /**
     * Busca un odontologo por su nombre de usuario.
     *
     * @param nombreUsuario nombre de usuario del odontologo
     * @return el odontologo asociado, o vacio si no existe
     */
    Optional<Odontologo> findByUsuarioNombreUsuario(String nombreUsuario);

    /** Checks if a user account is linked to any dentist, to guard user deletes. */
    boolean existsByUsuarioId(Integer usuarioId);
}
