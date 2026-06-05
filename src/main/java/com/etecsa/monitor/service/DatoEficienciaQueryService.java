package com.etecsa.monitor.service;

import com.etecsa.monitor.domain.*; // for static metamodels
import com.etecsa.monitor.domain.DatoEficiencia;
import com.etecsa.monitor.repository.DatoEficienciaRepository;
import com.etecsa.monitor.service.criteria.DatoEficienciaCriteria;
import com.etecsa.monitor.service.dto.DatoEficienciaDTO;
import com.etecsa.monitor.service.mapper.DatoEficienciaMapper;
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
 * Service for executing complex queries for {@link DatoEficiencia} entities in the database.
 * The main input is a {@link DatoEficienciaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DatoEficienciaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DatoEficienciaQueryService extends QueryService<DatoEficiencia> {

    private static final Logger LOG = LoggerFactory.getLogger(DatoEficienciaQueryService.class);

    private final DatoEficienciaRepository datoEficienciaRepository;

    private final DatoEficienciaMapper datoEficienciaMapper;

    public DatoEficienciaQueryService(DatoEficienciaRepository datoEficienciaRepository, DatoEficienciaMapper datoEficienciaMapper) {
        this.datoEficienciaRepository = datoEficienciaRepository;
        this.datoEficienciaMapper = datoEficienciaMapper;
    }

    /**
     * Return a {@link Page} of {@link DatoEficienciaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DatoEficienciaDTO> findByCriteria(DatoEficienciaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DatoEficiencia> specification = createSpecification(criteria);
        return datoEficienciaRepository.findAll(specification, page).map(datoEficienciaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DatoEficienciaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DatoEficiencia> specification = createSpecification(criteria);
        return datoEficienciaRepository.count(specification);
    }

    /**
     * Function to convert {@link DatoEficienciaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DatoEficiencia> createSpecification(DatoEficienciaCriteria criteria) {
        Specification<DatoEficiencia> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), DatoEficiencia_.id),
                buildRangeSpecification(criteria.getFecha(), DatoEficiencia_.fecha),
                buildRangeSpecification(criteria.getEnergiaPvWh(), DatoEficiencia_.energiaPvWh),
                buildRangeSpecification(criteria.getEnergiaCargaWh(), DatoEficiencia_.energiaCargaWh),
                buildRangeSpecification(criteria.getEnergiaRedWh(), DatoEficiencia_.energiaRedWh),
                buildRangeSpecification(criteria.getEficienciaPromPct(), DatoEficiencia_.eficienciaPromPct),
                buildRangeSpecification(criteria.getHorasSol(), DatoEficiencia_.horasSol),
                buildRangeSpecification(criteria.getPicoPvW(), DatoEficiencia_.picoPvW),
                buildRangeSpecification(criteria.getPicoCargaW(), DatoEficiencia_.picoCargaW),
                buildRangeSpecification(criteria.getMaxBateriaV(), DatoEficiencia_.maxBateriaV),
                buildRangeSpecification(criteria.getMinBateriaV(), DatoEficiencia_.minBateriaV),
                buildRangeSpecification(criteria.getTotalLecturas(), DatoEficiencia_.totalLecturas),
                buildRangeSpecification(criteria.getCalculadoEn(), DatoEficiencia_.calculadoEn),
                buildSpecification(criteria.getInversorId(), root -> root.join(DatoEficiencia_.inversor, JoinType.LEFT).get(Inversor_.id))
            );
        }
        return specification;
    }
}
