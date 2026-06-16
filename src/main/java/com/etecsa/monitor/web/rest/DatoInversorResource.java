package com.etecsa.monitor.web.rest;

import com.etecsa.monitor.repository.DatoInversorRepository;
import com.etecsa.monitor.service.DatoInversorQueryService;
import com.etecsa.monitor.service.DatoInversorService;
import com.etecsa.monitor.service.criteria.DatoInversorCriteria;
import com.etecsa.monitor.service.dto.DatoInversorDTO;
import com.etecsa.monitor.web.rest.errors.BadRequestAlertException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.etecsa.monitor.domain.DatoInversor}.
 */
@RestController
@RequestMapping("/api/dato-inversors")
public class DatoInversorResource {

    private static final Logger LOG = LoggerFactory.getLogger(DatoInversorResource.class);

    private static final String ENTITY_NAME = "datoInversor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DatoInversorService datoInversorService;

    private final DatoInversorRepository datoInversorRepository;

    private final DatoInversorQueryService datoInversorQueryService;

    public DatoInversorResource(
        DatoInversorService datoInversorService,
        DatoInversorRepository datoInversorRepository,
        DatoInversorQueryService datoInversorQueryService
    ) {
        this.datoInversorService = datoInversorService;
        this.datoInversorRepository = datoInversorRepository;
        this.datoInversorQueryService = datoInversorQueryService;
    }

    /**
     * {@code POST  /dato-inversors} : Create a new datoInversor.
     *
     * @param datoInversorDTO the datoInversorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new datoInversorDTO, or with status {@code 400 (Bad Request)} if the datoInversor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DatoInversorDTO> createDatoInversor(@Valid @RequestBody DatoInversorDTO datoInversorDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DatoInversor : {}", datoInversorDTO);
        if (datoInversorDTO.getId() != null) {
            throw new BadRequestAlertException("A new datoInversor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        datoInversorDTO = datoInversorService.save(datoInversorDTO);
        return ResponseEntity.created(new URI("/api/dato-inversors/" + datoInversorDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, datoInversorDTO.getId().toString()))
            .body(datoInversorDTO);
    }

    /**
     * {@code PUT  /dato-inversors/:id} : Updates an existing datoInversor.
     *
     * @param id the id of the datoInversorDTO to save.
     * @param datoInversorDTO the datoInversorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated datoInversorDTO,
     * or with status {@code 400 (Bad Request)} if the datoInversorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the datoInversorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DatoInversorDTO> updateDatoInversor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DatoInversorDTO datoInversorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DatoInversor : {}, {}", id, datoInversorDTO);
        if (datoInversorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, datoInversorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!datoInversorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        datoInversorDTO = datoInversorService.update(datoInversorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, datoInversorDTO.getId().toString()))
            .body(datoInversorDTO);
    }

    /**
     * {@code PATCH  /dato-inversors/:id} : Partial updates given fields of an existing datoInversor, field will ignore if it is null
     *
     * @param id the id of the datoInversorDTO to save.
     * @param datoInversorDTO the datoInversorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated datoInversorDTO,
     * or with status {@code 400 (Bad Request)} if the datoInversorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the datoInversorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the datoInversorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DatoInversorDTO> partialUpdateDatoInversor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DatoInversorDTO datoInversorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DatoInversor partially : {}, {}", id, datoInversorDTO);
        if (datoInversorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, datoInversorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!datoInversorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DatoInversorDTO> result = datoInversorService.partialUpdate(datoInversorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, datoInversorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /dato-inversors} : get all the datoInversors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of datoInversors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DatoInversorDTO>> getAllDatoInversors(
        DatoInversorCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DatoInversors by criteria: {}", criteria);

        Page<DatoInversorDTO> page = datoInversorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

  /**
   * {@code GET /dato-inversors/latest-readings} : get the latest reading for each inversor.
   *
   * This is the primary endpoint for the Solar Monitor dashboard.
   * Returns exactly one row per inversor — the most recent reading —
   * using an efficient INNER JOIN on MAX(tiempo).
   *
   * With 300 inversores this returns 300 rows instead of
   * having to paginate through millions of historical rows.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of latest readings.
   */
  @GetMapping("/latest-readings")
  public ResponseEntity<List<DatoInversorDTO>> getLatestReadings() {
    LOG.debug("REST request to get latest readings for all inversores");
    List<DatoInversorDTO> readings = datoInversorService.findLatestReadings();
    return ResponseEntity.ok().body(readings);
  }

  /**
   * {@code GET /dato-inversors/trend-24h} : get hourly aggregated trend for the dashboard.
   *
   * Returns an array of 24 objects, one per hour, with average PV power.
   * Hours without data will have null values — the frontend fills them as zero.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the trend array in body.
   */
  @GetMapping("/trend-24h")
  public ResponseEntity<List<Object[]>> getHourlyTrend() {
    LOG.debug("REST request to get hourly trend for last 24h");
    List<Object[]> trend = datoInversorService.findHourlyTrend24h();
    return ResponseEntity.ok().body(trend);
  }

