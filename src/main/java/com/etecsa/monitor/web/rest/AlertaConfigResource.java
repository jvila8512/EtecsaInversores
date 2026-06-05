package com.etecsa.monitor.web.rest;

import com.etecsa.monitor.repository.AlertaConfigRepository;
import com.etecsa.monitor.service.AlertaConfigService;
import com.etecsa.monitor.service.dto.AlertaConfigDTO;
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
 * REST controller for managing {@link com.etecsa.monitor.domain.AlertaConfig}.
 */
@RestController
@RequestMapping("/api/alerta-configs")
public class AlertaConfigResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlertaConfigResource.class);

    private static final String ENTITY_NAME = "alertaConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlertaConfigService alertaConfigService;

    private final AlertaConfigRepository alertaConfigRepository;

    public AlertaConfigResource(AlertaConfigService alertaConfigService, AlertaConfigRepository alertaConfigRepository) {
        this.alertaConfigService = alertaConfigService;
        this.alertaConfigRepository = alertaConfigRepository;
    }

    /**
     * {@code POST  /alerta-configs} : Create a new alertaConfig.
     *
     * @param alertaConfigDTO the alertaConfigDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alertaConfigDTO, or with status {@code 400 (Bad Request)} if the alertaConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlertaConfigDTO> createAlertaConfig(@Valid @RequestBody AlertaConfigDTO alertaConfigDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AlertaConfig : {}", alertaConfigDTO);
        if (alertaConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new alertaConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alertaConfigDTO = alertaConfigService.save(alertaConfigDTO);
        return ResponseEntity.created(new URI("/api/alerta-configs/" + alertaConfigDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alertaConfigDTO.getId().toString()))
            .body(alertaConfigDTO);
    }

    /**
     * {@code PUT  /alerta-configs/:id} : Updates an existing alertaConfig.
     *
     * @param id the id of the alertaConfigDTO to save.
     * @param alertaConfigDTO the alertaConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alertaConfigDTO,
     * or with status {@code 400 (Bad Request)} if the alertaConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alertaConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlertaConfigDTO> updateAlertaConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlertaConfigDTO alertaConfigDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlertaConfig : {}, {}", id, alertaConfigDTO);
        if (alertaConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alertaConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alertaConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alertaConfigDTO = alertaConfigService.update(alertaConfigDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alertaConfigDTO.getId().toString()))
            .body(alertaConfigDTO);
    }

    /**
     * {@code PATCH  /alerta-configs/:id} : Partial updates given fields of an existing alertaConfig, field will ignore if it is null
     *
     * @param id the id of the alertaConfigDTO to save.
     * @param alertaConfigDTO the alertaConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alertaConfigDTO,
     * or with status {@code 400 (Bad Request)} if the alertaConfigDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alertaConfigDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alertaConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlertaConfigDTO> partialUpdateAlertaConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlertaConfigDTO alertaConfigDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlertaConfig partially : {}, {}", id, alertaConfigDTO);
        if (alertaConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alertaConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alertaConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlertaConfigDTO> result = alertaConfigService.partialUpdate(alertaConfigDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alertaConfigDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /alerta-configs} : get all the alertaConfigs.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alertaConfigs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlertaConfigDTO>> getAllAlertaConfigs(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of AlertaConfigs");
        Page<AlertaConfigDTO> page;
        if (eagerload) {
            page = alertaConfigService.findAllWithEagerRelationships(pageable);
        } else {
            page = alertaConfigService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /alerta-configs/:id} : get the "id" alertaConfig.
     *
     * @param id the id of the alertaConfigDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alertaConfigDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlertaConfigDTO> getAlertaConfig(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlertaConfig : {}", id);
        Optional<AlertaConfigDTO> alertaConfigDTO = alertaConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alertaConfigDTO);
    }

    /**
     * {@code DELETE  /alerta-configs/:id} : delete the "id" alertaConfig.
     *
     * @param id the id of the alertaConfigDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlertaConfig(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlertaConfig : {}", id);
        alertaConfigService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
