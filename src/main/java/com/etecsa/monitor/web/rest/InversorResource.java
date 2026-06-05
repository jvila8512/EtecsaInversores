package com.etecsa.monitor.web.rest;

import com.etecsa.monitor.repository.InversorRepository;
import com.etecsa.monitor.service.InversorQueryService;
import com.etecsa.monitor.service.InversorService;
import com.etecsa.monitor.service.criteria.InversorCriteria;
import com.etecsa.monitor.service.dto.InversorDTO;
import com.etecsa.monitor.service.mapper.InversorMapper;
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
 * REST controller for managing {@link com.etecsa.monitor.domain.Inversor}.
 */
@RestController
@RequestMapping("/api/inversors")
public class InversorResource {

    private static final Logger LOG = LoggerFactory.getLogger(InversorResource.class);

    private static final String ENTITY_NAME = "inversor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InversorService inversorService;

    private final InversorRepository inversorRepository;

    private final InversorQueryService inversorQueryService;

    private final InversorMapper inversorMapper;

    public InversorResource(
        InversorService inversorService,
        InversorRepository inversorRepository,
        InversorQueryService inversorQueryService,
        InversorMapper inversorMapper
    ) {
        this.inversorService = inversorService;
        this.inversorRepository = inversorRepository;
        this.inversorQueryService = inversorQueryService;
        this.inversorMapper = inversorMapper;
    }

    /**
     * {@code POST  /inversors} : Create a new inversor.
     *
     * @param inversorDTO the inversorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inversorDTO, or with status {@code 400 (Bad Request)} if the inversor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InversorDTO> createInversor(@Valid @RequestBody InversorDTO inversorDTO) throws URISyntaxException {
        LOG.debug("REST request to save Inversor : {}", inversorDTO);
        if (inversorDTO.getId() != null) {
            throw new BadRequestAlertException("A new inversor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        inversorDTO = inversorService.save(inversorDTO);
        return ResponseEntity.created(new URI("/api/inversors/" + inversorDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, inversorDTO.getId().toString()))
            .body(inversorDTO);
    }

    /**
     * {@code PUT  /inversors/:id} : Updates an existing inversor.
     *
     * @param id the id of the inversorDTO to save.
     * @param inversorDTO the inversorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inversorDTO,
     * or with status {@code 400 (Bad Request)} if the inversorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inversorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InversorDTO> updateInversor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InversorDTO inversorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Inversor : {}, {}", id, inversorDTO);
        if (inversorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inversorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inversorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        inversorDTO = inversorService.update(inversorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inversorDTO.getId().toString()))
            .body(inversorDTO);
    }

    /**
     * {@code PATCH  /inversors/:id} : Partial updates given fields of an existing inversor, field will ignore if it is null
     *
     * @param id the id of the inversorDTO to save.
     * @param inversorDTO the inversorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inversorDTO,
     * or with status {@code 400 (Bad Request)} if the inversorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the inversorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the inversorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InversorDTO> partialUpdateInversor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InversorDTO inversorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Inversor partially : {}, {}", id, inversorDTO);
        if (inversorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inversorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inversorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InversorDTO> result = inversorService.partialUpdate(inversorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inversorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /inversors} : get all the inversors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inversors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InversorDTO>> getAllInversors(
        InversorCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Inversors by criteria: {}", criteria);

        Page<InversorDTO> page = inversorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /inversors/count} : count all the inversors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInversors(InversorCriteria criteria) {
        LOG.debug("REST request to count Inversors by criteria: {}", criteria);
        return ResponseEntity.ok().body(inversorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /inversors/:id} : get the "id" inversor.
     *
     * @param id the id of the inversorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inversorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InversorDTO> getInversor(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Inversor : {}", id);
        Optional<InversorDTO> inversorDTO = inversorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inversorDTO);
    }

    /**
     * {@code DELETE /inversors/:id} : delete the "id" inversor.
     *
     * @param id the id of the inversorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInversor(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Inversor : {}", id);
        inversorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code POST /inversors/:masterId/esclavos} : Registra un nuevo inversor esclavo bajo un master.
     *
     * El esclavo HEREDA del master: marca, modelo, gateway, sitio, tipoConexion, periodoMuestreoSeg.
     * Solo necesita: nombre, slaveId, numeroSerie (opcional).
     *
     * Arquitectura Multi-Master: el master es el inversor con esMaster=true que
     * comparte la misma conexión TCP al gateway. Los esclavos se consultan
     * cambiando el unitId (slaveId) en el pedido Modbus.
     *
     * @param masterId el ID del inversor master
     * @param inversorDTO datos del esclavo (nombre, slaveId obligatorios)
     * @return the {@link ResponseEntity} with status {@code 201 (Created)}
     */
    @PostMapping("/{masterId}/esclavos")
    public ResponseEntity<InversorDTO> registrarEsclavo(
        @PathVariable("masterId") Long masterId,
        @Valid @RequestBody InversorDTO inversorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to register slave under master {} : {}", masterId, inversorDTO);

        // Verificar que el master existe y es master
        var masterOpt = inversorRepository.findOneWithEagerRelationships(masterId);
        if (masterOpt.isEmpty()) {
            throw new BadRequestAlertException("Master inversor not found", ENTITY_NAME, "masternotfound");
        }

        var master = masterOpt.get();
        if (!Boolean.TRUE.equals(master.getEsMaster())) {
            throw new BadRequestAlertException("The referenced inversor is not a master", ENTITY_NAME, "notamaster");
        }

        // Verificar que el slaveId es único dentro del gateway
        if (inversorDTO.getSlaveId() == null) {
            throw new BadRequestAlertException("slaveId is required", ENTITY_NAME, "slaveidrequired");
        }

        // Construir entidad esclavo heredando del master directamente
        // (evitamos mapper porque DTOs parciales con solo ID corrompen entidades relacionadas)
        com.etecsa.monitor.domain.Inversor slave = new com.etecsa.monitor.domain.Inversor();
        slave.setNombre(inversorDTO.getNombre());
        slave.setNumeroSerie(inversorDTO.getNumeroSerie());
        slave.setEsMaster(false);
        slave.setSlaveId(inversorDTO.getSlaveId());
        slave.setTipoConexion(master.getTipoConexion());
        slave.setActivo(true);
        slave.setFechaRegistro(inversorDTO.getFechaRegistro() != null ? inversorDTO.getFechaRegistro() : java.time.Instant.now());
        slave.setPeriodoMuestreoSeg(master.getPeriodoMuestreoSeg());
        // Heredar relaciones del master (referencias existentes, sin crear nuevas entidades)
        slave.setMasterReferencia(master);
        slave.setMarca(master.getMarca());
        slave.setModelo(master.getModelo());
        slave.setGateway(master.getGateway());
        slave.setSitio(master.getSitio());

        com.etecsa.monitor.domain.Inversor saved = inversorRepository.save(slave);
        InversorDTO result = inversorMapper.toDto(saved);

        return ResponseEntity.created(new URI("/api/inversors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
