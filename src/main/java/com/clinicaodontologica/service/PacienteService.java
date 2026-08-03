package com.clinicaodontologica.service;

import com.clinicaodontologica.model.Paciente;
import com.clinicaodontologica.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de gestion de pacientes: CRUD y busqueda.
 */
@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;

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
     * Busca pacientes por nombre o apellido. Si la busqueda es vacia o
     * nula, devuelve todos.
     *
     * @param busqueda texto a buscar
     * @return pacientes coincidentes
     */
    @Transactional(readOnly = true)
    public List<Paciente> buscar(String busqueda) {
        if (busqueda == null || busqueda.isBlank()) {
            return listar();
        }
        return pacienteRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
                busqueda, busqueda);
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
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado: " + id));
    }

    /**
     * Crea un paciente.
     *
     * @param paciente datos del paciente
     * @return el paciente creado
     */
    @Transactional
    public Paciente crear(Paciente paciente) {
        paciente.setId(null);
        return pacienteRepository.save(paciente);
    }

    /**
     * Actualiza los datos de un paciente.
     *
     * @param paciente datos actualizados
     * @return el paciente actualizado
     */
    @Transactional
    public Paciente actualizar(Paciente paciente) {
        obtener(paciente.getId());
        return pacienteRepository.save(paciente);
    }

    /**
     * Elimina un paciente por su id.
     *
     * @param id id del paciente a eliminar
     */
    @Transactional
    public void eliminar(Integer id) {
        pacienteRepository.deleteById(id);
    }
}
