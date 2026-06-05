package com.etecsa.monitor.web.rest;

import com.etecsa.monitor.repository.ModeloInversorRepository;
import com.etecsa.monitor.service.ModeloInversorService;
import com.etecsa.monitor.service.dto.ModeloInversorDTO;
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
 * REST controller for managing {@link com.etecsa.monitor.domain.ModeloInversor}.
 */
@RestController
@RequestMapping("/api/modelo-inversors")
public class ModeloInversorResource {

    private static final Logger LOG = LoggerFactory.getLogger(ModeloInversorResource.class);

    private static final String ENTITY_NAME = "modeloInversor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ModeloInversorService modeloInversorService;

    private final ModeloInversorRepository modeloInversorRepository;

    public ModeloInversorResource(ModeloInversorService modeloInversorService, ModeloInversorRepository modeloInversorRepository) {
        this.modeloInversorService = modeloInversorService;
        this.modeloInversorRepository = modeloInversorRepository;
    }

    /**
     * {@code POST  /modelo-inversors} : Create a new modeloInversor.
     *
     * @param modeloInversorDTO the modeloInversorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new modeloInversorDTO, or with status {@code 400 (Bad Request)} if the modeloInversor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ModeloInversorDTO> createModeloInversor(@Valid @RequestBody ModeloInversorDTO modeloInversorDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ModeloInversor : {}", modeloInversorDTO);
        if (modeloInversorDTO.getId() != null) {
            throw new BadRequestAlertException("A new modeloInversor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        modeloInversorDTO = modeloInversorService.save(modeloInversorDTO);
        return ResponseEntity.created(new URI("/api/modelo-inversors/" + modeloInversorDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, modeloInversorDTO.getId().toString()))
            .body(modeloInversorDTO);
    }

    /**
     * {@code PUT  /modelo-inversors/:id} : Updates an existing modeloInversor.
     *
     * @param id the id of the modeloInversorDTO to save.
     * @param modeloInversorDTO the modeloInversorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated modeloInversorDTO,
     * or with status {@code 400 (Bad Request)} if the modeloInversorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the modeloInversorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ModeloInversorDTO> updateModeloInversor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ModeloInversorDTO modeloInversorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ModeloInversor : {}, {}", id, modeloInversorDTO);
        if (modeloInversorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, modeloInversorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!modeloInversorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        modeloInversorDTO = modeloInversorService.update(modeloInversorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, modeloInversorDTO.getId().toString()))
            .body(modeloInversorDTO);
    }

    /**
     * {@code PATCH  /modelo-inversors/:id} : Partial updates given fields of an existing modeloInversor, field will ignore if it is null
     *
     * @param id the id of the modeloInversorDTO to save.
     * @param modeloInversorDTO the modeloInversorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated modeloInversorDTO,
     * or with status {@code 400 (Bad Request)} if the modeloInversorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the modeloInversorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the modeloInversorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ModeloInversorDTO> partialUpdateModeloInversor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ModeloInversorDTO modeloInversorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ModeloInversor partially : {}, {}", id, modeloInversorDTO);
        if (modeloInversorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, modeloInversorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!modeloInversorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ModeloInversorDTO> result = modeloInversorService.partialUpdate(modeloInversorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, modeloInversorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /modelo-inversors} : get all the modeloInversors.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of modeloInversors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ModeloInversorDTO>> getAllModeloInversors(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of ModeloInversors");
        Page<ModeloInversorDTO> page;
        if (eagerload) {
            page = modeloInversorService.findAllWithEagerRelationships(pageable);
        } else {
            page = modeloInversorService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /modelo-inversors/:id} : get the "id" modeloInversor.
     *
     * @param id the id of the modeloInversorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the modeloInversorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ModeloInversorDTO> getModeloInversor(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ModeloInversor : {}", id);
        Optional<ModeloInversorDTO> modeloInversorDTO = modeloInversorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(modeloInversorDTO);
    }

    /**
     * {@code DELETE  /modelo-inversors/:id} : delete the "id" modeloInversor.
     *
     * @param id the id of the modeloInversorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModeloInversor(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ModeloInversor : {}", id);
        modeloInversorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
