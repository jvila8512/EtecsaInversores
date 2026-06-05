package com.etecsa.monitor.web.rest;

import com.etecsa.monitor.repository.EventoAlertaRepository;
import com.etecsa.monitor.service.EventoAlertaQueryService;
import com.etecsa.monitor.service.EventoAlertaService;
import com.etecsa.monitor.service.criteria.EventoAlertaCriteria;
import com.etecsa.monitor.service.dto.EventoAlertaDTO;
import com.etecsa.monitor.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.etecsa.monitor.domain.EventoAlerta}.
 */
@RestController
@RequestMapping("/api/evento-alertas")
public class EventoAlertaResource {

    private static final Logger LOG = LoggerFactory.getLogger(EventoAlertaResource.class);

    private static final String ENTITY_NAME = "eventoAlerta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventoAlertaService eventoAlertaService;

    private final EventoAlertaRepository eventoAlertaRepository;

    private final EventoAlertaQueryService eventoAlertaQueryService;

    public EventoAlertaResource(
        EventoAlertaService eventoAlertaService,
        EventoAlertaRepository eventoAlertaRepository,
        EventoAlertaQueryService eventoAlertaQueryService
    ) {
        this.eventoAlertaService = eventoAlertaService;
        this.eventoAlertaRepository = eventoAlertaRepository;
        this.eventoAlertaQueryService = eventoAlertaQueryService;
    }

    /**
     * {@code POST  /evento-alertas} : Create a new eventoAlerta.
     *
     * @param eventoAlertaDTO the eventoAlertaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventoAlertaDTO, or with status {@code 400 (Bad Request)} if the eventoAlerta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EventoAlertaDTO> createEventoAlerta(@Valid @RequestBody EventoAlertaDTO eventoAlertaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save EventoAlerta : {}", eventoAlertaDTO);
        if (eventoAlertaDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventoAlerta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        eventoAlertaDTO = eventoAlertaService.save(eventoAlertaDTO);
        return ResponseEntity.created(new URI("/api/evento-alertas/" + eventoAlertaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, eventoAlertaDTO.getId().toString()))
            .body(eventoAlertaDTO);
    }

    /**
     * {@code PUT  /evento-alertas/:id} : Updates an existing eventoAlerta.
     *
     * @param id the id of the eventoAlertaDTO to save.
     * @param eventoAlertaDTO the eventoAlertaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventoAlertaDTO,
     * or with status {@code 400 (Bad Request)} if the eventoAlertaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventoAlertaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventoAlertaDTO> updateEventoAlerta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventoAlertaDTO eventoAlertaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EventoAlerta : {}, {}", id, eventoAlertaDTO);
        if (eventoAlertaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventoAlertaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventoAlertaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        eventoAlertaDTO = eventoAlertaService.update(eventoAlertaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventoAlertaDTO.getId().toString()))
            .body(eventoAlertaDTO);
    }

    /**
     * {@code PATCH  /evento-alertas/:id} : Partial updates given fields of an existing eventoAlerta, field will ignore if it is null
     *
     * @param id the id of the eventoAlertaDTO to save.
     * @param eventoAlertaDTO the eventoAlertaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventoAlertaDTO,
     * or with status {@code 400 (Bad Request)} if the eventoAlertaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eventoAlertaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventoAlertaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventoAlertaDTO> partialUpdateEventoAlerta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventoAlertaDTO eventoAlertaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EventoAlerta partially : {}, {}", id, eventoAlertaDTO);
        if (eventoAlertaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventoAlertaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventoAlertaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventoAlertaDTO> result = eventoAlertaService.partialUpdate(eventoAlertaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventoAlertaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /evento-alertas} : get all the eventoAlertas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventoAlertas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EventoAlertaDTO>> getAllEventoAlertas(
        EventoAlertaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EventoAlertas by criteria: {}", criteria);

        Page<EventoAlertaDTO> page = eventoAlertaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /evento-alertas/count} : count all the eventoAlertas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEventoAlertas(EventoAlertaCriteria criteria) {
        LOG.debug("REST request to count EventoAlertas by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventoAlertaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /evento-alertas/:id} : get the "id" eventoAlerta.
     *
     * @param id the id of the eventoAlertaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventoAlertaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventoAlertaDTO> getEventoAlerta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EventoAlerta : {}", id);
        Optional<EventoAlertaDTO> eventoAlertaDTO = eventoAlertaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventoAlertaDTO);
    }

    /**
     * {@code DELETE  /evento-alertas/:id} : delete the "id" eventoAlerta.
     *
     * @param id the id of the eventoAlertaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventoAlerta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EventoAlerta : {}", id);
        eventoAlertaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
