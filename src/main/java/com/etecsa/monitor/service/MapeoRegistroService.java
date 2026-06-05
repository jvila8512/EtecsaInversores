package com.etecsa.monitor.service;

import com.etecsa.monitor.domain.MapeoRegistro;
import com.etecsa.monitor.repository.MapeoRegistroRepository;
import com.etecsa.monitor.service.dto.MapeoRegistroDTO;
import com.etecsa.monitor.service.mapper.MapeoRegistroMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.etecsa.monitor.domain.MapeoRegistro}.
 */
@Service
@Transactional
public class MapeoRegistroService {

    private static final Logger LOG = LoggerFactory.getLogger(MapeoRegistroService.class);

    private final MapeoRegistroRepository mapeoRegistroRepository;

    private final MapeoRegistroMapper mapeoRegistroMapper;

    public MapeoRegistroService(MapeoRegistroRepository mapeoRegistroRepository, MapeoRegistroMapper mapeoRegistroMapper) {
        this.mapeoRegistroRepository = mapeoRegistroRepository;
        this.mapeoRegistroMapper = mapeoRegistroMapper;
    }

    /**
     * Save a mapeoRegistro.
     *
     * @param mapeoRegistroDTO the entity to save.
     * @return the persisted entity.
     */
    public MapeoRegistroDTO save(MapeoRegistroDTO mapeoRegistroDTO) {
        LOG.debug("Request to save MapeoRegistro : {}", mapeoRegistroDTO);
        MapeoRegistro mapeoRegistro = mapeoRegistroMapper.toEntity(mapeoRegistroDTO);
        mapeoRegistro = mapeoRegistroRepository.save(mapeoRegistro);
        return mapeoRegistroMapper.toDto(mapeoRegistro);
    }

    /**
     * Update a mapeoRegistro.
     *
     * @param mapeoRegistroDTO the entity to save.
     * @return the persisted entity.
     */
    public MapeoRegistroDTO update(MapeoRegistroDTO mapeoRegistroDTO) {
        LOG.debug("Request to update MapeoRegistro : {}", mapeoRegistroDTO);
        MapeoRegistro mapeoRegistro = mapeoRegistroMapper.toEntity(mapeoRegistroDTO);
        mapeoRegistro = mapeoRegistroRepository.save(mapeoRegistro);
        return mapeoRegistroMapper.toDto(mapeoRegistro);
    }

    /**
     * Partially update a mapeoRegistro.
     *
     * @param mapeoRegistroDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MapeoRegistroDTO> partialUpdate(MapeoRegistroDTO mapeoRegistroDTO) {
        LOG.debug("Request to partially update MapeoRegistro : {}", mapeoRegistroDTO);

        return mapeoRegistroRepository
            .findById(mapeoRegistroDTO.getId())
            .map(existingMapeoRegistro -> {
                mapeoRegistroMapper.partialUpdate(existingMapeoRegistro, mapeoRegistroDTO);

                return existingMapeoRegistro;
            })
            .map(mapeoRegistroRepository::save)
            .map(mapeoRegistroMapper::toDto);
    }

    /**
     * Get all the mapeoRegistros with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MapeoRegistroDTO> findAllWithEagerRelationships(Pageable pageable) {
        return mapeoRegistroRepository.findAllWithEagerRelationships(pageable).map(mapeoRegistroMapper::toDto);
    }

    /**
     * Get one mapeoRegistro by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MapeoRegistroDTO> findOne(Long id) {
        LOG.debug("Request to get MapeoRegistro : {}", id);
        return mapeoRegistroRepository.findOneWithEagerRelationships(id).map(mapeoRegistroMapper::toDto);
    }

    /**
     * Delete the mapeoRegistro by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MapeoRegistro : {}", id);
        mapeoRegistroRepository.deleteById(id);
    }
}
