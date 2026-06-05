package com.etecsa.monitor.service;

import com.etecsa.monitor.domain.*; // for static metamodels
import com.etecsa.monitor.domain.GatewayTcp;
import com.etecsa.monitor.repository.GatewayTcpRepository;
import com.etecsa.monitor.service.criteria.GatewayTcpCriteria;
import com.etecsa.monitor.service.dto.GatewayTcpDTO;
import com.etecsa.monitor.service.mapper.GatewayTcpMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link GatewayTcp} entities in the database.
 * The main input is a {@link GatewayTcpCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link GatewayTcpDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GatewayTcpQueryService extends QueryService<GatewayTcp> {

    private static final Logger LOG = LoggerFactory.getLogger(GatewayTcpQueryService.class);

    private final GatewayTcpRepository gatewayTcpRepository;

    private final GatewayTcpMapper gatewayTcpMapper;

    public GatewayTcpQueryService(GatewayTcpRepository gatewayTcpRepository, GatewayTcpMapper gatewayTcpMapper) {
        this.gatewayTcpRepository = gatewayTcpRepository;
        this.gatewayTcpMapper = gatewayTcpMapper;
    }

    /**
     * Return a {@link Page} of {@link GatewayTcpDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GatewayTcpDTO> findByCriteria(GatewayTcpCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GatewayTcp> specification = createSpecification(criteria);
        return gatewayTcpRepository.findAll(specification, page).map(gatewayTcpMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GatewayTcpCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<GatewayTcp> specification = createSpecification(criteria);
        return gatewayTcpRepository.count(specification);
    }

    /**
     * Function to convert {@link GatewayTcpCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GatewayTcp> createSpecification(GatewayTcpCriteria criteria) {
        Specification<GatewayTcp> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), GatewayTcp_.id),
                buildStringSpecification(criteria.getNombre(), GatewayTcp_.nombre),
                buildStringSpecification(criteria.getDescripcion(), GatewayTcp_.descripcion),
                buildStringSpecification(criteria.getUbicacion(), GatewayTcp_.ubicacion),
                buildStringSpecification(criteria.getDireccionIp(), GatewayTcp_.direccionIp),
                buildRangeSpecification(criteria.getPuerto(), GatewayTcp_.puerto),
                buildSpecification(criteria.getActivo(), GatewayTcp_.activo),
                buildSpecification(criteria.getEstadoConexion(), GatewayTcp_.estadoConexion),
                buildRangeSpecification(criteria.getUltimaLectura(), GatewayTcp_.ultimaLectura),
                buildStringSpecification(criteria.getUltimoError(), GatewayTcp_.ultimoError),
                buildRangeSpecification(criteria.getTiempoEsperaMs(), GatewayTcp_.tiempoEsperaMs),
                buildRangeSpecification(criteria.getReintentos(), GatewayTcp_.reintentos),
                buildSpecification(criteria.getSitioId(), root -> root.join(GatewayTcp_.sitio, JoinType.LEFT).get(Sitio_.id))
            );
        }
        return specification;
    }
}
