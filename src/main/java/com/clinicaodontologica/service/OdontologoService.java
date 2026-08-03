package com.clinicaodontologica.service;

import com.clinicaodontologica.model.Odontologo;
import com.clinicaodontologica.repository.OdontologoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de gestion de odontologos: CRUD y busqueda.
 */
@Service
@RequiredArgsConstructor
public class OdontologoService {

    private final OdontologoRepository odontologoRepository;

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
        obtener(odontologo.getId());
        return odontologoRepository.save(odontologo);
    }

    /**
     * Elimina un odontologo por su id.
     *
     * @param id id del odontologo a eliminar
     */
    @Transactional
    public void eliminar(Integer id) {
        odontologoRepository.deleteById(id);
    }
}
