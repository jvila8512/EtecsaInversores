package com.etecsa.monitor.web.rest;

import com.etecsa.monitor.repository.DatoEficienciaRepository;
import com.etecsa.monitor.service.DatoEficienciaQueryService;
import com.etecsa.monitor.service.DatoEficienciaService;
import com.etecsa.monitor.service.criteria.DatoEficienciaCriteria;
import com.etecsa.monitor.service.dto.DatoEficienciaDTO;
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
 * REST controller for managing {@link com.etecsa.monitor.domain.DatoEficiencia}.
 */
@RestController
@RequestMapping("/api/dato-eficiencias")
public class DatoEficienciaResource {

    private static final Logger LOG = LoggerFactory.getLogger(DatoEficienciaResource.class);

    private static final String ENTITY_NAME = "datoEficiencia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DatoEficienciaService datoEficienciaService;

    private final DatoEficienciaRepository datoEficienciaRepository;

    private final DatoEficienciaQueryService datoEficienciaQueryService;

    public DatoEficienciaResource(
        DatoEficienciaService datoEficienciaService,
        DatoEficienciaRepository datoEficienciaRepository,
        DatoEficienciaQueryService datoEficienciaQueryService
    ) {
        this.datoEficienciaService = datoEficienciaService;
        this.datoEficienciaRepository = datoEficienciaRepository;
        this.datoEficienciaQueryService = datoEficienciaQueryService;
    }

    /**
     * {@code POST  /dato-eficiencias} : Create a new datoEficiencia.
     *
     * @param datoEficienciaDTO the datoEficienciaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new datoEficienciaDTO, or with status {@code 400 (Bad Request)} if the datoEficiencia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DatoEficienciaDTO> createDatoEficiencia(@Valid @RequestBody DatoEficienciaDTO datoEficienciaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DatoEficiencia : {}", datoEficienciaDTO);
        if (datoEficienciaDTO.getId() != null) {
            throw new BadRequestAlertException("A new datoEficiencia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        datoEficienciaDTO = datoEficienciaService.save(datoEficienciaDTO);
        return ResponseEntity.created(new URI("/api/dato-eficiencias/" + datoEficienciaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, datoEficienciaDTO.getId().toString()))
            .body(datoEficienciaDTO);
    }

    /**
     * {@code PUT  /dato-eficiencias/:id} : Updates an existing datoEficiencia.
     *
     * @param id the id of the datoEficienciaDTO to save.
     * @param datoEficienciaDTO the datoEficienciaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated datoEficienciaDTO,
     * or with status {@code 400 (Bad Request)} if the datoEficienciaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the datoEficienciaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DatoEficienciaDTO> updateDatoEficiencia(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DatoEficienciaDTO datoEficienciaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DatoEficiencia : {}, {}", id, datoEficienciaDTO);
        if (datoEficienciaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, datoEficienciaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!datoEficienciaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        datoEficienciaDTO = datoEficienciaService.update(datoEficienciaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, datoEficienciaDTO.getId().toString()))
            .body(datoEficienciaDTO);
    }

    /**
     * {@code PATCH  /dato-eficiencias/:id} : Partial updates given fields of an existing datoEficiencia, field will ignore if it is null
     *
     * @param id the id of the datoEficienciaDTO to save.
     * @param datoEficienciaDTO the datoEficienciaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated datoEficienciaDTO,
     * or with status {@code 400 (Bad Request)} if the datoEficienciaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the datoEficienciaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the datoEficienciaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DatoEficienciaDTO> partialUpdateDatoEficiencia(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DatoEficienciaDTO datoEficienciaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DatoEficiencia partially : {}, {}", id, datoEficienciaDTO);
        if (datoEficienciaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, datoEficienciaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!datoEficienciaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DatoEficienciaDTO> result = datoEficienciaService.partialUpdate(datoEficienciaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, datoEficienciaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /dato-eficiencias} : get all the datoEficiencias.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of datoEficiencias in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DatoEficienciaDTO>> getAllDatoEficiencias(
        DatoEficienciaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DatoEficiencias by criteria: {}", criteria);

        Page<DatoEficienciaDTO> page = datoEficienciaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dato-eficiencias/count} : count all the datoEficiencias.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDatoEficiencias(DatoEficienciaCriteria criteria) {
        LOG.debug("REST request to count DatoEficiencias by criteria: {}", criteria);
        return ResponseEntity.ok().body(datoEficienciaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /dato-eficiencias/:id} : get the "id" datoEficiencia.
     *
     * @param id the id of the datoEficienciaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the datoEficienciaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DatoEficienciaDTO> getDatoEficiencia(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DatoEficiencia : {}", id);
        Optional<DatoEficienciaDTO> datoEficienciaDTO = datoEficienciaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(datoEficienciaDTO);
    }

    /**
     * {@code DELETE  /dato-eficiencias/:id} : delete the "id" datoEficiencia.
     *
     * @param id the id of the datoEficienciaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDatoEficiencia(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DatoEficiencia : {}", id);
        datoEficienciaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
