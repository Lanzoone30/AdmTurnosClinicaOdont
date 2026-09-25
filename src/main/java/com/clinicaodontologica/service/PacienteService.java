package com.clinicaodontologica.service;

import com.clinicaodontologica.model.Paciente;
import com.clinicaodontologica.repository.PacienteRepository;
import com.clinicaodontologica.repository.TurnoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio de gestion de pacientes: CRUD y busqueda.
 */
@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final TurnoRepository turnoRepository;
    private final Mensajes mensajes;

    /**
     * Lista todos los pacientes.
     *
     * @return lista de pacientes
     */
    @Transactional(readOnly = true)
    public List<Paciente> listar() {
        return pacienteRepository.findAll();
    }

    /**
     * Busca pacientes por nombre, apellido o DNI exacto. Si la busqueda
     * es vacia o nula, devuelve todos.
     *
     * @param busqueda texto a buscar
     * @return pacientes coincidentes
     */
    @Transactional(readOnly = true)
    public List<Paciente> buscar(String busqueda) {
        if (busqueda == null || busqueda.isBlank()) {
            return listar();
        }
        Paciente porDni = buscarPorDni(busqueda.trim());
        if (porDni != null) {
            return List.of(porDni);
        }
        return pacienteRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
                busqueda, busqueda);
    }

    /**
     * Busca un paciente por su DNI exacto.
     *
     * @param dni DNI del paciente
     * @return el paciente encontrado, o {@code null} si no existe
     */
    @Transactional(readOnly = true)
    public Paciente buscarPorDni(String dni) {
        return pacienteRepository.findByDni(dni).orElse(null);
    }

    /**
     * Busca un paciente por su id.
     *
     * @param id id del paciente
     * @return el paciente encontrado
     * @throws IllegalArgumentException si el paciente no existe
     */
    @Transactional(readOnly = true)
    public Paciente obtener(Integer id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(mensajes.get("error.paciente.no-encontrado", id)));
    }

    /**
     * Crea un paciente validando que el DNI no este en uso.
     *
     * @param paciente datos del paciente
     * @return el paciente creado
     * @throws IllegalArgumentException si el DNI ya existe
     */
    @Transactional
    public Paciente crear(Paciente paciente) {
        validarDniUnico(paciente.getDni(), null);
        paciente.setId(null);
        paciente.setFechaAlta(LocalDate.now());
        return pacienteRepository.save(paciente);
    }

    /**
     * Actualiza los datos de un paciente validando que el DNI no este en
     * uso por otro paciente.
     *
     * @param paciente datos actualizados
     * @return el paciente actualizado
     * @throws IllegalArgumentException si el DNI ya existe en otro paciente
     */
    @Transactional
    public Paciente actualizar(Paciente paciente) {
        obtener(paciente.getId());
        validarDniUnico(paciente.getDni(), paciente.getId());
        return pacienteRepository.save(paciente);
    }

    /**
     * Verifica que el DNI no pertenezca a otro paciente.
     *
     * @param dni DNI a validar
     * @param idPacienteExcluido id del paciente que se esta editando, o
     *        {@code null} al crear
     * @throws IllegalArgumentException si el DNI ya existe
     */
    private void validarDniUnico(String dni, Integer idPacienteExcluido) {
        if (dni == null || dni.isBlank()) {
            return;
        }
        Paciente existente = buscarPorDni(dni);
        if (existente != null && !existente.getId().equals(idPacienteExcluido)) {
            throw new IllegalArgumentException(mensajes.get("error.paciente.dni-duplicado", dni));
        }
    }

    /**
     * Elimina un paciente por su id.
     *
     * @param id id del paciente a eliminar
     */
    @Transactional
    public void eliminar(Integer id) {
        if (turnoRepository.existsByPacienteId(id)) {
            throw new IllegalArgumentException(
                    mensajes.get("error.paciente.eliminar-con-turnos"));
        }
        pacienteRepository.deleteById(id);
    }
}
