package com.etecsa.monitor.service;

import com.etecsa.monitor.domain.GatewayTcp;
import com.etecsa.monitor.repository.GatewayTcpRepository;
import com.etecsa.monitor.service.dto.GatewayTcpDTO;
import com.etecsa.monitor.service.mapper.GatewayTcpMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.etecsa.monitor.domain.GatewayTcp}.
 */
@Service
@Transactional
public class GatewayTcpService {

    private static final Logger LOG = LoggerFactory.getLogger(GatewayTcpService.class);

    private final GatewayTcpRepository gatewayTcpRepository;

    private final GatewayTcpMapper gatewayTcpMapper;

    public GatewayTcpService(GatewayTcpRepository gatewayTcpRepository, GatewayTcpMapper gatewayTcpMapper) {
        this.gatewayTcpRepository = gatewayTcpRepository;
        this.gatewayTcpMapper = gatewayTcpMapper;
    }

    /**
     * Save a gatewayTcp.
     *
     * @param gatewayTcpDTO the entity to save.
     * @return the persisted entity.
     */
    public GatewayTcpDTO save(GatewayTcpDTO gatewayTcpDTO) {
        LOG.debug("Request to save GatewayTcp : {}", gatewayTcpDTO);
        GatewayTcp gatewayTcp = gatewayTcpMapper.toEntity(gatewayTcpDTO);
        gatewayTcp = gatewayTcpRepository.save(gatewayTcp);
        return gatewayTcpMapper.toDto(gatewayTcp);
    }

    /**
     * Update a gatewayTcp.
     *
     * @param gatewayTcpDTO the entity to save.
     * @return the persisted entity.
     */
    public GatewayTcpDTO update(GatewayTcpDTO gatewayTcpDTO) {
        LOG.debug("Request to update GatewayTcp : {}", gatewayTcpDTO);
        GatewayTcp gatewayTcp = gatewayTcpMapper.toEntity(gatewayTcpDTO);
        gatewayTcp = gatewayTcpRepository.save(gatewayTcp);
        return gatewayTcpMapper.toDto(gatewayTcp);
    }

    /**
     * Partially update a gatewayTcp.
     *
     * @param gatewayTcpDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GatewayTcpDTO> partialUpdate(GatewayTcpDTO gatewayTcpDTO) {
        LOG.debug("Request to partially update GatewayTcp : {}", gatewayTcpDTO);

        return gatewayTcpRepository
            .findById(gatewayTcpDTO.getId())
            .map(existingGatewayTcp -> {
                gatewayTcpMapper.partialUpdate(existingGatewayTcp, gatewayTcpDTO);

                return existingGatewayTcp;
            })
            .map(gatewayTcpRepository::save)
            .map(gatewayTcpMapper::toDto);
    }

    /**
     * Get all the gatewayTcps with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<GatewayTcpDTO> findAllWithEagerRelationships(Pageable pageable) {
        return gatewayTcpRepository.findAllWithEagerRelationships(pageable).map(gatewayTcpMapper::toDto);
    }

    /**
     * Get one gatewayTcp by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GatewayTcpDTO> findOne(Long id) {
        LOG.debug("Request to get GatewayTcp : {}", id);
        return gatewayTcpRepository.findOneWithEagerRelationships(id).map(gatewayTcpMapper::toDto);
    }

    /**
     * Delete the gatewayTcp by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete GatewayTcp : {}", id);
        gatewayTcpRepository.deleteById(id);
    }
}
