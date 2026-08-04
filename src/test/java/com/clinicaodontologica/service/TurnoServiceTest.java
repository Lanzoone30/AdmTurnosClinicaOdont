package com.clinicaodontologica.service;

import com.clinicaodontologica.model.EstadoTurno;
import com.clinicaodontologica.model.Odontologo;
import com.clinicaodontologica.model.Paciente;
import com.clinicaodontologica.model.Turno;
import com.clinicaodontologica.repository.OdontologoRepository;
import com.clinicaodontologica.repository.PacienteRepository;
import com.clinicaodontologica.repository.TurnoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

/**
 * Tests unitarios de {@link TurnoService}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("TurnoService")
class TurnoServiceTest {

    @Mock
    private TurnoRepository turnoRepository;

    @Mock
    private OdontologoRepository odontologoRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private TurnoService turnoService;

    private Odontologo odontologo;
    private Paciente paciente;

    @BeforeEach
    void setUp() {
        odontologo = new Odontologo();
        odontologo.setId(1);
        odontologo.setNombre("Juan");
        odontologo.setApellido("Perez");

        paciente = new Paciente();
        paciente.setId(2);
        paciente.setNombre("Maria");
        paciente.setApellido("Gomez");
    }

    @Test
    @DisplayName("crear: crea turno con estado PENDIENTE por defecto")
    void crear_ConEstadoDefault_DevuelveTurnoPendiente() {
        // given
        given(odontologoRepository.findById(1)).willReturn(Optional.of(odontologo));
        given(pacienteRepository.findById(2)).willReturn(Optional.of(paciente));
        given(turnoRepository.findByOdontologoIdAndFechaTurno(1, LocalDate.of(2026, 8, 5)))
                .willReturn(List.of());
        given(turnoRepository.save(org.mockito.ArgumentMatchers.any(Turno.class)))
                .willAnswer(inv -> inv.getArgument(0));

        // when
        Turno resultado = turnoService.crear(
                LocalDate.of(2026, 8, 5), LocalTime.of(10, 0), "Dolor", 1, 2);

        // then
        assertThat(resultado.getEstado()).isEqualTo(EstadoTurno.PENDIENTE);
        assertThat(resultado.getOdontologo()).isEqualTo(odontologo);
        assertThat(resultado.getPaciente()).isEqualTo(paciente);
    }

    @Test
    @DisplayName("crear: rechaza turno cuando el odontologo ya tiene uno a la misma hora")
    void crear_ConChoqueDeHorario_LanzaExcepcion() {
        // given
        Turno existente = new Turno();
        existente.setId(9);
        existente.setHoraTurno(LocalTime.of(10, 0));
        given(odontologoRepository.findById(1)).willReturn(Optional.of(odontologo));
        given(pacienteRepository.findById(2)).willReturn(Optional.of(paciente));
        given(turnoRepository.findByOdontologoIdAndFechaTurno(1, LocalDate.of(2026, 8, 5)))
                .willReturn(List.of(existente));

        // when/then
        assertThatThrownBy(() -> turnoService.crear(
                LocalDate.of(2026, 8, 5), LocalTime.of(10, 0), "Dolor", 1, 2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ya tiene un turno");

        then(turnoRepository).should(never()).save(org.mockito.ArgumentMatchers.any(Turno.class));
    }

    @Test
    @DisplayName("listar: sin estado devuelve todos; con estado filtra")
    void listar_ConYsinEstado_FiltraCorrectamente() {
        // given
        Turno confirmado = new Turno();
        confirmado.setEstado(EstadoTurno.CONFIRMADO);
        given(turnoRepository.findByEstado(EstadoTurno.CONFIRMADO)).willReturn(List.of(confirmado));

        // when
        List<Turno> todos = turnoService.listar();
        List<Turno> filtrados = turnoService.listar(EstadoTurno.CONFIRMADO);

        // then
        then(turnoRepository).should().findAll();
        then(turnoRepository).should().findByEstado(EstadoTurno.CONFIRMADO);
        assertThat(filtrados).hasSize(1);
        assertThat(todos).isEmpty();
    }

    @Test
    @DisplayName("listarPorPacienteLogueado: resuelve el paciente por usuario y devuelve sus turnos")
    void listarPorPacienteLogueado_ResuelvePacientePorUsuario() {
        // given
        given(pacienteRepository.findByUsuarioNombreUsuario("maria"))
                .willReturn(Optional.of(paciente));
        given(turnoRepository.findByPacienteIdOrderByFechaTurnoDescHoraTurnoDesc(2))
                .willReturn(List.of(new Turno()));

        // when
        List<Turno> resultado = turnoService.listarPorPacienteLogueado("maria");

        // then
        assertThat(resultado).hasSize(1);
        then(turnoRepository).should()
                .findByPacienteIdOrderByFechaTurnoDescHoraTurnoDesc(2);
    }

    @Test
    @DisplayName("listarPorPacienteLogueado: lanza excepcion si el usuario no tiene paciente")
    void listarPorPacienteLogueado_SinPaciente_LanzaExcepcion() {
        // given
        given(pacienteRepository.findByUsuarioNombreUsuario("sinpaciente"))
                .willReturn(Optional.empty());

        // when/then
        assertThatThrownBy(() -> turnoService.listarPorPacienteLogueado("sinpaciente"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No hay paciente vinculado");
    }
}
