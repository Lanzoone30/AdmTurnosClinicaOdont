package com.clinicaodontologica.controller;

import com.clinicaodontologica.service.Mensajes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Tests del mapeo de estado de {@link ErrorController}.
 */
@DisplayName("ErrorController")
class ErrorControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        Mensajes mensajes = mock(Mensajes.class);
        given(mensajes.get(anyString())).willAnswer(inv -> inv.getArgument(0));
        mockMvc = MockMvcBuilders.standaloneSetup(new ErrorController(mensajes))
                .setViewResolvers(new InternalResourceViewResolver("/WEB-INF/views/", ".html"))
                .build();
    }

    @ParameterizedTest
    @CsvSource({"403,error.acceso-denegado,error.acceso-denegado-texto",
                "404,error.titulo,error.texto",
                "500,error.servidor,error.servidor-texto"})
    @DisplayName("cada estado mapea a su aviso")
    void cadaEstadoMapeaSuAviso(int codigo, String titulo, String texto) throws Exception {
        mockMvc.perform(get("/error").requestAttr("jakarta.servlet.error.status_code", codigo))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("codigo", codigo))
                .andExpect(model().attribute("errorTitulo", titulo))
                .andExpect(model().attribute("errorTexto", texto));
    }

    @Test
    @DisplayName("un estado desconocido cae al aviso por defecto")
    void estadoDesconocidoCaeAlDefecto() throws Exception {
        mockMvc.perform(get("/error").requestAttr("jakarta.servlet.error.status_code", 418))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorTitulo", "error.titulo"))
                .andExpect(model().attribute("errorTexto", "error.texto"));
    }
}
