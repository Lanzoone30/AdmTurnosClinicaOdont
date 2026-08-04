package com.clinicaodontologica.service;

import com.clinicaodontologica.model.Usuario;
import com.clinicaodontologica.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de autenticacion para Spring Security: carga el usuario por
 * nombre de usuario y lo adapta a {@link UserDetails}.
 */
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Carga el usuario para la autenticacion de Spring Security.
     *
     * @param nombreUsuario nombre de usuario
     * @return detalles del usuario con su rol
     * @throws UsernameNotFoundException si el usuario no existe
     */
    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + nombreUsuario));

        return new User(
                usuario.getNombreUsuario(),
                usuario.getContrasenia(),
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name())));
    }
}
