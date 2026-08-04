package com.clinicaodontologica.service;

import com.clinicaodontologica.model.Rol;
import com.clinicaodontologica.model.Usuario;
import com.clinicaodontologica.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

/**
 * Tests unitarios de {@link UsuarioService}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioService")
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    @DisplayName("crear: hashea la contrasenia con BCrypt")
    void crear_HasheaContrasenia() {
        // given
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("juan");
        usuario.setContrasenia("secreto");
        usuario.setRol(Rol.ODONTOLOGO);
        given(passwordEncoder.encode("secreto")).willReturn("$2a$10$hash");
        given(usuarioRepository.save(org.mockito.ArgumentMatchers.any(Usuario.class)))
                .willAnswer(inv -> inv.getArgument(0));

        // when
        Usuario resultado = usuarioService.crear(usuario);

        // then
        assertThat(resultado.getId()).isNull();
        assertThat(resultado.getContrasenia()).isEqualTo("$2a$10$hash");
        assertThat(resultado.getContrasenia()).isNotEqualTo("secreto");
    }

    @Test
    @DisplayName("cambiarContrasenia: valida la actual y re-hashea la nueva")
    void cambiarContrasenia_ValidaYActualiza() {
        // given
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("juan");
        usuario.setContrasenia("$2a$10$viejo");
        given(usuarioRepository.findByNombreUsuario("juan")).willReturn(Optional.of(usuario));
        given(passwordEncoder.matches("correcta", "$2a$10$viejo")).willReturn(true);
        given(passwordEncoder.encode("nueva")).willReturn("$2a$10$nuevo");
        given(usuarioRepository.save(usuario)).willReturn(usuario);

        // when
        usuarioService.cambiarContrasenia("juan", "correcta", "nueva");

        // then
        assertThat(usuario.getContrasenia()).isEqualTo("$2a$10$nuevo");
    }

    @Test
    @DisplayName("cambiarContrasenia: rechaza contrasenia actual incorrecta")
    void cambiarContrasenia_ActualIncorrecta_LanzaExcepcion() {
        // given
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("juan");
        usuario.setContrasenia("$2a$10$viejo");
        given(usuarioRepository.findByNombreUsuario("juan")).willReturn(Optional.of(usuario));
        given(passwordEncoder.matches("mala", "$2a$10$viejo")).willReturn(false);

        // when/then
        assertThatThrownBy(() -> usuarioService.cambiarContrasenia("juan", "mala", "nueva"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("actual no es correcta");
    }

    @Test
    @DisplayName("cambiarContrasenia: rechaza contrasenia nueva vacia")
    void cambiarContrasenia_NuevaVacia_LanzaExcepcion() {
        // given
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("juan");
        usuario.setContrasenia("$2a$10$viejo");
        given(usuarioRepository.findByNombreUsuario("juan")).willReturn(Optional.of(usuario));
        given(passwordEncoder.matches("correcta", "$2a$10$viejo")).willReturn(true);

        // when/then
        assertThatThrownBy(() -> usuarioService.cambiarContrasenia("juan", "correcta", "  "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no puede estar vacia");
    }

    @Test
    @DisplayName("actualizar: conserva la contrasenia si llega vacia")
    void actualizar_ContraseniaVacia_ConservaLaAnterior() {
        // given
        Usuario existente = new Usuario();
        existente.setId(1);
        existente.setNombreUsuario("juan");
        existente.setContrasenia("$2a$10$viejo");

        Usuario datos = new Usuario();
        datos.setId(1);
        datos.setNombreUsuario("juan2");
        datos.setContrasenia("   ");

        given(usuarioRepository.findById(1)).willReturn(Optional.of(existente));
        given(usuarioRepository.save(existente)).willReturn(existente);

        // when
        Usuario resultado = usuarioService.actualizar(datos);

        // then
        assertThat(resultado.getNombreUsuario()).isEqualTo("juan2");
        assertThat(resultado.getContrasenia()).isEqualTo("$2a$10$viejo");
    }
}
