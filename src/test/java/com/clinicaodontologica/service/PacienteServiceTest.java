package com.clinicaodontologica.service;

import com.clinicaodontologica.model.Paciente;
import com.clinicaodontologica.repository.PacienteRepository;
import com.clinicaodontologica.repository.TurnoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

/**
 * Tests unitarios de {@link PacienteService}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PacienteService")
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private TurnoRepository turnoRepository;

    @InjectMocks
    private PacienteService pacienteService;

    @Test
    @DisplayName("crear: setea fecha de alta y guarda el paciente")
    void crear_AsignaFechaAlta_YGuarda() {
        // given
        Paciente paciente = new Paciente();
        paciente.setDni("30123456");
        given(pacienteRepository.findByDni("30123456")).willReturn(Optional.empty());
        given(pacienteRepository.save(org.mockito.ArgumentMatchers.any(Paciente.class)))
                .willAnswer(inv -> inv.getArgument(0));

        // when
        Paciente resultado = pacienteService.crear(paciente);

        // then
        assertThat(resultado.getId()).isNull();
        assertThat(resultado.getFechaAlta()).isEqualTo(LocalDate.now());
        then(pacienteRepository).should().save(paciente);
    }

    @Test
    @DisplayName("crear: rechaza DNI duplicado")
    void crear_ConDniDuplicado_LanzaExcepcion() {
        // given
        Paciente existente = new Paciente();
        existente.setId(5);
        existente.setDni("30123456");
        Paciente nuevo = new Paciente();
        nuevo.setDni("30123456");
        given(pacienteRepository.findByDni("30123456")).willReturn(Optional.of(existente));

        // when/then
        assertThatThrownBy(() -> pacienteService.crear(nuevo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ya existe un paciente");

        then(pacienteRepository).should(never()).save(org.mockito.ArgumentMatchers.any(Paciente.class));
    }

    @Test
    @DisplayName("actualizar: conserva los campos clinicos")
    void actualizar_ConservaCamposClinicos() {
        // given
        Paciente existente = new Paciente();
        existente.setId(5);
        existente.setDni("30123456");
        Paciente editado = new Paciente();
        editado.setId(5);
        editado.setDni("30123456");
        editado.setAlergias("penicilina");
        editado.setAntecedentes("hipertension");
        editado.setMedicacion("enalapril");
        given(pacienteRepository.findById(5)).willReturn(Optional.of(existente));
        given(pacienteRepository.findByDni("30123456")).willReturn(Optional.of(existente));
        given(pacienteRepository.save(editado)).willReturn(editado);

        // when
        Paciente resultado = pacienteService.actualizar(editado);

        // then
        assertThat(resultado.getAlergias()).isEqualTo("penicilina");
        assertThat(resultado.getAntecedentes()).isEqualTo("hipertension");
        assertThat(resultado.getMedicacion()).isEqualTo("enalapril");
    }

    @Test
    @DisplayName("actualizar: permite conservar el propio DNI al editar")
    void actualizar_ConElMismoDni_NoLanzaExcepcion() {        // given
        Paciente existente = new Paciente();
        existente.setId(5);
        existente.setDni("30123456");
        Paciente editado = new Paciente();
        editado.setId(5);
        editado.setDni("30123456");
        given(pacienteRepository.findById(5)).willReturn(Optional.of(existente));
        given(pacienteRepository.findByDni("30123456")).willReturn(Optional.of(existente));
        given(pacienteRepository.save(editado)).willReturn(editado);

        // when
        Paciente resultado = pacienteService.actualizar(editado);

        // then
        assertThat(resultado.getDni()).isEqualTo("30123456");
    }

    @Test
    @DisplayName("buscar: prioriza coincidencia exacta de DNI sobre nombre")
    void buscar_ConDniExacto_DevuelveSoloEsePaciente() {
        // given
        Paciente porDni = new Paciente();
        porDni.setId(7);
        porDni.setDni("30123456");
        given(pacienteRepository.findByDni("30123456")).willReturn(Optional.of(porDni));

        // when
        var resultado = pacienteService.buscar("30123456");

        // then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getId()).isEqualTo(7);
        then(pacienteRepository).should(never())
                .findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
                        org.mockito.ArgumentMatchers.anyString(),
                        org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("eliminar: bloquea si el paciente tiene turnos")
    void eliminar_ConTurnos_LanzaExcepcion() {
        // given
        given(turnoRepository.existsByPacienteId(5)).willReturn(true);

        // when/then
        assertThatThrownBy(() -> pacienteService.eliminar(5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("turnos registrados");

        then(pacienteRepository).should(never()).deleteById(5);
    }

    @Test
    @DisplayName("eliminar: borra si el paciente no tiene turnos")
    void eliminar_SinTurnos_Borra() {
        // given
        given(turnoRepository.existsByPacienteId(5)).willReturn(false);

        // when
        pacienteService.eliminar(5);

        // then
        then(pacienteRepository).should().deleteById(5);
    }
}
