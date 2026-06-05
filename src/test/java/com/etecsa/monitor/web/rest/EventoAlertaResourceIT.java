package com.etecsa.monitor.web.rest;

import static com.etecsa.monitor.domain.EventoAlertaAsserts.*;
import static com.etecsa.monitor.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.etecsa.monitor.IntegrationTest;
import com.etecsa.monitor.domain.AlertaConfig;
import com.etecsa.monitor.domain.EventoAlerta;
import com.etecsa.monitor.domain.Inversor;
import com.etecsa.monitor.repository.EventoAlertaRepository;
import com.etecsa.monitor.service.EventoAlertaService;
import com.etecsa.monitor.service.dto.EventoAlertaDTO;
import com.etecsa.monitor.service.mapper.EventoAlertaMapper;
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
 * Integration tests for the {@link EventoAlertaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EventoAlertaResourceIT {

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NOMBRE_ALERTA = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_ALERTA = "BBBBBBBBBB";

    private static final String DEFAULT_MENSAJE = "AAAAAAAAAA";
    private static final String UPDATED_MENSAJE = "BBBBBBBBBB";

    private static final String DEFAULT_CAMPO_MONITORIZADO = "AAAAAAAAAA";
    private static final String UPDATED_CAMPO_MONITORIZADO = "BBBBBBBBBB";

    private static final Double DEFAULT_VALOR_DETECTADO = 1D;
    private static final Double UPDATED_VALOR_DETECTADO = 2D;
    private static final Double SMALLER_VALOR_DETECTADO = 1D - 1D;

    private static final Double DEFAULT_UMBRAL = 1D;
    private static final Double UPDATED_UMBRAL = 2D;
    private static final Double SMALLER_UMBRAL = 1D - 1D;

    private static final String DEFAULT_OPERADOR = "AAAAA";
    private static final String UPDATED_OPERADOR = "BBBBB";

    private static final String DEFAULT_NIVEL_SEVERIDAD = "AAAAAAAAAA";
    private static final String UPDATED_NIVEL_SEVERIDAD = "BBBBBBBBBB";

    private static final Boolean DEFAULT_NOTIFICACION_ENVIADA = false;
    private static final Boolean UPDATED_NOTIFICACION_ENVIADA = true;

    private static final String DEFAULT_CANAL_USADO = "AAAAAAAAAA";
    private static final String UPDATED_CANAL_USADO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_RESUELTO = false;
    private static final Boolean UPDATED_RESUELTO = true;

    private static final Instant DEFAULT_FECHA_RESOLUCION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_RESOLUCION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NOTAS = "AAAAAAAAAA";
    private static final String UPDATED_NOTAS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/evento-alertas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EventoAlertaRepository eventoAlertaRepository;

    @Mock
    private EventoAlertaRepository eventoAlertaRepositoryMock;

    @Autowired
    private EventoAlertaMapper eventoAlertaMapper;

    @Mock
    private EventoAlertaService eventoAlertaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventoAlertaMockMvc;

    private EventoAlerta eventoAlerta;

    private EventoAlerta insertedEventoAlerta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventoAlerta createEntity() {
        return new EventoAlerta()
            .timestamp(DEFAULT_TIMESTAMP)
            .nombreAlerta(DEFAULT_NOMBRE_ALERTA)
            .mensaje(DEFAULT_MENSAJE)
            .campoMonitorizado(DEFAULT_CAMPO_MONITORIZADO)
            .valorDetectado(DEFAULT_VALOR_DETECTADO)
            .umbral(DEFAULT_UMBRAL)
            .operador(DEFAULT_OPERADOR)
            .nivelSeveridad(DEFAULT_NIVEL_SEVERIDAD)
            .notificacionEnviada(DEFAULT_NOTIFICACION_ENVIADA)
            .canalUsado(DEFAULT_CANAL_USADO)
            .resuelto(DEFAULT_RESUELTO)
            .fechaResolucion(DEFAULT_FECHA_RESOLUCION)
            .notas(DEFAULT_NOTAS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventoAlerta createUpdatedEntity() {
        return new EventoAlerta()
            .timestamp(UPDATED_TIMESTAMP)
            .nombreAlerta(UPDATED_NOMBRE_ALERTA)
            .mensaje(UPDATED_MENSAJE)
            .campoMonitorizado(UPDATED_CAMPO_MONITORIZADO)
            .valorDetectado(UPDATED_VALOR_DETECTADO)
            .umbral(UPDATED_UMBRAL)
            .operador(UPDATED_OPERADOR)
            .nivelSeveridad(UPDATED_NIVEL_SEVERIDAD)
            .notificacionEnviada(UPDATED_NOTIFICACION_ENVIADA)
            .canalUsado(UPDATED_CANAL_USADO)
            .resuelto(UPDATED_RESUELTO)
            .fechaResolucion(UPDATED_FECHA_RESOLUCION)
            .notas(UPDATED_NOTAS);
    }

    @BeforeEach
    void initTest() {
        eventoAlerta = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEventoAlerta != null) {
            eventoAlertaRepository.delete(insertedEventoAlerta);
            insertedEventoAlerta = null;
        }
    }

    @Test
    @Transactional
    void createEventoAlerta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EventoAlerta
        EventoAlertaDTO eventoAlertaDTO = eventoAlertaMapper.toDto(eventoAlerta);
        var returnedEventoAlertaDTO = om.readValue(
            restEventoAlertaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventoAlertaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EventoAlertaDTO.class
        );

        // Validate the EventoAlerta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEventoAlerta = eventoAlertaMapper.toEntity(returnedEventoAlertaDTO);
        assertEventoAlertaUpdatableFieldsEquals(returnedEventoAlerta, getPersistedEventoAlerta(returnedEventoAlerta));

        insertedEventoAlerta = returnedEventoAlerta;
    }

    @Test
    @Transactional
    void createEventoAlertaWithExistingId() throws Exception {
        // Create the EventoAlerta with an existing ID
        eventoAlerta.setId(1L);
        EventoAlertaDTO eventoAlertaDTO = eventoAlertaMapper.toDto(eventoAlerta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventoAlertaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventoAlertaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EventoAlerta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTimestampIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        eventoAlerta.setTimestamp(null);

        // Create the EventoAlerta, which fails.
        EventoAlertaDTO eventoAlertaDTO = eventoAlertaMapper.toDto(eventoAlerta);

        restEventoAlertaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventoAlertaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreAlertaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        eventoAlerta.setNombreAlerta(null);

        // Create the EventoAlerta, which fails.
        EventoAlertaDTO eventoAlertaDTO = eventoAlertaMapper.toDto(eventoAlerta);

        restEventoAlertaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventoAlertaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMensajeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        eventoAlerta.setMensaje(null);

        // Create the EventoAlerta, which fails.
        EventoAlertaDTO eventoAlertaDTO = eventoAlertaMapper.toDto(eventoAlerta);

        restEventoAlertaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventoAlertaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventoAlertas() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList
        restEventoAlertaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventoAlerta.getId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].nombreAlerta").value(hasItem(DEFAULT_NOMBRE_ALERTA)))
            .andExpect(jsonPath("$.[*].mensaje").value(hasItem(DEFAULT_MENSAJE)))
            .andExpect(jsonPath("$.[*].campoMonitorizado").value(hasItem(DEFAULT_CAMPO_MONITORIZADO)))
            .andExpect(jsonPath("$.[*].valorDetectado").value(hasItem(DEFAULT_VALOR_DETECTADO)))
            .andExpect(jsonPath("$.[*].umbral").value(hasItem(DEFAULT_UMBRAL)))
            .andExpect(jsonPath("$.[*].operador").value(hasItem(DEFAULT_OPERADOR)))
            .andExpect(jsonPath("$.[*].nivelSeveridad").value(hasItem(DEFAULT_NIVEL_SEVERIDAD)))
            .andExpect(jsonPath("$.[*].notificacionEnviada").value(hasItem(DEFAULT_NOTIFICACION_ENVIADA)))
            .andExpect(jsonPath("$.[*].canalUsado").value(hasItem(DEFAULT_CANAL_USADO)))
            .andExpect(jsonPath("$.[*].resuelto").value(hasItem(DEFAULT_RESUELTO)))
            .andExpect(jsonPath("$.[*].fechaResolucion").value(hasItem(DEFAULT_FECHA_RESOLUCION.toString())))
            .andExpect(jsonPath("$.[*].notas").value(hasItem(DEFAULT_NOTAS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEventoAlertasWithEagerRelationshipsIsEnabled() throws Exception {
        when(eventoAlertaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEventoAlertaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(eventoAlertaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEventoAlertasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(eventoAlertaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEventoAlertaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(eventoAlertaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEventoAlerta() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get the eventoAlerta
        restEventoAlertaMockMvc
            .perform(get(ENTITY_API_URL_ID, eventoAlerta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventoAlerta.getId().intValue()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.nombreAlerta").value(DEFAULT_NOMBRE_ALERTA))
            .andExpect(jsonPath("$.mensaje").value(DEFAULT_MENSAJE))
            .andExpect(jsonPath("$.campoMonitorizado").value(DEFAULT_CAMPO_MONITORIZADO))
            .andExpect(jsonPath("$.valorDetectado").value(DEFAULT_VALOR_DETECTADO))
            .andExpect(jsonPath("$.umbral").value(DEFAULT_UMBRAL))
            .andExpect(jsonPath("$.operador").value(DEFAULT_OPERADOR))
            .andExpect(jsonPath("$.nivelSeveridad").value(DEFAULT_NIVEL_SEVERIDAD))
            .andExpect(jsonPath("$.notificacionEnviada").value(DEFAULT_NOTIFICACION_ENVIADA))
            .andExpect(jsonPath("$.canalUsado").value(DEFAULT_CANAL_USADO))
            .andExpect(jsonPath("$.resuelto").value(DEFAULT_RESUELTO))
            .andExpect(jsonPath("$.fechaResolucion").value(DEFAULT_FECHA_RESOLUCION.toString()))
            .andExpect(jsonPath("$.notas").value(DEFAULT_NOTAS));
    }

    @Test
    @Transactional
    void getEventoAlertasByIdFiltering() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        Long id = eventoAlerta.getId();

        defaultEventoAlertaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEventoAlertaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEventoAlertaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventoAlertasByTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where timestamp equals to
        defaultEventoAlertaFiltering("timestamp.equals=" + DEFAULT_TIMESTAMP, "timestamp.equals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    void getAllEventoAlertasByTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where timestamp in
        defaultEventoAlertaFiltering("timestamp.in=" + DEFAULT_TIMESTAMP + "," + UPDATED_TIMESTAMP, "timestamp.in=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    void getAllEventoAlertasByTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where timestamp is not null
        defaultEventoAlertaFiltering("timestamp.specified=true", "timestamp.specified=false");
    }

    @Test
    @Transactional
    void getAllEventoAlertasByNombreAlertaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where nombreAlerta equals to
        defaultEventoAlertaFiltering("nombreAlerta.equals=" + DEFAULT_NOMBRE_ALERTA, "nombreAlerta.equals=" + UPDATED_NOMBRE_ALERTA);
    }

    @Test
    @Transactional
    void getAllEventoAlertasByNombreAlertaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where nombreAlerta in
        defaultEventoAlertaFiltering(
            "nombreAlerta.in=" + DEFAULT_NOMBRE_ALERTA + "," + UPDATED_NOMBRE_ALERTA,
            "nombreAlerta.in=" + UPDATED_NOMBRE_ALERTA
        );
    }

    @Test
    @Transactional
    void getAllEventoAlertasByNombreAlertaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where nombreAlerta is not null
        defaultEventoAlertaFiltering("nombreAlerta.specified=true", "nombreAlerta.specified=false");
    }

    @Test
    @Transactional
    void getAllEventoAlertasByNombreAlertaContainsSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where nombreAlerta contains
        defaultEventoAlertaFiltering("nombreAlerta.contains=" + DEFAULT_NOMBRE_ALERTA, "nombreAlerta.contains=" + UPDATED_NOMBRE_ALERTA);
    }

    @Test
    @Transactional
    void getAllEventoAlertasByNombreAlertaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where nombreAlerta does not contain
        defaultEventoAlertaFiltering(
            "nombreAlerta.doesNotContain=" + UPDATED_NOMBRE_ALERTA,
            "nombreAlerta.doesNotContain=" + DEFAULT_NOMBRE_ALERTA
        );
    }

    @Test
    @Transactional
    void getAllEventoAlertasByMensajeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where mensaje equals to
        defaultEventoAlertaFiltering("mensaje.equals=" + DEFAULT_MENSAJE, "mensaje.equals=" + UPDATED_MENSAJE);
    }

    @Test
    @Transactional
    void getAllEventoAlertasByMensajeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where mensaje in
        defaultEventoAlertaFiltering("mensaje.in=" + DEFAULT_MENSAJE + "," + UPDATED_MENSAJE, "mensaje.in=" + UPDATED_MENSAJE);
    }

    @Test
    @Transactional
    void getAllEventoAlertasByMensajeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where mensaje is not null
        defaultEventoAlertaFiltering("mensaje.specified=true", "mensaje.specified=false");
    }

    @Test
    @Transactional
    void getAllEventoAlertasByMensajeContainsSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where mensaje contains
        defaultEventoAlertaFiltering("mensaje.contains=" + DEFAULT_MENSAJE, "mensaje.contains=" + UPDATED_MENSAJE);
    }

    @Test
    @Transactional
    void getAllEventoAlertasByMensajeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where mensaje does not contain
        defaultEventoAlertaFiltering("mensaje.doesNotContain=" + UPDATED_MENSAJE, "mensaje.doesNotContain=" + DEFAULT_MENSAJE);
    }

    @Test
    @Transactional
    void getAllEventoAlertasByCampoMonitorizadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where campoMonitorizado equals to
        defaultEventoAlertaFiltering(
            "campoMonitorizado.equals=" + DEFAULT_CAMPO_MONITORIZADO,
            "campoMonitorizado.equals=" + UPDATED_CAMPO_MONITORIZADO
        );
    }

    @Test
    @Transactional
    void getAllEventoAlertasByCampoMonitorizadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where campoMonitorizado in
        defaultEventoAlertaFiltering(
            "campoMonitorizado.in=" + DEFAULT_CAMPO_MONITORIZADO + "," + UPDATED_CAMPO_MONITORIZADO,
            "campoMonitorizado.in=" + UPDATED_CAMPO_MONITORIZADO
        );
    }

    @Test
    @Transactional
    void getAllEventoAlertasByCampoMonitorizadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where campoMonitorizado is not null
        defaultEventoAlertaFiltering("campoMonitorizado.specified=true", "campoMonitorizado.specified=false");
    }

    @Test
    @Transactional
    void getAllEventoAlertasByCampoMonitorizadoContainsSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where campoMonitorizado contains
        defaultEventoAlertaFiltering(
            "campoMonitorizado.contains=" + DEFAULT_CAMPO_MONITORIZADO,
            "campoMonitorizado.contains=" + UPDATED_CAMPO_MONITORIZADO
        );
    }

    @Test
    @Transactional
    void getAllEventoAlertasByCampoMonitorizadoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where campoMonitorizado does not contain
        defaultEventoAlertaFiltering(
            "campoMonitorizado.doesNotContain=" + UPDATED_CAMPO_MONITORIZADO,
            "campoMonitorizado.doesNotContain=" + DEFAULT_CAMPO_MONITORIZADO
        );
    }

    @Test
    @Transactional
    void getAllEventoAlertasByValorDetectadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where valorDetectado equals to
        defaultEventoAlertaFiltering(
            "valorDetectado.equals=" + DEFAULT_VALOR_DETECTADO,
            "valorDetectado.equals=" + UPDATED_VALOR_DETECTADO
        );
    }

    @Test
    @Transactional
    void getAllEventoAlertasByValorDetectadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where valorDetectado in
        defaultEventoAlertaFiltering(
            "valorDetectado.in=" + DEFAULT_VALOR_DETECTADO + "," + UPDATED_VALOR_DETECTADO,
            "valorDetectado.in=" + UPDATED_VALOR_DETECTADO
        );
    }

    @Test
    @Transactional
    void getAllEventoAlertasByValorDetectadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where valorDetectado is not null
        defaultEventoAlertaFiltering("valorDetectado.specified=true", "valorDetectado.specified=false");
    }

    @Test
    @Transactional
    void getAllEventoAlertasByValorDetectadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where valorDetectado is greater than or equal to
        defaultEventoAlertaFiltering(
            "valorDetectado.greaterThanOrEqual=" + DEFAULT_VALOR_DETECTADO,
            "valorDetectado.greaterThanOrEqual=" + UPDATED_VALOR_DETECTADO
        );
    }

    @Test
    @Transactional
    void getAllEventoAlertasByValorDetectadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where valorDetectado is less than or equal to
        defaultEventoAlertaFiltering(
            "valorDetectado.lessThanOrEqual=" + DEFAULT_VALOR_DETECTADO,
            "valorDetectado.lessThanOrEqual=" + SMALLER_VALOR_DETECTADO
        );
    }

    @Test
    @Transactional
    void getAllEventoAlertasByValorDetectadoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where valorDetectado is less than
        defaultEventoAlertaFiltering(
            "valorDetectado.lessThan=" + UPDATED_VALOR_DETECTADO,
            "valorDetectado.lessThan=" + DEFAULT_VALOR_DETECTADO
        );
    }

    @Test
    @Transactional
    void getAllEventoAlertasByValorDetectadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where valorDetectado is greater than
        defaultEventoAlertaFiltering(
            "valorDetectado.greaterThan=" + SMALLER_VALOR_DETECTADO,
            "valorDetectado.greaterThan=" + DEFAULT_VALOR_DETECTADO
        );
    }

    @Test
    @Transactional
    void getAllEventoAlertasByUmbralIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where umbral equals to
        defaultEventoAlertaFiltering("umbral.equals=" + DEFAULT_UMBRAL, "umbral.equals=" + UPDATED_UMBRAL);
    }

    @Test
    @Transactional
    void getAllEventoAlertasByUmbralIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where umbral in
        defaultEventoAlertaFiltering("umbral.in=" + DEFAULT_UMBRAL + "," + UPDATED_UMBRAL, "umbral.in=" + UPDATED_UMBRAL);
    }

    @Test
    @Transactional
    void getAllEventoAlertasByUmbralIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where umbral is not null
        defaultEventoAlertaFiltering("umbral.specified=true", "umbral.specified=false");
    }

    @Test
    @Transactional
    void getAllEventoAlertasByUmbralIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where umbral is greater than or equal to
        defaultEventoAlertaFiltering("umbral.greaterThanOrEqual=" + DEFAULT_UMBRAL, "umbral.greaterThanOrEqual=" + UPDATED_UMBRAL);
    }

    @Test
    @Transactional
    void getAllEventoAlertasByUmbralIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where umbral is less than or equal to
        defaultEventoAlertaFiltering("umbral.lessThanOrEqual=" + DEFAULT_UMBRAL, "umbral.lessThanOrEqual=" + SMALLER_UMBRAL);
    }

    @Test
    @Transactional
    void getAllEventoAlertasByUmbralIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where umbral is less than
        defaultEventoAlertaFiltering("umbral.lessThan=" + UPDATED_UMBRAL, "umbral.lessThan=" + DEFAULT_UMBRAL);
    }

    @Test
    @Transactional
    void getAllEventoAlertasByUmbralIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where umbral is greater than
        defaultEventoAlertaFiltering("umbral.greaterThan=" + SMALLER_UMBRAL, "umbral.greaterThan=" + DEFAULT_UMBRAL);
    }

    @Test
    @Transactional
    void getAllEventoAlertasByOperadorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where operador equals to
        defaultEventoAlertaFiltering("operador.equals=" + DEFAULT_OPERADOR, "operador.equals=" + UPDATED_OPERADOR);
    }

    @Test
    @Transactional
    void getAllEventoAlertasByOperadorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where operador in
        defaultEventoAlertaFiltering("operador.in=" + DEFAULT_OPERADOR + "," + UPDATED_OPERADOR, "operador.in=" + UPDATED_OPERADOR);
    }

    @Test
    @Transactional
    void getAllEventoAlertasByOperadorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where operador is not null
        defaultEventoAlertaFiltering("operador.specified=true", "operador.specified=false");
    }

    @Test
    @Transactional
    void getAllEventoAlertasByOperadorContainsSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where operador contains
        defaultEventoAlertaFiltering("operador.contains=" + DEFAULT_OPERADOR, "operador.contains=" + UPDATED_OPERADOR);
    }

    @Test
    @Transactional
    void getAllEventoAlertasByOperadorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where operador does not contain
        defaultEventoAlertaFiltering("operador.doesNotContain=" + UPDATED_OPERADOR, "operador.doesNotContain=" + DEFAULT_OPERADOR);
    }

    @Test
    @Transactional
    void getAllEventoAlertasByNivelSeveridadIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where nivelSeveridad equals to
        defaultEventoAlertaFiltering(
            "nivelSeveridad.equals=" + DEFAULT_NIVEL_SEVERIDAD,
            "nivelSeveridad.equals=" + UPDATED_NIVEL_SEVERIDAD
        );
    }

    @Test
    @Transactional
    void getAllEventoAlertasByNivelSeveridadIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where nivelSeveridad in
        defaultEventoAlertaFiltering(
            "nivelSeveridad.in=" + DEFAULT_NIVEL_SEVERIDAD + "," + UPDATED_NIVEL_SEVERIDAD,
            "nivelSeveridad.in=" + UPDATED_NIVEL_SEVERIDAD
        );
    }

    @Test
    @Transactional
    void getAllEventoAlertasByNivelSeveridadIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where nivelSeveridad is not null
        defaultEventoAlertaFiltering("nivelSeveridad.specified=true", "nivelSeveridad.specified=false");
    }

    @Test
    @Transactional
    void getAllEventoAlertasByNivelSeveridadContainsSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where nivelSeveridad contains
        defaultEventoAlertaFiltering(
            "nivelSeveridad.contains=" + DEFAULT_NIVEL_SEVERIDAD,
            "nivelSeveridad.contains=" + UPDATED_NIVEL_SEVERIDAD
        );
    }

    @Test
    @Transactional
    void getAllEventoAlertasByNivelSeveridadNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where nivelSeveridad does not contain
        defaultEventoAlertaFiltering(
            "nivelSeveridad.doesNotContain=" + UPDATED_NIVEL_SEVERIDAD,
            "nivelSeveridad.doesNotContain=" + DEFAULT_NIVEL_SEVERIDAD
        );
    }

    @Test
    @Transactional
    void getAllEventoAlertasByNotificacionEnviadaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where notificacionEnviada equals to
        defaultEventoAlertaFiltering(
            "notificacionEnviada.equals=" + DEFAULT_NOTIFICACION_ENVIADA,
            "notificacionEnviada.equals=" + UPDATED_NOTIFICACION_ENVIADA
        );
    }

    @Test
    @Transactional
    void getAllEventoAlertasByNotificacionEnviadaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where notificacionEnviada in
        defaultEventoAlertaFiltering(
            "notificacionEnviada.in=" + DEFAULT_NOTIFICACION_ENVIADA + "," + UPDATED_NOTIFICACION_ENVIADA,
            "notificacionEnviada.in=" + UPDATED_NOTIFICACION_ENVIADA
        );
    }

    @Test
    @Transactional
    void getAllEventoAlertasByNotificacionEnviadaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where notificacionEnviada is not null
        defaultEventoAlertaFiltering("notificacionEnviada.specified=true", "notificacionEnviada.specified=false");
    }

    @Test
    @Transactional
    void getAllEventoAlertasByCanalUsadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where canalUsado equals to
        defaultEventoAlertaFiltering("canalUsado.equals=" + DEFAULT_CANAL_USADO, "canalUsado.equals=" + UPDATED_CANAL_USADO);
    }

    @Test
    @Transactional
    void getAllEventoAlertasByCanalUsadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where canalUsado in
        defaultEventoAlertaFiltering(
            "canalUsado.in=" + DEFAULT_CANAL_USADO + "," + UPDATED_CANAL_USADO,
            "canalUsado.in=" + UPDATED_CANAL_USADO
        );
    }

    @Test
    @Transactional
    void getAllEventoAlertasByCanalUsadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where canalUsado is not null
        defaultEventoAlertaFiltering("canalUsado.specified=true", "canalUsado.specified=false");
    }

    @Test
    @Transactional
    void getAllEventoAlertasByCanalUsadoContainsSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where canalUsado contains
        defaultEventoAlertaFiltering("canalUsado.contains=" + DEFAULT_CANAL_USADO, "canalUsado.contains=" + UPDATED_CANAL_USADO);
    }

    @Test
    @Transactional
    void getAllEventoAlertasByCanalUsadoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where canalUsado does not contain
        defaultEventoAlertaFiltering(
            "canalUsado.doesNotContain=" + UPDATED_CANAL_USADO,
            "canalUsado.doesNotContain=" + DEFAULT_CANAL_USADO
        );
    }

    @Test
    @Transactional
    void getAllEventoAlertasByResueltoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where resuelto equals to
        defaultEventoAlertaFiltering("resuelto.equals=" + DEFAULT_RESUELTO, "resuelto.equals=" + UPDATED_RESUELTO);
    }

    @Test
    @Transactional
    void getAllEventoAlertasByResueltoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where resuelto in
        defaultEventoAlertaFiltering("resuelto.in=" + DEFAULT_RESUELTO + "," + UPDATED_RESUELTO, "resuelto.in=" + UPDATED_RESUELTO);
    }

    @Test
    @Transactional
    void getAllEventoAlertasByResueltoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where resuelto is not null
        defaultEventoAlertaFiltering("resuelto.specified=true", "resuelto.specified=false");
    }

    @Test
    @Transactional
    void getAllEventoAlertasByFechaResolucionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where fechaResolucion equals to
        defaultEventoAlertaFiltering(
            "fechaResolucion.equals=" + DEFAULT_FECHA_RESOLUCION,
            "fechaResolucion.equals=" + UPDATED_FECHA_RESOLUCION
        );
    }

    @Test
    @Transactional
    void getAllEventoAlertasByFechaResolucionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where fechaResolucion in
        defaultEventoAlertaFiltering(
            "fechaResolucion.in=" + DEFAULT_FECHA_RESOLUCION + "," + UPDATED_FECHA_RESOLUCION,
            "fechaResolucion.in=" + UPDATED_FECHA_RESOLUCION
        );
    }

    @Test
    @Transactional
    void getAllEventoAlertasByFechaResolucionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where fechaResolucion is not null
        defaultEventoAlertaFiltering("fechaResolucion.specified=true", "fechaResolucion.specified=false");
    }

    @Test
    @Transactional
    void getAllEventoAlertasByNotasIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where notas equals to
        defaultEventoAlertaFiltering("notas.equals=" + DEFAULT_NOTAS, "notas.equals=" + UPDATED_NOTAS);
    }

    @Test
    @Transactional
    void getAllEventoAlertasByNotasIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where notas in
        defaultEventoAlertaFiltering("notas.in=" + DEFAULT_NOTAS + "," + UPDATED_NOTAS, "notas.in=" + UPDATED_NOTAS);
    }

    @Test
    @Transactional
    void getAllEventoAlertasByNotasIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where notas is not null
        defaultEventoAlertaFiltering("notas.specified=true", "notas.specified=false");
    }

    @Test
    @Transactional
    void getAllEventoAlertasByNotasContainsSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where notas contains
        defaultEventoAlertaFiltering("notas.contains=" + DEFAULT_NOTAS, "notas.contains=" + UPDATED_NOTAS);
    }

    @Test
    @Transactional
    void getAllEventoAlertasByNotasNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        // Get all the eventoAlertaList where notas does not contain
        defaultEventoAlertaFiltering("notas.doesNotContain=" + UPDATED_NOTAS, "notas.doesNotContain=" + DEFAULT_NOTAS);
    }

    @Test
    @Transactional
    void getAllEventoAlertasByInversorIsEqualToSomething() throws Exception {
        Inversor inversor;
        if (TestUtil.findAll(em, Inversor.class).isEmpty()) {
            eventoAlertaRepository.saveAndFlush(eventoAlerta);
            inversor = InversorResourceIT.createEntity();
        } else {
            inversor = TestUtil.findAll(em, Inversor.class).get(0);
        }
        em.persist(inversor);
        em.flush();
        eventoAlerta.setInversor(inversor);
        eventoAlertaRepository.saveAndFlush(eventoAlerta);
        Long inversorId = inversor.getId();
        // Get all the eventoAlertaList where inversor equals to inversorId
        defaultEventoAlertaShouldBeFound("inversorId.equals=" + inversorId);

        // Get all the eventoAlertaList where inversor equals to (inversorId + 1)
        defaultEventoAlertaShouldNotBeFound("inversorId.equals=" + (inversorId + 1));
    }

    @Test
    @Transactional
    void getAllEventoAlertasByAlertaConfigIsEqualToSomething() throws Exception {
        AlertaConfig alertaConfig;
        if (TestUtil.findAll(em, AlertaConfig.class).isEmpty()) {
            eventoAlertaRepository.saveAndFlush(eventoAlerta);
            alertaConfig = AlertaConfigResourceIT.createEntity();
        } else {
            alertaConfig = TestUtil.findAll(em, AlertaConfig.class).get(0);
        }
        em.persist(alertaConfig);
        em.flush();
        eventoAlerta.setAlertaConfig(alertaConfig);
        eventoAlertaRepository.saveAndFlush(eventoAlerta);
        Long alertaConfigId = alertaConfig.getId();
        // Get all the eventoAlertaList where alertaConfig equals to alertaConfigId
        defaultEventoAlertaShouldBeFound("alertaConfigId.equals=" + alertaConfigId);

        // Get all the eventoAlertaList where alertaConfig equals to (alertaConfigId + 1)
        defaultEventoAlertaShouldNotBeFound("alertaConfigId.equals=" + (alertaConfigId + 1));
    }

    private void defaultEventoAlertaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEventoAlertaShouldBeFound(shouldBeFound);
        defaultEventoAlertaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventoAlertaShouldBeFound(String filter) throws Exception {
        restEventoAlertaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventoAlerta.getId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].nombreAlerta").value(hasItem(DEFAULT_NOMBRE_ALERTA)))
            .andExpect(jsonPath("$.[*].mensaje").value(hasItem(DEFAULT_MENSAJE)))
            .andExpect(jsonPath("$.[*].campoMonitorizado").value(hasItem(DEFAULT_CAMPO_MONITORIZADO)))
            .andExpect(jsonPath("$.[*].valorDetectado").value(hasItem(DEFAULT_VALOR_DETECTADO)))
            .andExpect(jsonPath("$.[*].umbral").value(hasItem(DEFAULT_UMBRAL)))
            .andExpect(jsonPath("$.[*].operador").value(hasItem(DEFAULT_OPERADOR)))
            .andExpect(jsonPath("$.[*].nivelSeveridad").value(hasItem(DEFAULT_NIVEL_SEVERIDAD)))
            .andExpect(jsonPath("$.[*].notificacionEnviada").value(hasItem(DEFAULT_NOTIFICACION_ENVIADA)))
            .andExpect(jsonPath("$.[*].canalUsado").value(hasItem(DEFAULT_CANAL_USADO)))
            .andExpect(jsonPath("$.[*].resuelto").value(hasItem(DEFAULT_RESUELTO)))
            .andExpect(jsonPath("$.[*].fechaResolucion").value(hasItem(DEFAULT_FECHA_RESOLUCION.toString())))
            .andExpect(jsonPath("$.[*].notas").value(hasItem(DEFAULT_NOTAS)));

        // Check, that the count call also returns 1
        restEventoAlertaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventoAlertaShouldNotBeFound(String filter) throws Exception {
        restEventoAlertaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventoAlertaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventoAlerta() throws Exception {
        // Get the eventoAlerta
        restEventoAlertaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventoAlerta() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the eventoAlerta
        EventoAlerta updatedEventoAlerta = eventoAlertaRepository.findById(eventoAlerta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEventoAlerta are not directly saved in db
        em.detach(updatedEventoAlerta);
        updatedEventoAlerta
            .timestamp(UPDATED_TIMESTAMP)
            .nombreAlerta(UPDATED_NOMBRE_ALERTA)
            .mensaje(UPDATED_MENSAJE)
            .campoMonitorizado(UPDATED_CAMPO_MONITORIZADO)
            .valorDetectado(UPDATED_VALOR_DETECTADO)
            .umbral(UPDATED_UMBRAL)
            .operador(UPDATED_OPERADOR)
            .nivelSeveridad(UPDATED_NIVEL_SEVERIDAD)
            .notificacionEnviada(UPDATED_NOTIFICACION_ENVIADA)
            .canalUsado(UPDATED_CANAL_USADO)
            .resuelto(UPDATED_RESUELTO)
            .fechaResolucion(UPDATED_FECHA_RESOLUCION)
            .notas(UPDATED_NOTAS);
        EventoAlertaDTO eventoAlertaDTO = eventoAlertaMapper.toDto(updatedEventoAlerta);

        restEventoAlertaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventoAlertaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(eventoAlertaDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventoAlerta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEventoAlertaToMatchAllProperties(updatedEventoAlerta);
    }

    @Test
    @Transactional
    void putNonExistingEventoAlerta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventoAlerta.setId(longCount.incrementAndGet());

        // Create the EventoAlerta
        EventoAlertaDTO eventoAlertaDTO = eventoAlertaMapper.toDto(eventoAlerta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventoAlertaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventoAlertaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(eventoAlertaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventoAlerta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventoAlerta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventoAlerta.setId(longCount.incrementAndGet());

        // Create the EventoAlerta
        EventoAlertaDTO eventoAlertaDTO = eventoAlertaMapper.toDto(eventoAlerta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventoAlertaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(eventoAlertaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventoAlerta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventoAlerta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventoAlerta.setId(longCount.incrementAndGet());

        // Create the EventoAlerta
        EventoAlertaDTO eventoAlertaDTO = eventoAlertaMapper.toDto(eventoAlerta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventoAlertaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eventoAlertaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventoAlerta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventoAlertaWithPatch() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the eventoAlerta using partial update
        EventoAlerta partialUpdatedEventoAlerta = new EventoAlerta();
        partialUpdatedEventoAlerta.setId(eventoAlerta.getId());

        partialUpdatedEventoAlerta
            .timestamp(UPDATED_TIMESTAMP)
            .campoMonitorizado(UPDATED_CAMPO_MONITORIZADO)
            .operador(UPDATED_OPERADOR)
            .nivelSeveridad(UPDATED_NIVEL_SEVERIDAD)
            .notificacionEnviada(UPDATED_NOTIFICACION_ENVIADA)
            .canalUsado(UPDATED_CANAL_USADO);

        restEventoAlertaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventoAlerta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEventoAlerta))
            )
            .andExpect(status().isOk());

        // Validate the EventoAlerta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEventoAlertaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEventoAlerta, eventoAlerta),
            getPersistedEventoAlerta(eventoAlerta)
        );
    }

    @Test
    @Transactional
    void fullUpdateEventoAlertaWithPatch() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the eventoAlerta using partial update
        EventoAlerta partialUpdatedEventoAlerta = new EventoAlerta();
        partialUpdatedEventoAlerta.setId(eventoAlerta.getId());

        partialUpdatedEventoAlerta
            .timestamp(UPDATED_TIMESTAMP)
            .nombreAlerta(UPDATED_NOMBRE_ALERTA)
            .mensaje(UPDATED_MENSAJE)
            .campoMonitorizado(UPDATED_CAMPO_MONITORIZADO)
            .valorDetectado(UPDATED_VALOR_DETECTADO)
            .umbral(UPDATED_UMBRAL)
            .operador(UPDATED_OPERADOR)
            .nivelSeveridad(UPDATED_NIVEL_SEVERIDAD)
            .notificacionEnviada(UPDATED_NOTIFICACION_ENVIADA)
            .canalUsado(UPDATED_CANAL_USADO)
            .resuelto(UPDATED_RESUELTO)
            .fechaResolucion(UPDATED_FECHA_RESOLUCION)
            .notas(UPDATED_NOTAS);

        restEventoAlertaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventoAlerta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEventoAlerta))
            )
            .andExpect(status().isOk());

        // Validate the EventoAlerta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEventoAlertaUpdatableFieldsEquals(partialUpdatedEventoAlerta, getPersistedEventoAlerta(partialUpdatedEventoAlerta));
    }

    @Test
    @Transactional
    void patchNonExistingEventoAlerta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventoAlerta.setId(longCount.incrementAndGet());

        // Create the EventoAlerta
        EventoAlertaDTO eventoAlertaDTO = eventoAlertaMapper.toDto(eventoAlerta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventoAlertaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventoAlertaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(eventoAlertaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventoAlerta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventoAlerta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventoAlerta.setId(longCount.incrementAndGet());

        // Create the EventoAlerta
        EventoAlertaDTO eventoAlertaDTO = eventoAlertaMapper.toDto(eventoAlerta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventoAlertaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(eventoAlertaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventoAlerta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventoAlerta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eventoAlerta.setId(longCount.incrementAndGet());

        // Create the EventoAlerta
        EventoAlertaDTO eventoAlertaDTO = eventoAlertaMapper.toDto(eventoAlerta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventoAlertaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(eventoAlertaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventoAlerta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventoAlerta() throws Exception {
        // Initialize the database
        insertedEventoAlerta = eventoAlertaRepository.saveAndFlush(eventoAlerta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the eventoAlerta
        restEventoAlertaMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventoAlerta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return eventoAlertaRepository.count();
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

    protected EventoAlerta getPersistedEventoAlerta(EventoAlerta eventoAlerta) {
        return eventoAlertaRepository.findById(eventoAlerta.getId()).orElseThrow();
    }

    protected void assertPersistedEventoAlertaToMatchAllProperties(EventoAlerta expectedEventoAlerta) {
        assertEventoAlertaAllPropertiesEquals(expectedEventoAlerta, getPersistedEventoAlerta(expectedEventoAlerta));
    }

    protected void assertPersistedEventoAlertaToMatchUpdatableProperties(EventoAlerta expectedEventoAlerta) {
        assertEventoAlertaAllUpdatablePropertiesEquals(expectedEventoAlerta, getPersistedEventoAlerta(expectedEventoAlerta));
    }
}
