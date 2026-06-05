package com.etecsa.monitor.service;

import com.etecsa.monitor.domain.DatoEficiencia;
import com.etecsa.monitor.repository.DatoEficienciaRepository;
import com.etecsa.monitor.service.dto.DatoEficienciaDTO;
import com.etecsa.monitor.service.mapper.DatoEficienciaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.etecsa.monitor.domain.DatoEficiencia}.
 */
@Service
@Transactional
public class DatoEficienciaService {

    private static final Logger LOG = LoggerFactory.getLogger(DatoEficienciaService.class);

    private final DatoEficienciaRepository datoEficienciaRepository;

    private final DatoEficienciaMapper datoEficienciaMapper;

    public DatoEficienciaService(DatoEficienciaRepository datoEficienciaRepository, DatoEficienciaMapper datoEficienciaMapper) {
        this.datoEficienciaRepository = datoEficienciaRepository;
        this.datoEficienciaMapper = datoEficienciaMapper;
    }

    /**
     * Save a datoEficiencia.
     *
     * @param datoEficienciaDTO the entity to save.
     * @return the persisted entity.
     */
    public DatoEficienciaDTO save(DatoEficienciaDTO datoEficienciaDTO) {
        LOG.debug("Request to save DatoEficiencia : {}", datoEficienciaDTO);
        DatoEficiencia datoEficiencia = datoEficienciaMapper.toEntity(datoEficienciaDTO);
        datoEficiencia = datoEficienciaRepository.save(datoEficiencia);
        return datoEficienciaMapper.toDto(datoEficiencia);
    }

    /**
     * Update a datoEficiencia.
     *
     * @param datoEficienciaDTO the entity to save.
     * @return the persisted entity.
     */
    public DatoEficienciaDTO update(DatoEficienciaDTO datoEficienciaDTO) {
        LOG.debug("Request to update DatoEficiencia : {}", datoEficienciaDTO);
        DatoEficiencia datoEficiencia = datoEficienciaMapper.toEntity(datoEficienciaDTO);
        datoEficiencia = datoEficienciaRepository.save(datoEficiencia);
        return datoEficienciaMapper.toDto(datoEficiencia);
    }

    /**
     * Partially update a datoEficiencia.
     *
     * @param datoEficienciaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DatoEficienciaDTO> partialUpdate(DatoEficienciaDTO datoEficienciaDTO) {
        LOG.debug("Request to partially update DatoEficiencia : {}", datoEficienciaDTO);

        return datoEficienciaRepository
            .findById(datoEficienciaDTO.getId())
            .map(existingDatoEficiencia -> {
                datoEficienciaMapper.partialUpdate(existingDatoEficiencia, datoEficienciaDTO);

                return existingDatoEficiencia;
            })
            .map(datoEficienciaRepository::save)
            .map(datoEficienciaMapper::toDto);
    }

    /**
     * Get all the datoEficiencias with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DatoEficienciaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return datoEficienciaRepository.findAllWithEagerRelationships(pageable).map(datoEficienciaMapper::toDto);
    }

    /**
     * Get one datoEficiencia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DatoEficienciaDTO> findOne(Long id) {
        LOG.debug("Request to get DatoEficiencia : {}", id);
        return datoEficienciaRepository.findOneWithEagerRelationships(id).map(datoEficienciaMapper::toDto);
    }

    /**
     * Delete the datoEficiencia by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DatoEficiencia : {}", id);
        datoEficienciaRepository.deleteById(id);
    }
}
