package com.etecsa.monitor.service;

import com.etecsa.monitor.domain.ModeloInversor;
import com.etecsa.monitor.repository.ModeloInversorRepository;
import com.etecsa.monitor.service.dto.ModeloInversorDTO;
import com.etecsa.monitor.service.mapper.ModeloInversorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.etecsa.monitor.domain.ModeloInversor}.
 */
@Service
@Transactional
public class ModeloInversorService {

    private static final Logger LOG = LoggerFactory.getLogger(ModeloInversorService.class);

    private final ModeloInversorRepository modeloInversorRepository;

    private final ModeloInversorMapper modeloInversorMapper;

    public ModeloInversorService(ModeloInversorRepository modeloInversorRepository, ModeloInversorMapper modeloInversorMapper) {
        this.modeloInversorRepository = modeloInversorRepository;
        this.modeloInversorMapper = modeloInversorMapper;
    }

    /**
     * Save a modeloInversor.
     *
     * @param modeloInversorDTO the entity to save.
     * @return the persisted entity.
     */
    public ModeloInversorDTO save(ModeloInversorDTO modeloInversorDTO) {
        LOG.debug("Request to save ModeloInversor : {}", modeloInversorDTO);
        ModeloInversor modeloInversor = modeloInversorMapper.toEntity(modeloInversorDTO);
        modeloInversor = modeloInversorRepository.save(modeloInversor);
        return modeloInversorMapper.toDto(modeloInversor);
    }

    /**
     * Update a modeloInversor.
     *
     * @param modeloInversorDTO the entity to save.
     * @return the persisted entity.
     */
    public ModeloInversorDTO update(ModeloInversorDTO modeloInversorDTO) {
        LOG.debug("Request to update ModeloInversor : {}", modeloInversorDTO);
        ModeloInversor modeloInversor = modeloInversorMapper.toEntity(modeloInversorDTO);
        modeloInversor = modeloInversorRepository.save(modeloInversor);
        return modeloInversorMapper.toDto(modeloInversor);
    }

    /**
     * Partially update a modeloInversor.
     *
     * @param modeloInversorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ModeloInversorDTO> partialUpdate(ModeloInversorDTO modeloInversorDTO) {
        LOG.debug("Request to partially update ModeloInversor : {}", modeloInversorDTO);

        return modeloInversorRepository
            .findById(modeloInversorDTO.getId())
            .map(existingModeloInversor -> {
                modeloInversorMapper.partialUpdate(existingModeloInversor, modeloInversorDTO);

                return existingModeloInversor;
            })
            .map(modeloInversorRepository::save)
            .map(modeloInversorMapper::toDto);
    }

    /**
     * Get all the modeloInversors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ModeloInversorDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ModeloInversors");
        return modeloInversorRepository.findAll(pageable).map(modeloInversorMapper::toDto);
    }

    /**
     * Get all the modeloInversors with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ModeloInversorDTO> findAllWithEagerRelationships(Pageable pageable) {
        return modeloInversorRepository.findAllWithEagerRelationships(pageable).map(modeloInversorMapper::toDto);
    }

    /**
     * Get one modeloInversor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ModeloInversorDTO> findOne(Long id) {
        LOG.debug("Request to get ModeloInversor : {}", id);
        return modeloInversorRepository.findOneWithEagerRelationships(id).map(modeloInversorMapper::toDto);
    }

    /**
     * Delete the modeloInversor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ModeloInversor : {}", id);
        modeloInversorRepository.deleteById(id);
    }
}
