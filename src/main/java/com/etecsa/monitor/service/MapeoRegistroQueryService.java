package com.etecsa.monitor.service;

import com.etecsa.monitor.domain.*; // for static metamodels
import com.etecsa.monitor.domain.MapeoRegistro;
import com.etecsa.monitor.repository.MapeoRegistroRepository;
import com.etecsa.monitor.service.criteria.MapeoRegistroCriteria;
import com.etecsa.monitor.service.dto.MapeoRegistroDTO;
import com.etecsa.monitor.service.mapper.MapeoRegistroMapper;
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
 * Service for executing complex queries for {@link MapeoRegistro} entities in the database.
 * The main input is a {@link MapeoRegistroCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MapeoRegistroDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MapeoRegistroQueryService extends QueryService<MapeoRegistro> {

    private static final Logger LOG = LoggerFactory.getLogger(MapeoRegistroQueryService.class);

    private final MapeoRegistroRepository mapeoRegistroRepository;

    private final MapeoRegistroMapper mapeoRegistroMapper;

    public MapeoRegistroQueryService(MapeoRegistroRepository mapeoRegistroRepository, MapeoRegistroMapper mapeoRegistroMapper) {
        this.mapeoRegistroRepository = mapeoRegistroRepository;
        this.mapeoRegistroMapper = mapeoRegistroMapper;
    }

    /**
     * Return a {@link Page} of {@link MapeoRegistroDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MapeoRegistroDTO> findByCriteria(MapeoRegistroCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MapeoRegistro> specification = createSpecification(criteria);
        return mapeoRegistroRepository.findAll(specification, page).map(mapeoRegistroMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MapeoRegistroCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MapeoRegistro> specification = createSpecification(criteria);
        return mapeoRegistroRepository.count(specification);
    }

    /**
     * Function to convert {@link MapeoRegistroCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MapeoRegistro> createSpecification(MapeoRegistroCriteria criteria) {
        Specification<MapeoRegistro> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), MapeoRegistro_.id),
                buildRangeSpecification(criteria.getDireccionRegistro(), MapeoRegistro_.direccionRegistro),
                buildStringSpecification(criteria.getNombreRegistro(), MapeoRegistro_.nombreRegistro),
                buildStringSpecification(criteria.getCampoSql(), MapeoRegistro_.campoSql),
                buildSpecification(criteria.getTipoDato(), MapeoRegistro_.tipoDato),
                buildRangeSpecification(criteria.getEscala(), MapeoRegistro_.escala),
                buildStringSpecification(criteria.getUnidad(), MapeoRegistro_.unidad),
                buildSpecification(criteria.getActivo(), MapeoRegistro_.activo),
                buildStringSpecification(criteria.getDescripcion(), MapeoRegistro_.descripcion),
                buildSpecification(criteria.getMarcaId(), root -> root.join(MapeoRegistro_.marca, JoinType.LEFT).get(Marca_.id))
            );
        }
        return specification;
    }
}
