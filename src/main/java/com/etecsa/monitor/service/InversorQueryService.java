package com.etecsa.monitor.service;

import com.etecsa.monitor.domain.*; // for static metamodels
import com.etecsa.monitor.domain.Inversor;
import com.etecsa.monitor.repository.InversorRepository;
import com.etecsa.monitor.service.criteria.InversorCriteria;
import com.etecsa.monitor.service.dto.InversorDTO;
import com.etecsa.monitor.service.mapper.InversorMapper;
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
 * Service for executing complex queries for {@link Inversor} entities in the database.
 * The main input is a {@link InversorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InversorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InversorQueryService extends QueryService<Inversor> {

    private static final Logger LOG = LoggerFactory.getLogger(InversorQueryService.class);

    private final InversorRepository inversorRepository;

    private final InversorMapper inversorMapper;

    public InversorQueryService(InversorRepository inversorRepository, InversorMapper inversorMapper) {
        this.inversorRepository = inversorRepository;
        this.inversorMapper = inversorMapper;
    }

    /**
     * Return a {@link Page} of {@link InversorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InversorDTO> findByCriteria(InversorCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Inversor> specification = createSpecification(criteria);
        return inversorRepository.findAll(specification, page).map(inversorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InversorCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Inversor> specification = createSpecification(criteria);
        return inversorRepository.count(specification);
    }

    /**
     * Function to convert {@link InversorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Inversor> createSpecification(InversorCriteria criteria) {
        Specification<Inversor> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Inversor_.id),
                buildStringSpecification(criteria.getNombre(), Inversor_.nombre),
                buildStringSpecification(criteria.getNumeroSerie(), Inversor_.numeroSerie),
                buildSpecification(criteria.getEsMaster(), Inversor_.esMaster),
                buildSpecification(criteria.getTipoConexion(), Inversor_.tipoConexion),
                buildStringSpecification(criteria.getPuertoSerial(), Inversor_.puertoSerial),
                buildRangeSpecification(criteria.getSlaveId(), Inversor_.slaveId),
                buildSpecification(criteria.getActivo(), Inversor_.activo),
                buildSpecification(criteria.getEstadoDispositivo(), Inversor_.estadoDispositivo),
                buildRangeSpecification(criteria.getUltimaLectura(), Inversor_.ultimaLectura),
                buildStringSpecification(criteria.getUltimoError(), Inversor_.ultimoError),
                buildRangeSpecification(criteria.getPeriodoMuestreoSeg(), Inversor_.periodoMuestreoSeg),
                buildRangeSpecification(criteria.getFechaRegistro(), Inversor_.fechaRegistro),
                buildRangeSpecification(criteria.getUltimaConexion(), Inversor_.ultimaConexion),
                buildSpecification(criteria.getSitioId(), root -> root.join(Inversor_.sitio, JoinType.LEFT).get(Sitio_.id)),
                buildSpecification(criteria.getMarcaId(), root -> root.join(Inversor_.marca, JoinType.LEFT).get(Marca_.id)),
                buildSpecification(criteria.getModeloId(), root -> root.join(Inversor_.modelo, JoinType.LEFT).get(ModeloInversor_.id)),
                buildSpecification(criteria.getGatewayId(), root -> root.join(Inversor_.gateway, JoinType.LEFT).get(GatewayTcp_.id)),
                buildSpecification(criteria.getMasterReferenciaId(), root ->
                    root.join(Inversor_.masterReferencia, JoinType.LEFT).get(Inversor_.id)
                )
            );
        }
        return specification;
    }
}