  /**
   * {@code GET /dato-inversors/export-csv} : export filtered data as CSV.
   *
   * Streams a CSV file with all DatoInversor fields filtered by
   * inversor ID and/or date range. Useful for downloading historical
   * data for external analysis or compliance.
   *
   * @param inversorId optional — filter by inversor
   * @param from optional — start date (YYYY-MM-DD), inclusive
   * @param to optional — end date (YYYY-MM-DD), inclusive
   * @param response the servlet response to stream the CSV into
   */
  @GetMapping("/export-csv")
  public void exportCsv(
      @RequestParam(required = false) Long inversorId,
      @RequestParam(required = false) String from,
      @RequestParam(required = false) String to,
      HttpServletResponse response
  ) throws IOException {
    LOG.debug("REST request to export CSV: inversorId={}, from={}, to={}", inversorId, from, to);

    // Build criteria
    DatoInversorCriteria criteria = new DatoInversorCriteria();
    if (from != null || to != null) {
      var tiempoFilter = criteria.tiempo();
      ZoneId zone = ZoneId.of("America/Havana");
      if (from != null) {
        Instant fromInstant = LocalDate.parse(from, DateTimeFormatter.ISO_LOCAL_DATE)
            .atStartOfDay(zone).toInstant();
        tiempoFilter.setGreaterThanOrEqual(fromInstant);
      }
      if (to != null) {
        Instant toInstant = LocalDate.parse(to, DateTimeFormatter.ISO_LOCAL_DATE)
            .plusDays(1).atStartOfDay(zone).toInstant();
        tiempoFilter.setLessThan(toInstant);
      }
    }
    if (inversorId != null) {
      var inversorFilter = criteria.inversorId();
      inversorFilter.setEquals(inversorId);
    }

    // Fetch all matching rows (paginated 10k at a time to avoid OOM)
    response.setContentType("text/csv; charset=UTF-8");
    response.setHeader("Content-Disposition", "attachment; filename=\"datos-inversor.csv\"");

    try (PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8))) {
      // CSV header
      out.println("id,tiempo,inversor_id,bateria_voltaje_v,bateria_corriente_a,bateria_potencia_w,"
          + "pv_voltaje_v,pv_potencia_w,pv_corriente_a,"
          + "salida_voltaje_v,salida_potencia_w,salida_corriente_a,salida_frecuencia_hz,"
          + "carga_porcentaje,soc_pct,"
          + "red_voltaje_v,red_frecuencia_hz,"
          + "temperatura_c,bus_voltaje_v,"
          + "eficiencia_pct,consumo_red_w,"
          + "modo_operacion,codigo_fallo,"
          + "bateria_cargando,pv_activo,red_activa,crc_valido,latencia_ms");

      int page = 0;
      List<DatoInversorDTO> batch;
      do {
        Pageable pageable = PageRequest.of(page, 10_000, Sort.by("tiempo").ascending());
        batch = datoInversorQueryService.findByCriteria(criteria, pageable).getContent();
        for (DatoInversorDTO d : batch) {
          out.println(csvRow(d));
        }
        page++;
      } while (!batch.isEmpty());
    }
  }

  private String csvRow(DatoInversorDTO d) {
    return String.join(",",
        safe(d.getId()),
        safe(d.getTiempo() != null ? d.getTiempo().toString() : null),
        safe(d.getInversor() != null ? d.getInversor().getId() : null),
        safe(d.getBateriaVoltajeV()),
        safe(d.getBateriaCorrienteA()),
        safe(d.getBateriaPotenciaW()),
        safe(d.getPvVoltajeV()),
        safe(d.getPvPotenciaW()),
        safe(d.getPvCorrienteA()),
        safe(d.getSalidaVoltajeV()),
        safe(d.getSalidaPotenciaW()),
        safe(d.getSalidaCorrienteA()),
        safe(d.getSalidaFrecuenciaHz()),
        safe(d.getCargaPorcentaje()),
        safe(d.getSocPct()),
        safe(d.getRedVoltajeV()),
        safe(d.getRedFrecuenciaHz()),
        safe(d.getTemperaturaC()),
        safe(d.getBusVoltajeV()),
        safe(d.getEficienciaPct()),
        safe(d.getConsumoRedW()),
        safe(d.getModoOperacion()),
        safe(d.getCodigoFallo()),
        safe(d.getBateriaCargando()),
        safe(d.getPvActivo()),
        safe(d.getRedActiva()),
        safe(d.getCrcValido()),
        safe(d.getLatenciaMs())
    );
  }

  private String safe(Object val) {
    if (val == null) return "";
    String s = val.toString();
    // Escape CSV: wrap in quotes if contains comma, quote, or newline
    if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
      return "\"" + s.replace("\"", "\"\"") + "\"";
    }
    return s;
  }

  /**
   * {@code GET /dato-inversors/count} : count all the datoInversors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDatoInversors(DatoInversorCriteria criteria) {
        LOG.debug("REST request to count DatoInversors by criteria: {}", criteria);
        return ResponseEntity.ok().body(datoInversorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /dato-inversors/:id} : get the "id" datoInversor.
     *
     * @param id the id of the datoInversorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the datoInversorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DatoInversorDTO> getDatoInversor(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DatoInversor : {}", id);
        Optional<DatoInversorDTO> datoInversorDTO = datoInversorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(datoInversorDTO);
    }

    /**
     * {@code DELETE  /dato-inversors/:id} : delete the "id" datoInversor.
     *
     * @param id the id of the datoInversorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDatoInversor(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DatoInversor : {}", id);
        datoInversorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
