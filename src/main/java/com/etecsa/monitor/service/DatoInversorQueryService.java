package com.etecsa.monitor.service;

import com.etecsa.monitor.domain.*; // for static metamodels
import com.etecsa.monitor.domain.DatoInversor;
import com.etecsa.monitor.repository.DatoInversorRepository;
import com.etecsa.monitor.service.criteria.DatoInversorCriteria;
import com.etecsa.monitor.service.dto.DatoInversorDTO;
import com.etecsa.monitor.service.mapper.DatoInversorMapper;
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
 * Service for executing complex queries for {@link DatoInversor} entities in the database.
 * The main input is a {@link DatoInversorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DatoInversorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DatoInversorQueryService extends QueryService<DatoInversor> {

    private static final Logger LOG = LoggerFactory.getLogger(DatoInversorQueryService.class);

    private final DatoInversorRepository datoInversorRepository;

    private final DatoInversorMapper datoInversorMapper;

    public DatoInversorQueryService(DatoInversorRepository datoInversorRepository, DatoInversorMapper datoInversorMapper) {
        this.datoInversorRepository = datoInversorRepository;
        this.datoInversorMapper = datoInversorMapper;
    }

    /**
     * Return a {@link Page} of {@link DatoInversorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DatoInversorDTO> findByCriteria(DatoInversorCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DatoInversor> specification = createSpecification(criteria);
        return datoInversorRepository.findAll(specification, page).map(datoInversorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DatoInversorCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DatoInversor> specification = createSpecification(criteria);
        return datoInversorRepository.count(specification);
    }

    /**
     * Function to convert {@link DatoInversorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DatoInversor> createSpecification(DatoInversorCriteria criteria) {
        Specification<DatoInversor> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), DatoInversor_.id),
                buildRangeSpecification(criteria.getTiempo(), DatoInversor_.tiempo),
                buildRangeSpecification(criteria.getBateriaVoltajeV(), DatoInversor_.bateriaVoltajeV),
                buildRangeSpecification(criteria.getBateriaCorrienteA(), DatoInversor_.bateriaCorrienteA),
                buildRangeSpecification(criteria.getBateriaPotenciaW(), DatoInversor_.bateriaPotenciaW),
                buildRangeSpecification(criteria.getPvVoltajeV(), DatoInversor_.pvVoltajeV),
                buildRangeSpecification(criteria.getPvPotenciaW(), DatoInversor_.pvPotenciaW),
                buildRangeSpecification(criteria.getSalidaVoltajeV(), DatoInversor_.salidaVoltajeV),
                buildRangeSpecification(criteria.getSalidaPotenciaW(), DatoInversor_.salidaPotenciaW),
                buildRangeSpecification(criteria.getSalidaAparenteVa(), DatoInversor_.salidaAparenteVa),
                buildRangeSpecification(criteria.getCargaPorcentaje(), DatoInversor_.cargaPorcentaje),
                buildRangeSpecification(criteria.getRedVoltajeV(), DatoInversor_.redVoltajeV),
                buildRangeSpecification(criteria.getRedFrecuenciaHz(), DatoInversor_.redFrecuenciaHz),
                buildStringSpecification(criteria.getModoOperacion(), DatoInversor_.modoOperacion),
                buildRangeSpecification(criteria.getCodigoFallo(), DatoInversor_.codigoFallo),
                buildRangeSpecification(criteria.getEficienciaPct(), DatoInversor_.eficienciaPct),
                buildRangeSpecification(criteria.getConsumoRedW(), DatoInversor_.consumoRedW),
                buildSpecification(criteria.getBateriaCargando(), DatoInversor_.bateriaCargando),
                buildSpecification(criteria.getPvActivo(), DatoInversor_.pvActivo),
                buildSpecification(criteria.getRedActiva(), DatoInversor_.redActiva),
                buildSpecification(criteria.getCrcValido(), DatoInversor_.crcValido),
                buildRangeSpecification(criteria.getLatenciaMs(), DatoInversor_.latenciaMs),
                buildSpecification(criteria.getInversorId(), root -> root.join(DatoInversor_.inversor, JoinType.LEFT).get(Inversor_.id))
            );
        }
        return specification;
    }
}
