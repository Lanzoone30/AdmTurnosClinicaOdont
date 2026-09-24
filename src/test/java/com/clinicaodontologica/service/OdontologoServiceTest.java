package com.clinicaodontologica.service;

import com.clinicaodontologica.model.DiaSemana;
import com.clinicaodontologica.model.Horario;
import com.clinicaodontologica.model.Odontologo;
import com.clinicaodontologica.repository.OdontologoRepository;
import com.clinicaodontologica.repository.TurnoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

/**
 * Tests unitarios de {@link OdontologoService}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("OdontologoService")
class OdontologoServiceTest {

    @Mock
    private OdontologoRepository odontologoRepository;

    @Mock
    private TurnoRepository turnoRepository;

    @InjectMocks
    private OdontologoService odontologoService;

    @Test
    @DisplayName("horarioSemanal: devuelve una fila por dia de la semana")
    void horarioSemanal_DevuelveUnaFilaPorDia() {
        // given
        Odontologo odontologo = new Odontologo();

        // when
        List<Horario> filas = OdontologoService.horarioSemanal(odontologo);

        // then
        assertThat(filas).hasSize(DiaSemana.values().length);
        assertThat(filas.get(0).getDiaSemana()).isEqualTo(DiaSemana.LUNES);
        assertThat(filas).allSatisfy(h -> assertThat(h.getHorarioInicio()).isNull());
    }

    @Test
    @DisplayName("horarioSemanal: reutiliza el horario cargado del dia")
    void horarioSemanal_ReutilizaHorarioExistente() {
        // given
        Horario lunes = new Horario();
        lunes.setId(7);
        lunes.setDiaSemana(DiaSemana.LUNES);
        lunes.setHorarioInicio(LocalTime.of(9, 0));
        lunes.setHorarioFin(LocalTime.of(13, 0));
        Odontologo odontologo = new Odontologo();
        odontologo.setHorarios(List.of(lunes));

        // when
        List<Horario> filas = OdontologoService.horarioSemanal(odontologo);

        // then
        assertThat(filas).hasSize(DiaSemana.values().length);
        assertThat(filas.get(0).getId()).isEqualTo(7);
        assertThat(filas.get(0).getHorarioInicio()).isEqualTo(LocalTime.of(9, 0));
    }

    @Test
    @DisplayName("actualizar: descarta filas sin horario y guarda las completas")
    void actualizar_DescartaFilasVacias() {
        // given
        Odontologo existente = new Odontologo();
        existente.setId(1);
        given(odontologoRepository.findById(1)).willReturn(java.util.Optional.of(existente));
        given(odontologoRepository.save(existente)).willReturn(existente);

        Horario vacio = new Horario();
        vacio.setDiaSemana(DiaSemana.MARTES);
        Horario completo = new Horario();
        completo.setDiaSemana(DiaSemana.LUNES);
        completo.setHorarioInicio(LocalTime.of(8, 0));
        completo.setHorarioFin(LocalTime.of(12, 0));
        Odontologo editado = new Odontologo();
        editado.setId(1);
        editado.setHorarios(new java.util.ArrayList<>(List.of(vacio, completo)));

        // when
        Odontologo resultado = odontologoService.actualizar(editado);

        // then
        assertThat(resultado.getHorarios()).hasSize(1);
        assertThat(resultado.getHorarios().get(0).getDiaSemana()).isEqualTo(DiaSemana.LUNES);
    }

    @Test
    @DisplayName("eliminar: bloquea si el odontologo tiene turnos")
    void eliminar_ConTurnos_LanzaExcepcion() {
        // given
        given(turnoRepository.existsByOdontologoId(1)).willReturn(true);

        // when/then
        assertThatThrownBy(() -> odontologoService.eliminar(1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("turnos registrados");

        then(odontologoRepository).should(never()).deleteById(1);
    }

    @Test
    @DisplayName("eliminar: bloquea si el odontologo tiene usuario asociado")
    void eliminar_ConUsuario_LanzaExcepcion() {
        // given
        Odontologo odontologo = new Odontologo();
        odontologo.setId(1);
        odontologo.setUsuario(new com.clinicaodontologica.model.Usuario());
        given(turnoRepository.existsByOdontologoId(1)).willReturn(false);
        given(odontologoRepository.findById(1)).willReturn(Optional.of(odontologo));

        // when/then
        assertThatThrownBy(() -> odontologoService.eliminar(1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("usuario asociado");

        then(odontologoRepository).should(never()).deleteById(1);
    }
}
