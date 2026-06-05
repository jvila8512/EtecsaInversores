package com.etecsa.monitor.web.rest;

import com.etecsa.monitor.repository.MapeoRegistroRepository;
import com.etecsa.monitor.service.MapeoRegistroQueryService;
import com.etecsa.monitor.service.MapeoRegistroService;
import com.etecsa.monitor.service.criteria.MapeoRegistroCriteria;
import com.etecsa.monitor.service.dto.MapeoRegistroDTO;
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
 * REST controller for managing {@link com.etecsa.monitor.domain.MapeoRegistro}.
 */
@RestController
@RequestMapping("/api/mapeo-registros")
public class MapeoRegistroResource {

    private static final Logger LOG = LoggerFactory.getLogger(MapeoRegistroResource.class);

    private static final String ENTITY_NAME = "mapeoRegistro";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MapeoRegistroService mapeoRegistroService;

    private final MapeoRegistroRepository mapeoRegistroRepository;

    private final MapeoRegistroQueryService mapeoRegistroQueryService;

    public MapeoRegistroResource(
        MapeoRegistroService mapeoRegistroService,
        MapeoRegistroRepository mapeoRegistroRepository,
        MapeoRegistroQueryService mapeoRegistroQueryService
    ) {
        this.mapeoRegistroService = mapeoRegistroService;
        this.mapeoRegistroRepository = mapeoRegistroRepository;
        this.mapeoRegistroQueryService = mapeoRegistroQueryService;
    }

    /**
     * {@code POST  /mapeo-registros} : Create a new mapeoRegistro.
     *
     * @param mapeoRegistroDTO the mapeoRegistroDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mapeoRegistroDTO, or with status {@code 400 (Bad Request)} if the mapeoRegistro has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MapeoRegistroDTO> createMapeoRegistro(@Valid @RequestBody MapeoRegistroDTO mapeoRegistroDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save MapeoRegistro : {}", mapeoRegistroDTO);
        if (mapeoRegistroDTO.getId() != null) {
            throw new BadRequestAlertException("A new mapeoRegistro cannot already have an ID", ENTITY_NAME, "idexists");
        }
        mapeoRegistroDTO = mapeoRegistroService.save(mapeoRegistroDTO);
        return ResponseEntity.created(new URI("/api/mapeo-registros/" + mapeoRegistroDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, mapeoRegistroDTO.getId().toString()))
            .body(mapeoRegistroDTO);
    }

    /**
     * {@code PUT  /mapeo-registros/:id} : Updates an existing mapeoRegistro.
     *
     * @param id the id of the mapeoRegistroDTO to save.
     * @param mapeoRegistroDTO the mapeoRegistroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mapeoRegistroDTO,
     * or with status {@code 400 (Bad Request)} if the mapeoRegistroDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mapeoRegistroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MapeoRegistroDTO> updateMapeoRegistro(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MapeoRegistroDTO mapeoRegistroDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MapeoRegistro : {}, {}", id, mapeoRegistroDTO);
        if (mapeoRegistroDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mapeoRegistroDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mapeoRegistroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        mapeoRegistroDTO = mapeoRegistroService.update(mapeoRegistroDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mapeoRegistroDTO.getId().toString()))
            .body(mapeoRegistroDTO);
    }

    /**
     * {@code PATCH  /mapeo-registros/:id} : Partial updates given fields of an existing mapeoRegistro, field will ignore if it is null
     *
     * @param id the id of the mapeoRegistroDTO to save.
     * @param mapeoRegistroDTO the mapeoRegistroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mapeoRegistroDTO,
     * or with status {@code 400 (Bad Request)} if the mapeoRegistroDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mapeoRegistroDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mapeoRegistroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MapeoRegistroDTO> partialUpdateMapeoRegistro(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MapeoRegistroDTO mapeoRegistroDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MapeoRegistro partially : {}, {}", id, mapeoRegistroDTO);
        if (mapeoRegistroDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mapeoRegistroDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mapeoRegistroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MapeoRegistroDTO> result = mapeoRegistroService.partialUpdate(mapeoRegistroDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mapeoRegistroDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /mapeo-registros} : get all the mapeoRegistros.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mapeoRegistros in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MapeoRegistroDTO>> getAllMapeoRegistros(
        MapeoRegistroCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MapeoRegistros by criteria: {}", criteria);

        Page<MapeoRegistroDTO> page = mapeoRegistroQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mapeo-registros/count} : count all the mapeoRegistros.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMapeoRegistros(MapeoRegistroCriteria criteria) {
        LOG.debug("REST request to count MapeoRegistros by criteria: {}", criteria);
        return ResponseEntity.ok().body(mapeoRegistroQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /mapeo-registros/:id} : get the "id" mapeoRegistro.
     *
     * @param id the id of the mapeoRegistroDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mapeoRegistroDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MapeoRegistroDTO> getMapeoRegistro(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MapeoRegistro : {}", id);
        Optional<MapeoRegistroDTO> mapeoRegistroDTO = mapeoRegistroService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mapeoRegistroDTO);
    }

    /**
     * {@code DELETE  /mapeo-registros/:id} : delete the "id" mapeoRegistro.
     *
     * @param id the id of the mapeoRegistroDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMapeoRegistro(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MapeoRegistro : {}", id);
        mapeoRegistroService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
