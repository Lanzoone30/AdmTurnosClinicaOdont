package com.clinicaodontologica.service;

import com.clinicaodontologica.model.DiaSemana;
import com.clinicaodontologica.model.EstadoTurno;
import com.clinicaodontologica.model.Horario;
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

    @org.mockito.Spy
    private Mensajes mensajes = new MensajesEco();

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
    @DisplayName("crear: rechaza turno cuando el odontologo ya tiene uno a la misma hora")    void crear_ConChoqueDeHorario_LanzaExcepcion() {
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
                .hasMessageContaining("error.turno.choque");

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
    @DisplayName("crear: acepta turno dentro del horario del odontologo")
    void crear_DentroDeHorario_NoLanzaExcepcion() {
        // given
        Horario horario = new Horario();
        horario.setDiaSemana(DiaSemana.MIERCOLES);
        horario.setHorarioInicio(LocalTime.of(8, 0));
        horario.setHorarioFin(LocalTime.of(12, 0));
        odontologo.setHorarios(List.of(horario));
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
        assertThat(resultado.getHoraTurno()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    @DisplayName("crear: rechaza turno fuera del horario del odontologo")
    void crear_FueraDeHorario_LanzaExcepcion() {
        // given
        Horario horario = new Horario();
        horario.setDiaSemana(DiaSemana.MIERCOLES);
        horario.setHorarioInicio(LocalTime.of(8, 0));
        horario.setHorarioFin(LocalTime.of(12, 0));
        odontologo.setHorarios(List.of(horario));
        given(odontologoRepository.findById(1)).willReturn(Optional.of(odontologo));
        given(pacienteRepository.findById(2)).willReturn(Optional.of(paciente));
        given(turnoRepository.findByOdontologoIdAndFechaTurno(1, LocalDate.of(2026, 8, 5)))
                .willReturn(List.of());

        // when/then
        assertThatThrownBy(() -> turnoService.crear(
                LocalDate.of(2026, 8, 5), LocalTime.of(15, 0), "Dolor", 1, 2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("error.turno.fuera-horario");

        then(turnoRepository).should(never()).save(org.mockito.ArgumentMatchers.any(Turno.class));
    }

    @Test
    @DisplayName("registrarNota: guarda la nota clinica recortada")
    void registrarNota_GuardaNotaRecortada() {
        // given
        Turno turno = new Turno();
        turno.setId(3);
        given(turnoRepository.findById(3)).willReturn(Optional.of(turno));
        given(turnoRepository.save(turno)).willReturn(turno);

        // when
        Turno resultado = turnoService.registrarNota(3, "  Conducto radicular  ");

        // then
        assertThat(resultado.getNotaClinica()).isEqualTo("Conducto radicular");
    }

    @Test
    @DisplayName("cambiarEstado: confirma un turno pendiente")
    void cambiarEstado_PendienteAConfirmado_ActualizaEstado() {
        // given
        Turno turno = new Turno();
        turno.setId(4);
        turno.setEstado(EstadoTurno.PENDIENTE);
        given(turnoRepository.findById(4)).willReturn(Optional.of(turno));
        given(turnoRepository.save(turno)).willReturn(turno);

        // when
        Turno resultado = turnoService.cambiarEstado(4, EstadoTurno.CONFIRMADO, null);

        // then
        assertThat(resultado.getEstado()).isEqualTo(EstadoTurno.CONFIRMADO);
    }

    @Test
    @DisplayName("cambiarEstado: rechaza cancelar sin motivo")
    void cambiarEstado_CanceladoSinMotivo_LanzaExcepcion() {
        // given
        Turno turno = new Turno();
        turno.setId(5);
        turno.setEstado(EstadoTurno.PENDIENTE);
        given(turnoRepository.findById(5)).willReturn(Optional.of(turno));

        // when/then
        assertThatThrownBy(() -> turnoService.cambiarEstado(5, EstadoTurno.CANCELADO, "  "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("error.turno.motivo-obligatorio");

        then(turnoRepository).should(never()).save(org.mockito.ArgumentMatchers.any(Turno.class));
    }

    @Test
    @DisplayName("cambiarEstado: guarda el motivo al cancelar")
    void cambiarEstado_CanceladoConMotivo_GuardaMotivo() {
        // given
        Turno turno = new Turno();
        turno.setId(6);
        turno.setEstado(EstadoTurno.CONFIRMADO);
        given(turnoRepository.findById(6)).willReturn(Optional.of(turno));
        given(turnoRepository.save(turno)).willReturn(turno);

        // when
        Turno resultado = turnoService.cambiarEstado(6, EstadoTurno.CANCELADO, "  Paciente reprograma ");

        // then
        assertThat(resultado.getEstado()).isEqualTo(EstadoTurno.CANCELADO);
        assertThat(resultado.getMotivoCancelacion()).isEqualTo("Paciente reprograma");
    }

    @Test
    @DisplayName("cambiarEstado: marca no asistio desde pendiente")
    void cambiarEstado_PendienteANoAsistio_ActualizaEstado() {
        // given
        Turno turno = new Turno();
        turno.setId(7);
        turno.setEstado(EstadoTurno.PENDIENTE);
        given(turnoRepository.findById(7)).willReturn(Optional.of(turno));
        given(turnoRepository.save(turno)).willReturn(turno);

        // when
        Turno resultado = turnoService.cambiarEstado(7, EstadoTurno.NO_ASISTIO, null);

        // then
        assertThat(resultado.getEstado()).isEqualTo(EstadoTurno.NO_ASISTIO);
    }

    @Test
    @DisplayName("cambiarEstado: rechaza reabrir un turno cancelado")
    void cambiarEstado_CanceladoAConfirmado_LanzaExcepcion() {
        // given
        Turno turno = new Turno();
        turno.setId(8);
        turno.setEstado(EstadoTurno.CANCELADO);
        given(turnoRepository.findById(8)).willReturn(Optional.of(turno));

        // when/then
        assertThatThrownBy(() -> turnoService.cambiarEstado(8, EstadoTurno.CONFIRMADO, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("error.turno.transicion-invalida");

        then(turnoRepository).should(never()).save(org.mockito.ArgumentMatchers.any(Turno.class));
    }

    @Test
    @DisplayName("cambiarEstado: exige nota clinica al marcar realizado")
    void cambiarEstado_RealizadoSinNota_LanzaExcepcion() {
        // given
        Turno turno = new Turno();
        turno.setId(9);
        turno.setEstado(EstadoTurno.CONFIRMADO);
        given(turnoRepository.findById(9)).willReturn(Optional.of(turno));

        // when/then
        assertThatThrownBy(() -> turnoService.cambiarEstado(9, EstadoTurno.REALIZADO, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("error.turno.nota-obligatoria");

        then(turnoRepository).should(never()).save(org.mockito.ArgumentMatchers.any(Turno.class));
    }
}
