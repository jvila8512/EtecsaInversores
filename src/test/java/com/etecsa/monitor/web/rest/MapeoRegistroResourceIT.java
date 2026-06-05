package com.etecsa.monitor.web.rest;

import static com.etecsa.monitor.domain.MapeoRegistroAsserts.*;
import static com.etecsa.monitor.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.etecsa.monitor.IntegrationTest;
import com.etecsa.monitor.domain.MapeoRegistro;
import com.etecsa.monitor.domain.Marca;
import com.etecsa.monitor.domain.enumeration.TipoDato;
import com.etecsa.monitor.repository.MapeoRegistroRepository;
import com.etecsa.monitor.service.MapeoRegistroService;
import com.etecsa.monitor.service.dto.MapeoRegistroDTO;
import com.etecsa.monitor.service.mapper.MapeoRegistroMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link MapeoRegistroResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MapeoRegistroResourceIT {

    private static final Integer DEFAULT_DIRECCION_REGISTRO = 1;
    private static final Integer UPDATED_DIRECCION_REGISTRO = 2;
    private static final Integer SMALLER_DIRECCION_REGISTRO = 1 - 1;

    private static final String DEFAULT_NOMBRE_REGISTRO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_REGISTRO = "BBBBBBBBBB";

    private static final String DEFAULT_CAMPO_SQL = "AAAAAAAAAA";
    private static final String UPDATED_CAMPO_SQL = "BBBBBBBBBB";

    private static final TipoDato DEFAULT_TIPO_DATO = TipoDato.UnsignedInt16;
    private static final TipoDato UPDATED_TIPO_DATO = TipoDato.SignedInt16;

    private static final Double DEFAULT_ESCALA = 1D;
    private static final Double UPDATED_ESCALA = 2D;
    private static final Double SMALLER_ESCALA = 1D - 1D;

    private static final String DEFAULT_UNIDAD = "AAAAAAAAAA";
    private static final String UPDATED_UNIDAD = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/mapeo-registros";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MapeoRegistroRepository mapeoRegistroRepository;

    @Mock
    private MapeoRegistroRepository mapeoRegistroRepositoryMock;

    @Autowired
    private MapeoRegistroMapper mapeoRegistroMapper;

    @Mock
    private MapeoRegistroService mapeoRegistroServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMapeoRegistroMockMvc;

    private MapeoRegistro mapeoRegistro;

    private MapeoRegistro insertedMapeoRegistro;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MapeoRegistro createEntity() {
        return new MapeoRegistro()
            .direccionRegistro(DEFAULT_DIRECCION_REGISTRO)
            .nombreRegistro(DEFAULT_NOMBRE_REGISTRO)
            .campoSql(DEFAULT_CAMPO_SQL)
            .tipoDato(DEFAULT_TIPO_DATO)
            .escala(DEFAULT_ESCALA)
            .unidad(DEFAULT_UNIDAD)
            .activo(DEFAULT_ACTIVO)
            .descripcion(DEFAULT_DESCRIPCION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MapeoRegistro createUpdatedEntity() {
        return new MapeoRegistro()
            .direccionRegistro(UPDATED_DIRECCION_REGISTRO)
            .nombreRegistro(UPDATED_NOMBRE_REGISTRO)
            .campoSql(UPDATED_CAMPO_SQL)
            .tipoDato(UPDATED_TIPO_DATO)
            .escala(UPDATED_ESCALA)
            .unidad(UPDATED_UNIDAD)
            .activo(UPDATED_ACTIVO)
            .descripcion(UPDATED_DESCRIPCION);
    }

    @BeforeEach
    void initTest() {
        mapeoRegistro = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMapeoRegistro != null) {
            mapeoRegistroRepository.delete(insertedMapeoRegistro);
            insertedMapeoRegistro = null;
        }
    }

    @Test
    @Transactional
    void createMapeoRegistro() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MapeoRegistro
        MapeoRegistroDTO mapeoRegistroDTO = mapeoRegistroMapper.toDto(mapeoRegistro);
        var returnedMapeoRegistroDTO = om.readValue(
            restMapeoRegistroMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mapeoRegistroDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MapeoRegistroDTO.class
        );

        // Validate the MapeoRegistro in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMapeoRegistro = mapeoRegistroMapper.toEntity(returnedMapeoRegistroDTO);
        assertMapeoRegistroUpdatableFieldsEquals(returnedMapeoRegistro, getPersistedMapeoRegistro(returnedMapeoRegistro));

        insertedMapeoRegistro = returnedMapeoRegistro;
    }

    @Test
    @Transactional
    void createMapeoRegistroWithExistingId() throws Exception {
        // Create the MapeoRegistro with an existing ID
        mapeoRegistro.setId(1L);
        MapeoRegistroDTO mapeoRegistroDTO = mapeoRegistroMapper.toDto(mapeoRegistro);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMapeoRegistroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mapeoRegistroDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MapeoRegistro in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDireccionRegistroIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mapeoRegistro.setDireccionRegistro(null);

        // Create the MapeoRegistro, which fails.
        MapeoRegistroDTO mapeoRegistroDTO = mapeoRegistroMapper.toDto(mapeoRegistro);

        restMapeoRegistroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mapeoRegistroDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreRegistroIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mapeoRegistro.setNombreRegistro(null);

        // Create the MapeoRegistro, which fails.
        MapeoRegistroDTO mapeoRegistroDTO = mapeoRegistroMapper.toDto(mapeoRegistro);

        restMapeoRegistroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mapeoRegistroDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCampoSqlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mapeoRegistro.setCampoSql(null);

        // Create the MapeoRegistro, which fails.
        MapeoRegistroDTO mapeoRegistroDTO = mapeoRegistroMapper.toDto(mapeoRegistro);

        restMapeoRegistroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mapeoRegistroDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoDatoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mapeoRegistro.setTipoDato(null);

        // Create the MapeoRegistro, which fails.
        MapeoRegistroDTO mapeoRegistroDTO = mapeoRegistroMapper.toDto(mapeoRegistro);

        restMapeoRegistroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mapeoRegistroDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mapeoRegistro.setActivo(null);

        // Create the MapeoRegistro, which fails.
        MapeoRegistroDTO mapeoRegistroDTO = mapeoRegistroMapper.toDto(mapeoRegistro);

        restMapeoRegistroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mapeoRegistroDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMapeoRegistros() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList
        restMapeoRegistroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mapeoRegistro.getId().intValue())))
            .andExpect(jsonPath("$.[*].direccionRegistro").value(hasItem(DEFAULT_DIRECCION_REGISTRO)))
            .andExpect(jsonPath("$.[*].nombreRegistro").value(hasItem(DEFAULT_NOMBRE_REGISTRO)))
            .andExpect(jsonPath("$.[*].campoSql").value(hasItem(DEFAULT_CAMPO_SQL)))
            .andExpect(jsonPath("$.[*].tipoDato").value(hasItem(DEFAULT_TIPO_DATO.toString())))
            .andExpect(jsonPath("$.[*].escala").value(hasItem(DEFAULT_ESCALA)))
            .andExpect(jsonPath("$.[*].unidad").value(hasItem(DEFAULT_UNIDAD)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMapeoRegistrosWithEagerRelationshipsIsEnabled() throws Exception {
        when(mapeoRegistroServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMapeoRegistroMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(mapeoRegistroServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMapeoRegistrosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(mapeoRegistroServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMapeoRegistroMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(mapeoRegistroRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMapeoRegistro() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get the mapeoRegistro
        restMapeoRegistroMockMvc
            .perform(get(ENTITY_API_URL_ID, mapeoRegistro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mapeoRegistro.getId().intValue()))
            .andExpect(jsonPath("$.direccionRegistro").value(DEFAULT_DIRECCION_REGISTRO))
            .andExpect(jsonPath("$.nombreRegistro").value(DEFAULT_NOMBRE_REGISTRO))
            .andExpect(jsonPath("$.campoSql").value(DEFAULT_CAMPO_SQL))
            .andExpect(jsonPath("$.tipoDato").value(DEFAULT_TIPO_DATO.toString()))
            .andExpect(jsonPath("$.escala").value(DEFAULT_ESCALA))
            .andExpect(jsonPath("$.unidad").value(DEFAULT_UNIDAD))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getMapeoRegistrosByIdFiltering() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        Long id = mapeoRegistro.getId();

        defaultMapeoRegistroFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMapeoRegistroFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMapeoRegistroFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByDireccionRegistroIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where direccionRegistro equals to
        defaultMapeoRegistroFiltering(
            "direccionRegistro.equals=" + DEFAULT_DIRECCION_REGISTRO,
            "direccionRegistro.equals=" + UPDATED_DIRECCION_REGISTRO
        );
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByDireccionRegistroIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where direccionRegistro in
        defaultMapeoRegistroFiltering(
            "direccionRegistro.in=" + DEFAULT_DIRECCION_REGISTRO + "," + UPDATED_DIRECCION_REGISTRO,
            "direccionRegistro.in=" + UPDATED_DIRECCION_REGISTRO
        );
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByDireccionRegistroIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where direccionRegistro is not null
        defaultMapeoRegistroFiltering("direccionRegistro.specified=true", "direccionRegistro.specified=false");
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByDireccionRegistroIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where direccionRegistro is greater than or equal to
        defaultMapeoRegistroFiltering(
            "direccionRegistro.greaterThanOrEqual=" + DEFAULT_DIRECCION_REGISTRO,
            "direccionRegistro.greaterThanOrEqual=" + UPDATED_DIRECCION_REGISTRO
        );
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByDireccionRegistroIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where direccionRegistro is less than or equal to
        defaultMapeoRegistroFiltering(
            "direccionRegistro.lessThanOrEqual=" + DEFAULT_DIRECCION_REGISTRO,
            "direccionRegistro.lessThanOrEqual=" + SMALLER_DIRECCION_REGISTRO
        );
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByDireccionRegistroIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where direccionRegistro is less than
        defaultMapeoRegistroFiltering(
            "direccionRegistro.lessThan=" + UPDATED_DIRECCION_REGISTRO,
            "direccionRegistro.lessThan=" + DEFAULT_DIRECCION_REGISTRO
        );
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByDireccionRegistroIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where direccionRegistro is greater than
        defaultMapeoRegistroFiltering(
            "direccionRegistro.greaterThan=" + SMALLER_DIRECCION_REGISTRO,
            "direccionRegistro.greaterThan=" + DEFAULT_DIRECCION_REGISTRO
        );
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByNombreRegistroIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where nombreRegistro equals to
        defaultMapeoRegistroFiltering(
            "nombreRegistro.equals=" + DEFAULT_NOMBRE_REGISTRO,
            "nombreRegistro.equals=" + UPDATED_NOMBRE_REGISTRO
        );
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByNombreRegistroIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where nombreRegistro in
        defaultMapeoRegistroFiltering(
            "nombreRegistro.in=" + DEFAULT_NOMBRE_REGISTRO + "," + UPDATED_NOMBRE_REGISTRO,
            "nombreRegistro.in=" + UPDATED_NOMBRE_REGISTRO
        );
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByNombreRegistroIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where nombreRegistro is not null
        defaultMapeoRegistroFiltering("nombreRegistro.specified=true", "nombreRegistro.specified=false");
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByNombreRegistroContainsSomething() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where nombreRegistro contains
        defaultMapeoRegistroFiltering(
            "nombreRegistro.contains=" + DEFAULT_NOMBRE_REGISTRO,
            "nombreRegistro.contains=" + UPDATED_NOMBRE_REGISTRO
        );
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByNombreRegistroNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where nombreRegistro does not contain
        defaultMapeoRegistroFiltering(
            "nombreRegistro.doesNotContain=" + UPDATED_NOMBRE_REGISTRO,
            "nombreRegistro.doesNotContain=" + DEFAULT_NOMBRE_REGISTRO
        );
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByCampoSqlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where campoSql equals to
        defaultMapeoRegistroFiltering("campoSql.equals=" + DEFAULT_CAMPO_SQL, "campoSql.equals=" + UPDATED_CAMPO_SQL);
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByCampoSqlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where campoSql in
        defaultMapeoRegistroFiltering("campoSql.in=" + DEFAULT_CAMPO_SQL + "," + UPDATED_CAMPO_SQL, "campoSql.in=" + UPDATED_CAMPO_SQL);
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByCampoSqlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where campoSql is not null
        defaultMapeoRegistroFiltering("campoSql.specified=true", "campoSql.specified=false");
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByCampoSqlContainsSomething() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where campoSql contains
        defaultMapeoRegistroFiltering("campoSql.contains=" + DEFAULT_CAMPO_SQL, "campoSql.contains=" + UPDATED_CAMPO_SQL);
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByCampoSqlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where campoSql does not contain
        defaultMapeoRegistroFiltering("campoSql.doesNotContain=" + UPDATED_CAMPO_SQL, "campoSql.doesNotContain=" + DEFAULT_CAMPO_SQL);
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByTipoDatoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where tipoDato equals to
        defaultMapeoRegistroFiltering("tipoDato.equals=" + DEFAULT_TIPO_DATO, "tipoDato.equals=" + UPDATED_TIPO_DATO);
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByTipoDatoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where tipoDato in
        defaultMapeoRegistroFiltering("tipoDato.in=" + DEFAULT_TIPO_DATO + "," + UPDATED_TIPO_DATO, "tipoDato.in=" + UPDATED_TIPO_DATO);
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByTipoDatoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where tipoDato is not null
        defaultMapeoRegistroFiltering("tipoDato.specified=true", "tipoDato.specified=false");
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByEscalaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where escala equals to
        defaultMapeoRegistroFiltering("escala.equals=" + DEFAULT_ESCALA, "escala.equals=" + UPDATED_ESCALA);
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByEscalaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where escala in
        defaultMapeoRegistroFiltering("escala.in=" + DEFAULT_ESCALA + "," + UPDATED_ESCALA, "escala.in=" + UPDATED_ESCALA);
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByEscalaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where escala is not null
        defaultMapeoRegistroFiltering("escala.specified=true", "escala.specified=false");
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByEscalaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where escala is greater than or equal to
        defaultMapeoRegistroFiltering("escala.greaterThanOrEqual=" + DEFAULT_ESCALA, "escala.greaterThanOrEqual=" + UPDATED_ESCALA);
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByEscalaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where escala is less than or equal to
        defaultMapeoRegistroFiltering("escala.lessThanOrEqual=" + DEFAULT_ESCALA, "escala.lessThanOrEqual=" + SMALLER_ESCALA);
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByEscalaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where escala is less than
        defaultMapeoRegistroFiltering("escala.lessThan=" + UPDATED_ESCALA, "escala.lessThan=" + DEFAULT_ESCALA);
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByEscalaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where escala is greater than
        defaultMapeoRegistroFiltering("escala.greaterThan=" + SMALLER_ESCALA, "escala.greaterThan=" + DEFAULT_ESCALA);
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByUnidadIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where unidad equals to
        defaultMapeoRegistroFiltering("unidad.equals=" + DEFAULT_UNIDAD, "unidad.equals=" + UPDATED_UNIDAD);
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByUnidadIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where unidad in
        defaultMapeoRegistroFiltering("unidad.in=" + DEFAULT_UNIDAD + "," + UPDATED_UNIDAD, "unidad.in=" + UPDATED_UNIDAD);
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByUnidadIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where unidad is not null
        defaultMapeoRegistroFiltering("unidad.specified=true", "unidad.specified=false");
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByUnidadContainsSomething() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where unidad contains
        defaultMapeoRegistroFiltering("unidad.contains=" + DEFAULT_UNIDAD, "unidad.contains=" + UPDATED_UNIDAD);
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByUnidadNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where unidad does not contain
        defaultMapeoRegistroFiltering("unidad.doesNotContain=" + UPDATED_UNIDAD, "unidad.doesNotContain=" + DEFAULT_UNIDAD);
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where activo equals to
        defaultMapeoRegistroFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where activo in
        defaultMapeoRegistroFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where activo is not null
        defaultMapeoRegistroFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where descripcion equals to
        defaultMapeoRegistroFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where descripcion in
        defaultMapeoRegistroFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where descripcion is not null
        defaultMapeoRegistroFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where descripcion contains
        defaultMapeoRegistroFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        // Get all the mapeoRegistroList where descripcion does not contain
        defaultMapeoRegistroFiltering(
            "descripcion.doesNotContain=" + UPDATED_DESCRIPCION,
            "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllMapeoRegistrosByMarcaIsEqualToSomething() throws Exception {
        Marca marca;
        if (TestUtil.findAll(em, Marca.class).isEmpty()) {
            mapeoRegistroRepository.saveAndFlush(mapeoRegistro);
            marca = MarcaResourceIT.createEntity();
        } else {
            marca = TestUtil.findAll(em, Marca.class).get(0);
        }
        em.persist(marca);
        em.flush();
        mapeoRegistro.setMarca(marca);
        mapeoRegistroRepository.saveAndFlush(mapeoRegistro);
        Long marcaId = marca.getId();
        // Get all the mapeoRegistroList where marca equals to marcaId
        defaultMapeoRegistroShouldBeFound("marcaId.equals=" + marcaId);

        // Get all the mapeoRegistroList where marca equals to (marcaId + 1)
        defaultMapeoRegistroShouldNotBeFound("marcaId.equals=" + (marcaId + 1));
    }

    private void defaultMapeoRegistroFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMapeoRegistroShouldBeFound(shouldBeFound);
        defaultMapeoRegistroShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMapeoRegistroShouldBeFound(String filter) throws Exception {
        restMapeoRegistroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mapeoRegistro.getId().intValue())))
            .andExpect(jsonPath("$.[*].direccionRegistro").value(hasItem(DEFAULT_DIRECCION_REGISTRO)))
            .andExpect(jsonPath("$.[*].nombreRegistro").value(hasItem(DEFAULT_NOMBRE_REGISTRO)))
            .andExpect(jsonPath("$.[*].campoSql").value(hasItem(DEFAULT_CAMPO_SQL)))
            .andExpect(jsonPath("$.[*].tipoDato").value(hasItem(DEFAULT_TIPO_DATO.toString())))
            .andExpect(jsonPath("$.[*].escala").value(hasItem(DEFAULT_ESCALA)))
            .andExpect(jsonPath("$.[*].unidad").value(hasItem(DEFAULT_UNIDAD)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));

        // Check, that the count call also returns 1
        restMapeoRegistroMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMapeoRegistroShouldNotBeFound(String filter) throws Exception {
        restMapeoRegistroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMapeoRegistroMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMapeoRegistro() throws Exception {
        // Get the mapeoRegistro
        restMapeoRegistroMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMapeoRegistro() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mapeoRegistro
        MapeoRegistro updatedMapeoRegistro = mapeoRegistroRepository.findById(mapeoRegistro.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMapeoRegistro are not directly saved in db
        em.detach(updatedMapeoRegistro);
        updatedMapeoRegistro
            .direccionRegistro(UPDATED_DIRECCION_REGISTRO)
            .nombreRegistro(UPDATED_NOMBRE_REGISTRO)
            .campoSql(UPDATED_CAMPO_SQL)
            .tipoDato(UPDATED_TIPO_DATO)
            .escala(UPDATED_ESCALA)
            .unidad(UPDATED_UNIDAD)
            .activo(UPDATED_ACTIVO)
            .descripcion(UPDATED_DESCRIPCION);
        MapeoRegistroDTO mapeoRegistroDTO = mapeoRegistroMapper.toDto(updatedMapeoRegistro);

        restMapeoRegistroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mapeoRegistroDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mapeoRegistroDTO))
            )
            .andExpect(status().isOk());

        // Validate the MapeoRegistro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMapeoRegistroToMatchAllProperties(updatedMapeoRegistro);
    }

    @Test
    @Transactional
    void putNonExistingMapeoRegistro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mapeoRegistro.setId(longCount.incrementAndGet());

        // Create the MapeoRegistro
        MapeoRegistroDTO mapeoRegistroDTO = mapeoRegistroMapper.toDto(mapeoRegistro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMapeoRegistroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mapeoRegistroDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mapeoRegistroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MapeoRegistro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMapeoRegistro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mapeoRegistro.setId(longCount.incrementAndGet());

        // Create the MapeoRegistro
        MapeoRegistroDTO mapeoRegistroDTO = mapeoRegistroMapper.toDto(mapeoRegistro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMapeoRegistroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mapeoRegistroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MapeoRegistro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMapeoRegistro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mapeoRegistro.setId(longCount.incrementAndGet());

        // Create the MapeoRegistro
        MapeoRegistroDTO mapeoRegistroDTO = mapeoRegistroMapper.toDto(mapeoRegistro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMapeoRegistroMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mapeoRegistroDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MapeoRegistro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMapeoRegistroWithPatch() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mapeoRegistro using partial update
        MapeoRegistro partialUpdatedMapeoRegistro = new MapeoRegistro();
        partialUpdatedMapeoRegistro.setId(mapeoRegistro.getId());

        partialUpdatedMapeoRegistro.escala(UPDATED_ESCALA).descripcion(UPDATED_DESCRIPCION);

        restMapeoRegistroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMapeoRegistro.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMapeoRegistro))
            )
            .andExpect(status().isOk());

        // Validate the MapeoRegistro in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMapeoRegistroUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMapeoRegistro, mapeoRegistro),
            getPersistedMapeoRegistro(mapeoRegistro)
        );
    }

    @Test
    @Transactional
    void fullUpdateMapeoRegistroWithPatch() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mapeoRegistro using partial update
        MapeoRegistro partialUpdatedMapeoRegistro = new MapeoRegistro();
        partialUpdatedMapeoRegistro.setId(mapeoRegistro.getId());

        partialUpdatedMapeoRegistro
            .direccionRegistro(UPDATED_DIRECCION_REGISTRO)
            .nombreRegistro(UPDATED_NOMBRE_REGISTRO)
            .campoSql(UPDATED_CAMPO_SQL)
            .tipoDato(UPDATED_TIPO_DATO)
            .escala(UPDATED_ESCALA)
            .unidad(UPDATED_UNIDAD)
            .activo(UPDATED_ACTIVO)
            .descripcion(UPDATED_DESCRIPCION);

        restMapeoRegistroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMapeoRegistro.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMapeoRegistro))
            )
            .andExpect(status().isOk());

        // Validate the MapeoRegistro in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMapeoRegistroUpdatableFieldsEquals(partialUpdatedMapeoRegistro, getPersistedMapeoRegistro(partialUpdatedMapeoRegistro));
    }

    @Test
    @Transactional
    void patchNonExistingMapeoRegistro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mapeoRegistro.setId(longCount.incrementAndGet());

        // Create the MapeoRegistro
        MapeoRegistroDTO mapeoRegistroDTO = mapeoRegistroMapper.toDto(mapeoRegistro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMapeoRegistroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mapeoRegistroDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mapeoRegistroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MapeoRegistro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMapeoRegistro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mapeoRegistro.setId(longCount.incrementAndGet());

        // Create the MapeoRegistro
        MapeoRegistroDTO mapeoRegistroDTO = mapeoRegistroMapper.toDto(mapeoRegistro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMapeoRegistroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mapeoRegistroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MapeoRegistro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMapeoRegistro() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mapeoRegistro.setId(longCount.incrementAndGet());

        // Create the MapeoRegistro
        MapeoRegistroDTO mapeoRegistroDTO = mapeoRegistroMapper.toDto(mapeoRegistro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMapeoRegistroMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(mapeoRegistroDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MapeoRegistro in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMapeoRegistro() throws Exception {
        // Initialize the database
        insertedMapeoRegistro = mapeoRegistroRepository.saveAndFlush(mapeoRegistro);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the mapeoRegistro
        restMapeoRegistroMockMvc
            .perform(delete(ENTITY_API_URL_ID, mapeoRegistro.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return mapeoRegistroRepository.count();
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

    protected MapeoRegistro getPersistedMapeoRegistro(MapeoRegistro mapeoRegistro) {
        return mapeoRegistroRepository.findById(mapeoRegistro.getId()).orElseThrow();
    }

    protected void assertPersistedMapeoRegistroToMatchAllProperties(MapeoRegistro expectedMapeoRegistro) {
        assertMapeoRegistroAllPropertiesEquals(expectedMapeoRegistro, getPersistedMapeoRegistro(expectedMapeoRegistro));
    }

    protected void assertPersistedMapeoRegistroToMatchUpdatableProperties(MapeoRegistro expectedMapeoRegistro) {
        assertMapeoRegistroAllUpdatablePropertiesEquals(expectedMapeoRegistro, getPersistedMapeoRegistro(expectedMapeoRegistro));
    }
}
