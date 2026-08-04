package com.clinicaodontologica.service;

import com.clinicaodontologica.model.EstadoTurno;
import com.clinicaodontologica.model.Paciente;
import com.clinicaodontologica.model.Turno;
import com.clinicaodontologica.repository.OdontologoRepository;
import com.clinicaodontologica.repository.PacienteRepository;
import com.clinicaodontologica.repository.TurnoRepository;
import com.clinicaodontologica.repository.UsuarioRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio que agrega metricas del sistema para el dashboard del admin.
 */
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final PacienteRepository pacienteRepository;
    private final OdontologoRepository odontologoRepository;
    private final TurnoRepository turnoRepository;
    private final UsuarioRepository usuarioRepository;

    @Getter
    @RequiredArgsConstructor
    public static class DashboardData {
        private final long totalPacientes;
        private final long totalOdontologos;
        private final long totalTurnos;
        private final long totalUsuarios;
        private final long turnosHoy;
        private final List<Turno> proximosTurnos;
        private final List<Paciente> ultimosPacientes;
        // Resumen ejecutivo
        private final long turnosPendientes;
        private final long turnosConfirmados;
        private final long turnosRealizados;
        private final long turnosCancelados;
        private final long turnosSemanaActual;
        private final long turnosSemanaPasada;
        private final long pacientesNuevosMes;
        private final TurnoRepository.OdontologoConteo odontologoTop;
    }

    /**
     * Recolecta todas las metricas del dashboard en una sola transaccion.
     *
     * @return datos agregados del dashboard
     */
    @Transactional(readOnly = true)
    public DashboardData obtenerDashboard() {
        LocalDate hoy = LocalDate.now();
        LocalDate inicioSemana = hoy.minusDays(hoy.getDayOfWeek().getValue() - 1L);
        LocalDate inicioSemanaPasada = inicioSemana.minusDays(7);
        LocalDate inicioMes = hoy.withDayOfMonth(1);

        List<TurnoRepository.OdontologoConteo> ranking = turnoRepository.contarTurnosPorOdontologo();

        return new DashboardData(
                pacienteRepository.count(),
                odontologoRepository.count(),
                turnoRepository.count(),
                usuarioRepository.count(),
                turnoRepository.countByFechaTurno(hoy),
                turnoRepository.findTop5ByOrderByFechaTurnoDescHoraTurnoDesc(),
                pacienteRepository.findTop5ByOrderByIdDesc(),
                // Resumen ejecutivo
                turnoRepository.countByEstado(EstadoTurno.PENDIENTE),
                turnoRepository.countByEstado(EstadoTurno.CONFIRMADO),
                turnoRepository.countByEstado(EstadoTurno.REALIZADO),
                turnoRepository.countByEstado(EstadoTurno.CANCELADO),
                turnoRepository.countByFechaTurnoBetween(inicioSemana, hoy),
                turnoRepository.countByFechaTurnoBetween(inicioSemanaPasada, inicioSemana.minusDays(1)),
                pacienteRepository.countByFechaAltaBetween(inicioMes, hoy),
                ranking.isEmpty() ? null : ranking.get(0)
        );
    }
}
