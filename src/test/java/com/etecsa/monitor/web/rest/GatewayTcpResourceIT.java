package com.etecsa.monitor.web.rest;

import static com.etecsa.monitor.domain.GatewayTcpAsserts.*;
import static com.etecsa.monitor.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.etecsa.monitor.IntegrationTest;
import com.etecsa.monitor.domain.GatewayTcp;
import com.etecsa.monitor.domain.Sitio;
import com.etecsa.monitor.domain.enumeration.EstadoDispositivo;
import com.etecsa.monitor.repository.GatewayTcpRepository;
import com.etecsa.monitor.service.GatewayTcpService;
import com.etecsa.monitor.service.dto.GatewayTcpDTO;
import com.etecsa.monitor.service.mapper.GatewayTcpMapper;
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
 * Integration tests for the {@link GatewayTcpResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class GatewayTcpResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_UBICACION = "AAAAAAAAAA";
    private static final String UPDATED_UBICACION = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION_IP = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION_IP = "BBBBBBBBBB";

    private static final Integer DEFAULT_PUERTO = 1;
    private static final Integer UPDATED_PUERTO = 2;
    private static final Integer SMALLER_PUERTO = 1 - 1;

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final EstadoDispositivo DEFAULT_ESTADO_CONEXION = EstadoDispositivo.Conectado;
    private static final EstadoDispositivo UPDATED_ESTADO_CONEXION = EstadoDispositivo.Error;

    private static final Instant DEFAULT_ULTIMA_LECTURA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ULTIMA_LECTURA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ULTIMO_ERROR = "AAAAAAAAAA";
    private static final String UPDATED_ULTIMO_ERROR = "BBBBBBBBBB";

    private static final Integer DEFAULT_TIEMPO_ESPERA_MS = 1;
    private static final Integer UPDATED_TIEMPO_ESPERA_MS = 2;
    private static final Integer SMALLER_TIEMPO_ESPERA_MS = 1 - 1;

    private static final Integer DEFAULT_REINTENTOS = 1;
    private static final Integer UPDATED_REINTENTOS = 2;
    private static final Integer SMALLER_REINTENTOS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/gateway-tcps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GatewayTcpRepository gatewayTcpRepository;

    @Mock
    private GatewayTcpRepository gatewayTcpRepositoryMock;

    @Autowired
    private GatewayTcpMapper gatewayTcpMapper;

    @Mock
    private GatewayTcpService gatewayTcpServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGatewayTcpMockMvc;

    private GatewayTcp gatewayTcp;

    private GatewayTcp insertedGatewayTcp;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GatewayTcp createEntity() {
        return new GatewayTcp()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .ubicacion(DEFAULT_UBICACION)
            .direccionIp(DEFAULT_DIRECCION_IP)
            .puerto(DEFAULT_PUERTO)
            .activo(DEFAULT_ACTIVO)
            .estadoConexion(DEFAULT_ESTADO_CONEXION)
            .ultimaLectura(DEFAULT_ULTIMA_LECTURA)
            .ultimoError(DEFAULT_ULTIMO_ERROR)
            .tiempoEsperaMs(DEFAULT_TIEMPO_ESPERA_MS)
            .reintentos(DEFAULT_REINTENTOS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GatewayTcp createUpdatedEntity() {
        return new GatewayTcp()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .ubicacion(UPDATED_UBICACION)
            .direccionIp(UPDATED_DIRECCION_IP)
            .puerto(UPDATED_PUERTO)
            .activo(UPDATED_ACTIVO)
            .estadoConexion(UPDATED_ESTADO_CONEXION)
            .ultimaLectura(UPDATED_ULTIMA_LECTURA)
            .ultimoError(UPDATED_ULTIMO_ERROR)
            .tiempoEsperaMs(UPDATED_TIEMPO_ESPERA_MS)
            .reintentos(UPDATED_REINTENTOS);
    }

    @BeforeEach
    void initTest() {
        gatewayTcp = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedGatewayTcp != null) {
            gatewayTcpRepository.delete(insertedGatewayTcp);
            insertedGatewayTcp = null;
        }
    }

    @Test
    @Transactional
    void createGatewayTcp() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the GatewayTcp
        GatewayTcpDTO gatewayTcpDTO = gatewayTcpMapper.toDto(gatewayTcp);
        var returnedGatewayTcpDTO = om.readValue(
            restGatewayTcpMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gatewayTcpDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            GatewayTcpDTO.class
        );

        // Validate the GatewayTcp in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedGatewayTcp = gatewayTcpMapper.toEntity(returnedGatewayTcpDTO);
        assertGatewayTcpUpdatableFieldsEquals(returnedGatewayTcp, getPersistedGatewayTcp(returnedGatewayTcp));

        insertedGatewayTcp = returnedGatewayTcp;
    }

    @Test
    @Transactional
    void createGatewayTcpWithExistingId() throws Exception {
        // Create the GatewayTcp with an existing ID
        gatewayTcp.setId(1L);
        GatewayTcpDTO gatewayTcpDTO = gatewayTcpMapper.toDto(gatewayTcp);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGatewayTcpMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gatewayTcpDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GatewayTcp in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        gatewayTcp.setNombre(null);

        // Create the GatewayTcp, which fails.
        GatewayTcpDTO gatewayTcpDTO = gatewayTcpMapper.toDto(gatewayTcp);

        restGatewayTcpMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gatewayTcpDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDireccionIpIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        gatewayTcp.setDireccionIp(null);

        // Create the GatewayTcp, which fails.
        GatewayTcpDTO gatewayTcpDTO = gatewayTcpMapper.toDto(gatewayTcp);

        restGatewayTcpMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gatewayTcpDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPuertoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        gatewayTcp.setPuerto(null);

        // Create the GatewayTcp, which fails.
        GatewayTcpDTO gatewayTcpDTO = gatewayTcpMapper.toDto(gatewayTcp);

        restGatewayTcpMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gatewayTcpDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        gatewayTcp.setActivo(null);

        // Create the GatewayTcp, which fails.
        GatewayTcpDTO gatewayTcpDTO = gatewayTcpMapper.toDto(gatewayTcp);

        restGatewayTcpMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gatewayTcpDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGatewayTcps() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList
        restGatewayTcpMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gatewayTcp.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].ubicacion").value(hasItem(DEFAULT_UBICACION)))
            .andExpect(jsonPath("$.[*].direccionIp").value(hasItem(DEFAULT_DIRECCION_IP)))
            .andExpect(jsonPath("$.[*].puerto").value(hasItem(DEFAULT_PUERTO)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].estadoConexion").value(hasItem(DEFAULT_ESTADO_CONEXION.toString())))
            .andExpect(jsonPath("$.[*].ultimaLectura").value(hasItem(DEFAULT_ULTIMA_LECTURA.toString())))
            .andExpect(jsonPath("$.[*].ultimoError").value(hasItem(DEFAULT_ULTIMO_ERROR)))
            .andExpect(jsonPath("$.[*].tiempoEsperaMs").value(hasItem(DEFAULT_TIEMPO_ESPERA_MS)))
            .andExpect(jsonPath("$.[*].reintentos").value(hasItem(DEFAULT_REINTENTOS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGatewayTcpsWithEagerRelationshipsIsEnabled() throws Exception {
        when(gatewayTcpServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGatewayTcpMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(gatewayTcpServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGatewayTcpsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(gatewayTcpServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGatewayTcpMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(gatewayTcpRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getGatewayTcp() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get the gatewayTcp
        restGatewayTcpMockMvc
            .perform(get(ENTITY_API_URL_ID, gatewayTcp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gatewayTcp.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.ubicacion").value(DEFAULT_UBICACION))
            .andExpect(jsonPath("$.direccionIp").value(DEFAULT_DIRECCION_IP))
            .andExpect(jsonPath("$.puerto").value(DEFAULT_PUERTO))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.estadoConexion").value(DEFAULT_ESTADO_CONEXION.toString()))
            .andExpect(jsonPath("$.ultimaLectura").value(DEFAULT_ULTIMA_LECTURA.toString()))
            .andExpect(jsonPath("$.ultimoError").value(DEFAULT_ULTIMO_ERROR))
            .andExpect(jsonPath("$.tiempoEsperaMs").value(DEFAULT_TIEMPO_ESPERA_MS))
            .andExpect(jsonPath("$.reintentos").value(DEFAULT_REINTENTOS));
    }

    @Test
    @Transactional
    void getGatewayTcpsByIdFiltering() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        Long id = gatewayTcp.getId();

        defaultGatewayTcpFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultGatewayTcpFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultGatewayTcpFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where nombre equals to
        defaultGatewayTcpFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where nombre in
        defaultGatewayTcpFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where nombre is not null
        defaultGatewayTcpFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where nombre contains
        defaultGatewayTcpFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where nombre does not contain
        defaultGatewayTcpFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where descripcion equals to
        defaultGatewayTcpFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where descripcion in
        defaultGatewayTcpFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where descripcion is not null
        defaultGatewayTcpFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where descripcion contains
        defaultGatewayTcpFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where descripcion does not contain
        defaultGatewayTcpFiltering(
            "descripcion.doesNotContain=" + UPDATED_DESCRIPCION,
            "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByUbicacionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where ubicacion equals to
        defaultGatewayTcpFiltering("ubicacion.equals=" + DEFAULT_UBICACION, "ubicacion.equals=" + UPDATED_UBICACION);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByUbicacionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where ubicacion in
        defaultGatewayTcpFiltering("ubicacion.in=" + DEFAULT_UBICACION + "," + UPDATED_UBICACION, "ubicacion.in=" + UPDATED_UBICACION);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByUbicacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where ubicacion is not null
        defaultGatewayTcpFiltering("ubicacion.specified=true", "ubicacion.specified=false");
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByUbicacionContainsSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where ubicacion contains
        defaultGatewayTcpFiltering("ubicacion.contains=" + DEFAULT_UBICACION, "ubicacion.contains=" + UPDATED_UBICACION);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByUbicacionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where ubicacion does not contain
        defaultGatewayTcpFiltering("ubicacion.doesNotContain=" + UPDATED_UBICACION, "ubicacion.doesNotContain=" + DEFAULT_UBICACION);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByDireccionIpIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where direccionIp equals to
        defaultGatewayTcpFiltering("direccionIp.equals=" + DEFAULT_DIRECCION_IP, "direccionIp.equals=" + UPDATED_DIRECCION_IP);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByDireccionIpIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where direccionIp in
        defaultGatewayTcpFiltering(
            "direccionIp.in=" + DEFAULT_DIRECCION_IP + "," + UPDATED_DIRECCION_IP,
            "direccionIp.in=" + UPDATED_DIRECCION_IP
        );
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByDireccionIpIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where direccionIp is not null
        defaultGatewayTcpFiltering("direccionIp.specified=true", "direccionIp.specified=false");
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByDireccionIpContainsSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where direccionIp contains
        defaultGatewayTcpFiltering("direccionIp.contains=" + DEFAULT_DIRECCION_IP, "direccionIp.contains=" + UPDATED_DIRECCION_IP);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByDireccionIpNotContainsSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where direccionIp does not contain
        defaultGatewayTcpFiltering(
            "direccionIp.doesNotContain=" + UPDATED_DIRECCION_IP,
            "direccionIp.doesNotContain=" + DEFAULT_DIRECCION_IP
        );
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByPuertoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where puerto equals to
        defaultGatewayTcpFiltering("puerto.equals=" + DEFAULT_PUERTO, "puerto.equals=" + UPDATED_PUERTO);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByPuertoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where puerto in
        defaultGatewayTcpFiltering("puerto.in=" + DEFAULT_PUERTO + "," + UPDATED_PUERTO, "puerto.in=" + UPDATED_PUERTO);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByPuertoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where puerto is not null
        defaultGatewayTcpFiltering("puerto.specified=true", "puerto.specified=false");
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByPuertoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where puerto is greater than or equal to
        defaultGatewayTcpFiltering("puerto.greaterThanOrEqual=" + DEFAULT_PUERTO, "puerto.greaterThanOrEqual=" + UPDATED_PUERTO);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByPuertoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where puerto is less than or equal to
        defaultGatewayTcpFiltering("puerto.lessThanOrEqual=" + DEFAULT_PUERTO, "puerto.lessThanOrEqual=" + SMALLER_PUERTO);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByPuertoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where puerto is less than
        defaultGatewayTcpFiltering("puerto.lessThan=" + UPDATED_PUERTO, "puerto.lessThan=" + DEFAULT_PUERTO);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByPuertoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where puerto is greater than
        defaultGatewayTcpFiltering("puerto.greaterThan=" + SMALLER_PUERTO, "puerto.greaterThan=" + DEFAULT_PUERTO);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where activo equals to
        defaultGatewayTcpFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where activo in
        defaultGatewayTcpFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where activo is not null
        defaultGatewayTcpFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByEstadoConexionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where estadoConexion equals to
        defaultGatewayTcpFiltering("estadoConexion.equals=" + DEFAULT_ESTADO_CONEXION, "estadoConexion.equals=" + UPDATED_ESTADO_CONEXION);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByEstadoConexionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where estadoConexion in
        defaultGatewayTcpFiltering(
            "estadoConexion.in=" + DEFAULT_ESTADO_CONEXION + "," + UPDATED_ESTADO_CONEXION,
            "estadoConexion.in=" + UPDATED_ESTADO_CONEXION
        );
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByEstadoConexionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where estadoConexion is not null
        defaultGatewayTcpFiltering("estadoConexion.specified=true", "estadoConexion.specified=false");
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByUltimaLecturaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where ultimaLectura equals to
        defaultGatewayTcpFiltering("ultimaLectura.equals=" + DEFAULT_ULTIMA_LECTURA, "ultimaLectura.equals=" + UPDATED_ULTIMA_LECTURA);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByUltimaLecturaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where ultimaLectura in
        defaultGatewayTcpFiltering(
            "ultimaLectura.in=" + DEFAULT_ULTIMA_LECTURA + "," + UPDATED_ULTIMA_LECTURA,
            "ultimaLectura.in=" + UPDATED_ULTIMA_LECTURA
        );
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByUltimaLecturaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where ultimaLectura is not null
        defaultGatewayTcpFiltering("ultimaLectura.specified=true", "ultimaLectura.specified=false");
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByUltimoErrorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where ultimoError equals to
        defaultGatewayTcpFiltering("ultimoError.equals=" + DEFAULT_ULTIMO_ERROR, "ultimoError.equals=" + UPDATED_ULTIMO_ERROR);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByUltimoErrorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where ultimoError in
        defaultGatewayTcpFiltering(
            "ultimoError.in=" + DEFAULT_ULTIMO_ERROR + "," + UPDATED_ULTIMO_ERROR,
            "ultimoError.in=" + UPDATED_ULTIMO_ERROR
        );
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByUltimoErrorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where ultimoError is not null
        defaultGatewayTcpFiltering("ultimoError.specified=true", "ultimoError.specified=false");
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByUltimoErrorContainsSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where ultimoError contains
        defaultGatewayTcpFiltering("ultimoError.contains=" + DEFAULT_ULTIMO_ERROR, "ultimoError.contains=" + UPDATED_ULTIMO_ERROR);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByUltimoErrorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where ultimoError does not contain
        defaultGatewayTcpFiltering(
            "ultimoError.doesNotContain=" + UPDATED_ULTIMO_ERROR,
            "ultimoError.doesNotContain=" + DEFAULT_ULTIMO_ERROR
        );
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByTiempoEsperaMsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where tiempoEsperaMs equals to
        defaultGatewayTcpFiltering(
            "tiempoEsperaMs.equals=" + DEFAULT_TIEMPO_ESPERA_MS,
            "tiempoEsperaMs.equals=" + UPDATED_TIEMPO_ESPERA_MS
        );
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByTiempoEsperaMsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where tiempoEsperaMs in
        defaultGatewayTcpFiltering(
            "tiempoEsperaMs.in=" + DEFAULT_TIEMPO_ESPERA_MS + "," + UPDATED_TIEMPO_ESPERA_MS,
            "tiempoEsperaMs.in=" + UPDATED_TIEMPO_ESPERA_MS
        );
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByTiempoEsperaMsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where tiempoEsperaMs is not null
        defaultGatewayTcpFiltering("tiempoEsperaMs.specified=true", "tiempoEsperaMs.specified=false");
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByTiempoEsperaMsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where tiempoEsperaMs is greater than or equal to
        defaultGatewayTcpFiltering(
            "tiempoEsperaMs.greaterThanOrEqual=" + DEFAULT_TIEMPO_ESPERA_MS,
            "tiempoEsperaMs.greaterThanOrEqual=" + UPDATED_TIEMPO_ESPERA_MS
        );
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByTiempoEsperaMsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where tiempoEsperaMs is less than or equal to
        defaultGatewayTcpFiltering(
            "tiempoEsperaMs.lessThanOrEqual=" + DEFAULT_TIEMPO_ESPERA_MS,
            "tiempoEsperaMs.lessThanOrEqual=" + SMALLER_TIEMPO_ESPERA_MS
        );
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByTiempoEsperaMsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where tiempoEsperaMs is less than
        defaultGatewayTcpFiltering(
            "tiempoEsperaMs.lessThan=" + UPDATED_TIEMPO_ESPERA_MS,
            "tiempoEsperaMs.lessThan=" + DEFAULT_TIEMPO_ESPERA_MS
        );
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByTiempoEsperaMsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where tiempoEsperaMs is greater than
        defaultGatewayTcpFiltering(
            "tiempoEsperaMs.greaterThan=" + SMALLER_TIEMPO_ESPERA_MS,
            "tiempoEsperaMs.greaterThan=" + DEFAULT_TIEMPO_ESPERA_MS
        );
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByReintentosIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where reintentos equals to
        defaultGatewayTcpFiltering("reintentos.equals=" + DEFAULT_REINTENTOS, "reintentos.equals=" + UPDATED_REINTENTOS);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByReintentosIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where reintentos in
        defaultGatewayTcpFiltering("reintentos.in=" + DEFAULT_REINTENTOS + "," + UPDATED_REINTENTOS, "reintentos.in=" + UPDATED_REINTENTOS);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByReintentosIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where reintentos is not null
        defaultGatewayTcpFiltering("reintentos.specified=true", "reintentos.specified=false");
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByReintentosIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where reintentos is greater than or equal to
        defaultGatewayTcpFiltering(
            "reintentos.greaterThanOrEqual=" + DEFAULT_REINTENTOS,
            "reintentos.greaterThanOrEqual=" + UPDATED_REINTENTOS
        );
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByReintentosIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where reintentos is less than or equal to
        defaultGatewayTcpFiltering("reintentos.lessThanOrEqual=" + DEFAULT_REINTENTOS, "reintentos.lessThanOrEqual=" + SMALLER_REINTENTOS);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByReintentosIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where reintentos is less than
        defaultGatewayTcpFiltering("reintentos.lessThan=" + UPDATED_REINTENTOS, "reintentos.lessThan=" + DEFAULT_REINTENTOS);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsByReintentosIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        // Get all the gatewayTcpList where reintentos is greater than
        defaultGatewayTcpFiltering("reintentos.greaterThan=" + SMALLER_REINTENTOS, "reintentos.greaterThan=" + DEFAULT_REINTENTOS);
    }

    @Test
    @Transactional
    void getAllGatewayTcpsBySitioIsEqualToSomething() throws Exception {
        Sitio sitio;
        if (TestUtil.findAll(em, Sitio.class).isEmpty()) {
            gatewayTcpRepository.saveAndFlush(gatewayTcp);
            sitio = SitioResourceIT.createEntity();
        } else {
            sitio = TestUtil.findAll(em, Sitio.class).get(0);
        }
        em.persist(sitio);
        em.flush();
        gatewayTcp.setSitio(sitio);
        gatewayTcpRepository.saveAndFlush(gatewayTcp);
        Long sitioId = sitio.getId();
        // Get all the gatewayTcpList where sitio equals to sitioId
        defaultGatewayTcpShouldBeFound("sitioId.equals=" + sitioId);

        // Get all the gatewayTcpList where sitio equals to (sitioId + 1)
        defaultGatewayTcpShouldNotBeFound("sitioId.equals=" + (sitioId + 1));
    }

    private void defaultGatewayTcpFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultGatewayTcpShouldBeFound(shouldBeFound);
        defaultGatewayTcpShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGatewayTcpShouldBeFound(String filter) throws Exception {
        restGatewayTcpMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gatewayTcp.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].ubicacion").value(hasItem(DEFAULT_UBICACION)))
            .andExpect(jsonPath("$.[*].direccionIp").value(hasItem(DEFAULT_DIRECCION_IP)))
            .andExpect(jsonPath("$.[*].puerto").value(hasItem(DEFAULT_PUERTO)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].estadoConexion").value(hasItem(DEFAULT_ESTADO_CONEXION.toString())))
            .andExpect(jsonPath("$.[*].ultimaLectura").value(hasItem(DEFAULT_ULTIMA_LECTURA.toString())))
            .andExpect(jsonPath("$.[*].ultimoError").value(hasItem(DEFAULT_ULTIMO_ERROR)))
            .andExpect(jsonPath("$.[*].tiempoEsperaMs").value(hasItem(DEFAULT_TIEMPO_ESPERA_MS)))
            .andExpect(jsonPath("$.[*].reintentos").value(hasItem(DEFAULT_REINTENTOS)));

        // Check, that the count call also returns 1
        restGatewayTcpMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGatewayTcpShouldNotBeFound(String filter) throws Exception {
        restGatewayTcpMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGatewayTcpMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGatewayTcp() throws Exception {
        // Get the gatewayTcp
        restGatewayTcpMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGatewayTcp() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the gatewayTcp
        GatewayTcp updatedGatewayTcp = gatewayTcpRepository.findById(gatewayTcp.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGatewayTcp are not directly saved in db
        em.detach(updatedGatewayTcp);
        updatedGatewayTcp
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .ubicacion(UPDATED_UBICACION)
            .direccionIp(UPDATED_DIRECCION_IP)
            .puerto(UPDATED_PUERTO)
            .activo(UPDATED_ACTIVO)
            .estadoConexion(UPDATED_ESTADO_CONEXION)
            .ultimaLectura(UPDATED_ULTIMA_LECTURA)
            .ultimoError(UPDATED_ULTIMO_ERROR)
            .tiempoEsperaMs(UPDATED_TIEMPO_ESPERA_MS)
            .reintentos(UPDATED_REINTENTOS);
        GatewayTcpDTO gatewayTcpDTO = gatewayTcpMapper.toDto(updatedGatewayTcp);

        restGatewayTcpMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gatewayTcpDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(gatewayTcpDTO))
            )
            .andExpect(status().isOk());

        // Validate the GatewayTcp in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGatewayTcpToMatchAllProperties(updatedGatewayTcp);
    }

    @Test
    @Transactional
    void putNonExistingGatewayTcp() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gatewayTcp.setId(longCount.incrementAndGet());

        // Create the GatewayTcp
        GatewayTcpDTO gatewayTcpDTO = gatewayTcpMapper.toDto(gatewayTcp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGatewayTcpMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gatewayTcpDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(gatewayTcpDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GatewayTcp in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGatewayTcp() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gatewayTcp.setId(longCount.incrementAndGet());

        // Create the GatewayTcp
        GatewayTcpDTO gatewayTcpDTO = gatewayTcpMapper.toDto(gatewayTcp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGatewayTcpMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(gatewayTcpDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GatewayTcp in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGatewayTcp() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gatewayTcp.setId(longCount.incrementAndGet());

        // Create the GatewayTcp
        GatewayTcpDTO gatewayTcpDTO = gatewayTcpMapper.toDto(gatewayTcp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGatewayTcpMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gatewayTcpDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GatewayTcp in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGatewayTcpWithPatch() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the gatewayTcp using partial update
        GatewayTcp partialUpdatedGatewayTcp = new GatewayTcp();
        partialUpdatedGatewayTcp.setId(gatewayTcp.getId());

        partialUpdatedGatewayTcp
            .nombre(UPDATED_NOMBRE)
            .puerto(UPDATED_PUERTO)
            .activo(UPDATED_ACTIVO)
            .estadoConexion(UPDATED_ESTADO_CONEXION)
            .ultimoError(UPDATED_ULTIMO_ERROR)
            .tiempoEsperaMs(UPDATED_TIEMPO_ESPERA_MS);

        restGatewayTcpMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGatewayTcp.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGatewayTcp))
            )
            .andExpect(status().isOk());

        // Validate the GatewayTcp in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGatewayTcpUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedGatewayTcp, gatewayTcp),
            getPersistedGatewayTcp(gatewayTcp)
        );
    }

    @Test
    @Transactional
    void fullUpdateGatewayTcpWithPatch() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the gatewayTcp using partial update
        GatewayTcp partialUpdatedGatewayTcp = new GatewayTcp();
        partialUpdatedGatewayTcp.setId(gatewayTcp.getId());

        partialUpdatedGatewayTcp
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .ubicacion(UPDATED_UBICACION)
            .direccionIp(UPDATED_DIRECCION_IP)
            .puerto(UPDATED_PUERTO)
            .activo(UPDATED_ACTIVO)
            .estadoConexion(UPDATED_ESTADO_CONEXION)
            .ultimaLectura(UPDATED_ULTIMA_LECTURA)
            .ultimoError(UPDATED_ULTIMO_ERROR)
            .tiempoEsperaMs(UPDATED_TIEMPO_ESPERA_MS)
            .reintentos(UPDATED_REINTENTOS);

        restGatewayTcpMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGatewayTcp.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGatewayTcp))
            )
            .andExpect(status().isOk());

        // Validate the GatewayTcp in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGatewayTcpUpdatableFieldsEquals(partialUpdatedGatewayTcp, getPersistedGatewayTcp(partialUpdatedGatewayTcp));
    }

    @Test
    @Transactional
    void patchNonExistingGatewayTcp() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gatewayTcp.setId(longCount.incrementAndGet());

        // Create the GatewayTcp
        GatewayTcpDTO gatewayTcpDTO = gatewayTcpMapper.toDto(gatewayTcp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGatewayTcpMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gatewayTcpDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(gatewayTcpDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GatewayTcp in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGatewayTcp() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gatewayTcp.setId(longCount.incrementAndGet());

        // Create the GatewayTcp
        GatewayTcpDTO gatewayTcpDTO = gatewayTcpMapper.toDto(gatewayTcp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGatewayTcpMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(gatewayTcpDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GatewayTcp in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGatewayTcp() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gatewayTcp.setId(longCount.incrementAndGet());

        // Create the GatewayTcp
        GatewayTcpDTO gatewayTcpDTO = gatewayTcpMapper.toDto(gatewayTcp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGatewayTcpMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(gatewayTcpDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GatewayTcp in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGatewayTcp() throws Exception {
        // Initialize the database
        insertedGatewayTcp = gatewayTcpRepository.saveAndFlush(gatewayTcp);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the gatewayTcp
        restGatewayTcpMockMvc
            .perform(delete(ENTITY_API_URL_ID, gatewayTcp.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return gatewayTcpRepository.count();
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

    protected GatewayTcp getPersistedGatewayTcp(GatewayTcp gatewayTcp) {
        return gatewayTcpRepository.findById(gatewayTcp.getId()).orElseThrow();
    }

    protected void assertPersistedGatewayTcpToMatchAllProperties(GatewayTcp expectedGatewayTcp) {
        assertGatewayTcpAllPropertiesEquals(expectedGatewayTcp, getPersistedGatewayTcp(expectedGatewayTcp));
    }

    protected void assertPersistedGatewayTcpToMatchUpdatableProperties(GatewayTcp expectedGatewayTcp) {
        assertGatewayTcpAllUpdatablePropertiesEquals(expectedGatewayTcp, getPersistedGatewayTcp(expectedGatewayTcp));
    }
}
