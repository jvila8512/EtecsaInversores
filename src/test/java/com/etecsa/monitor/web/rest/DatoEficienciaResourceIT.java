package com.etecsa.monitor.web.rest;

import static com.etecsa.monitor.domain.DatoEficienciaAsserts.*;
import static com.etecsa.monitor.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.etecsa.monitor.IntegrationTest;
import com.etecsa.monitor.domain.DatoEficiencia;
import com.etecsa.monitor.domain.Inversor;
import com.etecsa.monitor.repository.DatoEficienciaRepository;
import com.etecsa.monitor.service.DatoEficienciaService;
import com.etecsa.monitor.service.dto.DatoEficienciaDTO;
import com.etecsa.monitor.service.mapper.DatoEficienciaMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DatoEficienciaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DatoEficienciaResourceIT {

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA = LocalDate.ofEpochDay(-1L);

    private static final Double DEFAULT_ENERGIA_PV_WH = 1D;
    private static final Double UPDATED_ENERGIA_PV_WH = 2D;
    private static final Double SMALLER_ENERGIA_PV_WH = 1D - 1D;

    private static final Double DEFAULT_ENERGIA_CARGA_WH = 1D;
    private static final Double UPDATED_ENERGIA_CARGA_WH = 2D;
    private static final Double SMALLER_ENERGIA_CARGA_WH = 1D - 1D;

    private static final Double DEFAULT_ENERGIA_RED_WH = 1D;
    private static final Double UPDATED_ENERGIA_RED_WH = 2D;
    private static final Double SMALLER_ENERGIA_RED_WH = 1D - 1D;

    private static final Double DEFAULT_EFICIENCIA_PROM_PCT = 1D;
    private static final Double UPDATED_EFICIENCIA_PROM_PCT = 2D;
    private static final Double SMALLER_EFICIENCIA_PROM_PCT = 1D - 1D;

    private static final Double DEFAULT_HORAS_SOL = 1D;
    private static final Double UPDATED_HORAS_SOL = 2D;
    private static final Double SMALLER_HORAS_SOL = 1D - 1D;

    private static final Double DEFAULT_PICO_PV_W = 1D;
    private static final Double UPDATED_PICO_PV_W = 2D;
    private static final Double SMALLER_PICO_PV_W = 1D - 1D;

    private static final Double DEFAULT_PICO_CARGA_W = 1D;
    private static final Double UPDATED_PICO_CARGA_W = 2D;
    private static final Double SMALLER_PICO_CARGA_W = 1D - 1D;

    private static final Double DEFAULT_MAX_BATERIA_V = 1D;
    private static final Double UPDATED_MAX_BATERIA_V = 2D;
    private static final Double SMALLER_MAX_BATERIA_V = 1D - 1D;

    private static final Double DEFAULT_MIN_BATERIA_V = 1D;
    private static final Double UPDATED_MIN_BATERIA_V = 2D;
    private static final Double SMALLER_MIN_BATERIA_V = 1D - 1D;

    private static final Integer DEFAULT_TOTAL_LECTURAS = 1;
    private static final Integer UPDATED_TOTAL_LECTURAS = 2;
    private static final Integer SMALLER_TOTAL_LECTURAS = 1 - 1;

    private static final Instant DEFAULT_CALCULADO_EN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CALCULADO_EN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/dato-eficiencias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DatoEficienciaRepository datoEficienciaRepository;

    @Mock
    private DatoEficienciaRepository datoEficienciaRepositoryMock;

    @Autowired
    private DatoEficienciaMapper datoEficienciaMapper;

    @Mock
    private DatoEficienciaService datoEficienciaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDatoEficienciaMockMvc;

    private DatoEficiencia datoEficiencia;

    private DatoEficiencia insertedDatoEficiencia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DatoEficiencia createEntity() {
        return new DatoEficiencia()
            .fecha(DEFAULT_FECHA)
            .energiaPvWh(DEFAULT_ENERGIA_PV_WH)
            .energiaCargaWh(DEFAULT_ENERGIA_CARGA_WH)
            .energiaRedWh(DEFAULT_ENERGIA_RED_WH)
            .eficienciaPromPct(DEFAULT_EFICIENCIA_PROM_PCT)
            .horasSol(DEFAULT_HORAS_SOL)
            .picoPvW(DEFAULT_PICO_PV_W)
            .picoCargaW(DEFAULT_PICO_CARGA_W)
            .maxBateriaV(DEFAULT_MAX_BATERIA_V)
            .minBateriaV(DEFAULT_MIN_BATERIA_V)
            .totalLecturas(DEFAULT_TOTAL_LECTURAS)
            .calculadoEn(DEFAULT_CALCULADO_EN);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DatoEficiencia createUpdatedEntity() {
        return new DatoEficiencia()
            .fecha(UPDATED_FECHA)
            .energiaPvWh(UPDATED_ENERGIA_PV_WH)
            .energiaCargaWh(UPDATED_ENERGIA_CARGA_WH)
            .energiaRedWh(UPDATED_ENERGIA_RED_WH)
            .eficienciaPromPct(UPDATED_EFICIENCIA_PROM_PCT)
            .horasSol(UPDATED_HORAS_SOL)
            .picoPvW(UPDATED_PICO_PV_W)
            .picoCargaW(UPDATED_PICO_CARGA_W)
            .maxBateriaV(UPDATED_MAX_BATERIA_V)
            .minBateriaV(UPDATED_MIN_BATERIA_V)
            .totalLecturas(UPDATED_TOTAL_LECTURAS)
            .calculadoEn(UPDATED_CALCULADO_EN);
    }

    @BeforeEach
    void initTest() {
        datoEficiencia = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDatoEficiencia != null) {
            datoEficienciaRepository.delete(insertedDatoEficiencia);
            insertedDatoEficiencia = null;
        }
    }

    @Test
    @Transactional
    void createDatoEficiencia() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DatoEficiencia
        DatoEficienciaDTO datoEficienciaDTO = datoEficienciaMapper.toDto(datoEficiencia);
        var returnedDatoEficienciaDTO = om.readValue(
            restDatoEficienciaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(datoEficienciaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DatoEficienciaDTO.class
        );

        // Validate the DatoEficiencia in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDatoEficiencia = datoEficienciaMapper.toEntity(returnedDatoEficienciaDTO);
        assertDatoEficienciaUpdatableFieldsEquals(returnedDatoEficiencia, getPersistedDatoEficiencia(returnedDatoEficiencia));

        insertedDatoEficiencia = returnedDatoEficiencia;
    }

    @Test
    @Transactional
    void createDatoEficienciaWithExistingId() throws Exception {
        // Create the DatoEficiencia with an existing ID
        datoEficiencia.setId(1L);
        DatoEficienciaDTO datoEficienciaDTO = datoEficienciaMapper.toDto(datoEficiencia);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDatoEficienciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(datoEficienciaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DatoEficiencia in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        datoEficiencia.setFecha(null);

        // Create the DatoEficiencia, which fails.
        DatoEficienciaDTO datoEficienciaDTO = datoEficienciaMapper.toDto(datoEficiencia);

        restDatoEficienciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(datoEficienciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDatoEficiencias() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList
        restDatoEficienciaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(datoEficiencia.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].energiaPvWh").value(hasItem(DEFAULT_ENERGIA_PV_WH)))
            .andExpect(jsonPath("$.[*].energiaCargaWh").value(hasItem(DEFAULT_ENERGIA_CARGA_WH)))
            .andExpect(jsonPath("$.[*].energiaRedWh").value(hasItem(DEFAULT_ENERGIA_RED_WH)))
            .andExpect(jsonPath("$.[*].eficienciaPromPct").value(hasItem(DEFAULT_EFICIENCIA_PROM_PCT)))
            .andExpect(jsonPath("$.[*].horasSol").value(hasItem(DEFAULT_HORAS_SOL)))
            .andExpect(jsonPath("$.[*].picoPvW").value(hasItem(DEFAULT_PICO_PV_W)))
            .andExpect(jsonPath("$.[*].picoCargaW").value(hasItem(DEFAULT_PICO_CARGA_W)))
            .andExpect(jsonPath("$.[*].maxBateriaV").value(hasItem(DEFAULT_MAX_BATERIA_V)))
            .andExpect(jsonPath("$.[*].minBateriaV").value(hasItem(DEFAULT_MIN_BATERIA_V)))
            .andExpect(jsonPath("$.[*].totalLecturas").value(hasItem(DEFAULT_TOTAL_LECTURAS)))
            .andExpect(jsonPath("$.[*].calculadoEn").value(hasItem(DEFAULT_CALCULADO_EN.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDatoEficienciasWithEagerRelationshipsIsEnabled() throws Exception {
        when(datoEficienciaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDatoEficienciaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(datoEficienciaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDatoEficienciasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(datoEficienciaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDatoEficienciaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(datoEficienciaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDatoEficiencia() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get the datoEficiencia
        restDatoEficienciaMockMvc
            .perform(get(ENTITY_API_URL_ID, datoEficiencia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(datoEficiencia.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.energiaPvWh").value(DEFAULT_ENERGIA_PV_WH))
            .andExpect(jsonPath("$.energiaCargaWh").value(DEFAULT_ENERGIA_CARGA_WH))
            .andExpect(jsonPath("$.energiaRedWh").value(DEFAULT_ENERGIA_RED_WH))
            .andExpect(jsonPath("$.eficienciaPromPct").value(DEFAULT_EFICIENCIA_PROM_PCT))
            .andExpect(jsonPath("$.horasSol").value(DEFAULT_HORAS_SOL))
            .andExpect(jsonPath("$.picoPvW").value(DEFAULT_PICO_PV_W))
            .andExpect(jsonPath("$.picoCargaW").value(DEFAULT_PICO_CARGA_W))
            .andExpect(jsonPath("$.maxBateriaV").value(DEFAULT_MAX_BATERIA_V))
            .andExpect(jsonPath("$.minBateriaV").value(DEFAULT_MIN_BATERIA_V))
            .andExpect(jsonPath("$.totalLecturas").value(DEFAULT_TOTAL_LECTURAS))
            .andExpect(jsonPath("$.calculadoEn").value(DEFAULT_CALCULADO_EN.toString()));
    }

    @Test
    @Transactional
    void getDatoEficienciasByIdFiltering() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        Long id = datoEficiencia.getId();

        defaultDatoEficienciaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDatoEficienciaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDatoEficienciaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where fecha equals to
        defaultDatoEficienciaFiltering("fecha.equals=" + DEFAULT_FECHA, "fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where fecha in
        defaultDatoEficienciaFiltering("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA, "fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where fecha is not null
        defaultDatoEficienciaFiltering("fecha.specified=true", "fecha.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByFechaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where fecha is greater than or equal to
        defaultDatoEficienciaFiltering("fecha.greaterThanOrEqual=" + DEFAULT_FECHA, "fecha.greaterThanOrEqual=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByFechaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where fecha is less than or equal to
        defaultDatoEficienciaFiltering("fecha.lessThanOrEqual=" + DEFAULT_FECHA, "fecha.lessThanOrEqual=" + SMALLER_FECHA);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByFechaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where fecha is less than
        defaultDatoEficienciaFiltering("fecha.lessThan=" + UPDATED_FECHA, "fecha.lessThan=" + DEFAULT_FECHA);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByFechaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where fecha is greater than
        defaultDatoEficienciaFiltering("fecha.greaterThan=" + SMALLER_FECHA, "fecha.greaterThan=" + DEFAULT_FECHA);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByEnergiaPvWhIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where energiaPvWh equals to
        defaultDatoEficienciaFiltering("energiaPvWh.equals=" + DEFAULT_ENERGIA_PV_WH, "energiaPvWh.equals=" + UPDATED_ENERGIA_PV_WH);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByEnergiaPvWhIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where energiaPvWh in
        defaultDatoEficienciaFiltering(
            "energiaPvWh.in=" + DEFAULT_ENERGIA_PV_WH + "," + UPDATED_ENERGIA_PV_WH,
            "energiaPvWh.in=" + UPDATED_ENERGIA_PV_WH
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByEnergiaPvWhIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where energiaPvWh is not null
        defaultDatoEficienciaFiltering("energiaPvWh.specified=true", "energiaPvWh.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByEnergiaPvWhIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where energiaPvWh is greater than or equal to
        defaultDatoEficienciaFiltering(
            "energiaPvWh.greaterThanOrEqual=" + DEFAULT_ENERGIA_PV_WH,
            "energiaPvWh.greaterThanOrEqual=" + UPDATED_ENERGIA_PV_WH
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByEnergiaPvWhIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where energiaPvWh is less than or equal to
        defaultDatoEficienciaFiltering(
            "energiaPvWh.lessThanOrEqual=" + DEFAULT_ENERGIA_PV_WH,
            "energiaPvWh.lessThanOrEqual=" + SMALLER_ENERGIA_PV_WH
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByEnergiaPvWhIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where energiaPvWh is less than
        defaultDatoEficienciaFiltering("energiaPvWh.lessThan=" + UPDATED_ENERGIA_PV_WH, "energiaPvWh.lessThan=" + DEFAULT_ENERGIA_PV_WH);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByEnergiaPvWhIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where energiaPvWh is greater than
        defaultDatoEficienciaFiltering(
            "energiaPvWh.greaterThan=" + SMALLER_ENERGIA_PV_WH,
            "energiaPvWh.greaterThan=" + DEFAULT_ENERGIA_PV_WH
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByEnergiaCargaWhIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where energiaCargaWh equals to
        defaultDatoEficienciaFiltering(
            "energiaCargaWh.equals=" + DEFAULT_ENERGIA_CARGA_WH,
            "energiaCargaWh.equals=" + UPDATED_ENERGIA_CARGA_WH
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByEnergiaCargaWhIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where energiaCargaWh in
        defaultDatoEficienciaFiltering(
            "energiaCargaWh.in=" + DEFAULT_ENERGIA_CARGA_WH + "," + UPDATED_ENERGIA_CARGA_WH,
            "energiaCargaWh.in=" + UPDATED_ENERGIA_CARGA_WH
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByEnergiaCargaWhIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where energiaCargaWh is not null
        defaultDatoEficienciaFiltering("energiaCargaWh.specified=true", "energiaCargaWh.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByEnergiaCargaWhIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where energiaCargaWh is greater than or equal to
        defaultDatoEficienciaFiltering(
            "energiaCargaWh.greaterThanOrEqual=" + DEFAULT_ENERGIA_CARGA_WH,
            "energiaCargaWh.greaterThanOrEqual=" + UPDATED_ENERGIA_CARGA_WH
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByEnergiaCargaWhIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where energiaCargaWh is less than or equal to
        defaultDatoEficienciaFiltering(
            "energiaCargaWh.lessThanOrEqual=" + DEFAULT_ENERGIA_CARGA_WH,
            "energiaCargaWh.lessThanOrEqual=" + SMALLER_ENERGIA_CARGA_WH
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByEnergiaCargaWhIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where energiaCargaWh is less than
        defaultDatoEficienciaFiltering(
            "energiaCargaWh.lessThan=" + UPDATED_ENERGIA_CARGA_WH,
            "energiaCargaWh.lessThan=" + DEFAULT_ENERGIA_CARGA_WH
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByEnergiaCargaWhIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where energiaCargaWh is greater than
        defaultDatoEficienciaFiltering(
            "energiaCargaWh.greaterThan=" + SMALLER_ENERGIA_CARGA_WH,
            "energiaCargaWh.greaterThan=" + DEFAULT_ENERGIA_CARGA_WH
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByEnergiaRedWhIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where energiaRedWh equals to
        defaultDatoEficienciaFiltering("energiaRedWh.equals=" + DEFAULT_ENERGIA_RED_WH, "energiaRedWh.equals=" + UPDATED_ENERGIA_RED_WH);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByEnergiaRedWhIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where energiaRedWh in
        defaultDatoEficienciaFiltering(
            "energiaRedWh.in=" + DEFAULT_ENERGIA_RED_WH + "," + UPDATED_ENERGIA_RED_WH,
            "energiaRedWh.in=" + UPDATED_ENERGIA_RED_WH
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByEnergiaRedWhIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where energiaRedWh is not null
        defaultDatoEficienciaFiltering("energiaRedWh.specified=true", "energiaRedWh.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByEnergiaRedWhIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where energiaRedWh is greater than or equal to
        defaultDatoEficienciaFiltering(
            "energiaRedWh.greaterThanOrEqual=" + DEFAULT_ENERGIA_RED_WH,
            "energiaRedWh.greaterThanOrEqual=" + UPDATED_ENERGIA_RED_WH
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByEnergiaRedWhIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where energiaRedWh is less than or equal to
        defaultDatoEficienciaFiltering(
            "energiaRedWh.lessThanOrEqual=" + DEFAULT_ENERGIA_RED_WH,
            "energiaRedWh.lessThanOrEqual=" + SMALLER_ENERGIA_RED_WH
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByEnergiaRedWhIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where energiaRedWh is less than
        defaultDatoEficienciaFiltering(
            "energiaRedWh.lessThan=" + UPDATED_ENERGIA_RED_WH,
            "energiaRedWh.lessThan=" + DEFAULT_ENERGIA_RED_WH
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByEnergiaRedWhIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where energiaRedWh is greater than
        defaultDatoEficienciaFiltering(
            "energiaRedWh.greaterThan=" + SMALLER_ENERGIA_RED_WH,
            "energiaRedWh.greaterThan=" + DEFAULT_ENERGIA_RED_WH
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByEficienciaPromPctIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where eficienciaPromPct equals to
        defaultDatoEficienciaFiltering(
            "eficienciaPromPct.equals=" + DEFAULT_EFICIENCIA_PROM_PCT,
            "eficienciaPromPct.equals=" + UPDATED_EFICIENCIA_PROM_PCT
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByEficienciaPromPctIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where eficienciaPromPct in
        defaultDatoEficienciaFiltering(
            "eficienciaPromPct.in=" + DEFAULT_EFICIENCIA_PROM_PCT + "," + UPDATED_EFICIENCIA_PROM_PCT,
            "eficienciaPromPct.in=" + UPDATED_EFICIENCIA_PROM_PCT
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByEficienciaPromPctIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where eficienciaPromPct is not null
        defaultDatoEficienciaFiltering("eficienciaPromPct.specified=true", "eficienciaPromPct.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByEficienciaPromPctIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where eficienciaPromPct is greater than or equal to
        defaultDatoEficienciaFiltering(
            "eficienciaPromPct.greaterThanOrEqual=" + DEFAULT_EFICIENCIA_PROM_PCT,
            "eficienciaPromPct.greaterThanOrEqual=" + UPDATED_EFICIENCIA_PROM_PCT
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByEficienciaPromPctIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where eficienciaPromPct is less than or equal to
        defaultDatoEficienciaFiltering(
            "eficienciaPromPct.lessThanOrEqual=" + DEFAULT_EFICIENCIA_PROM_PCT,
            "eficienciaPromPct.lessThanOrEqual=" + SMALLER_EFICIENCIA_PROM_PCT
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByEficienciaPromPctIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where eficienciaPromPct is less than
        defaultDatoEficienciaFiltering(
            "eficienciaPromPct.lessThan=" + UPDATED_EFICIENCIA_PROM_PCT,
            "eficienciaPromPct.lessThan=" + DEFAULT_EFICIENCIA_PROM_PCT
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByEficienciaPromPctIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where eficienciaPromPct is greater than
        defaultDatoEficienciaFiltering(
            "eficienciaPromPct.greaterThan=" + SMALLER_EFICIENCIA_PROM_PCT,
            "eficienciaPromPct.greaterThan=" + DEFAULT_EFICIENCIA_PROM_PCT
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByHorasSolIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where horasSol equals to
        defaultDatoEficienciaFiltering("horasSol.equals=" + DEFAULT_HORAS_SOL, "horasSol.equals=" + UPDATED_HORAS_SOL);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByHorasSolIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where horasSol in
        defaultDatoEficienciaFiltering("horasSol.in=" + DEFAULT_HORAS_SOL + "," + UPDATED_HORAS_SOL, "horasSol.in=" + UPDATED_HORAS_SOL);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByHorasSolIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where horasSol is not null
        defaultDatoEficienciaFiltering("horasSol.specified=true", "horasSol.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByHorasSolIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where horasSol is greater than or equal to
        defaultDatoEficienciaFiltering(
            "horasSol.greaterThanOrEqual=" + DEFAULT_HORAS_SOL,
            "horasSol.greaterThanOrEqual=" + UPDATED_HORAS_SOL
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByHorasSolIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where horasSol is less than or equal to
        defaultDatoEficienciaFiltering("horasSol.lessThanOrEqual=" + DEFAULT_HORAS_SOL, "horasSol.lessThanOrEqual=" + SMALLER_HORAS_SOL);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByHorasSolIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where horasSol is less than
        defaultDatoEficienciaFiltering("horasSol.lessThan=" + UPDATED_HORAS_SOL, "horasSol.lessThan=" + DEFAULT_HORAS_SOL);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByHorasSolIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where horasSol is greater than
        defaultDatoEficienciaFiltering("horasSol.greaterThan=" + SMALLER_HORAS_SOL, "horasSol.greaterThan=" + DEFAULT_HORAS_SOL);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByPicoPvWIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where picoPvW equals to
        defaultDatoEficienciaFiltering("picoPvW.equals=" + DEFAULT_PICO_PV_W, "picoPvW.equals=" + UPDATED_PICO_PV_W);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByPicoPvWIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where picoPvW in
        defaultDatoEficienciaFiltering("picoPvW.in=" + DEFAULT_PICO_PV_W + "," + UPDATED_PICO_PV_W, "picoPvW.in=" + UPDATED_PICO_PV_W);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByPicoPvWIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where picoPvW is not null
        defaultDatoEficienciaFiltering("picoPvW.specified=true", "picoPvW.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByPicoPvWIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where picoPvW is greater than or equal to
        defaultDatoEficienciaFiltering(
            "picoPvW.greaterThanOrEqual=" + DEFAULT_PICO_PV_W,
            "picoPvW.greaterThanOrEqual=" + UPDATED_PICO_PV_W
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByPicoPvWIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where picoPvW is less than or equal to
        defaultDatoEficienciaFiltering("picoPvW.lessThanOrEqual=" + DEFAULT_PICO_PV_W, "picoPvW.lessThanOrEqual=" + SMALLER_PICO_PV_W);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByPicoPvWIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where picoPvW is less than
        defaultDatoEficienciaFiltering("picoPvW.lessThan=" + UPDATED_PICO_PV_W, "picoPvW.lessThan=" + DEFAULT_PICO_PV_W);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByPicoPvWIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where picoPvW is greater than
        defaultDatoEficienciaFiltering("picoPvW.greaterThan=" + SMALLER_PICO_PV_W, "picoPvW.greaterThan=" + DEFAULT_PICO_PV_W);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByPicoCargaWIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where picoCargaW equals to
        defaultDatoEficienciaFiltering("picoCargaW.equals=" + DEFAULT_PICO_CARGA_W, "picoCargaW.equals=" + UPDATED_PICO_CARGA_W);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByPicoCargaWIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where picoCargaW in
        defaultDatoEficienciaFiltering(
            "picoCargaW.in=" + DEFAULT_PICO_CARGA_W + "," + UPDATED_PICO_CARGA_W,
            "picoCargaW.in=" + UPDATED_PICO_CARGA_W
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByPicoCargaWIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where picoCargaW is not null
        defaultDatoEficienciaFiltering("picoCargaW.specified=true", "picoCargaW.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByPicoCargaWIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where picoCargaW is greater than or equal to
        defaultDatoEficienciaFiltering(
            "picoCargaW.greaterThanOrEqual=" + DEFAULT_PICO_CARGA_W,
            "picoCargaW.greaterThanOrEqual=" + UPDATED_PICO_CARGA_W
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByPicoCargaWIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where picoCargaW is less than or equal to
        defaultDatoEficienciaFiltering(
            "picoCargaW.lessThanOrEqual=" + DEFAULT_PICO_CARGA_W,
            "picoCargaW.lessThanOrEqual=" + SMALLER_PICO_CARGA_W
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByPicoCargaWIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where picoCargaW is less than
        defaultDatoEficienciaFiltering("picoCargaW.lessThan=" + UPDATED_PICO_CARGA_W, "picoCargaW.lessThan=" + DEFAULT_PICO_CARGA_W);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByPicoCargaWIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where picoCargaW is greater than
        defaultDatoEficienciaFiltering("picoCargaW.greaterThan=" + SMALLER_PICO_CARGA_W, "picoCargaW.greaterThan=" + DEFAULT_PICO_CARGA_W);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByMaxBateriaVIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where maxBateriaV equals to
        defaultDatoEficienciaFiltering("maxBateriaV.equals=" + DEFAULT_MAX_BATERIA_V, "maxBateriaV.equals=" + UPDATED_MAX_BATERIA_V);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByMaxBateriaVIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where maxBateriaV in
        defaultDatoEficienciaFiltering(
            "maxBateriaV.in=" + DEFAULT_MAX_BATERIA_V + "," + UPDATED_MAX_BATERIA_V,
            "maxBateriaV.in=" + UPDATED_MAX_BATERIA_V
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByMaxBateriaVIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where maxBateriaV is not null
        defaultDatoEficienciaFiltering("maxBateriaV.specified=true", "maxBateriaV.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByMaxBateriaVIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where maxBateriaV is greater than or equal to
        defaultDatoEficienciaFiltering(
            "maxBateriaV.greaterThanOrEqual=" + DEFAULT_MAX_BATERIA_V,
            "maxBateriaV.greaterThanOrEqual=" + UPDATED_MAX_BATERIA_V
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByMaxBateriaVIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where maxBateriaV is less than or equal to
        defaultDatoEficienciaFiltering(
            "maxBateriaV.lessThanOrEqual=" + DEFAULT_MAX_BATERIA_V,
            "maxBateriaV.lessThanOrEqual=" + SMALLER_MAX_BATERIA_V
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByMaxBateriaVIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where maxBateriaV is less than
        defaultDatoEficienciaFiltering("maxBateriaV.lessThan=" + UPDATED_MAX_BATERIA_V, "maxBateriaV.lessThan=" + DEFAULT_MAX_BATERIA_V);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByMaxBateriaVIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where maxBateriaV is greater than
        defaultDatoEficienciaFiltering(
            "maxBateriaV.greaterThan=" + SMALLER_MAX_BATERIA_V,
            "maxBateriaV.greaterThan=" + DEFAULT_MAX_BATERIA_V
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByMinBateriaVIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where minBateriaV equals to
        defaultDatoEficienciaFiltering("minBateriaV.equals=" + DEFAULT_MIN_BATERIA_V, "minBateriaV.equals=" + UPDATED_MIN_BATERIA_V);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByMinBateriaVIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where minBateriaV in
        defaultDatoEficienciaFiltering(
            "minBateriaV.in=" + DEFAULT_MIN_BATERIA_V + "," + UPDATED_MIN_BATERIA_V,
            "minBateriaV.in=" + UPDATED_MIN_BATERIA_V
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByMinBateriaVIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where minBateriaV is not null
        defaultDatoEficienciaFiltering("minBateriaV.specified=true", "minBateriaV.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByMinBateriaVIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where minBateriaV is greater than or equal to
        defaultDatoEficienciaFiltering(
            "minBateriaV.greaterThanOrEqual=" + DEFAULT_MIN_BATERIA_V,
            "minBateriaV.greaterThanOrEqual=" + UPDATED_MIN_BATERIA_V
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByMinBateriaVIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where minBateriaV is less than or equal to
        defaultDatoEficienciaFiltering(
            "minBateriaV.lessThanOrEqual=" + DEFAULT_MIN_BATERIA_V,
            "minBateriaV.lessThanOrEqual=" + SMALLER_MIN_BATERIA_V
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByMinBateriaVIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where minBateriaV is less than
        defaultDatoEficienciaFiltering("minBateriaV.lessThan=" + UPDATED_MIN_BATERIA_V, "minBateriaV.lessThan=" + DEFAULT_MIN_BATERIA_V);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByMinBateriaVIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where minBateriaV is greater than
        defaultDatoEficienciaFiltering(
            "minBateriaV.greaterThan=" + SMALLER_MIN_BATERIA_V,
            "minBateriaV.greaterThan=" + DEFAULT_MIN_BATERIA_V
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByTotalLecturasIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where totalLecturas equals to
        defaultDatoEficienciaFiltering("totalLecturas.equals=" + DEFAULT_TOTAL_LECTURAS, "totalLecturas.equals=" + UPDATED_TOTAL_LECTURAS);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByTotalLecturasIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where totalLecturas in
        defaultDatoEficienciaFiltering(
            "totalLecturas.in=" + DEFAULT_TOTAL_LECTURAS + "," + UPDATED_TOTAL_LECTURAS,
            "totalLecturas.in=" + UPDATED_TOTAL_LECTURAS
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByTotalLecturasIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where totalLecturas is not null
        defaultDatoEficienciaFiltering("totalLecturas.specified=true", "totalLecturas.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByTotalLecturasIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where totalLecturas is greater than or equal to
        defaultDatoEficienciaFiltering(
            "totalLecturas.greaterThanOrEqual=" + DEFAULT_TOTAL_LECTURAS,
            "totalLecturas.greaterThanOrEqual=" + UPDATED_TOTAL_LECTURAS
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByTotalLecturasIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where totalLecturas is less than or equal to
        defaultDatoEficienciaFiltering(
            "totalLecturas.lessThanOrEqual=" + DEFAULT_TOTAL_LECTURAS,
            "totalLecturas.lessThanOrEqual=" + SMALLER_TOTAL_LECTURAS
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByTotalLecturasIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where totalLecturas is less than
        defaultDatoEficienciaFiltering(
            "totalLecturas.lessThan=" + UPDATED_TOTAL_LECTURAS,
            "totalLecturas.lessThan=" + DEFAULT_TOTAL_LECTURAS
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByTotalLecturasIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where totalLecturas is greater than
        defaultDatoEficienciaFiltering(
            "totalLecturas.greaterThan=" + SMALLER_TOTAL_LECTURAS,
            "totalLecturas.greaterThan=" + DEFAULT_TOTAL_LECTURAS
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByCalculadoEnIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where calculadoEn equals to
        defaultDatoEficienciaFiltering("calculadoEn.equals=" + DEFAULT_CALCULADO_EN, "calculadoEn.equals=" + UPDATED_CALCULADO_EN);
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByCalculadoEnIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where calculadoEn in
        defaultDatoEficienciaFiltering(
            "calculadoEn.in=" + DEFAULT_CALCULADO_EN + "," + UPDATED_CALCULADO_EN,
            "calculadoEn.in=" + UPDATED_CALCULADO_EN
        );
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByCalculadoEnIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        // Get all the datoEficienciaList where calculadoEn is not null
        defaultDatoEficienciaFiltering("calculadoEn.specified=true", "calculadoEn.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoEficienciasByInversorIsEqualToSomething() throws Exception {
        Inversor inversor;
        if (TestUtil.findAll(em, Inversor.class).isEmpty()) {
            datoEficienciaRepository.saveAndFlush(datoEficiencia);
            inversor = InversorResourceIT.createEntity();
        } else {
            inversor = TestUtil.findAll(em, Inversor.class).get(0);
        }
        em.persist(inversor);
        em.flush();
        datoEficiencia.setInversor(inversor);
        datoEficienciaRepository.saveAndFlush(datoEficiencia);
        Long inversorId = inversor.getId();
        // Get all the datoEficienciaList where inversor equals to inversorId
        defaultDatoEficienciaShouldBeFound("inversorId.equals=" + inversorId);

        // Get all the datoEficienciaList where inversor equals to (inversorId + 1)
        defaultDatoEficienciaShouldNotBeFound("inversorId.equals=" + (inversorId + 1));
    }

    private void defaultDatoEficienciaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDatoEficienciaShouldBeFound(shouldBeFound);
        defaultDatoEficienciaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDatoEficienciaShouldBeFound(String filter) throws Exception {
        restDatoEficienciaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(datoEficiencia.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].energiaPvWh").value(hasItem(DEFAULT_ENERGIA_PV_WH)))
            .andExpect(jsonPath("$.[*].energiaCargaWh").value(hasItem(DEFAULT_ENERGIA_CARGA_WH)))
            .andExpect(jsonPath("$.[*].energiaRedWh").value(hasItem(DEFAULT_ENERGIA_RED_WH)))
            .andExpect(jsonPath("$.[*].eficienciaPromPct").value(hasItem(DEFAULT_EFICIENCIA_PROM_PCT)))
            .andExpect(jsonPath("$.[*].horasSol").value(hasItem(DEFAULT_HORAS_SOL)))
            .andExpect(jsonPath("$.[*].picoPvW").value(hasItem(DEFAULT_PICO_PV_W)))
            .andExpect(jsonPath("$.[*].picoCargaW").value(hasItem(DEFAULT_PICO_CARGA_W)))
            .andExpect(jsonPath("$.[*].maxBateriaV").value(hasItem(DEFAULT_MAX_BATERIA_V)))
            .andExpect(jsonPath("$.[*].minBateriaV").value(hasItem(DEFAULT_MIN_BATERIA_V)))
            .andExpect(jsonPath("$.[*].totalLecturas").value(hasItem(DEFAULT_TOTAL_LECTURAS)))
            .andExpect(jsonPath("$.[*].calculadoEn").value(hasItem(DEFAULT_CALCULADO_EN.toString())));

        // Check, that the count call also returns 1
        restDatoEficienciaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDatoEficienciaShouldNotBeFound(String filter) throws Exception {
        restDatoEficienciaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDatoEficienciaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDatoEficiencia() throws Exception {
        // Get the datoEficiencia
        restDatoEficienciaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDatoEficiencia() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the datoEficiencia
        DatoEficiencia updatedDatoEficiencia = datoEficienciaRepository.findById(datoEficiencia.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDatoEficiencia are not directly saved in db
        em.detach(updatedDatoEficiencia);
        updatedDatoEficiencia
            .fecha(UPDATED_FECHA)
            .energiaPvWh(UPDATED_ENERGIA_PV_WH)
            .energiaCargaWh(UPDATED_ENERGIA_CARGA_WH)
            .energiaRedWh(UPDATED_ENERGIA_RED_WH)
            .eficienciaPromPct(UPDATED_EFICIENCIA_PROM_PCT)
            .horasSol(UPDATED_HORAS_SOL)
            .picoPvW(UPDATED_PICO_PV_W)
            .picoCargaW(UPDATED_PICO_CARGA_W)
            .maxBateriaV(UPDATED_MAX_BATERIA_V)
            .minBateriaV(UPDATED_MIN_BATERIA_V)
            .totalLecturas(UPDATED_TOTAL_LECTURAS)
            .calculadoEn(UPDATED_CALCULADO_EN);
        DatoEficienciaDTO datoEficienciaDTO = datoEficienciaMapper.toDto(updatedDatoEficiencia);

        restDatoEficienciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, datoEficienciaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(datoEficienciaDTO))
            )
            .andExpect(status().isOk());

        // Validate the DatoEficiencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDatoEficienciaToMatchAllProperties(updatedDatoEficiencia);
    }

    @Test
    @Transactional
    void putNonExistingDatoEficiencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        datoEficiencia.setId(longCount.incrementAndGet());

        // Create the DatoEficiencia
        DatoEficienciaDTO datoEficienciaDTO = datoEficienciaMapper.toDto(datoEficiencia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDatoEficienciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, datoEficienciaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(datoEficienciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DatoEficiencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDatoEficiencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        datoEficiencia.setId(longCount.incrementAndGet());

        // Create the DatoEficiencia
        DatoEficienciaDTO datoEficienciaDTO = datoEficienciaMapper.toDto(datoEficiencia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDatoEficienciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(datoEficienciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DatoEficiencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDatoEficiencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        datoEficiencia.setId(longCount.incrementAndGet());

        // Create the DatoEficiencia
        DatoEficienciaDTO datoEficienciaDTO = datoEficienciaMapper.toDto(datoEficiencia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDatoEficienciaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(datoEficienciaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DatoEficiencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDatoEficienciaWithPatch() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the datoEficiencia using partial update
        DatoEficiencia partialUpdatedDatoEficiencia = new DatoEficiencia();
        partialUpdatedDatoEficiencia.setId(datoEficiencia.getId());

        partialUpdatedDatoEficiencia
            .fecha(UPDATED_FECHA)
            .energiaCargaWh(UPDATED_ENERGIA_CARGA_WH)
            .energiaRedWh(UPDATED_ENERGIA_RED_WH)
            .eficienciaPromPct(UPDATED_EFICIENCIA_PROM_PCT)
            .picoPvW(UPDATED_PICO_PV_W)
            .maxBateriaV(UPDATED_MAX_BATERIA_V)
            .totalLecturas(UPDATED_TOTAL_LECTURAS)
            .calculadoEn(UPDATED_CALCULADO_EN);

        restDatoEficienciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDatoEficiencia.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDatoEficiencia))
            )
            .andExpect(status().isOk());

        // Validate the DatoEficiencia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDatoEficienciaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDatoEficiencia, datoEficiencia),
            getPersistedDatoEficiencia(datoEficiencia)
        );
    }

    @Test
    @Transactional
    void fullUpdateDatoEficienciaWithPatch() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the datoEficiencia using partial update
        DatoEficiencia partialUpdatedDatoEficiencia = new DatoEficiencia();
        partialUpdatedDatoEficiencia.setId(datoEficiencia.getId());

        partialUpdatedDatoEficiencia
            .fecha(UPDATED_FECHA)
            .energiaPvWh(UPDATED_ENERGIA_PV_WH)
            .energiaCargaWh(UPDATED_ENERGIA_CARGA_WH)
            .energiaRedWh(UPDATED_ENERGIA_RED_WH)
            .eficienciaPromPct(UPDATED_EFICIENCIA_PROM_PCT)
            .horasSol(UPDATED_HORAS_SOL)
            .picoPvW(UPDATED_PICO_PV_W)
            .picoCargaW(UPDATED_PICO_CARGA_W)
            .maxBateriaV(UPDATED_MAX_BATERIA_V)
            .minBateriaV(UPDATED_MIN_BATERIA_V)
            .totalLecturas(UPDATED_TOTAL_LECTURAS)
            .calculadoEn(UPDATED_CALCULADO_EN);

        restDatoEficienciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDatoEficiencia.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDatoEficiencia))
            )
            .andExpect(status().isOk());

        // Validate the DatoEficiencia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDatoEficienciaUpdatableFieldsEquals(partialUpdatedDatoEficiencia, getPersistedDatoEficiencia(partialUpdatedDatoEficiencia));
    }

    @Test
    @Transactional
    void patchNonExistingDatoEficiencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        datoEficiencia.setId(longCount.incrementAndGet());

        // Create the DatoEficiencia
        DatoEficienciaDTO datoEficienciaDTO = datoEficienciaMapper.toDto(datoEficiencia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDatoEficienciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, datoEficienciaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(datoEficienciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DatoEficiencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDatoEficiencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        datoEficiencia.setId(longCount.incrementAndGet());

        // Create the DatoEficiencia
        DatoEficienciaDTO datoEficienciaDTO = datoEficienciaMapper.toDto(datoEficiencia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDatoEficienciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(datoEficienciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DatoEficiencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDatoEficiencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        datoEficiencia.setId(longCount.incrementAndGet());

        // Create the DatoEficiencia
        DatoEficienciaDTO datoEficienciaDTO = datoEficienciaMapper.toDto(datoEficiencia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDatoEficienciaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(datoEficienciaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DatoEficiencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDatoEficiencia() throws Exception {
        // Initialize the database
        insertedDatoEficiencia = datoEficienciaRepository.saveAndFlush(datoEficiencia);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the datoEficiencia
        restDatoEficienciaMockMvc
            .perform(delete(ENTITY_API_URL_ID, datoEficiencia.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return datoEficienciaRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected DatoEficiencia getPersistedDatoEficiencia(DatoEficiencia datoEficiencia) {
        return datoEficienciaRepository.findById(datoEficiencia.getId()).orElseThrow();
    }

    protected void assertPersistedDatoEficienciaToMatchAllProperties(DatoEficiencia expectedDatoEficiencia) {
        assertDatoEficienciaAllPropertiesEquals(expectedDatoEficiencia, getPersistedDatoEficiencia(expectedDatoEficiencia));
    }

    protected void assertPersistedDatoEficienciaToMatchUpdatableProperties(DatoEficiencia expectedDatoEficiencia) {
        assertDatoEficienciaAllUpdatablePropertiesEquals(expectedDatoEficiencia, getPersistedDatoEficiencia(expectedDatoEficiencia));
    }
}
