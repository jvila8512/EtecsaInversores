package com.etecsa.monitor.web.rest;

import com.etecsa.monitor.repository.GatewayTcpRepository;
import com.etecsa.monitor.service.GatewayTcpQueryService;
import com.etecsa.monitor.service.GatewayTcpService;
import com.etecsa.monitor.service.criteria.GatewayTcpCriteria;
import com.etecsa.monitor.service.dto.GatewayTcpDTO;
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
 * REST controller for managing {@link com.etecsa.monitor.domain.GatewayTcp}.
 */
@RestController
@RequestMapping("/api/gateway-tcps")
public class GatewayTcpResource {

    private static final Logger LOG = LoggerFactory.getLogger(GatewayTcpResource.class);

    private static final String ENTITY_NAME = "gatewayTcp";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GatewayTcpService gatewayTcpService;

    private final GatewayTcpRepository gatewayTcpRepository;

    private final GatewayTcpQueryService gatewayTcpQueryService;

    public GatewayTcpResource(
        GatewayTcpService gatewayTcpService,
        GatewayTcpRepository gatewayTcpRepository,
        GatewayTcpQueryService gatewayTcpQueryService
    ) {
        this.gatewayTcpService = gatewayTcpService;
        this.gatewayTcpRepository = gatewayTcpRepository;
        this.gatewayTcpQueryService = gatewayTcpQueryService;
    }

    /**
     * {@code POST  /gateway-tcps} : Create a new gatewayTcp.
     *
     * @param gatewayTcpDTO the gatewayTcpDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gatewayTcpDTO, or with status {@code 400 (Bad Request)} if the gatewayTcp has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<GatewayTcpDTO> createGatewayTcp(@Valid @RequestBody GatewayTcpDTO gatewayTcpDTO) throws URISyntaxException {
        LOG.debug("REST request to save GatewayTcp : {}", gatewayTcpDTO);
        if (gatewayTcpDTO.getId() != null) {
            throw new BadRequestAlertException("A new gatewayTcp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        gatewayTcpDTO = gatewayTcpService.save(gatewayTcpDTO);
        return ResponseEntity.created(new URI("/api/gateway-tcps/" + gatewayTcpDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, gatewayTcpDTO.getId().toString()))
            .body(gatewayTcpDTO);
    }

    /**
     * {@code PUT  /gateway-tcps/:id} : Updates an existing gatewayTcp.
     *
     * @param id the id of the gatewayTcpDTO to save.
     * @param gatewayTcpDTO the gatewayTcpDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gatewayTcpDTO,
     * or with status {@code 400 (Bad Request)} if the gatewayTcpDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gatewayTcpDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GatewayTcpDTO> updateGatewayTcp(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GatewayTcpDTO gatewayTcpDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update GatewayTcp : {}, {}", id, gatewayTcpDTO);
        if (gatewayTcpDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gatewayTcpDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gatewayTcpRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        gatewayTcpDTO = gatewayTcpService.update(gatewayTcpDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gatewayTcpDTO.getId().toString()))
            .body(gatewayTcpDTO);
    }

    /**
     * {@code PATCH  /gateway-tcps/:id} : Partial updates given fields of an existing gatewayTcp, field will ignore if it is null
     *
     * @param id the id of the gatewayTcpDTO to save.
     * @param gatewayTcpDTO the gatewayTcpDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gatewayTcpDTO,
     * or with status {@code 400 (Bad Request)} if the gatewayTcpDTO is not valid,
     * or with status {@code 404 (Not Found)} if the gatewayTcpDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the gatewayTcpDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GatewayTcpDTO> partialUpdateGatewayTcp(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GatewayTcpDTO gatewayTcpDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update GatewayTcp partially : {}, {}", id, gatewayTcpDTO);
        if (gatewayTcpDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gatewayTcpDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gatewayTcpRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GatewayTcpDTO> result = gatewayTcpService.partialUpdate(gatewayTcpDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gatewayTcpDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /gateway-tcps} : get all the gatewayTcps.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gatewayTcps in body.
     */
    @GetMapping("")
    public ResponseEntity<List<GatewayTcpDTO>> getAllGatewayTcps(
        GatewayTcpCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get GatewayTcps by criteria: {}", criteria);

        Page<GatewayTcpDTO> page = gatewayTcpQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /gateway-tcps/count} : count all the gatewayTcps.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countGatewayTcps(GatewayTcpCriteria criteria) {
        LOG.debug("REST request to count GatewayTcps by criteria: {}", criteria);
        return ResponseEntity.ok().body(gatewayTcpQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /gateway-tcps/:id} : get the "id" gatewayTcp.
     *
     * @param id the id of the gatewayTcpDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gatewayTcpDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GatewayTcpDTO> getGatewayTcp(@PathVariable("id") Long id) {
        LOG.debug("REST request to get GatewayTcp : {}", id);
        Optional<GatewayTcpDTO> gatewayTcpDTO = gatewayTcpService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gatewayTcpDTO);
    }

    /**
     * {@code DELETE  /gateway-tcps/:id} : delete the "id" gatewayTcp.
     *
     * @param id the id of the gatewayTcpDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGatewayTcp(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete GatewayTcp : {}", id);
        gatewayTcpService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
