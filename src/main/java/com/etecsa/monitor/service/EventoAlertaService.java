package com.etecsa.monitor.service;

import com.etecsa.monitor.domain.EventoAlerta;
import com.etecsa.monitor.repository.EventoAlertaRepository;
import com.etecsa.monitor.service.dto.EventoAlertaDTO;
import com.etecsa.monitor.service.mapper.EventoAlertaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.etecsa.monitor.domain.EventoAlerta}.
 */
@Service
@Transactional
public class EventoAlertaService {

    private static final Logger LOG = LoggerFactory.getLogger(EventoAlertaService.class);

    private final EventoAlertaRepository eventoAlertaRepository;

    private final EventoAlertaMapper eventoAlertaMapper;

    public EventoAlertaService(EventoAlertaRepository eventoAlertaRepository, EventoAlertaMapper eventoAlertaMapper) {
        this.eventoAlertaRepository = eventoAlertaRepository;
        this.eventoAlertaMapper = eventoAlertaMapper;
    }

    /**
     * Save a eventoAlerta.
     *
     * @param eventoAlertaDTO the entity to save.
     * @return the persisted entity.
     */
    public EventoAlertaDTO save(EventoAlertaDTO eventoAlertaDTO) {
        LOG.debug("Request to save EventoAlerta : {}", eventoAlertaDTO);
        EventoAlerta eventoAlerta = eventoAlertaMapper.toEntity(eventoAlertaDTO);
        eventoAlerta = eventoAlertaRepository.save(eventoAlerta);
        return eventoAlertaMapper.toDto(eventoAlerta);
    }

    /**
     * Update a eventoAlerta.
     *
     * @param eventoAlertaDTO the entity to save.
     * @return the persisted entity.
     */
    public EventoAlertaDTO update(EventoAlertaDTO eventoAlertaDTO) {
        LOG.debug("Request to update EventoAlerta : {}", eventoAlertaDTO);
        EventoAlerta eventoAlerta = eventoAlertaMapper.toEntity(eventoAlertaDTO);
        eventoAlerta = eventoAlertaRepository.save(eventoAlerta);
        return eventoAlertaMapper.toDto(eventoAlerta);
    }

    /**
     * Partially update a eventoAlerta.
     *
     * @param eventoAlertaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EventoAlertaDTO> partialUpdate(EventoAlertaDTO eventoAlertaDTO) {
        LOG.debug("Request to partially update EventoAlerta : {}", eventoAlertaDTO);

        return eventoAlertaRepository
            .findById(eventoAlertaDTO.getId())
            .map(existingEventoAlerta -> {
                eventoAlertaMapper.partialUpdate(existingEventoAlerta, eventoAlertaDTO);

                return existingEventoAlerta;
            })
            .map(eventoAlertaRepository::save)
            .map(eventoAlertaMapper::toDto);
    }

    /**
     * Get all the eventoAlertas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<EventoAlertaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return eventoAlertaRepository.findAllWithEagerRelationships(pageable).map(eventoAlertaMapper::toDto);
    }

    /**
     * Get one eventoAlerta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EventoAlertaDTO> findOne(Long id) {
        LOG.debug("Request to get EventoAlerta : {}", id);
        return eventoAlertaRepository.findOneWithEagerRelationships(id).map(eventoAlertaMapper::toDto);
    }

    /**
     * Delete the eventoAlerta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EventoAlerta : {}", id);
        eventoAlertaRepository.deleteById(id);
    }
}
