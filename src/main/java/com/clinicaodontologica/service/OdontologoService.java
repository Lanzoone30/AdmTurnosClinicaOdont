package com.clinicaodontologica.service;

import com.clinicaodontologica.model.DiaSemana;
import com.clinicaodontologica.model.Horario;
import com.clinicaodontologica.model.Odontologo;
import com.clinicaodontologica.repository.OdontologoRepository;
import com.clinicaodontologica.repository.TurnoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de gestion de odontologos: CRUD y busqueda.
 */
@Service
@RequiredArgsConstructor
public class OdontologoService {

    private final OdontologoRepository odontologoRepository;
    private final TurnoRepository turnoRepository;

    /**
     * Builds a fixed seven-row weekly schedule for the form, reusing any
     * existing slot for each weekday so the dentist edits one row per day.
     *
     * @param odontologo the dentist, or a new instance
     * @return one {@link Horario} per {@link DiaSemana}, in order
     */
    public static List<Horario> horarioSemanal(Odontologo odontologo) {
        List<Horario> existentes = odontologo.getHorarios() == null
                ? List.of() : odontologo.getHorarios();
        List<Horario> filas = new ArrayList<>();
        for (DiaSemana dia : DiaSemana.values()) {
            Horario fila = existentes.stream()
                    .filter(h -> dia.equals(h.getDiaSemana()))
                    .findFirst().orElse(null);
            if (fila == null) {
                fila = new Horario();
                fila.setDiaSemana(dia);
            }
            filas.add(fila);
        }
        return filas;
    }

    /**
     * Drops empty rows so a blank weekday is not persisted.
     *
     * @param odontologo the dentist whose schedule is sanitized in place
     */
    private static void limpiarHorarios(Odontologo odontologo) {
        if (odontologo.getHorarios() == null) {
            odontologo.setHorarios(new ArrayList<>());
            return;
        }
        odontologo.getHorarios().removeIf(h -> h.getHorarioInicio() == null
                || h.getHorarioFin() == null);
    }

    /**
     * Lista todos los odontologos.
     *
     * @return lista de odontologos
     */
    @Transactional(readOnly = true)
    public List<Odontologo> listar() {
        return odontologoRepository.findAll();
    }

    /**
     * Busca odontologos por nombre o apellido. Si la busqueda es vacia o
     * nula, devuelve todos.
     *
     * @param busqueda texto a buscar
     * @return odontologos coincidentes
     */
    @Transactional(readOnly = true)
    public List<Odontologo> buscar(String busqueda) {
        if (busqueda == null || busqueda.isBlank()) {
            return listar();
        }
        return odontologoRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
                busqueda, busqueda);
    }

    /**
     * Busca un odontologo por su id.
     *
     * @param id id del odontologo
     * @return el odontologo encontrado
     * @throws IllegalArgumentException si el odontologo no existe
     */
    @Transactional(readOnly = true)
    public Odontologo obtener(Integer id) {
        return odontologoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Odontologo no encontrado: " + id));
    }

    /**
     * Crea un odontologo.
     *
     * @param odontologo datos del odontologo
     * @return el odontologo creado
     */
    @Transactional
    public Odontologo crear(Odontologo odontologo) {
        odontologo.setId(null);
        odontologo.setFechaAlta(LocalDate.now());
        limpiarHorarios(odontologo);
        return odontologoRepository.save(odontologo);
    }

    /**
     * Actualiza los datos de un odontologo.
     *
     * @param odontologo datos actualizados
     * @return el odontologo actualizado
     */
    @Transactional
    public Odontologo actualizar(Odontologo odontologo) {
        Odontologo actual = obtener(odontologo.getId());
        limpiarHorarios(odontologo);
        actual.getHorarios().clear();
        actual.getHorarios().addAll(odontologo.getHorarios());
        actual.setDni(odontologo.getDni());
        actual.setNombre(odontologo.getNombre());
        actual.setApellido(odontologo.getApellido());
        actual.setTelefono(odontologo.getTelefono());
        actual.setDireccion(odontologo.getDireccion());
        actual.setEspecialidad(odontologo.getEspecialidad());
        return odontologoRepository.save(actual);
    }

    /**
     * Elimina un odontologo por su id.
     *
     * @param id id del odontologo a eliminar
     */
    @Transactional
    public void eliminar(Integer id) {
        if (turnoRepository.existsByOdontologoId(id)) {
            throw new IllegalArgumentException(
                    "No se puede eliminar el odontologo: tiene turnos registrados");
        }
        Odontologo odontologo = obtener(id);
        if (odontologo.getUsuario() != null) {
            throw new IllegalArgumentException(
                    "No se puede eliminar el odontologo: tiene un usuario asociado");
        }
        odontologoRepository.deleteById(id);
    }
}
