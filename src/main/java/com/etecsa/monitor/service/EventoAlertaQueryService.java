package com.etecsa.monitor.service;

import com.etecsa.monitor.domain.*; // for static metamodels
import com.etecsa.monitor.domain.EventoAlerta;
import com.etecsa.monitor.repository.EventoAlertaRepository;
import com.etecsa.monitor.service.criteria.EventoAlertaCriteria;
import com.etecsa.monitor.service.dto.EventoAlertaDTO;
import com.etecsa.monitor.service.mapper.EventoAlertaMapper;
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
 * Service for executing complex queries for {@link EventoAlerta} entities in the database.
 * The main input is a {@link EventoAlertaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EventoAlertaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventoAlertaQueryService extends QueryService<EventoAlerta> {

    private static final Logger LOG = LoggerFactory.getLogger(EventoAlertaQueryService.class);

    private final EventoAlertaRepository eventoAlertaRepository;

    private final EventoAlertaMapper eventoAlertaMapper;

    public EventoAlertaQueryService(EventoAlertaRepository eventoAlertaRepository, EventoAlertaMapper eventoAlertaMapper) {
        this.eventoAlertaRepository = eventoAlertaRepository;
        this.eventoAlertaMapper = eventoAlertaMapper;
    }

    /**
     * Return a {@link Page} of {@link EventoAlertaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventoAlertaDTO> findByCriteria(EventoAlertaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventoAlerta> specification = createSpecification(criteria);
        return eventoAlertaRepository.findAll(specification, page).map(eventoAlertaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventoAlertaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EventoAlerta> specification = createSpecification(criteria);
        return eventoAlertaRepository.count(specification);
    }

    /**
     * Function to convert {@link EventoAlertaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventoAlerta> createSpecification(EventoAlertaCriteria criteria) {
        Specification<EventoAlerta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), EventoAlerta_.id),
                buildRangeSpecification(criteria.getTimestamp(), EventoAlerta_.timestamp),
                buildStringSpecification(criteria.getNombreAlerta(), EventoAlerta_.nombreAlerta),
                buildStringSpecification(criteria.getMensaje(), EventoAlerta_.mensaje),
                buildStringSpecification(criteria.getCampoMonitorizado(), EventoAlerta_.campoMonitorizado),
                buildRangeSpecification(criteria.getValorDetectado(), EventoAlerta_.valorDetectado),
                buildRangeSpecification(criteria.getUmbral(), EventoAlerta_.umbral),
                buildStringSpecification(criteria.getOperador(), EventoAlerta_.operador),
                buildStringSpecification(criteria.getNivelSeveridad(), EventoAlerta_.nivelSeveridad),
                buildSpecification(criteria.getNotificacionEnviada(), EventoAlerta_.notificacionEnviada),
                buildStringSpecification(criteria.getCanalUsado(), EventoAlerta_.canalUsado),
                buildSpecification(criteria.getResuelto(), EventoAlerta_.resuelto),
                buildRangeSpecification(criteria.getFechaResolucion(), EventoAlerta_.fechaResolucion),
                buildStringSpecification(criteria.getNotas(), EventoAlerta_.notas),
                buildSpecification(criteria.getInversorId(), root -> root.join(EventoAlerta_.inversor, JoinType.LEFT).get(Inversor_.id)),
                buildSpecification(criteria.getAlertaConfigId(), root ->
                    root.join(EventoAlerta_.alertaConfig, JoinType.LEFT).get(AlertaConfig_.id)
                )
            );
        }
        return specification;
    }
}
