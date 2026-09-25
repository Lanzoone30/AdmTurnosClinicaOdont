package com.clinicaodontologica.service;

import com.clinicaodontologica.model.Usuario;
import com.clinicaodontologica.repository.OdontologoRepository;
import com.clinicaodontologica.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de gestion de usuarios: CRUD y creacion con contrasenia
 * hasheada.
 */
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final OdontologoRepository odontologoRepository;
    private final PasswordEncoder passwordEncoder;
    private final Mensajes mensajes;

    /**
     * Lista todos los usuarios del sistema.
     *
     * @return lista de usuarios
     */
    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    /**
     * Busca un usuario por su id.
     *
     * @param id id del usuario
     * @return el usuario encontrado
     * @throws IllegalArgumentException si el usuario no existe
     */
    @Transactional(readOnly = true)
    public Usuario obtener(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(mensajes.get("error.usuario.no-encontrado-id", id)));
    }

    /**
     * Crea un usuario hasheando la contrasenia con BCrypt.
     *
     * @param usuario datos del usuario (con contrasenia en texto plano)
     * @return el usuario creado
     */
    @Transactional
    public Usuario crear(Usuario usuario) {
        usuario.setId(null);
        usuario.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));
        return usuarioRepository.save(usuario);
    }

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param nombreUsuario nombre de usuario
     * @return el usuario, o {@code null} si no existe
     */
    @Transactional(readOnly = true)
    public Usuario obtenerPorNombre(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario).orElse(null);
    }

    /**
     * Guarda la preferencia de idioma del usuario.
     *
     * @param nombreUsuario nombre de usuario
     * @param idioma codigo de idioma ("es" o "en")
     */
    @Transactional
    public void cambiarIdioma(String nombreUsuario, String idioma) {
        usuarioRepository.findByNombreUsuario(nombreUsuario).ifPresent(usuario -> {
            usuario.setIdioma(idioma);
            usuarioRepository.save(usuario);
        });
    }

    /**
     * Cambia la contrasenia de un usuario validando la contrasenia actual.
     *
     * @param nombreUsuario nombre de usuario
     * @param contraseniaActual contrasenia actual en texto plano
     * @param contraseniaNueva nueva contrasenia en texto plano
     * @throws IllegalArgumentException si la contrasenia actual no coincide
     */
    @Transactional
    public void cambiarContrasenia(String nombreUsuario, String contraseniaActual,
                                   String contraseniaNueva) {
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new IllegalArgumentException(mensajes.get("error.usuario.no-encontrado", nombreUsuario)));
        if (!passwordEncoder.matches(contraseniaActual, usuario.getContrasenia())) {
            throw new IllegalArgumentException(mensajes.get("error.usuario.password-actual"));
        }
        if (contraseniaNueva == null || contraseniaNueva.isBlank()) {
            throw new IllegalArgumentException(mensajes.get("error.usuario.password-vacia"));
        }
        usuario.setContrasenia(passwordEncoder.encode(contraseniaNueva));
        usuarioRepository.save(usuario);
    }

    /**
     * Actualiza los datos de un usuario. Si llega una contrasenia nueva
     * (no vacia) la re-hashea; si llega vacia conserva la anterior.
     *
     * @param usuario datos actualizados
     * @return el usuario actualizado
     */
    @Transactional
    public Usuario actualizar(Usuario usuario) {
        Usuario existente = obtener(usuario.getId());
        existente.setNombreUsuario(usuario.getNombreUsuario());
        existente.setRol(usuario.getRol());
        if (usuario.getContrasenia() != null && !usuario.getContrasenia().isBlank()) {
            existente.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));
        }
        return usuarioRepository.save(existente);
    }

    /**
     * Elimina un usuario por su id.
     *
     * @param id id del usuario a eliminar
     */
    @Transactional
    public void eliminar(Integer id) {
        if (odontologoRepository.existsByUsuarioId(id)) {
            throw new IllegalArgumentException(
                    mensajes.get("error.usuario.eliminar-asociado"));
        }
        usuarioRepository.deleteById(id);
    }
}
