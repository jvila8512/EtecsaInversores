package com.etecsa.monitor.service;

import com.etecsa.monitor.domain.AlertaConfig;
import com.etecsa.monitor.repository.AlertaConfigRepository;
import com.etecsa.monitor.service.dto.AlertaConfigDTO;
import com.etecsa.monitor.service.mapper.AlertaConfigMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.etecsa.monitor.domain.AlertaConfig}.
 */
@Service
@Transactional
public class AlertaConfigService {

    private static final Logger LOG = LoggerFactory.getLogger(AlertaConfigService.class);

    private final AlertaConfigRepository alertaConfigRepository;

    private final AlertaConfigMapper alertaConfigMapper;

    public AlertaConfigService(AlertaConfigRepository alertaConfigRepository, AlertaConfigMapper alertaConfigMapper) {
        this.alertaConfigRepository = alertaConfigRepository;
        this.alertaConfigMapper = alertaConfigMapper;
    }

    /**
     * Save a alertaConfig.
     *
     * @param alertaConfigDTO the entity to save.
     * @return the persisted entity.
     */
    public AlertaConfigDTO save(AlertaConfigDTO alertaConfigDTO) {
        LOG.debug("Request to save AlertaConfig : {}", alertaConfigDTO);
        AlertaConfig alertaConfig = alertaConfigMapper.toEntity(alertaConfigDTO);
        alertaConfig = alertaConfigRepository.save(alertaConfig);
        return alertaConfigMapper.toDto(alertaConfig);
    }

    /**
     * Update a alertaConfig.
     *
     * @param alertaConfigDTO the entity to save.
     * @return the persisted entity.
     */
    public AlertaConfigDTO update(AlertaConfigDTO alertaConfigDTO) {
        LOG.debug("Request to update AlertaConfig : {}", alertaConfigDTO);
        AlertaConfig alertaConfig = alertaConfigMapper.toEntity(alertaConfigDTO);
        alertaConfig = alertaConfigRepository.save(alertaConfig);
        return alertaConfigMapper.toDto(alertaConfig);
    }

    /**
     * Partially update a alertaConfig.
     *
     * @param alertaConfigDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlertaConfigDTO> partialUpdate(AlertaConfigDTO alertaConfigDTO) {
        LOG.debug("Request to partially update AlertaConfig : {}", alertaConfigDTO);

        return alertaConfigRepository
            .findById(alertaConfigDTO.getId())
            .map(existingAlertaConfig -> {
                alertaConfigMapper.partialUpdate(existingAlertaConfig, alertaConfigDTO);

                return existingAlertaConfig;
            })
            .map(alertaConfigRepository::save)
            .map(alertaConfigMapper::toDto);
    }

    /**
     * Get all the alertaConfigs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AlertaConfigDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all AlertaConfigs");
        return alertaConfigRepository.findAll(pageable).map(alertaConfigMapper::toDto);
    }

    /**
     * Get all the alertaConfigs with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AlertaConfigDTO> findAllWithEagerRelationships(Pageable pageable) {
        return alertaConfigRepository.findAllWithEagerRelationships(pageable).map(alertaConfigMapper::toDto);
    }

    /**
     * Get one alertaConfig by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlertaConfigDTO> findOne(Long id) {
        LOG.debug("Request to get AlertaConfig : {}", id);
        return alertaConfigRepository.findOneWithEagerRelationships(id).map(alertaConfigMapper::toDto);
    }

    /**
     * Delete the alertaConfig by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlertaConfig : {}", id);
        alertaConfigRepository.deleteById(id);
    }
}
