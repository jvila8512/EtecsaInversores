package com.etecsa.monitor.web.rest;

import static com.etecsa.monitor.domain.DatoInversorAsserts.*;
import static com.etecsa.monitor.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.etecsa.monitor.IntegrationTest;
import com.etecsa.monitor.domain.DatoInversor;
import com.etecsa.monitor.domain.Inversor;
import com.etecsa.monitor.repository.DatoInversorRepository;
import com.etecsa.monitor.service.DatoInversorService;
import com.etecsa.monitor.service.dto.DatoInversorDTO;
import com.etecsa.monitor.service.mapper.DatoInversorMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
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
 * Integration tests for the {@link DatoInversorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DatoInversorResourceIT {

    private static final Instant DEFAULT_TIEMPO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIEMPO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_BATERIA_VOLTAJE_V = 1D;
    private static final Double UPDATED_BATERIA_VOLTAJE_V = 2D;
    private static final Double SMALLER_BATERIA_VOLTAJE_V = 1D - 1D;

    private static final Double DEFAULT_BATERIA_CORRIENTE_A = 1D;
    private static final Double UPDATED_BATERIA_CORRIENTE_A = 2D;
    private static final Double SMALLER_BATERIA_CORRIENTE_A = 1D - 1D;

    private static final Double DEFAULT_BATERIA_POTENCIA_W = 1D;
    private static final Double UPDATED_BATERIA_POTENCIA_W = 2D;
    private static final Double SMALLER_BATERIA_POTENCIA_W = 1D - 1D;

    private static final Double DEFAULT_PV_VOLTAJE_V = 1D;
    private static final Double UPDATED_PV_VOLTAJE_V = 2D;
    private static final Double SMALLER_PV_VOLTAJE_V = 1D - 1D;

    private static final Double DEFAULT_PV_POTENCIA_W = 1D;
    private static final Double UPDATED_PV_POTENCIA_W = 2D;
    private static final Double SMALLER_PV_POTENCIA_W = 1D - 1D;

    private static final Double DEFAULT_SALIDA_VOLTAJE_V = 1D;
    private static final Double UPDATED_SALIDA_VOLTAJE_V = 2D;
    private static final Double SMALLER_SALIDA_VOLTAJE_V = 1D - 1D;

    private static final Double DEFAULT_SALIDA_POTENCIA_W = 1D;
    private static final Double UPDATED_SALIDA_POTENCIA_W = 2D;
    private static final Double SMALLER_SALIDA_POTENCIA_W = 1D - 1D;

    private static final Double DEFAULT_SALIDA_APARENTE_VA = 1D;
    private static final Double UPDATED_SALIDA_APARENTE_VA = 2D;
    private static final Double SMALLER_SALIDA_APARENTE_VA = 1D - 1D;

    private static final Integer DEFAULT_CARGA_PORCENTAJE = 1;
    private static final Integer UPDATED_CARGA_PORCENTAJE = 2;
    private static final Integer SMALLER_CARGA_PORCENTAJE = 1 - 1;

    private static final Double DEFAULT_RED_VOLTAJE_V = 1D;
    private static final Double UPDATED_RED_VOLTAJE_V = 2D;
    private static final Double SMALLER_RED_VOLTAJE_V = 1D - 1D;

    private static final Double DEFAULT_RED_FRECUENCIA_HZ = 1D;
    private static final Double UPDATED_RED_FRECUENCIA_HZ = 2D;
    private static final Double SMALLER_RED_FRECUENCIA_HZ = 1D - 1D;

    private static final String DEFAULT_MODO_OPERACION = "AAAAAAAAAA";
    private static final String UPDATED_MODO_OPERACION = "BBBBBBBBBB";

    private static final Integer DEFAULT_CODIGO_FALLO = 1;
    private static final Integer UPDATED_CODIGO_FALLO = 2;
    private static final Integer SMALLER_CODIGO_FALLO = 1 - 1;

    private static final Double DEFAULT_EFICIENCIA_PCT = 1D;
    private static final Double UPDATED_EFICIENCIA_PCT = 2D;
    private static final Double SMALLER_EFICIENCIA_PCT = 1D - 1D;

    private static final Double DEFAULT_CONSUMO_RED_W = 1D;
    private static final Double UPDATED_CONSUMO_RED_W = 2D;
    private static final Double SMALLER_CONSUMO_RED_W = 1D - 1D;

    private static final Boolean DEFAULT_BATERIA_CARGANDO = false;
    private static final Boolean UPDATED_BATERIA_CARGANDO = true;

    private static final Boolean DEFAULT_PV_ACTIVO = false;
    private static final Boolean UPDATED_PV_ACTIVO = true;

    private static final Boolean DEFAULT_RED_ACTIVA = false;
    private static final Boolean UPDATED_RED_ACTIVA = true;

    private static final Boolean DEFAULT_CRC_VALIDO = false;
    private static final Boolean UPDATED_CRC_VALIDO = true;

    private static final Integer DEFAULT_LATENCIA_MS = 1;
    private static final Integer UPDATED_LATENCIA_MS = 2;
    private static final Integer SMALLER_LATENCIA_MS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/dato-inversors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DatoInversorRepository datoInversorRepository;

    @Mock
    private DatoInversorRepository datoInversorRepositoryMock;

    @Autowired
    private DatoInversorMapper datoInversorMapper;

    @Mock
    private DatoInversorService datoInversorServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDatoInversorMockMvc;

    private DatoInversor datoInversor;

    private DatoInversor insertedDatoInversor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DatoInversor createEntity() {
        return new DatoInversor()
            .tiempo(DEFAULT_TIEMPO)
            .bateriaVoltajeV(DEFAULT_BATERIA_VOLTAJE_V)
            .bateriaCorrienteA(DEFAULT_BATERIA_CORRIENTE_A)
            .bateriaPotenciaW(DEFAULT_BATERIA_POTENCIA_W)
            .pvVoltajeV(DEFAULT_PV_VOLTAJE_V)
            .pvPotenciaW(DEFAULT_PV_POTENCIA_W)
            .salidaVoltajeV(DEFAULT_SALIDA_VOLTAJE_V)
            .salidaPotenciaW(DEFAULT_SALIDA_POTENCIA_W)
            .salidaAparenteVa(DEFAULT_SALIDA_APARENTE_VA)
            .cargaPorcentaje(DEFAULT_CARGA_PORCENTAJE)
            .redVoltajeV(DEFAULT_RED_VOLTAJE_V)
            .redFrecuenciaHz(DEFAULT_RED_FRECUENCIA_HZ)
            .modoOperacion(DEFAULT_MODO_OPERACION)
            .codigoFallo(DEFAULT_CODIGO_FALLO)
            .eficienciaPct(DEFAULT_EFICIENCIA_PCT)
            .consumoRedW(DEFAULT_CONSUMO_RED_W)
            .bateriaCargando(DEFAULT_BATERIA_CARGANDO)
            .pvActivo(DEFAULT_PV_ACTIVO)
            .redActiva(DEFAULT_RED_ACTIVA)
            .crcValido(DEFAULT_CRC_VALIDO)
            .latenciaMs(DEFAULT_LATENCIA_MS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DatoInversor createUpdatedEntity() {
        return new DatoInversor()
            .tiempo(UPDATED_TIEMPO)
            .bateriaVoltajeV(UPDATED_BATERIA_VOLTAJE_V)
            .bateriaCorrienteA(UPDATED_BATERIA_CORRIENTE_A)
            .bateriaPotenciaW(UPDATED_BATERIA_POTENCIA_W)
            .pvVoltajeV(UPDATED_PV_VOLTAJE_V)
            .pvPotenciaW(UPDATED_PV_POTENCIA_W)
            .salidaVoltajeV(UPDATED_SALIDA_VOLTAJE_V)
            .salidaPotenciaW(UPDATED_SALIDA_POTENCIA_W)
            .salidaAparenteVa(UPDATED_SALIDA_APARENTE_VA)
            .cargaPorcentaje(UPDATED_CARGA_PORCENTAJE)
            .redVoltajeV(UPDATED_RED_VOLTAJE_V)
            .redFrecuenciaHz(UPDATED_RED_FRECUENCIA_HZ)
            .modoOperacion(UPDATED_MODO_OPERACION)
            .codigoFallo(UPDATED_CODIGO_FALLO)
            .eficienciaPct(UPDATED_EFICIENCIA_PCT)
            .consumoRedW(UPDATED_CONSUMO_RED_W)
            .bateriaCargando(UPDATED_BATERIA_CARGANDO)
            .pvActivo(UPDATED_PV_ACTIVO)
            .redActiva(UPDATED_RED_ACTIVA)
            .crcValido(UPDATED_CRC_VALIDO)
            .latenciaMs(UPDATED_LATENCIA_MS);
    }

    @BeforeEach
    void initTest() {
        datoInversor = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDatoInversor != null) {
            datoInversorRepository.delete(insertedDatoInversor);
            insertedDatoInversor = null;
        }
    }

    @Test
    @Transactional
    void createDatoInversor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DatoInversor
        DatoInversorDTO datoInversorDTO = datoInversorMapper.toDto(datoInversor);
        var returnedDatoInversorDTO = om.readValue(
            restDatoInversorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(datoInversorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DatoInversorDTO.class
        );

        // Validate the DatoInversor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDatoInversor = datoInversorMapper.toEntity(returnedDatoInversorDTO);
        assertDatoInversorUpdatableFieldsEquals(returnedDatoInversor, getPersistedDatoInversor(returnedDatoInversor));

        insertedDatoInversor = returnedDatoInversor;
    }

    @Test
    @Transactional
    void createDatoInversorWithExistingId() throws Exception {
        // Create the DatoInversor with an existing ID
        datoInversor.setId(1L);
        DatoInversorDTO datoInversorDTO = datoInversorMapper.toDto(datoInversor);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDatoInversorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(datoInversorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DatoInversor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTiempoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        datoInversor.setTiempo(null);

        // Create the DatoInversor, which fails.
        DatoInversorDTO datoInversorDTO = datoInversorMapper.toDto(datoInversor);

        restDatoInversorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(datoInversorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDatoInversors() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList
        restDatoInversorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(datoInversor.getId().intValue())))
            .andExpect(jsonPath("$.[*].tiempo").value(hasItem(DEFAULT_TIEMPO.toString())))
            .andExpect(jsonPath("$.[*].bateriaVoltajeV").value(hasItem(DEFAULT_BATERIA_VOLTAJE_V)))
            .andExpect(jsonPath("$.[*].bateriaCorrienteA").value(hasItem(DEFAULT_BATERIA_CORRIENTE_A)))
            .andExpect(jsonPath("$.[*].bateriaPotenciaW").value(hasItem(DEFAULT_BATERIA_POTENCIA_W)))
            .andExpect(jsonPath("$.[*].pvVoltajeV").value(hasItem(DEFAULT_PV_VOLTAJE_V)))
            .andExpect(jsonPath("$.[*].pvPotenciaW").value(hasItem(DEFAULT_PV_POTENCIA_W)))
            .andExpect(jsonPath("$.[*].salidaVoltajeV").value(hasItem(DEFAULT_SALIDA_VOLTAJE_V)))
            .andExpect(jsonPath("$.[*].salidaPotenciaW").value(hasItem(DEFAULT_SALIDA_POTENCIA_W)))
            .andExpect(jsonPath("$.[*].salidaAparenteVa").value(hasItem(DEFAULT_SALIDA_APARENTE_VA)))
            .andExpect(jsonPath("$.[*].cargaPorcentaje").value(hasItem(DEFAULT_CARGA_PORCENTAJE)))
            .andExpect(jsonPath("$.[*].redVoltajeV").value(hasItem(DEFAULT_RED_VOLTAJE_V)))
            .andExpect(jsonPath("$.[*].redFrecuenciaHz").value(hasItem(DEFAULT_RED_FRECUENCIA_HZ)))
            .andExpect(jsonPath("$.[*].modoOperacion").value(hasItem(DEFAULT_MODO_OPERACION)))
            .andExpect(jsonPath("$.[*].codigoFallo").value(hasItem(DEFAULT_CODIGO_FALLO)))
            .andExpect(jsonPath("$.[*].eficienciaPct").value(hasItem(DEFAULT_EFICIENCIA_PCT)))
            .andExpect(jsonPath("$.[*].consumoRedW").value(hasItem(DEFAULT_CONSUMO_RED_W)))
            .andExpect(jsonPath("$.[*].bateriaCargando").value(hasItem(DEFAULT_BATERIA_CARGANDO)))
            .andExpect(jsonPath("$.[*].pvActivo").value(hasItem(DEFAULT_PV_ACTIVO)))
            .andExpect(jsonPath("$.[*].redActiva").value(hasItem(DEFAULT_RED_ACTIVA)))
            .andExpect(jsonPath("$.[*].crcValido").value(hasItem(DEFAULT_CRC_VALIDO)))
            .andExpect(jsonPath("$.[*].latenciaMs").value(hasItem(DEFAULT_LATENCIA_MS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDatoInversorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(datoInversorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDatoInversorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(datoInversorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDatoInversorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(datoInversorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDatoInversorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(datoInversorRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDatoInversor() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get the datoInversor
        restDatoInversorMockMvc
            .perform(get(ENTITY_API_URL_ID, datoInversor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(datoInversor.getId().intValue()))
            .andExpect(jsonPath("$.tiempo").value(DEFAULT_TIEMPO.toString()))
            .andExpect(jsonPath("$.bateriaVoltajeV").value(DEFAULT_BATERIA_VOLTAJE_V))
            .andExpect(jsonPath("$.bateriaCorrienteA").value(DEFAULT_BATERIA_CORRIENTE_A))
            .andExpect(jsonPath("$.bateriaPotenciaW").value(DEFAULT_BATERIA_POTENCIA_W))
            .andExpect(jsonPath("$.pvVoltajeV").value(DEFAULT_PV_VOLTAJE_V))
            .andExpect(jsonPath("$.pvPotenciaW").value(DEFAULT_PV_POTENCIA_W))
            .andExpect(jsonPath("$.salidaVoltajeV").value(DEFAULT_SALIDA_VOLTAJE_V))
            .andExpect(jsonPath("$.salidaPotenciaW").value(DEFAULT_SALIDA_POTENCIA_W))
            .andExpect(jsonPath("$.salidaAparenteVa").value(DEFAULT_SALIDA_APARENTE_VA))
            .andExpect(jsonPath("$.cargaPorcentaje").value(DEFAULT_CARGA_PORCENTAJE))
            .andExpect(jsonPath("$.redVoltajeV").value(DEFAULT_RED_VOLTAJE_V))
            .andExpect(jsonPath("$.redFrecuenciaHz").value(DEFAULT_RED_FRECUENCIA_HZ))
            .andExpect(jsonPath("$.modoOperacion").value(DEFAULT_MODO_OPERACION))
            .andExpect(jsonPath("$.codigoFallo").value(DEFAULT_CODIGO_FALLO))
            .andExpect(jsonPath("$.eficienciaPct").value(DEFAULT_EFICIENCIA_PCT))
            .andExpect(jsonPath("$.consumoRedW").value(DEFAULT_CONSUMO_RED_W))
            .andExpect(jsonPath("$.bateriaCargando").value(DEFAULT_BATERIA_CARGANDO))
            .andExpect(jsonPath("$.pvActivo").value(DEFAULT_PV_ACTIVO))
            .andExpect(jsonPath("$.redActiva").value(DEFAULT_RED_ACTIVA))
            .andExpect(jsonPath("$.crcValido").value(DEFAULT_CRC_VALIDO))
            .andExpect(jsonPath("$.latenciaMs").value(DEFAULT_LATENCIA_MS));
    }

    @Test
    @Transactional
    void getDatoInversorsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        Long id = datoInversor.getId();

        defaultDatoInversorFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDatoInversorFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDatoInversorFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDatoInversorsByTiempoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where tiempo equals to
        defaultDatoInversorFiltering("tiempo.equals=" + DEFAULT_TIEMPO, "tiempo.equals=" + UPDATED_TIEMPO);
    }

    @Test
    @Transactional
    void getAllDatoInversorsByTiempoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where tiempo in
        defaultDatoInversorFiltering("tiempo.in=" + DEFAULT_TIEMPO + "," + UPDATED_TIEMPO, "tiempo.in=" + UPDATED_TIEMPO);
    }

    @Test
    @Transactional
    void getAllDatoInversorsByTiempoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where tiempo is not null
        defaultDatoInversorFiltering("tiempo.specified=true", "tiempo.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoInversorsByBateriaVoltajeVIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where bateriaVoltajeV equals to
        defaultDatoInversorFiltering(
            "bateriaVoltajeV.equals=" + DEFAULT_BATERIA_VOLTAJE_V,
            "bateriaVoltajeV.equals=" + UPDATED_BATERIA_VOLTAJE_V
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByBateriaVoltajeVIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where bateriaVoltajeV in
        defaultDatoInversorFiltering(
            "bateriaVoltajeV.in=" + DEFAULT_BATERIA_VOLTAJE_V + "," + UPDATED_BATERIA_VOLTAJE_V,
            "bateriaVoltajeV.in=" + UPDATED_BATERIA_VOLTAJE_V
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByBateriaVoltajeVIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where bateriaVoltajeV is not null
        defaultDatoInversorFiltering("bateriaVoltajeV.specified=true", "bateriaVoltajeV.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoInversorsByBateriaVoltajeVIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where bateriaVoltajeV is greater than or equal to
        defaultDatoInversorFiltering(
            "bateriaVoltajeV.greaterThanOrEqual=" + DEFAULT_BATERIA_VOLTAJE_V,
            "bateriaVoltajeV.greaterThanOrEqual=" + UPDATED_BATERIA_VOLTAJE_V
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByBateriaVoltajeVIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where bateriaVoltajeV is less than or equal to
        defaultDatoInversorFiltering(
            "bateriaVoltajeV.lessThanOrEqual=" + DEFAULT_BATERIA_VOLTAJE_V,
            "bateriaVoltajeV.lessThanOrEqual=" + SMALLER_BATERIA_VOLTAJE_V
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByBateriaVoltajeVIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where bateriaVoltajeV is less than
        defaultDatoInversorFiltering(
            "bateriaVoltajeV.lessThan=" + UPDATED_BATERIA_VOLTAJE_V,
            "bateriaVoltajeV.lessThan=" + DEFAULT_BATERIA_VOLTAJE_V
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByBateriaVoltajeVIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where bateriaVoltajeV is greater than
        defaultDatoInversorFiltering(
            "bateriaVoltajeV.greaterThan=" + SMALLER_BATERIA_VOLTAJE_V,
            "bateriaVoltajeV.greaterThan=" + DEFAULT_BATERIA_VOLTAJE_V
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByBateriaCorrienteAIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where bateriaCorrienteA equals to
        defaultDatoInversorFiltering(
            "bateriaCorrienteA.equals=" + DEFAULT_BATERIA_CORRIENTE_A,
            "bateriaCorrienteA.equals=" + UPDATED_BATERIA_CORRIENTE_A
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByBateriaCorrienteAIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where bateriaCorrienteA in
        defaultDatoInversorFiltering(
            "bateriaCorrienteA.in=" + DEFAULT_BATERIA_CORRIENTE_A + "," + UPDATED_BATERIA_CORRIENTE_A,
            "bateriaCorrienteA.in=" + UPDATED_BATERIA_CORRIENTE_A
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByBateriaCorrienteAIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where bateriaCorrienteA is not null
        defaultDatoInversorFiltering("bateriaCorrienteA.specified=true", "bateriaCorrienteA.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoInversorsByBateriaCorrienteAIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where bateriaCorrienteA is greater than or equal to
        defaultDatoInversorFiltering(
            "bateriaCorrienteA.greaterThanOrEqual=" + DEFAULT_BATERIA_CORRIENTE_A,
            "bateriaCorrienteA.greaterThanOrEqual=" + UPDATED_BATERIA_CORRIENTE_A
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByBateriaCorrienteAIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where bateriaCorrienteA is less than or equal to
        defaultDatoInversorFiltering(
            "bateriaCorrienteA.lessThanOrEqual=" + DEFAULT_BATERIA_CORRIENTE_A,
            "bateriaCorrienteA.lessThanOrEqual=" + SMALLER_BATERIA_CORRIENTE_A
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByBateriaCorrienteAIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where bateriaCorrienteA is less than
        defaultDatoInversorFiltering(
            "bateriaCorrienteA.lessThan=" + UPDATED_BATERIA_CORRIENTE_A,
            "bateriaCorrienteA.lessThan=" + DEFAULT_BATERIA_CORRIENTE_A
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByBateriaCorrienteAIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where bateriaCorrienteA is greater than
        defaultDatoInversorFiltering(
            "bateriaCorrienteA.greaterThan=" + SMALLER_BATERIA_CORRIENTE_A,
            "bateriaCorrienteA.greaterThan=" + DEFAULT_BATERIA_CORRIENTE_A
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByBateriaPotenciaWIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where bateriaPotenciaW equals to
        defaultDatoInversorFiltering(
            "bateriaPotenciaW.equals=" + DEFAULT_BATERIA_POTENCIA_W,
            "bateriaPotenciaW.equals=" + UPDATED_BATERIA_POTENCIA_W
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByBateriaPotenciaWIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where bateriaPotenciaW in
        defaultDatoInversorFiltering(
            "bateriaPotenciaW.in=" + DEFAULT_BATERIA_POTENCIA_W + "," + UPDATED_BATERIA_POTENCIA_W,
            "bateriaPotenciaW.in=" + UPDATED_BATERIA_POTENCIA_W
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByBateriaPotenciaWIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where bateriaPotenciaW is not null
        defaultDatoInversorFiltering("bateriaPotenciaW.specified=true", "bateriaPotenciaW.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoInversorsByBateriaPotenciaWIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where bateriaPotenciaW is greater than or equal to
        defaultDatoInversorFiltering(
            "bateriaPotenciaW.greaterThanOrEqual=" + DEFAULT_BATERIA_POTENCIA_W,
            "bateriaPotenciaW.greaterThanOrEqual=" + UPDATED_BATERIA_POTENCIA_W
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByBateriaPotenciaWIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where bateriaPotenciaW is less than or equal to
        defaultDatoInversorFiltering(
            "bateriaPotenciaW.lessThanOrEqual=" + DEFAULT_BATERIA_POTENCIA_W,
            "bateriaPotenciaW.lessThanOrEqual=" + SMALLER_BATERIA_POTENCIA_W
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByBateriaPotenciaWIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where bateriaPotenciaW is less than
        defaultDatoInversorFiltering(
            "bateriaPotenciaW.lessThan=" + UPDATED_BATERIA_POTENCIA_W,
            "bateriaPotenciaW.lessThan=" + DEFAULT_BATERIA_POTENCIA_W
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByBateriaPotenciaWIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where bateriaPotenciaW is greater than
        defaultDatoInversorFiltering(
            "bateriaPotenciaW.greaterThan=" + SMALLER_BATERIA_POTENCIA_W,
            "bateriaPotenciaW.greaterThan=" + DEFAULT_BATERIA_POTENCIA_W
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByPvVoltajeVIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where pvVoltajeV equals to
        defaultDatoInversorFiltering("pvVoltajeV.equals=" + DEFAULT_PV_VOLTAJE_V, "pvVoltajeV.equals=" + UPDATED_PV_VOLTAJE_V);
    }

    @Test
    @Transactional
    void getAllDatoInversorsByPvVoltajeVIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where pvVoltajeV in
        defaultDatoInversorFiltering(
            "pvVoltajeV.in=" + DEFAULT_PV_VOLTAJE_V + "," + UPDATED_PV_VOLTAJE_V,
            "pvVoltajeV.in=" + UPDATED_PV_VOLTAJE_V
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByPvVoltajeVIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where pvVoltajeV is not null
        defaultDatoInversorFiltering("pvVoltajeV.specified=true", "pvVoltajeV.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoInversorsByPvVoltajeVIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where pvVoltajeV is greater than or equal to
        defaultDatoInversorFiltering(
            "pvVoltajeV.greaterThanOrEqual=" + DEFAULT_PV_VOLTAJE_V,
            "pvVoltajeV.greaterThanOrEqual=" + UPDATED_PV_VOLTAJE_V
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByPvVoltajeVIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where pvVoltajeV is less than or equal to
        defaultDatoInversorFiltering(
            "pvVoltajeV.lessThanOrEqual=" + DEFAULT_PV_VOLTAJE_V,
            "pvVoltajeV.lessThanOrEqual=" + SMALLER_PV_VOLTAJE_V
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByPvVoltajeVIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where pvVoltajeV is less than
        defaultDatoInversorFiltering("pvVoltajeV.lessThan=" + UPDATED_PV_VOLTAJE_V, "pvVoltajeV.lessThan=" + DEFAULT_PV_VOLTAJE_V);
    }

    @Test
    @Transactional
    void getAllDatoInversorsByPvVoltajeVIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where pvVoltajeV is greater than
        defaultDatoInversorFiltering("pvVoltajeV.greaterThan=" + SMALLER_PV_VOLTAJE_V, "pvVoltajeV.greaterThan=" + DEFAULT_PV_VOLTAJE_V);
    }

    @Test
    @Transactional
    void getAllDatoInversorsByPvPotenciaWIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where pvPotenciaW equals to
        defaultDatoInversorFiltering("pvPotenciaW.equals=" + DEFAULT_PV_POTENCIA_W, "pvPotenciaW.equals=" + UPDATED_PV_POTENCIA_W);
    }

    @Test
    @Transactional
    void getAllDatoInversorsByPvPotenciaWIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where pvPotenciaW in
        defaultDatoInversorFiltering(
            "pvPotenciaW.in=" + DEFAULT_PV_POTENCIA_W + "," + UPDATED_PV_POTENCIA_W,
            "pvPotenciaW.in=" + UPDATED_PV_POTENCIA_W
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByPvPotenciaWIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where pvPotenciaW is not null
        defaultDatoInversorFiltering("pvPotenciaW.specified=true", "pvPotenciaW.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoInversorsByPvPotenciaWIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where pvPotenciaW is greater than or equal to
        defaultDatoInversorFiltering(
            "pvPotenciaW.greaterThanOrEqual=" + DEFAULT_PV_POTENCIA_W,
            "pvPotenciaW.greaterThanOrEqual=" + UPDATED_PV_POTENCIA_W
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByPvPotenciaWIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where pvPotenciaW is less than or equal to
        defaultDatoInversorFiltering(
            "pvPotenciaW.lessThanOrEqual=" + DEFAULT_PV_POTENCIA_W,
            "pvPotenciaW.lessThanOrEqual=" + SMALLER_PV_POTENCIA_W
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByPvPotenciaWIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where pvPotenciaW is less than
        defaultDatoInversorFiltering("pvPotenciaW.lessThan=" + UPDATED_PV_POTENCIA_W, "pvPotenciaW.lessThan=" + DEFAULT_PV_POTENCIA_W);
    }

    @Test
    @Transactional
    void getAllDatoInversorsByPvPotenciaWIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where pvPotenciaW is greater than
        defaultDatoInversorFiltering(
            "pvPotenciaW.greaterThan=" + SMALLER_PV_POTENCIA_W,
            "pvPotenciaW.greaterThan=" + DEFAULT_PV_POTENCIA_W
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsBySalidaVoltajeVIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where salidaVoltajeV equals to
        defaultDatoInversorFiltering(
            "salidaVoltajeV.equals=" + DEFAULT_SALIDA_VOLTAJE_V,
            "salidaVoltajeV.equals=" + UPDATED_SALIDA_VOLTAJE_V
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsBySalidaVoltajeVIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where salidaVoltajeV in
        defaultDatoInversorFiltering(
            "salidaVoltajeV.in=" + DEFAULT_SALIDA_VOLTAJE_V + "," + UPDATED_SALIDA_VOLTAJE_V,
            "salidaVoltajeV.in=" + UPDATED_SALIDA_VOLTAJE_V
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsBySalidaVoltajeVIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where salidaVoltajeV is not null
        defaultDatoInversorFiltering("salidaVoltajeV.specified=true", "salidaVoltajeV.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoInversorsBySalidaVoltajeVIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where salidaVoltajeV is greater than or equal to
        defaultDatoInversorFiltering(
            "salidaVoltajeV.greaterThanOrEqual=" + DEFAULT_SALIDA_VOLTAJE_V,
            "salidaVoltajeV.greaterThanOrEqual=" + UPDATED_SALIDA_VOLTAJE_V
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsBySalidaVoltajeVIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where salidaVoltajeV is less than or equal to
        defaultDatoInversorFiltering(
            "salidaVoltajeV.lessThanOrEqual=" + DEFAULT_SALIDA_VOLTAJE_V,
            "salidaVoltajeV.lessThanOrEqual=" + SMALLER_SALIDA_VOLTAJE_V
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsBySalidaVoltajeVIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where salidaVoltajeV is less than
        defaultDatoInversorFiltering(
            "salidaVoltajeV.lessThan=" + UPDATED_SALIDA_VOLTAJE_V,
            "salidaVoltajeV.lessThan=" + DEFAULT_SALIDA_VOLTAJE_V
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsBySalidaVoltajeVIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where salidaVoltajeV is greater than
        defaultDatoInversorFiltering(
            "salidaVoltajeV.greaterThan=" + SMALLER_SALIDA_VOLTAJE_V,
            "salidaVoltajeV.greaterThan=" + DEFAULT_SALIDA_VOLTAJE_V
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsBySalidaPotenciaWIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where salidaPotenciaW equals to
        defaultDatoInversorFiltering(
            "salidaPotenciaW.equals=" + DEFAULT_SALIDA_POTENCIA_W,
            "salidaPotenciaW.equals=" + UPDATED_SALIDA_POTENCIA_W
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsBySalidaPotenciaWIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where salidaPotenciaW in
        defaultDatoInversorFiltering(
            "salidaPotenciaW.in=" + DEFAULT_SALIDA_POTENCIA_W + "," + UPDATED_SALIDA_POTENCIA_W,
            "salidaPotenciaW.in=" + UPDATED_SALIDA_POTENCIA_W
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsBySalidaPotenciaWIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where salidaPotenciaW is not null
        defaultDatoInversorFiltering("salidaPotenciaW.specified=true", "salidaPotenciaW.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoInversorsBySalidaPotenciaWIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where salidaPotenciaW is greater than or equal to
        defaultDatoInversorFiltering(
            "salidaPotenciaW.greaterThanOrEqual=" + DEFAULT_SALIDA_POTENCIA_W,
            "salidaPotenciaW.greaterThanOrEqual=" + UPDATED_SALIDA_POTENCIA_W
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsBySalidaPotenciaWIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where salidaPotenciaW is less than or equal to
        defaultDatoInversorFiltering(
            "salidaPotenciaW.lessThanOrEqual=" + DEFAULT_SALIDA_POTENCIA_W,
            "salidaPotenciaW.lessThanOrEqual=" + SMALLER_SALIDA_POTENCIA_W
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsBySalidaPotenciaWIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where salidaPotenciaW is less than
        defaultDatoInversorFiltering(
            "salidaPotenciaW.lessThan=" + UPDATED_SALIDA_POTENCIA_W,
            "salidaPotenciaW.lessThan=" + DEFAULT_SALIDA_POTENCIA_W
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsBySalidaPotenciaWIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where salidaPotenciaW is greater than
        defaultDatoInversorFiltering(
            "salidaPotenciaW.greaterThan=" + SMALLER_SALIDA_POTENCIA_W,
            "salidaPotenciaW.greaterThan=" + DEFAULT_SALIDA_POTENCIA_W
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsBySalidaAparenteVaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where salidaAparenteVa equals to
        defaultDatoInversorFiltering(
            "salidaAparenteVa.equals=" + DEFAULT_SALIDA_APARENTE_VA,
            "salidaAparenteVa.equals=" + UPDATED_SALIDA_APARENTE_VA
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsBySalidaAparenteVaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where salidaAparenteVa in
        defaultDatoInversorFiltering(
            "salidaAparenteVa.in=" + DEFAULT_SALIDA_APARENTE_VA + "," + UPDATED_SALIDA_APARENTE_VA,
            "salidaAparenteVa.in=" + UPDATED_SALIDA_APARENTE_VA
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsBySalidaAparenteVaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where salidaAparenteVa is not null
        defaultDatoInversorFiltering("salidaAparenteVa.specified=true", "salidaAparenteVa.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoInversorsBySalidaAparenteVaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where salidaAparenteVa is greater than or equal to
        defaultDatoInversorFiltering(
            "salidaAparenteVa.greaterThanOrEqual=" + DEFAULT_SALIDA_APARENTE_VA,
            "salidaAparenteVa.greaterThanOrEqual=" + UPDATED_SALIDA_APARENTE_VA
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsBySalidaAparenteVaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where salidaAparenteVa is less than or equal to
        defaultDatoInversorFiltering(
            "salidaAparenteVa.lessThanOrEqual=" + DEFAULT_SALIDA_APARENTE_VA,
            "salidaAparenteVa.lessThanOrEqual=" + SMALLER_SALIDA_APARENTE_VA
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsBySalidaAparenteVaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where salidaAparenteVa is less than
        defaultDatoInversorFiltering(
            "salidaAparenteVa.lessThan=" + UPDATED_SALIDA_APARENTE_VA,
            "salidaAparenteVa.lessThan=" + DEFAULT_SALIDA_APARENTE_VA
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsBySalidaAparenteVaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where salidaAparenteVa is greater than
        defaultDatoInversorFiltering(
            "salidaAparenteVa.greaterThan=" + SMALLER_SALIDA_APARENTE_VA,
            "salidaAparenteVa.greaterThan=" + DEFAULT_SALIDA_APARENTE_VA
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByCargaPorcentajeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where cargaPorcentaje equals to
        defaultDatoInversorFiltering(
            "cargaPorcentaje.equals=" + DEFAULT_CARGA_PORCENTAJE,
            "cargaPorcentaje.equals=" + UPDATED_CARGA_PORCENTAJE
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByCargaPorcentajeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where cargaPorcentaje in
        defaultDatoInversorFiltering(
            "cargaPorcentaje.in=" + DEFAULT_CARGA_PORCENTAJE + "," + UPDATED_CARGA_PORCENTAJE,
            "cargaPorcentaje.in=" + UPDATED_CARGA_PORCENTAJE
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByCargaPorcentajeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where cargaPorcentaje is not null
        defaultDatoInversorFiltering("cargaPorcentaje.specified=true", "cargaPorcentaje.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoInversorsByCargaPorcentajeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where cargaPorcentaje is greater than or equal to
        defaultDatoInversorFiltering(
            "cargaPorcentaje.greaterThanOrEqual=" + DEFAULT_CARGA_PORCENTAJE,
            "cargaPorcentaje.greaterThanOrEqual=" + UPDATED_CARGA_PORCENTAJE
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByCargaPorcentajeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where cargaPorcentaje is less than or equal to
        defaultDatoInversorFiltering(
            "cargaPorcentaje.lessThanOrEqual=" + DEFAULT_CARGA_PORCENTAJE,
            "cargaPorcentaje.lessThanOrEqual=" + SMALLER_CARGA_PORCENTAJE
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByCargaPorcentajeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where cargaPorcentaje is less than
        defaultDatoInversorFiltering(
            "cargaPorcentaje.lessThan=" + UPDATED_CARGA_PORCENTAJE,
            "cargaPorcentaje.lessThan=" + DEFAULT_CARGA_PORCENTAJE
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByCargaPorcentajeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where cargaPorcentaje is greater than
        defaultDatoInversorFiltering(
            "cargaPorcentaje.greaterThan=" + SMALLER_CARGA_PORCENTAJE,
            "cargaPorcentaje.greaterThan=" + DEFAULT_CARGA_PORCENTAJE
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByRedVoltajeVIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where redVoltajeV equals to
        defaultDatoInversorFiltering("redVoltajeV.equals=" + DEFAULT_RED_VOLTAJE_V, "redVoltajeV.equals=" + UPDATED_RED_VOLTAJE_V);
    }

    @Test
    @Transactional
    void getAllDatoInversorsByRedVoltajeVIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where redVoltajeV in
        defaultDatoInversorFiltering(
            "redVoltajeV.in=" + DEFAULT_RED_VOLTAJE_V + "," + UPDATED_RED_VOLTAJE_V,
            "redVoltajeV.in=" + UPDATED_RED_VOLTAJE_V
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByRedVoltajeVIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where redVoltajeV is not null
        defaultDatoInversorFiltering("redVoltajeV.specified=true", "redVoltajeV.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoInversorsByRedVoltajeVIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where redVoltajeV is greater than or equal to
        defaultDatoInversorFiltering(
            "redVoltajeV.greaterThanOrEqual=" + DEFAULT_RED_VOLTAJE_V,
            "redVoltajeV.greaterThanOrEqual=" + UPDATED_RED_VOLTAJE_V
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByRedVoltajeVIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where redVoltajeV is less than or equal to
        defaultDatoInversorFiltering(
            "redVoltajeV.lessThanOrEqual=" + DEFAULT_RED_VOLTAJE_V,
            "redVoltajeV.lessThanOrEqual=" + SMALLER_RED_VOLTAJE_V
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByRedVoltajeVIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where redVoltajeV is less than
        defaultDatoInversorFiltering("redVoltajeV.lessThan=" + UPDATED_RED_VOLTAJE_V, "redVoltajeV.lessThan=" + DEFAULT_RED_VOLTAJE_V);
    }

    @Test
    @Transactional
    void getAllDatoInversorsByRedVoltajeVIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where redVoltajeV is greater than
        defaultDatoInversorFiltering(
            "redVoltajeV.greaterThan=" + SMALLER_RED_VOLTAJE_V,
            "redVoltajeV.greaterThan=" + DEFAULT_RED_VOLTAJE_V
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByRedFrecuenciaHzIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where redFrecuenciaHz equals to
        defaultDatoInversorFiltering(
            "redFrecuenciaHz.equals=" + DEFAULT_RED_FRECUENCIA_HZ,
            "redFrecuenciaHz.equals=" + UPDATED_RED_FRECUENCIA_HZ
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByRedFrecuenciaHzIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where redFrecuenciaHz in
        defaultDatoInversorFiltering(
            "redFrecuenciaHz.in=" + DEFAULT_RED_FRECUENCIA_HZ + "," + UPDATED_RED_FRECUENCIA_HZ,
            "redFrecuenciaHz.in=" + UPDATED_RED_FRECUENCIA_HZ
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByRedFrecuenciaHzIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where redFrecuenciaHz is not null
        defaultDatoInversorFiltering("redFrecuenciaHz.specified=true", "redFrecuenciaHz.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoInversorsByRedFrecuenciaHzIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where redFrecuenciaHz is greater than or equal to
        defaultDatoInversorFiltering(
            "redFrecuenciaHz.greaterThanOrEqual=" + DEFAULT_RED_FRECUENCIA_HZ,
            "redFrecuenciaHz.greaterThanOrEqual=" + UPDATED_RED_FRECUENCIA_HZ
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByRedFrecuenciaHzIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where redFrecuenciaHz is less than or equal to
        defaultDatoInversorFiltering(
            "redFrecuenciaHz.lessThanOrEqual=" + DEFAULT_RED_FRECUENCIA_HZ,
            "redFrecuenciaHz.lessThanOrEqual=" + SMALLER_RED_FRECUENCIA_HZ
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByRedFrecuenciaHzIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where redFrecuenciaHz is less than
        defaultDatoInversorFiltering(
            "redFrecuenciaHz.lessThan=" + UPDATED_RED_FRECUENCIA_HZ,
            "redFrecuenciaHz.lessThan=" + DEFAULT_RED_FRECUENCIA_HZ
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByRedFrecuenciaHzIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where redFrecuenciaHz is greater than
        defaultDatoInversorFiltering(
            "redFrecuenciaHz.greaterThan=" + SMALLER_RED_FRECUENCIA_HZ,
            "redFrecuenciaHz.greaterThan=" + DEFAULT_RED_FRECUENCIA_HZ
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByModoOperacionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where modoOperacion equals to
        defaultDatoInversorFiltering("modoOperacion.equals=" + DEFAULT_MODO_OPERACION, "modoOperacion.equals=" + UPDATED_MODO_OPERACION);
    }

    @Test
    @Transactional
    void getAllDatoInversorsByModoOperacionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where modoOperacion in
        defaultDatoInversorFiltering(
            "modoOperacion.in=" + DEFAULT_MODO_OPERACION + "," + UPDATED_MODO_OPERACION,
            "modoOperacion.in=" + UPDATED_MODO_OPERACION
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByModoOperacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where modoOperacion is not null
        defaultDatoInversorFiltering("modoOperacion.specified=true", "modoOperacion.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoInversorsByModoOperacionContainsSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where modoOperacion contains
        defaultDatoInversorFiltering(
            "modoOperacion.contains=" + DEFAULT_MODO_OPERACION,
            "modoOperacion.contains=" + UPDATED_MODO_OPERACION
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByModoOperacionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where modoOperacion does not contain
        defaultDatoInversorFiltering(
            "modoOperacion.doesNotContain=" + UPDATED_MODO_OPERACION,
            "modoOperacion.doesNotContain=" + DEFAULT_MODO_OPERACION
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByCodigoFalloIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where codigoFallo equals to
        defaultDatoInversorFiltering("codigoFallo.equals=" + DEFAULT_CODIGO_FALLO, "codigoFallo.equals=" + UPDATED_CODIGO_FALLO);
    }

    @Test
    @Transactional
    void getAllDatoInversorsByCodigoFalloIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where codigoFallo in
        defaultDatoInversorFiltering(
            "codigoFallo.in=" + DEFAULT_CODIGO_FALLO + "," + UPDATED_CODIGO_FALLO,
            "codigoFallo.in=" + UPDATED_CODIGO_FALLO
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByCodigoFalloIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where codigoFallo is not null
        defaultDatoInversorFiltering("codigoFallo.specified=true", "codigoFallo.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoInversorsByCodigoFalloIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where codigoFallo is greater than or equal to
        defaultDatoInversorFiltering(
            "codigoFallo.greaterThanOrEqual=" + DEFAULT_CODIGO_FALLO,
            "codigoFallo.greaterThanOrEqual=" + UPDATED_CODIGO_FALLO
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByCodigoFalloIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where codigoFallo is less than or equal to
        defaultDatoInversorFiltering(
            "codigoFallo.lessThanOrEqual=" + DEFAULT_CODIGO_FALLO,
            "codigoFallo.lessThanOrEqual=" + SMALLER_CODIGO_FALLO
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByCodigoFalloIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where codigoFallo is less than
        defaultDatoInversorFiltering("codigoFallo.lessThan=" + UPDATED_CODIGO_FALLO, "codigoFallo.lessThan=" + DEFAULT_CODIGO_FALLO);
    }

    @Test
    @Transactional
    void getAllDatoInversorsByCodigoFalloIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where codigoFallo is greater than
        defaultDatoInversorFiltering("codigoFallo.greaterThan=" + SMALLER_CODIGO_FALLO, "codigoFallo.greaterThan=" + DEFAULT_CODIGO_FALLO);
    }

    @Test
    @Transactional
    void getAllDatoInversorsByEficienciaPctIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where eficienciaPct equals to
        defaultDatoInversorFiltering("eficienciaPct.equals=" + DEFAULT_EFICIENCIA_PCT, "eficienciaPct.equals=" + UPDATED_EFICIENCIA_PCT);
    }

    @Test
    @Transactional
    void getAllDatoInversorsByEficienciaPctIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where eficienciaPct in
        defaultDatoInversorFiltering(
            "eficienciaPct.in=" + DEFAULT_EFICIENCIA_PCT + "," + UPDATED_EFICIENCIA_PCT,
            "eficienciaPct.in=" + UPDATED_EFICIENCIA_PCT
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByEficienciaPctIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where eficienciaPct is not null
        defaultDatoInversorFiltering("eficienciaPct.specified=true", "eficienciaPct.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoInversorsByEficienciaPctIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where eficienciaPct is greater than or equal to
        defaultDatoInversorFiltering(
            "eficienciaPct.greaterThanOrEqual=" + DEFAULT_EFICIENCIA_PCT,
            "eficienciaPct.greaterThanOrEqual=" + UPDATED_EFICIENCIA_PCT
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByEficienciaPctIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where eficienciaPct is less than or equal to
        defaultDatoInversorFiltering(
            "eficienciaPct.lessThanOrEqual=" + DEFAULT_EFICIENCIA_PCT,
            "eficienciaPct.lessThanOrEqual=" + SMALLER_EFICIENCIA_PCT
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByEficienciaPctIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where eficienciaPct is less than
        defaultDatoInversorFiltering(
            "eficienciaPct.lessThan=" + UPDATED_EFICIENCIA_PCT,
            "eficienciaPct.lessThan=" + DEFAULT_EFICIENCIA_PCT
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByEficienciaPctIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where eficienciaPct is greater than
        defaultDatoInversorFiltering(
            "eficienciaPct.greaterThan=" + SMALLER_EFICIENCIA_PCT,
            "eficienciaPct.greaterThan=" + DEFAULT_EFICIENCIA_PCT
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByConsumoRedWIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where consumoRedW equals to
        defaultDatoInversorFiltering("consumoRedW.equals=" + DEFAULT_CONSUMO_RED_W, "consumoRedW.equals=" + UPDATED_CONSUMO_RED_W);
    }

    @Test
    @Transactional
    void getAllDatoInversorsByConsumoRedWIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where consumoRedW in
        defaultDatoInversorFiltering(
            "consumoRedW.in=" + DEFAULT_CONSUMO_RED_W + "," + UPDATED_CONSUMO_RED_W,
            "consumoRedW.in=" + UPDATED_CONSUMO_RED_W
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByConsumoRedWIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where consumoRedW is not null
        defaultDatoInversorFiltering("consumoRedW.specified=true", "consumoRedW.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoInversorsByConsumoRedWIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where consumoRedW is greater than or equal to
        defaultDatoInversorFiltering(
            "consumoRedW.greaterThanOrEqual=" + DEFAULT_CONSUMO_RED_W,
            "consumoRedW.greaterThanOrEqual=" + UPDATED_CONSUMO_RED_W
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByConsumoRedWIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where consumoRedW is less than or equal to
        defaultDatoInversorFiltering(
            "consumoRedW.lessThanOrEqual=" + DEFAULT_CONSUMO_RED_W,
            "consumoRedW.lessThanOrEqual=" + SMALLER_CONSUMO_RED_W
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByConsumoRedWIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where consumoRedW is less than
        defaultDatoInversorFiltering("consumoRedW.lessThan=" + UPDATED_CONSUMO_RED_W, "consumoRedW.lessThan=" + DEFAULT_CONSUMO_RED_W);
    }

    @Test
    @Transactional
    void getAllDatoInversorsByConsumoRedWIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where consumoRedW is greater than
        defaultDatoInversorFiltering(
            "consumoRedW.greaterThan=" + SMALLER_CONSUMO_RED_W,
            "consumoRedW.greaterThan=" + DEFAULT_CONSUMO_RED_W
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByBateriaCargandoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where bateriaCargando equals to
        defaultDatoInversorFiltering(
            "bateriaCargando.equals=" + DEFAULT_BATERIA_CARGANDO,
            "bateriaCargando.equals=" + UPDATED_BATERIA_CARGANDO
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByBateriaCargandoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where bateriaCargando in
        defaultDatoInversorFiltering(
            "bateriaCargando.in=" + DEFAULT_BATERIA_CARGANDO + "," + UPDATED_BATERIA_CARGANDO,
            "bateriaCargando.in=" + UPDATED_BATERIA_CARGANDO
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByBateriaCargandoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where bateriaCargando is not null
        defaultDatoInversorFiltering("bateriaCargando.specified=true", "bateriaCargando.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoInversorsByPvActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where pvActivo equals to
        defaultDatoInversorFiltering("pvActivo.equals=" + DEFAULT_PV_ACTIVO, "pvActivo.equals=" + UPDATED_PV_ACTIVO);
    }

    @Test
    @Transactional
    void getAllDatoInversorsByPvActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where pvActivo in
        defaultDatoInversorFiltering("pvActivo.in=" + DEFAULT_PV_ACTIVO + "," + UPDATED_PV_ACTIVO, "pvActivo.in=" + UPDATED_PV_ACTIVO);
    }

    @Test
    @Transactional
    void getAllDatoInversorsByPvActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where pvActivo is not null
        defaultDatoInversorFiltering("pvActivo.specified=true", "pvActivo.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoInversorsByRedActivaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where redActiva equals to
        defaultDatoInversorFiltering("redActiva.equals=" + DEFAULT_RED_ACTIVA, "redActiva.equals=" + UPDATED_RED_ACTIVA);
    }

    @Test
    @Transactional
    void getAllDatoInversorsByRedActivaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where redActiva in
        defaultDatoInversorFiltering("redActiva.in=" + DEFAULT_RED_ACTIVA + "," + UPDATED_RED_ACTIVA, "redActiva.in=" + UPDATED_RED_ACTIVA);
    }

    @Test
    @Transactional
    void getAllDatoInversorsByRedActivaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where redActiva is not null
        defaultDatoInversorFiltering("redActiva.specified=true", "redActiva.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoInversorsByCrcValidoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where crcValido equals to
        defaultDatoInversorFiltering("crcValido.equals=" + DEFAULT_CRC_VALIDO, "crcValido.equals=" + UPDATED_CRC_VALIDO);
    }

    @Test
    @Transactional
    void getAllDatoInversorsByCrcValidoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where crcValido in
        defaultDatoInversorFiltering("crcValido.in=" + DEFAULT_CRC_VALIDO + "," + UPDATED_CRC_VALIDO, "crcValido.in=" + UPDATED_CRC_VALIDO);
    }

    @Test
    @Transactional
    void getAllDatoInversorsByCrcValidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where crcValido is not null
        defaultDatoInversorFiltering("crcValido.specified=true", "crcValido.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoInversorsByLatenciaMsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where latenciaMs equals to
        defaultDatoInversorFiltering("latenciaMs.equals=" + DEFAULT_LATENCIA_MS, "latenciaMs.equals=" + UPDATED_LATENCIA_MS);
    }

    @Test
    @Transactional
    void getAllDatoInversorsByLatenciaMsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where latenciaMs in
        defaultDatoInversorFiltering(
            "latenciaMs.in=" + DEFAULT_LATENCIA_MS + "," + UPDATED_LATENCIA_MS,
            "latenciaMs.in=" + UPDATED_LATENCIA_MS
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByLatenciaMsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where latenciaMs is not null
        defaultDatoInversorFiltering("latenciaMs.specified=true", "latenciaMs.specified=false");
    }

    @Test
    @Transactional
    void getAllDatoInversorsByLatenciaMsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where latenciaMs is greater than or equal to
        defaultDatoInversorFiltering(
            "latenciaMs.greaterThanOrEqual=" + DEFAULT_LATENCIA_MS,
            "latenciaMs.greaterThanOrEqual=" + UPDATED_LATENCIA_MS
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByLatenciaMsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where latenciaMs is less than or equal to
        defaultDatoInversorFiltering(
            "latenciaMs.lessThanOrEqual=" + DEFAULT_LATENCIA_MS,
            "latenciaMs.lessThanOrEqual=" + SMALLER_LATENCIA_MS
        );
    }

    @Test
    @Transactional
    void getAllDatoInversorsByLatenciaMsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where latenciaMs is less than
        defaultDatoInversorFiltering("latenciaMs.lessThan=" + UPDATED_LATENCIA_MS, "latenciaMs.lessThan=" + DEFAULT_LATENCIA_MS);
    }

    @Test
    @Transactional
    void getAllDatoInversorsByLatenciaMsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        // Get all the datoInversorList where latenciaMs is greater than
        defaultDatoInversorFiltering("latenciaMs.greaterThan=" + SMALLER_LATENCIA_MS, "latenciaMs.greaterThan=" + DEFAULT_LATENCIA_MS);
    }

    @Test
    @Transactional
    void getAllDatoInversorsByInversorIsEqualToSomething() throws Exception {
        Inversor inversor;
        if (TestUtil.findAll(em, Inversor.class).isEmpty()) {
            datoInversorRepository.saveAndFlush(datoInversor);
            inversor = InversorResourceIT.createEntity();
        } else {
            inversor = TestUtil.findAll(em, Inversor.class).get(0);
        }
        em.persist(inversor);
        em.flush();
        datoInversor.setInversor(inversor);
        datoInversorRepository.saveAndFlush(datoInversor);
        Long inversorId = inversor.getId();
        // Get all the datoInversorList where inversor equals to inversorId
        defaultDatoInversorShouldBeFound("inversorId.equals=" + inversorId);

        // Get all the datoInversorList where inversor equals to (inversorId + 1)
        defaultDatoInversorShouldNotBeFound("inversorId.equals=" + (inversorId + 1));
    }

    private void defaultDatoInversorFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDatoInversorShouldBeFound(shouldBeFound);
        defaultDatoInversorShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDatoInversorShouldBeFound(String filter) throws Exception {
        restDatoInversorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(datoInversor.getId().intValue())))
            .andExpect(jsonPath("$.[*].tiempo").value(hasItem(DEFAULT_TIEMPO.toString())))
            .andExpect(jsonPath("$.[*].bateriaVoltajeV").value(hasItem(DEFAULT_BATERIA_VOLTAJE_V)))
            .andExpect(jsonPath("$.[*].bateriaCorrienteA").value(hasItem(DEFAULT_BATERIA_CORRIENTE_A)))
            .andExpect(jsonPath("$.[*].bateriaPotenciaW").value(hasItem(DEFAULT_BATERIA_POTENCIA_W)))
            .andExpect(jsonPath("$.[*].pvVoltajeV").value(hasItem(DEFAULT_PV_VOLTAJE_V)))
            .andExpect(jsonPath("$.[*].pvPotenciaW").value(hasItem(DEFAULT_PV_POTENCIA_W)))
            .andExpect(jsonPath("$.[*].salidaVoltajeV").value(hasItem(DEFAULT_SALIDA_VOLTAJE_V)))
            .andExpect(jsonPath("$.[*].salidaPotenciaW").value(hasItem(DEFAULT_SALIDA_POTENCIA_W)))
            .andExpect(jsonPath("$.[*].salidaAparenteVa").value(hasItem(DEFAULT_SALIDA_APARENTE_VA)))
            .andExpect(jsonPath("$.[*].cargaPorcentaje").value(hasItem(DEFAULT_CARGA_PORCENTAJE)))
            .andExpect(jsonPath("$.[*].redVoltajeV").value(hasItem(DEFAULT_RED_VOLTAJE_V)))
            .andExpect(jsonPath("$.[*].redFrecuenciaHz").value(hasItem(DEFAULT_RED_FRECUENCIA_HZ)))
            .andExpect(jsonPath("$.[*].modoOperacion").value(hasItem(DEFAULT_MODO_OPERACION)))
            .andExpect(jsonPath("$.[*].codigoFallo").value(hasItem(DEFAULT_CODIGO_FALLO)))
            .andExpect(jsonPath("$.[*].eficienciaPct").value(hasItem(DEFAULT_EFICIENCIA_PCT)))
            .andExpect(jsonPath("$.[*].consumoRedW").value(hasItem(DEFAULT_CONSUMO_RED_W)))
            .andExpect(jsonPath("$.[*].bateriaCargando").value(hasItem(DEFAULT_BATERIA_CARGANDO)))
            .andExpect(jsonPath("$.[*].pvActivo").value(hasItem(DEFAULT_PV_ACTIVO)))
            .andExpect(jsonPath("$.[*].redActiva").value(hasItem(DEFAULT_RED_ACTIVA)))
            .andExpect(jsonPath("$.[*].crcValido").value(hasItem(DEFAULT_CRC_VALIDO)))
            .andExpect(jsonPath("$.[*].latenciaMs").value(hasItem(DEFAULT_LATENCIA_MS)));

        // Check, that the count call also returns 1
        restDatoInversorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDatoInversorShouldNotBeFound(String filter) throws Exception {
        restDatoInversorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDatoInversorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDatoInversor() throws Exception {
        // Get the datoInversor
        restDatoInversorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDatoInversor() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the datoInversor
        DatoInversor updatedDatoInversor = datoInversorRepository.findById(datoInversor.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDatoInversor are not directly saved in db
        em.detach(updatedDatoInversor);
        updatedDatoInversor
            .tiempo(UPDATED_TIEMPO)
            .bateriaVoltajeV(UPDATED_BATERIA_VOLTAJE_V)
            .bateriaCorrienteA(UPDATED_BATERIA_CORRIENTE_A)
            .bateriaPotenciaW(UPDATED_BATERIA_POTENCIA_W)
            .pvVoltajeV(UPDATED_PV_VOLTAJE_V)
            .pvPotenciaW(UPDATED_PV_POTENCIA_W)
            .salidaVoltajeV(UPDATED_SALIDA_VOLTAJE_V)
            .salidaPotenciaW(UPDATED_SALIDA_POTENCIA_W)
            .salidaAparenteVa(UPDATED_SALIDA_APARENTE_VA)
            .cargaPorcentaje(UPDATED_CARGA_PORCENTAJE)
            .redVoltajeV(UPDATED_RED_VOLTAJE_V)
            .redFrecuenciaHz(UPDATED_RED_FRECUENCIA_HZ)
            .modoOperacion(UPDATED_MODO_OPERACION)
            .codigoFallo(UPDATED_CODIGO_FALLO)
            .eficienciaPct(UPDATED_EFICIENCIA_PCT)
            .consumoRedW(UPDATED_CONSUMO_RED_W)
            .bateriaCargando(UPDATED_BATERIA_CARGANDO)
            .pvActivo(UPDATED_PV_ACTIVO)
            .redActiva(UPDATED_RED_ACTIVA)
            .crcValido(UPDATED_CRC_VALIDO)
            .latenciaMs(UPDATED_LATENCIA_MS);
        DatoInversorDTO datoInversorDTO = datoInversorMapper.toDto(updatedDatoInversor);

        restDatoInversorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, datoInversorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(datoInversorDTO))
            )
            .andExpect(status().isOk());

        // Validate the DatoInversor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDatoInversorToMatchAllProperties(updatedDatoInversor);
    }

    @Test
    @Transactional
    void putNonExistingDatoInversor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        datoInversor.setId(longCount.incrementAndGet());

        // Create the DatoInversor
        DatoInversorDTO datoInversorDTO = datoInversorMapper.toDto(datoInversor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDatoInversorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, datoInversorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(datoInversorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DatoInversor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDatoInversor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        datoInversor.setId(longCount.incrementAndGet());

        // Create the DatoInversor
        DatoInversorDTO datoInversorDTO = datoInversorMapper.toDto(datoInversor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDatoInversorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(datoInversorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DatoInversor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDatoInversor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        datoInversor.setId(longCount.incrementAndGet());

        // Create the DatoInversor
        DatoInversorDTO datoInversorDTO = datoInversorMapper.toDto(datoInversor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDatoInversorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(datoInversorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DatoInversor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDatoInversorWithPatch() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the datoInversor using partial update
        DatoInversor partialUpdatedDatoInversor = new DatoInversor();
        partialUpdatedDatoInversor.setId(datoInversor.getId());

        partialUpdatedDatoInversor
            .tiempo(UPDATED_TIEMPO)
            .bateriaVoltajeV(UPDATED_BATERIA_VOLTAJE_V)
            .bateriaCorrienteA(UPDATED_BATERIA_CORRIENTE_A)
            .bateriaPotenciaW(UPDATED_BATERIA_POTENCIA_W)
            .pvPotenciaW(UPDATED_PV_POTENCIA_W)
            .salidaPotenciaW(UPDATED_SALIDA_POTENCIA_W)
            .modoOperacion(UPDATED_MODO_OPERACION)
            .codigoFallo(UPDATED_CODIGO_FALLO)
            .pvActivo(UPDATED_PV_ACTIVO);

        restDatoInversorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDatoInversor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDatoInversor))
            )
            .andExpect(status().isOk());

        // Validate the DatoInversor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDatoInversorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDatoInversor, datoInversor),
            getPersistedDatoInversor(datoInversor)
        );
    }

    @Test
    @Transactional
    void fullUpdateDatoInversorWithPatch() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the datoInversor using partial update
        DatoInversor partialUpdatedDatoInversor = new DatoInversor();
        partialUpdatedDatoInversor.setId(datoInversor.getId());

        partialUpdatedDatoInversor
            .tiempo(UPDATED_TIEMPO)
            .bateriaVoltajeV(UPDATED_BATERIA_VOLTAJE_V)
            .bateriaCorrienteA(UPDATED_BATERIA_CORRIENTE_A)
            .bateriaPotenciaW(UPDATED_BATERIA_POTENCIA_W)
            .pvVoltajeV(UPDATED_PV_VOLTAJE_V)
            .pvPotenciaW(UPDATED_PV_POTENCIA_W)
            .salidaVoltajeV(UPDATED_SALIDA_VOLTAJE_V)
            .salidaPotenciaW(UPDATED_SALIDA_POTENCIA_W)
            .salidaAparenteVa(UPDATED_SALIDA_APARENTE_VA)
            .cargaPorcentaje(UPDATED_CARGA_PORCENTAJE)
            .redVoltajeV(UPDATED_RED_VOLTAJE_V)
            .redFrecuenciaHz(UPDATED_RED_FRECUENCIA_HZ)
            .modoOperacion(UPDATED_MODO_OPERACION)
            .codigoFallo(UPDATED_CODIGO_FALLO)
            .eficienciaPct(UPDATED_EFICIENCIA_PCT)
            .consumoRedW(UPDATED_CONSUMO_RED_W)
            .bateriaCargando(UPDATED_BATERIA_CARGANDO)
            .pvActivo(UPDATED_PV_ACTIVO)
            .redActiva(UPDATED_RED_ACTIVA)
            .crcValido(UPDATED_CRC_VALIDO)
            .latenciaMs(UPDATED_LATENCIA_MS);

        restDatoInversorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDatoInversor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDatoInversor))
            )
            .andExpect(status().isOk());

        // Validate the DatoInversor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDatoInversorUpdatableFieldsEquals(partialUpdatedDatoInversor, getPersistedDatoInversor(partialUpdatedDatoInversor));
    }

    @Test
    @Transactional
    void patchNonExistingDatoInversor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        datoInversor.setId(longCount.incrementAndGet());

        // Create the DatoInversor
        DatoInversorDTO datoInversorDTO = datoInversorMapper.toDto(datoInversor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDatoInversorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, datoInversorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(datoInversorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DatoInversor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDatoInversor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        datoInversor.setId(longCount.incrementAndGet());

        // Create the DatoInversor
        DatoInversorDTO datoInversorDTO = datoInversorMapper.toDto(datoInversor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDatoInversorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(datoInversorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DatoInversor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDatoInversor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        datoInversor.setId(longCount.incrementAndGet());

        // Create the DatoInversor
        DatoInversorDTO datoInversorDTO = datoInversorMapper.toDto(datoInversor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDatoInversorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(datoInversorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DatoInversor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDatoInversor() throws Exception {
        // Initialize the database
        insertedDatoInversor = datoInversorRepository.saveAndFlush(datoInversor);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the datoInversor
        restDatoInversorMockMvc
            .perform(delete(ENTITY_API_URL_ID, datoInversor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return datoInversorRepository.count();
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

    protected DatoInversor getPersistedDatoInversor(DatoInversor datoInversor) {
        return datoInversorRepository.findById(datoInversor.getId()).orElseThrow();
    }

    protected void assertPersistedDatoInversorToMatchAllProperties(DatoInversor expectedDatoInversor) {
        assertDatoInversorAllPropertiesEquals(expectedDatoInversor, getPersistedDatoInversor(expectedDatoInversor));
    }

    protected void assertPersistedDatoInversorToMatchUpdatableProperties(DatoInversor expectedDatoInversor) {
        assertDatoInversorAllUpdatablePropertiesEquals(expectedDatoInversor, getPersistedDatoInversor(expectedDatoInversor));
    }
}
