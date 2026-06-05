package com.etecsa.monitor.service;

import com.etecsa.monitor.domain.Inversor;
import com.etecsa.monitor.repository.InversorRepository;
import com.etecsa.monitor.service.dto.InversorDTO;
import com.etecsa.monitor.service.mapper.InversorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.etecsa.monitor.domain.Inversor}.
 */
@Service
@Transactional
public class InversorService {

    private static final Logger LOG = LoggerFactory.getLogger(InversorService.class);

    private final InversorRepository inversorRepository;

    private final InversorMapper inversorMapper;

    public InversorService(InversorRepository inversorRepository, InversorMapper inversorMapper) {
        this.inversorRepository = inversorRepository;
        this.inversorMapper = inversorMapper;
    }

    /**
     * Save a inversor.
     *
     * @param inversorDTO the entity to save.
     * @return the persisted entity.
     */
    public InversorDTO save(InversorDTO inversorDTO) {
        LOG.debug("Request to save Inversor : {}", inversorDTO);
        Inversor inversor = inversorMapper.toEntity(inversorDTO);
        inversor = inversorRepository.save(inversor);
        return inversorMapper.toDto(inversor);
    }

    /**
     * Update a inversor.
     *
     * @param inversorDTO the entity to save.
     * @return the persisted entity.
     */
    public InversorDTO update(InversorDTO inversorDTO) {
        LOG.debug("Request to update Inversor : {}", inversorDTO);
        Inversor inversor = inversorMapper.toEntity(inversorDTO);
        inversor = inversorRepository.save(inversor);
        return inversorMapper.toDto(inversor);
    }

    /**
     * Partially update a inversor.
     *
     * @param inversorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InversorDTO> partialUpdate(InversorDTO inversorDTO) {
        LOG.debug("Request to partially update Inversor : {}", inversorDTO);

        return inversorRepository
            .findById(inversorDTO.getId())
            .map(existingInversor -> {
                inversorMapper.partialUpdate(existingInversor, inversorDTO);

                return existingInversor;
            })
            .map(inversorRepository::save)
            .map(inversorMapper::toDto);
    }

    /**
     * Get all the inversors with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<InversorDTO> findAllWithEagerRelationships(Pageable pageable) {
        return inversorRepository.findAllWithEagerRelationships(pageable).map(inversorMapper::toDto);
    }

    /**
     * Get one inversor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InversorDTO> findOne(Long id) {
        LOG.debug("Request to get Inversor : {}", id);
        return inversorRepository.findOneWithEagerRelationships(id).map(inversorMapper::toDto);
    }

    /**
     * Delete the inversor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Inversor : {}", id);
        inversorRepository.deleteById(id);
    }
}
