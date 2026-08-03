package com.clinicaodontologica.repository;

import com.clinicaodontologica.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio de acceso a datos para {@link Usuario}.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param nombreUsuario nombre de usuario a buscar
     * @return el usuario encontrado, o vacio si no existe
     */
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
}
