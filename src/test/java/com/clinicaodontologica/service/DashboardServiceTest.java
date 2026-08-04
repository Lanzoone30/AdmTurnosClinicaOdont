package com.clinicaodontologica.service;

import com.clinicaodontologica.model.EstadoTurno;
import com.clinicaodontologica.model.Turno;
import com.clinicaodontologica.repository.OdontologoRepository;
import com.clinicaodontologica.repository.PacienteRepository;
import com.clinicaodontologica.repository.TurnoRepository;
import com.clinicaodontologica.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Tests unitarios de {@link DashboardService}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DashboardService")
class DashboardServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private OdontologoRepository odontologoRepository;

    @Mock
    private TurnoRepository turnoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    @DisplayName("obtenerDashboard: agrega todas las metricas en un solo llamado")
    void obtenerDashboard_AgregaMetricas() {
        // given
        given(pacienteRepository.count()).willReturn(10L);
        given(odontologoRepository.count()).willReturn(3L);
        given(turnoRepository.count()).willReturn(25L);
        given(usuarioRepository.count()).willReturn(5L);
        given(turnoRepository.countByFechaTurno(LocalDate.now())).willReturn(2L);
        given(turnoRepository.findTop5ByOrderByFechaTurnoDescHoraTurnoDesc()).willReturn(List.of(new Turno()));
        given(pacienteRepository.findTop5ByOrderByIdDesc()).willReturn(List.of());
        given(turnoRepository.countByEstado(EstadoTurno.PENDIENTE)).willReturn(7L);
        given(turnoRepository.countByEstado(EstadoTurno.CONFIRMADO)).willReturn(8L);
        given(turnoRepository.countByEstado(EstadoTurno.REALIZADO)).willReturn(5L);
        given(turnoRepository.countByEstado(EstadoTurno.CANCELADO)).willReturn(5L);
        given(turnoRepository.countByFechaTurnoBetween(
                org.mockito.ArgumentMatchers.any(LocalDate.class),
                org.mockito.ArgumentMatchers.any(LocalDate.class))).willReturn(4L);
        given(pacienteRepository.countByFechaAltaBetween(
                org.mockito.ArgumentMatchers.any(LocalDate.class),
                org.mockito.ArgumentMatchers.any(LocalDate.class))).willReturn(3L);
        given(turnoRepository.contarTurnosPorOdontologo()).willReturn(List.of());

        // when
        DashboardService.DashboardData data = dashboardService.obtenerDashboard();

        // then
        assertThat(data.getTotalPacientes()).isEqualTo(10L);
        assertThat(data.getTotalOdontologos()).isEqualTo(3L);
        assertThat(data.getTotalTurnos()).isEqualTo(25L);
        assertThat(data.getTotalUsuarios()).isEqualTo(5L);
        assertThat(data.getTurnosHoy()).isEqualTo(2L);
        assertThat(data.getTurnosPendientes()).isEqualTo(7L);
        assertThat(data.getTurnosConfirmados()).isEqualTo(8L);
        assertThat(data.getTurnosRealizados()).isEqualTo(5L);
        assertThat(data.getTurnosCancelados()).isEqualTo(5L);
        assertThat(data.getTurnosSemanaActual()).isEqualTo(4L);
        assertThat(data.getPacientesNuevosMes()).isEqualTo(3L);
        assertThat(data.getOdontologoTop()).isNull();
    }
}
