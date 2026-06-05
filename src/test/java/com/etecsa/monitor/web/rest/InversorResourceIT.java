package com.etecsa.monitor.web.rest;

import static com.etecsa.monitor.domain.InversorAsserts.*;
import static com.etecsa.monitor.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.etecsa.monitor.IntegrationTest;
import com.etecsa.monitor.domain.GatewayTcp;
import com.etecsa.monitor.domain.Inversor;
import com.etecsa.monitor.domain.Inversor;
import com.etecsa.monitor.domain.Marca;
import com.etecsa.monitor.domain.ModeloInversor;
import com.etecsa.monitor.domain.Sitio;
import com.etecsa.monitor.domain.enumeration.EstadoDispositivo;
import com.etecsa.monitor.domain.enumeration.TipoConexion;
import com.etecsa.monitor.repository.InversorRepository;
import com.etecsa.monitor.service.InversorService;
import com.etecsa.monitor.service.dto.InversorDTO;
import com.etecsa.monitor.service.mapper.InversorMapper;
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
 * Integration tests for the {@link InversorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InversorResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_SERIE = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_SERIE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ES_MASTER = false;
    private static final Boolean UPDATED_ES_MASTER = true;

    private static final TipoConexion DEFAULT_TIPO_CONEXION = TipoConexion.SerialRs485;
    private static final TipoConexion UPDATED_TIPO_CONEXION = TipoConexion.TcpModbus;

    private static final String DEFAULT_PUERTO_SERIAL = "AAAAAAAAAA";
    private static final String UPDATED_PUERTO_SERIAL = "BBBBBBBBBB";

    private static final Integer DEFAULT_SLAVE_ID = 1;
    private static final Integer UPDATED_SLAVE_ID = 2;
    private static final Integer SMALLER_SLAVE_ID = 1 - 1;

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final EstadoDispositivo DEFAULT_ESTADO_DISPOSITIVO = EstadoDispositivo.Conectado;
    private static final EstadoDispositivo UPDATED_ESTADO_DISPOSITIVO = EstadoDispositivo.Error;

    private static final Instant DEFAULT_ULTIMA_LECTURA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ULTIMA_LECTURA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ULTIMO_ERROR = "AAAAAAAAAA";
    private static final String UPDATED_ULTIMO_ERROR = "BBBBBBBBBB";

    private static final Integer DEFAULT_PERIODO_MUESTREO_SEG = 1;
    private static final Integer UPDATED_PERIODO_MUESTREO_SEG = 2;
    private static final Integer SMALLER_PERIODO_MUESTREO_SEG = 1 - 1;

    private static final Instant DEFAULT_FECHA_REGISTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_REGISTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ULTIMA_CONEXION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ULTIMA_CONEXION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/inversors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InversorRepository inversorRepository;

    @Mock
    private InversorRepository inversorRepositoryMock;

    @Autowired
    private InversorMapper inversorMapper;

    @Mock
    private InversorService inversorServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInversorMockMvc;

    private Inversor inversor;

    private Inversor insertedInversor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inversor createEntity() {
        return new Inversor()
            .nombre(DEFAULT_NOMBRE)
            .numeroSerie(DEFAULT_NUMERO_SERIE)
            .esMaster(DEFAULT_ES_MASTER)
            .tipoConexion(DEFAULT_TIPO_CONEXION)
            .puertoSerial(DEFAULT_PUERTO_SERIAL)
            .slaveId(DEFAULT_SLAVE_ID)
            .activo(DEFAULT_ACTIVO)
            .estadoDispositivo(DEFAULT_ESTADO_DISPOSITIVO)
            .ultimaLectura(DEFAULT_ULTIMA_LECTURA)
            .ultimoError(DEFAULT_ULTIMO_ERROR)
            .periodoMuestreoSeg(DEFAULT_PERIODO_MUESTREO_SEG)
            .fechaRegistro(DEFAULT_FECHA_REGISTRO)
            .ultimaConexion(DEFAULT_ULTIMA_CONEXION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inversor createUpdatedEntity() {
        return new Inversor()
            .nombre(UPDATED_NOMBRE)
            .numeroSerie(UPDATED_NUMERO_SERIE)
            .esMaster(UPDATED_ES_MASTER)
            .tipoConexion(UPDATED_TIPO_CONEXION)
            .puertoSerial(UPDATED_PUERTO_SERIAL)
            .slaveId(UPDATED_SLAVE_ID)
            .activo(UPDATED_ACTIVO)
            .estadoDispositivo(UPDATED_ESTADO_DISPOSITIVO)
            .ultimaLectura(UPDATED_ULTIMA_LECTURA)
            .ultimoError(UPDATED_ULTIMO_ERROR)
            .periodoMuestreoSeg(UPDATED_PERIODO_MUESTREO_SEG)
            .fechaRegistro(UPDATED_FECHA_REGISTRO)
            .ultimaConexion(UPDATED_ULTIMA_CONEXION);
    }

    @BeforeEach
    void initTest() {
        inversor = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedInversor != null) {
            inversorRepository.delete(insertedInversor);
            insertedInversor = null;
        }
    }

    @Test
    @Transactional
    void createInversor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Inversor
        InversorDTO inversorDTO = inversorMapper.toDto(inversor);
        var returnedInversorDTO = om.readValue(
            restInversorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inversorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InversorDTO.class
        );

        // Validate the Inversor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInversor = inversorMapper.toEntity(returnedInversorDTO);
        assertInversorUpdatableFieldsEquals(returnedInversor, getPersistedInversor(returnedInversor));

        insertedInversor = returnedInversor;
    }

    @Test
    @Transactional
    void createInversorWithExistingId() throws Exception {
        // Create the Inversor with an existing ID
        inversor.setId(1L);
        InversorDTO inversorDTO = inversorMapper.toDto(inversor);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInversorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inversorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Inversor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inversor.setNombre(null);

        // Create the Inversor, which fails.
        InversorDTO inversorDTO = inversorMapper.toDto(inversor);

        restInversorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inversorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEsMasterIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inversor.setEsMaster(null);

        // Create the Inversor, which fails.
        InversorDTO inversorDTO = inversorMapper.toDto(inversor);

        restInversorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inversorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoConexionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inversor.setTipoConexion(null);

        // Create the Inversor, which fails.
        InversorDTO inversorDTO = inversorMapper.toDto(inversor);

        restInversorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inversorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSlaveIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inversor.setSlaveId(null);

        // Create the Inversor, which fails.
        InversorDTO inversorDTO = inversorMapper.toDto(inversor);

        restInversorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inversorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inversor.setActivo(null);

        // Create the Inversor, which fails.
        InversorDTO inversorDTO = inversorMapper.toDto(inversor);

        restInversorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inversorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaRegistroIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inversor.setFechaRegistro(null);

        // Create the Inversor, which fails.
        InversorDTO inversorDTO = inversorMapper.toDto(inversor);

        restInversorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inversorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInversors() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList
        restInversorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inversor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].numeroSerie").value(hasItem(DEFAULT_NUMERO_SERIE)))
            .andExpect(jsonPath("$.[*].esMaster").value(hasItem(DEFAULT_ES_MASTER)))
            .andExpect(jsonPath("$.[*].tipoConexion").value(hasItem(DEFAULT_TIPO_CONEXION.toString())))
            .andExpect(jsonPath("$.[*].puertoSerial").value(hasItem(DEFAULT_PUERTO_SERIAL)))
            .andExpect(jsonPath("$.[*].slaveId").value(hasItem(DEFAULT_SLAVE_ID)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].estadoDispositivo").value(hasItem(DEFAULT_ESTADO_DISPOSITIVO.toString())))
            .andExpect(jsonPath("$.[*].ultimaLectura").value(hasItem(DEFAULT_ULTIMA_LECTURA.toString())))
            .andExpect(jsonPath("$.[*].ultimoError").value(hasItem(DEFAULT_ULTIMO_ERROR)))
            .andExpect(jsonPath("$.[*].periodoMuestreoSeg").value(hasItem(DEFAULT_PERIODO_MUESTREO_SEG)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())))
            .andExpect(jsonPath("$.[*].ultimaConexion").value(hasItem(DEFAULT_ULTIMA_CONEXION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInversorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(inversorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInversorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(inversorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInversorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(inversorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInversorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(inversorRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInversor() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get the inversor
        restInversorMockMvc
            .perform(get(ENTITY_API_URL_ID, inversor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inversor.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.numeroSerie").value(DEFAULT_NUMERO_SERIE))
            .andExpect(jsonPath("$.esMaster").value(DEFAULT_ES_MASTER))
            .andExpect(jsonPath("$.tipoConexion").value(DEFAULT_TIPO_CONEXION.toString()))
            .andExpect(jsonPath("$.puertoSerial").value(DEFAULT_PUERTO_SERIAL))
            .andExpect(jsonPath("$.slaveId").value(DEFAULT_SLAVE_ID))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.estadoDispositivo").value(DEFAULT_ESTADO_DISPOSITIVO.toString()))
            .andExpect(jsonPath("$.ultimaLectura").value(DEFAULT_ULTIMA_LECTURA.toString()))
            .andExpect(jsonPath("$.ultimoError").value(DEFAULT_ULTIMO_ERROR))
            .andExpect(jsonPath("$.periodoMuestreoSeg").value(DEFAULT_PERIODO_MUESTREO_SEG))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()))
            .andExpect(jsonPath("$.ultimaConexion").value(DEFAULT_ULTIMA_CONEXION.toString()));
    }

    @Test
    @Transactional
    void getInversorsByIdFiltering() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        Long id = inversor.getId();

        defaultInversorFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInversorFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInversorFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInversorsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where nombre equals to
        defaultInversorFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllInversorsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where nombre in
        defaultInversorFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllInversorsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where nombre is not null
        defaultInversorFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllInversorsByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where nombre contains
        defaultInversorFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllInversorsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where nombre does not contain
        defaultInversorFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllInversorsByNumeroSerieIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where numeroSerie equals to
        defaultInversorFiltering("numeroSerie.equals=" + DEFAULT_NUMERO_SERIE, "numeroSerie.equals=" + UPDATED_NUMERO_SERIE);
    }

    @Test
    @Transactional
    void getAllInversorsByNumeroSerieIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where numeroSerie in
        defaultInversorFiltering(
            "numeroSerie.in=" + DEFAULT_NUMERO_SERIE + "," + UPDATED_NUMERO_SERIE,
            "numeroSerie.in=" + UPDATED_NUMERO_SERIE
        );
    }

    @Test
    @Transactional
    void getAllInversorsByNumeroSerieIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where numeroSerie is not null
        defaultInversorFiltering("numeroSerie.specified=true", "numeroSerie.specified=false");
    }

    @Test
    @Transactional
    void getAllInversorsByNumeroSerieContainsSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where numeroSerie contains
        defaultInversorFiltering("numeroSerie.contains=" + DEFAULT_NUMERO_SERIE, "numeroSerie.contains=" + UPDATED_NUMERO_SERIE);
    }

    @Test
    @Transactional
    void getAllInversorsByNumeroSerieNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where numeroSerie does not contain
        defaultInversorFiltering(
            "numeroSerie.doesNotContain=" + UPDATED_NUMERO_SERIE,
            "numeroSerie.doesNotContain=" + DEFAULT_NUMERO_SERIE
        );
    }

    @Test
    @Transactional
    void getAllInversorsByEsMasterIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where esMaster equals to
        defaultInversorFiltering("esMaster.equals=" + DEFAULT_ES_MASTER, "esMaster.equals=" + UPDATED_ES_MASTER);
    }

    @Test
    @Transactional
    void getAllInversorsByEsMasterIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where esMaster in
        defaultInversorFiltering("esMaster.in=" + DEFAULT_ES_MASTER + "," + UPDATED_ES_MASTER, "esMaster.in=" + UPDATED_ES_MASTER);
    }

    @Test
    @Transactional
    void getAllInversorsByEsMasterIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where esMaster is not null
        defaultInversorFiltering("esMaster.specified=true", "esMaster.specified=false");
    }

    @Test
    @Transactional
    void getAllInversorsByTipoConexionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where tipoConexion equals to
        defaultInversorFiltering("tipoConexion.equals=" + DEFAULT_TIPO_CONEXION, "tipoConexion.equals=" + UPDATED_TIPO_CONEXION);
    }

    @Test
    @Transactional
    void getAllInversorsByTipoConexionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where tipoConexion in
        defaultInversorFiltering(
            "tipoConexion.in=" + DEFAULT_TIPO_CONEXION + "," + UPDATED_TIPO_CONEXION,
            "tipoConexion.in=" + UPDATED_TIPO_CONEXION
        );
    }

    @Test
    @Transactional
    void getAllInversorsByTipoConexionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where tipoConexion is not null
        defaultInversorFiltering("tipoConexion.specified=true", "tipoConexion.specified=false");
    }

    @Test
    @Transactional
    void getAllInversorsByPuertoSerialIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where puertoSerial equals to
        defaultInversorFiltering("puertoSerial.equals=" + DEFAULT_PUERTO_SERIAL, "puertoSerial.equals=" + UPDATED_PUERTO_SERIAL);
    }

    @Test
    @Transactional
    void getAllInversorsByPuertoSerialIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where puertoSerial in
        defaultInversorFiltering(
            "puertoSerial.in=" + DEFAULT_PUERTO_SERIAL + "," + UPDATED_PUERTO_SERIAL,
            "puertoSerial.in=" + UPDATED_PUERTO_SERIAL
        );
    }

    @Test
    @Transactional
    void getAllInversorsByPuertoSerialIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where puertoSerial is not null
        defaultInversorFiltering("puertoSerial.specified=true", "puertoSerial.specified=false");
    }

    @Test
    @Transactional
    void getAllInversorsByPuertoSerialContainsSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where puertoSerial contains
        defaultInversorFiltering("puertoSerial.contains=" + DEFAULT_PUERTO_SERIAL, "puertoSerial.contains=" + UPDATED_PUERTO_SERIAL);
    }

    @Test
    @Transactional
    void getAllInversorsByPuertoSerialNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where puertoSerial does not contain
        defaultInversorFiltering(
            "puertoSerial.doesNotContain=" + UPDATED_PUERTO_SERIAL,
            "puertoSerial.doesNotContain=" + DEFAULT_PUERTO_SERIAL
        );
    }

    @Test
    @Transactional
    void getAllInversorsBySlaveIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where slaveId equals to
        defaultInversorFiltering("slaveId.equals=" + DEFAULT_SLAVE_ID, "slaveId.equals=" + UPDATED_SLAVE_ID);
    }

    @Test
    @Transactional
    void getAllInversorsBySlaveIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where slaveId in
        defaultInversorFiltering("slaveId.in=" + DEFAULT_SLAVE_ID + "," + UPDATED_SLAVE_ID, "slaveId.in=" + UPDATED_SLAVE_ID);
    }

    @Test
    @Transactional
    void getAllInversorsBySlaveIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where slaveId is not null
        defaultInversorFiltering("slaveId.specified=true", "slaveId.specified=false");
    }

    @Test
    @Transactional
    void getAllInversorsBySlaveIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where slaveId is greater than or equal to
        defaultInversorFiltering("slaveId.greaterThanOrEqual=" + DEFAULT_SLAVE_ID, "slaveId.greaterThanOrEqual=" + UPDATED_SLAVE_ID);
    }

    @Test
    @Transactional
    void getAllInversorsBySlaveIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where slaveId is less than or equal to
        defaultInversorFiltering("slaveId.lessThanOrEqual=" + DEFAULT_SLAVE_ID, "slaveId.lessThanOrEqual=" + SMALLER_SLAVE_ID);
    }

    @Test
    @Transactional
    void getAllInversorsBySlaveIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where slaveId is less than
        defaultInversorFiltering("slaveId.lessThan=" + UPDATED_SLAVE_ID, "slaveId.lessThan=" + DEFAULT_SLAVE_ID);
    }

    @Test
    @Transactional
    void getAllInversorsBySlaveIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where slaveId is greater than
        defaultInversorFiltering("slaveId.greaterThan=" + SMALLER_SLAVE_ID, "slaveId.greaterThan=" + DEFAULT_SLAVE_ID);
    }

    @Test
    @Transactional
    void getAllInversorsByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where activo equals to
        defaultInversorFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllInversorsByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where activo in
        defaultInversorFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllInversorsByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where activo is not null
        defaultInversorFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllInversorsByEstadoDispositivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where estadoDispositivo equals to
        defaultInversorFiltering(
            "estadoDispositivo.equals=" + DEFAULT_ESTADO_DISPOSITIVO,
            "estadoDispositivo.equals=" + UPDATED_ESTADO_DISPOSITIVO
        );
    }

    @Test
    @Transactional
    void getAllInversorsByEstadoDispositivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where estadoDispositivo in
        defaultInversorFiltering(
            "estadoDispositivo.in=" + DEFAULT_ESTADO_DISPOSITIVO + "," + UPDATED_ESTADO_DISPOSITIVO,
            "estadoDispositivo.in=" + UPDATED_ESTADO_DISPOSITIVO
        );
    }

    @Test
    @Transactional
    void getAllInversorsByEstadoDispositivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where estadoDispositivo is not null
        defaultInversorFiltering("estadoDispositivo.specified=true", "estadoDispositivo.specified=false");
    }

    @Test
    @Transactional
    void getAllInversorsByUltimaLecturaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where ultimaLectura equals to
        defaultInversorFiltering("ultimaLectura.equals=" + DEFAULT_ULTIMA_LECTURA, "ultimaLectura.equals=" + UPDATED_ULTIMA_LECTURA);
    }

    @Test
    @Transactional
    void getAllInversorsByUltimaLecturaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where ultimaLectura in
        defaultInversorFiltering(
            "ultimaLectura.in=" + DEFAULT_ULTIMA_LECTURA + "," + UPDATED_ULTIMA_LECTURA,
            "ultimaLectura.in=" + UPDATED_ULTIMA_LECTURA
        );
    }

    @Test
    @Transactional
    void getAllInversorsByUltimaLecturaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where ultimaLectura is not null
        defaultInversorFiltering("ultimaLectura.specified=true", "ultimaLectura.specified=false");
    }

    @Test
    @Transactional
    void getAllInversorsByUltimoErrorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where ultimoError equals to
        defaultInversorFiltering("ultimoError.equals=" + DEFAULT_ULTIMO_ERROR, "ultimoError.equals=" + UPDATED_ULTIMO_ERROR);
    }

    @Test
    @Transactional
    void getAllInversorsByUltimoErrorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where ultimoError in
        defaultInversorFiltering(
            "ultimoError.in=" + DEFAULT_ULTIMO_ERROR + "," + UPDATED_ULTIMO_ERROR,
            "ultimoError.in=" + UPDATED_ULTIMO_ERROR
        );
    }

    @Test
    @Transactional
    void getAllInversorsByUltimoErrorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where ultimoError is not null
        defaultInversorFiltering("ultimoError.specified=true", "ultimoError.specified=false");
    }

    @Test
    @Transactional
    void getAllInversorsByUltimoErrorContainsSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where ultimoError contains
        defaultInversorFiltering("ultimoError.contains=" + DEFAULT_ULTIMO_ERROR, "ultimoError.contains=" + UPDATED_ULTIMO_ERROR);
    }

    @Test
    @Transactional
    void getAllInversorsByUltimoErrorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where ultimoError does not contain
        defaultInversorFiltering(
            "ultimoError.doesNotContain=" + UPDATED_ULTIMO_ERROR,
            "ultimoError.doesNotContain=" + DEFAULT_ULTIMO_ERROR
        );
    }

    @Test
    @Transactional
    void getAllInversorsByPeriodoMuestreoSegIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where periodoMuestreoSeg equals to
        defaultInversorFiltering(
            "periodoMuestreoSeg.equals=" + DEFAULT_PERIODO_MUESTREO_SEG,
            "periodoMuestreoSeg.equals=" + UPDATED_PERIODO_MUESTREO_SEG
        );
    }

    @Test
    @Transactional
    void getAllInversorsByPeriodoMuestreoSegIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where periodoMuestreoSeg in
        defaultInversorFiltering(
            "periodoMuestreoSeg.in=" + DEFAULT_PERIODO_MUESTREO_SEG + "," + UPDATED_PERIODO_MUESTREO_SEG,
            "periodoMuestreoSeg.in=" + UPDATED_PERIODO_MUESTREO_SEG
        );
    }

    @Test
    @Transactional
    void getAllInversorsByPeriodoMuestreoSegIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where periodoMuestreoSeg is not null
        defaultInversorFiltering("periodoMuestreoSeg.specified=true", "periodoMuestreoSeg.specified=false");
    }

    @Test
    @Transactional
    void getAllInversorsByPeriodoMuestreoSegIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where periodoMuestreoSeg is greater than or equal to
        defaultInversorFiltering(
            "periodoMuestreoSeg.greaterThanOrEqual=" + DEFAULT_PERIODO_MUESTREO_SEG,
            "periodoMuestreoSeg.greaterThanOrEqual=" + UPDATED_PERIODO_MUESTREO_SEG
        );
    }

    @Test
    @Transactional
    void getAllInversorsByPeriodoMuestreoSegIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where periodoMuestreoSeg is less than or equal to
        defaultInversorFiltering(
            "periodoMuestreoSeg.lessThanOrEqual=" + DEFAULT_PERIODO_MUESTREO_SEG,
            "periodoMuestreoSeg.lessThanOrEqual=" + SMALLER_PERIODO_MUESTREO_SEG
        );
    }

    @Test
    @Transactional
    void getAllInversorsByPeriodoMuestreoSegIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where periodoMuestreoSeg is less than
        defaultInversorFiltering(
            "periodoMuestreoSeg.lessThan=" + UPDATED_PERIODO_MUESTREO_SEG,
            "periodoMuestreoSeg.lessThan=" + DEFAULT_PERIODO_MUESTREO_SEG
        );
    }

    @Test
    @Transactional
    void getAllInversorsByPeriodoMuestreoSegIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where periodoMuestreoSeg is greater than
        defaultInversorFiltering(
            "periodoMuestreoSeg.greaterThan=" + SMALLER_PERIODO_MUESTREO_SEG,
            "periodoMuestreoSeg.greaterThan=" + DEFAULT_PERIODO_MUESTREO_SEG
        );
    }

    @Test
    @Transactional
    void getAllInversorsByFechaRegistroIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where fechaRegistro equals to
        defaultInversorFiltering("fechaRegistro.equals=" + DEFAULT_FECHA_REGISTRO, "fechaRegistro.equals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllInversorsByFechaRegistroIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where fechaRegistro in
        defaultInversorFiltering(
            "fechaRegistro.in=" + DEFAULT_FECHA_REGISTRO + "," + UPDATED_FECHA_REGISTRO,
            "fechaRegistro.in=" + UPDATED_FECHA_REGISTRO
        );
    }

    @Test
    @Transactional
    void getAllInversorsByFechaRegistroIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where fechaRegistro is not null
        defaultInversorFiltering("fechaRegistro.specified=true", "fechaRegistro.specified=false");
    }

    @Test
    @Transactional
    void getAllInversorsByUltimaConexionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where ultimaConexion equals to
        defaultInversorFiltering("ultimaConexion.equals=" + DEFAULT_ULTIMA_CONEXION, "ultimaConexion.equals=" + UPDATED_ULTIMA_CONEXION);
    }

    @Test
    @Transactional
    void getAllInversorsByUltimaConexionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where ultimaConexion in
        defaultInversorFiltering(
            "ultimaConexion.in=" + DEFAULT_ULTIMA_CONEXION + "," + UPDATED_ULTIMA_CONEXION,
            "ultimaConexion.in=" + UPDATED_ULTIMA_CONEXION
        );
    }

    @Test
    @Transactional
    void getAllInversorsByUltimaConexionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        // Get all the inversorList where ultimaConexion is not null
        defaultInversorFiltering("ultimaConexion.specified=true", "ultimaConexion.specified=false");
    }

    @Test
    @Transactional
    void getAllInversorsBySitioIsEqualToSomething() throws Exception {
        Sitio sitio;
        if (TestUtil.findAll(em, Sitio.class).isEmpty()) {
            inversorRepository.saveAndFlush(inversor);
            sitio = SitioResourceIT.createEntity();
        } else {
            sitio = TestUtil.findAll(em, Sitio.class).get(0);
        }
        em.persist(sitio);
        em.flush();
        inversor.setSitio(sitio);
        inversorRepository.saveAndFlush(inversor);
        Long sitioId = sitio.getId();
        // Get all the inversorList where sitio equals to sitioId
        defaultInversorShouldBeFound("sitioId.equals=" + sitioId);

        // Get all the inversorList where sitio equals to (sitioId + 1)
        defaultInversorShouldNotBeFound("sitioId.equals=" + (sitioId + 1));
    }

    @Test
    @Transactional
    void getAllInversorsByMarcaIsEqualToSomething() throws Exception {
        Marca marca;
        if (TestUtil.findAll(em, Marca.class).isEmpty()) {
            inversorRepository.saveAndFlush(inversor);
            marca = MarcaResourceIT.createEntity();
        } else {
            marca = TestUtil.findAll(em, Marca.class).get(0);
        }
        em.persist(marca);
        em.flush();
        inversor.setMarca(marca);
        inversorRepository.saveAndFlush(inversor);
        Long marcaId = marca.getId();
        // Get all the inversorList where marca equals to marcaId
        defaultInversorShouldBeFound("marcaId.equals=" + marcaId);

        // Get all the inversorList where marca equals to (marcaId + 1)
        defaultInversorShouldNotBeFound("marcaId.equals=" + (marcaId + 1));
    }

    @Test
    @Transactional
    void getAllInversorsByModeloIsEqualToSomething() throws Exception {
        ModeloInversor modelo;
        if (TestUtil.findAll(em, ModeloInversor.class).isEmpty()) {
            inversorRepository.saveAndFlush(inversor);
            modelo = ModeloInversorResourceIT.createEntity();
        } else {
            modelo = TestUtil.findAll(em, ModeloInversor.class).get(0);
        }
        em.persist(modelo);
        em.flush();
        inversor.setModelo(modelo);
        inversorRepository.saveAndFlush(inversor);
        Long modeloId = modelo.getId();
        // Get all the inversorList where modelo equals to modeloId
        defaultInversorShouldBeFound("modeloId.equals=" + modeloId);

        // Get all the inversorList where modelo equals to (modeloId + 1)
        defaultInversorShouldNotBeFound("modeloId.equals=" + (modeloId + 1));
    }

    @Test
    @Transactional
    void getAllInversorsByGatewayIsEqualToSomething() throws Exception {
        GatewayTcp gateway;
        if (TestUtil.findAll(em, GatewayTcp.class).isEmpty()) {
            inversorRepository.saveAndFlush(inversor);
            gateway = GatewayTcpResourceIT.createEntity();
        } else {
            gateway = TestUtil.findAll(em, GatewayTcp.class).get(0);
        }
        em.persist(gateway);
        em.flush();
        inversor.setGateway(gateway);
        inversorRepository.saveAndFlush(inversor);
        Long gatewayId = gateway.getId();
        // Get all the inversorList where gateway equals to gatewayId
        defaultInversorShouldBeFound("gatewayId.equals=" + gatewayId);

        // Get all the inversorList where gateway equals to (gatewayId + 1)
        defaultInversorShouldNotBeFound("gatewayId.equals=" + (gatewayId + 1));
    }

    @Test
    @Transactional
    void getAllInversorsByMasterReferenciaIsEqualToSomething() throws Exception {
        Inversor masterReferencia;
        if (TestUtil.findAll(em, Inversor.class).isEmpty()) {
            inversorRepository.saveAndFlush(inversor);
            masterReferencia = InversorResourceIT.createEntity();
        } else {
            masterReferencia = TestUtil.findAll(em, Inversor.class).get(0);
        }
        em.persist(masterReferencia);
        em.flush();
        inversor.setMasterReferencia(masterReferencia);
        inversorRepository.saveAndFlush(inversor);
        Long masterReferenciaId = masterReferencia.getId();
        // Get all the inversorList where masterReferencia equals to masterReferenciaId
        defaultInversorShouldBeFound("masterReferenciaId.equals=" + masterReferenciaId);

        // Get all the inversorList where masterReferencia equals to (masterReferenciaId + 1)
        defaultInversorShouldNotBeFound("masterReferenciaId.equals=" + (masterReferenciaId + 1));
    }

    private void defaultInversorFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInversorShouldBeFound(shouldBeFound);
        defaultInversorShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInversorShouldBeFound(String filter) throws Exception {
        restInversorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inversor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].numeroSerie").value(hasItem(DEFAULT_NUMERO_SERIE)))
            .andExpect(jsonPath("$.[*].esMaster").value(hasItem(DEFAULT_ES_MASTER)))
            .andExpect(jsonPath("$.[*].tipoConexion").value(hasItem(DEFAULT_TIPO_CONEXION.toString())))
            .andExpect(jsonPath("$.[*].puertoSerial").value(hasItem(DEFAULT_PUERTO_SERIAL)))
            .andExpect(jsonPath("$.[*].slaveId").value(hasItem(DEFAULT_SLAVE_ID)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].estadoDispositivo").value(hasItem(DEFAULT_ESTADO_DISPOSITIVO.toString())))
            .andExpect(jsonPath("$.[*].ultimaLectura").value(hasItem(DEFAULT_ULTIMA_LECTURA.toString())))
            .andExpect(jsonPath("$.[*].ultimoError").value(hasItem(DEFAULT_ULTIMO_ERROR)))
            .andExpect(jsonPath("$.[*].periodoMuestreoSeg").value(hasItem(DEFAULT_PERIODO_MUESTREO_SEG)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())))
            .andExpect(jsonPath("$.[*].ultimaConexion").value(hasItem(DEFAULT_ULTIMA_CONEXION.toString())));

        // Check, that the count call also returns 1
        restInversorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInversorShouldNotBeFound(String filter) throws Exception {
        restInversorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInversorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInversor() throws Exception {
        // Get the inversor
        restInversorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInversor() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inversor
        Inversor updatedInversor = inversorRepository.findById(inversor.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInversor are not directly saved in db
        em.detach(updatedInversor);
        updatedInversor
            .nombre(UPDATED_NOMBRE)
            .numeroSerie(UPDATED_NUMERO_SERIE)
            .esMaster(UPDATED_ES_MASTER)
            .tipoConexion(UPDATED_TIPO_CONEXION)
            .puertoSerial(UPDATED_PUERTO_SERIAL)
            .slaveId(UPDATED_SLAVE_ID)
            .activo(UPDATED_ACTIVO)
            .estadoDispositivo(UPDATED_ESTADO_DISPOSITIVO)
            .ultimaLectura(UPDATED_ULTIMA_LECTURA)
            .ultimoError(UPDATED_ULTIMO_ERROR)
            .periodoMuestreoSeg(UPDATED_PERIODO_MUESTREO_SEG)
            .fechaRegistro(UPDATED_FECHA_REGISTRO)
            .ultimaConexion(UPDATED_ULTIMA_CONEXION);
        InversorDTO inversorDTO = inversorMapper.toDto(updatedInversor);

        restInversorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inversorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inversorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Inversor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInversorToMatchAllProperties(updatedInversor);
    }

    @Test
    @Transactional
    void putNonExistingInversor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inversor.setId(longCount.incrementAndGet());

        // Create the Inversor
        InversorDTO inversorDTO = inversorMapper.toDto(inversor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInversorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inversorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inversorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inversor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInversor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inversor.setId(longCount.incrementAndGet());

        // Create the Inversor
        InversorDTO inversorDTO = inversorMapper.toDto(inversor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInversorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inversorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inversor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInversor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inversor.setId(longCount.incrementAndGet());

        // Create the Inversor
        InversorDTO inversorDTO = inversorMapper.toDto(inversor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInversorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inversorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inversor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInversorWithPatch() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inversor using partial update
        Inversor partialUpdatedInversor = new Inversor();
        partialUpdatedInversor.setId(inversor.getId());

        partialUpdatedInversor
            .numeroSerie(UPDATED_NUMERO_SERIE)
            .tipoConexion(UPDATED_TIPO_CONEXION)
            .puertoSerial(UPDATED_PUERTO_SERIAL)
            .activo(UPDATED_ACTIVO)
            .ultimoError(UPDATED_ULTIMO_ERROR)
            .periodoMuestreoSeg(UPDATED_PERIODO_MUESTREO_SEG)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restInversorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInversor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInversor))
            )
            .andExpect(status().isOk());

        // Validate the Inversor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInversorUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedInversor, inversor), getPersistedInversor(inversor));
    }

    @Test
    @Transactional
    void fullUpdateInversorWithPatch() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inversor using partial update
        Inversor partialUpdatedInversor = new Inversor();
        partialUpdatedInversor.setId(inversor.getId());

        partialUpdatedInversor
            .nombre(UPDATED_NOMBRE)
            .numeroSerie(UPDATED_NUMERO_SERIE)
            .esMaster(UPDATED_ES_MASTER)
            .tipoConexion(UPDATED_TIPO_CONEXION)
            .puertoSerial(UPDATED_PUERTO_SERIAL)
            .slaveId(UPDATED_SLAVE_ID)
            .activo(UPDATED_ACTIVO)
            .estadoDispositivo(UPDATED_ESTADO_DISPOSITIVO)
            .ultimaLectura(UPDATED_ULTIMA_LECTURA)
            .ultimoError(UPDATED_ULTIMO_ERROR)
            .periodoMuestreoSeg(UPDATED_PERIODO_MUESTREO_SEG)
            .fechaRegistro(UPDATED_FECHA_REGISTRO)
            .ultimaConexion(UPDATED_ULTIMA_CONEXION);

        restInversorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInversor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInversor))
            )
            .andExpect(status().isOk());

        // Validate the Inversor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInversorUpdatableFieldsEquals(partialUpdatedInversor, getPersistedInversor(partialUpdatedInversor));
    }

    @Test
    @Transactional
    void patchNonExistingInversor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inversor.setId(longCount.incrementAndGet());

        // Create the Inversor
        InversorDTO inversorDTO = inversorMapper.toDto(inversor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInversorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inversorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inversorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inversor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInversor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inversor.setId(longCount.incrementAndGet());

        // Create the Inversor
        InversorDTO inversorDTO = inversorMapper.toDto(inversor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInversorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inversorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inversor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInversor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inversor.setId(longCount.incrementAndGet());

        // Create the Inversor
        InversorDTO inversorDTO = inversorMapper.toDto(inversor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInversorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(inversorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inversor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInversor() throws Exception {
        // Initialize the database
        insertedInversor = inversorRepository.saveAndFlush(inversor);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the inversor
        restInversorMockMvc
            .perform(delete(ENTITY_API_URL_ID, inversor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return inversorRepository.count();
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

    protected Inversor getPersistedInversor(Inversor inversor) {
        return inversorRepository.findById(inversor.getId()).orElseThrow();
    }

    protected void assertPersistedInversorToMatchAllProperties(Inversor expectedInversor) {
        assertInversorAllPropertiesEquals(expectedInversor, getPersistedInversor(expectedInversor));
    }

    protected void assertPersistedInversorToMatchUpdatableProperties(Inversor expectedInversor) {
        assertInversorAllUpdatablePropertiesEquals(expectedInversor, getPersistedInversor(expectedInversor));
    }
}
