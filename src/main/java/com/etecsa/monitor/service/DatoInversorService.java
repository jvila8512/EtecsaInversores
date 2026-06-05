package com.etecsa.monitor.service;

import com.etecsa.monitor.domain.DatoInversor;
import com.etecsa.monitor.repository.DatoInversorRepository;
import com.etecsa.monitor.service.dto.DatoInversorDTO;
import com.etecsa.monitor.service.mapper.DatoInversorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.etecsa.monitor.domain.DatoInversor}.
 */
@Service
@Transactional
public class DatoInversorService {

    private static final Logger LOG = LoggerFactory.getLogger(DatoInversorService.class);

    private final DatoInversorRepository datoInversorRepository;

    private final DatoInversorMapper datoInversorMapper;

    public DatoInversorService(DatoInversorRepository datoInversorRepository, DatoInversorMapper datoInversorMapper) {
        this.datoInversorRepository = datoInversorRepository;
        this.datoInversorMapper = datoInversorMapper;
    }

    /**
     * Save a datoInversor.
     *
     * @param datoInversorDTO the entity to save.
     * @return the persisted entity.
     */
    public DatoInversorDTO save(DatoInversorDTO datoInversorDTO) {
        LOG.debug("Request to save DatoInversor : {}", datoInversorDTO);
        DatoInversor datoInversor = datoInversorMapper.toEntity(datoInversorDTO);
        datoInversor = datoInversorRepository.save(datoInversor);
        return datoInversorMapper.toDto(datoInversor);
    }

    /**
     * Update a datoInversor.
     *
     * @param datoInversorDTO the entity to save.
     * @return the persisted entity.
     */
    public DatoInversorDTO update(DatoInversorDTO datoInversorDTO) {
        LOG.debug("Request to update DatoInversor : {}", datoInversorDTO);
        DatoInversor datoInversor = datoInversorMapper.toEntity(datoInversorDTO);
        datoInversor = datoInversorRepository.save(datoInversor);
        return datoInversorMapper.toDto(datoInversor);
    }

    /**
     * Partially update a datoInversor.
     *
     * @param datoInversorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DatoInversorDTO> partialUpdate(DatoInversorDTO datoInversorDTO) {
        LOG.debug("Request to partially update DatoInversor : {}", datoInversorDTO);

        return datoInversorRepository
            .findById(datoInversorDTO.getId())
            .map(existingDatoInversor -> {
                datoInversorMapper.partialUpdate(existingDatoInversor, datoInversorDTO);

                return existingDatoInversor;
            })
            .map(datoInversorRepository::save)
            .map(datoInversorMapper::toDto);
    }

    /**
     * Get all the datoInversors with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DatoInversorDTO> findAllWithEagerRelationships(Pageable pageable) {
        return datoInversorRepository.findAllWithEagerRelationships(pageable).map(datoInversorMapper::toDto);
    }

    /**
     * Get one datoInversor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DatoInversorDTO> findOne(Long id) {
        LOG.debug("Request to get DatoInversor : {}", id);
        return datoInversorRepository.findOneWithEagerRelationships(id).map(datoInversorMapper::toDto);
    }

    /**
     * Delete the datoInversor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DatoInversor : {}", id);
        datoInversorRepository.deleteById(id);
    }
}
