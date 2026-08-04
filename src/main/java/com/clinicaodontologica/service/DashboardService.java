package com.clinicaodontologica.service;

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
    }

    /**
     * Recolecta todas las metricas del dashboard en una sola transaccion.
     *
     * @return datos agregados del dashboard
     */
    @Transactional(readOnly = true)
    public DashboardData obtenerDashboard() {
        return new DashboardData(
                pacienteRepository.count(),
                odontologoRepository.count(),
                turnoRepository.count(),
                usuarioRepository.count(),
                turnoRepository.countByFechaTurno(LocalDate.now()),
                turnoRepository.findTop5ByOrderByFechaTurnoDescHoraTurnoDesc(),
                pacienteRepository.findTop5ByOrderByIdDesc()
        );
    }
}
