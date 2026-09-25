package com.clinicaodontologica.controller;

import com.clinicaodontologica.model.Turno;
import com.clinicaodontologica.service.Mensajes;
import com.clinicaodontologica.service.OdontologoService;
import com.clinicaodontologica.service.PacienteService;
import com.clinicaodontologica.service.TurnoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

/**
 * Tests unitarios de los portales personales de {@link TurnoController}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("TurnoController")
class TurnoControllerTest {

    @Mock
    private TurnoService turnoService;

    @Mock
    private OdontologoService odontologoService;

    @Mock
    private PacienteService pacienteService;

    @Mock
    private Mensajes mensajes;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TurnoController turnoController;

    @Test
    @DisplayName("misTurnos devuelve la vista con los turnos del paciente")
    void misTurnosDevuelveSuPortal() {
        given(authentication.getName()).willReturn("pac1");
        List<Turno> turnos = List.of(new Turno());
        given(turnoService.listarPorPacienteLogueado("pac1")).willReturn(turnos);
        Model model = new ExtendedModelMap();

        String vista = turnoController.misTurnos(authentication, model);

        assertThat(vista).isEqualTo("turno/mis-turnos");
        assertThat(model.getAttribute("turnos")).isEqualTo(turnos);
    }

    @Test
    @DisplayName("miAgenda devuelve la vista con la agenda del odontologo")
    void miAgendaDevuelveSuPortal() {
        given(authentication.getName()).willReturn("odonto1");
        List<Turno> turnos = List.of(new Turno());
        given(turnoService.listarAgendaOdontologoLogueado(eq("odonto1"), any(LocalDate.class), any(LocalDate.class)))
                .willReturn(turnos);
        Model model = new ExtendedModelMap();

        String vista = turnoController.miAgenda(authentication, null, model);

        assertThat(vista).isEqualTo("turno/mi-agenda");
        assertThat(model.getAttribute("turnos")).isEqualTo(turnos);
    }

    @Test
    @DisplayName("misTurnos sin vinculo devuelve el aviso, no propaga la excepcion")
    void misTurnosSinVinculoMuestraAviso() {
        given(authentication.getName()).willReturn("sinficha");
        given(turnoService.listarPorPacienteLogueado("sinficha"))
                .willThrow(new IllegalArgumentException("sin paciente"));
        given(mensajes.get("error.titulo")).willReturn("Página no encontrada");
        Model model = new ExtendedModelMap();

        String vista = turnoController.misTurnos(authentication, model);

        assertThat(vista).isEqualTo("error");
        assertThat(model.getAttribute("errorTexto")).isEqualTo("sin paciente");
        assertThat(model.getAttribute("errorTitulo")).isEqualTo("Página no encontrada");
    }

    @Test
    @DisplayName("miAgenda sin vinculo devuelve el aviso, no propaga la excepcion")
    void miAgendaSinVinculoMuestraAviso() {
        given(authentication.getName()).willReturn("sinficha");
        given(turnoService.listarAgendaOdontologoLogueado(eq("sinficha"), any(LocalDate.class), any(LocalDate.class)))
                .willThrow(new IllegalArgumentException("sin odontologo"));
        given(mensajes.get("error.titulo")).willReturn("Página no encontrada");
        Model model = new ExtendedModelMap();

        String vista = turnoController.miAgenda(authentication, null, model);

        assertThat(vista).isEqualTo("error");
        assertThat(model.getAttribute("errorTexto")).isEqualTo("sin odontologo");
    }
}
