package com.etecsa.monitor.web.rest;

import static com.etecsa.monitor.domain.AlertaConfigAsserts.*;
import static com.etecsa.monitor.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.etecsa.monitor.IntegrationTest;
import com.etecsa.monitor.domain.AlertaConfig;
import com.etecsa.monitor.repository.AlertaConfigRepository;
import com.etecsa.monitor.service.AlertaConfigService;
import com.etecsa.monitor.service.dto.AlertaConfigDTO;
import com.etecsa.monitor.service.mapper.AlertaConfigMapper;
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
 * Integration tests for the {@link AlertaConfigResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AlertaConfigResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_CAMPO_MONITORIZADO = "AAAAAAAAAA";
    private static final String UPDATED_CAMPO_MONITORIZADO = "BBBBBBBBBB";

    private static final String DEFAULT_OPERADOR = "AAAAA";
    private static final String UPDATED_OPERADOR = "BBBBB";

    private static final Double DEFAULT_UMBRAL = 1D;
    private static final Double UPDATED_UMBRAL = 2D;

    private static final Boolean DEFAULT_ACTIVA = false;
    private static final Boolean UPDATED_ACTIVA = true;

    private static final String DEFAULT_NIVEL_SEVERIDAD = "AAAAAAAAAA";
    private static final String UPDATED_NIVEL_SEVERIDAD = "BBBBBBBBBB";

    private static final String DEFAULT_CANAL_NOTIFICACION = "AAAAAAAAAA";
    private static final String UPDATED_CANAL_NOTIFICACION = "BBBBBBBBBB";

    private static final Integer DEFAULT_COOLDOWN_MINUTOS = 1;
    private static final Integer UPDATED_COOLDOWN_MINUTOS = 2;

    private static final String ENTITY_API_URL = "/api/alerta-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlertaConfigRepository alertaConfigRepository;

    @Mock
    private AlertaConfigRepository alertaConfigRepositoryMock;

    @Autowired
    private AlertaConfigMapper alertaConfigMapper;

    @Mock
    private AlertaConfigService alertaConfigServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlertaConfigMockMvc;

    private AlertaConfig alertaConfig;

    private AlertaConfig insertedAlertaConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlertaConfig createEntity() {
        return new AlertaConfig()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .campoMonitorizado(DEFAULT_CAMPO_MONITORIZADO)
            .operador(DEFAULT_OPERADOR)
            .umbral(DEFAULT_UMBRAL)
            .activa(DEFAULT_ACTIVA)
            .nivelSeveridad(DEFAULT_NIVEL_SEVERIDAD)
            .canalNotificacion(DEFAULT_CANAL_NOTIFICACION)
            .cooldownMinutos(DEFAULT_COOLDOWN_MINUTOS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlertaConfig createUpdatedEntity() {
        return new AlertaConfig()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .campoMonitorizado(UPDATED_CAMPO_MONITORIZADO)
            .operador(UPDATED_OPERADOR)
            .umbral(UPDATED_UMBRAL)
            .activa(UPDATED_ACTIVA)
            .nivelSeveridad(UPDATED_NIVEL_SEVERIDAD)
            .canalNotificacion(UPDATED_CANAL_NOTIFICACION)
            .cooldownMinutos(UPDATED_COOLDOWN_MINUTOS);
    }

    @BeforeEach
    void initTest() {
        alertaConfig = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAlertaConfig != null) {
            alertaConfigRepository.delete(insertedAlertaConfig);
            insertedAlertaConfig = null;
        }
    }

    @Test
    @Transactional
    void createAlertaConfig() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlertaConfig
        AlertaConfigDTO alertaConfigDTO = alertaConfigMapper.toDto(alertaConfig);
        var returnedAlertaConfigDTO = om.readValue(
            restAlertaConfigMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alertaConfigDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlertaConfigDTO.class
        );

        // Validate the AlertaConfig in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlertaConfig = alertaConfigMapper.toEntity(returnedAlertaConfigDTO);
        assertAlertaConfigUpdatableFieldsEquals(returnedAlertaConfig, getPersistedAlertaConfig(returnedAlertaConfig));

        insertedAlertaConfig = returnedAlertaConfig;
    }

    @Test
    @Transactional
    void createAlertaConfigWithExistingId() throws Exception {
        // Create the AlertaConfig with an existing ID
        alertaConfig.setId(1L);
        AlertaConfigDTO alertaConfigDTO = alertaConfigMapper.toDto(alertaConfig);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlertaConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alertaConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlertaConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alertaConfig.setNombre(null);

        // Create the AlertaConfig, which fails.
        AlertaConfigDTO alertaConfigDTO = alertaConfigMapper.toDto(alertaConfig);

        restAlertaConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alertaConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCampoMonitorizadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alertaConfig.setCampoMonitorizado(null);

        // Create the AlertaConfig, which fails.
        AlertaConfigDTO alertaConfigDTO = alertaConfigMapper.toDto(alertaConfig);

        restAlertaConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alertaConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOperadorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alertaConfig.setOperador(null);

        // Create the AlertaConfig, which fails.
        AlertaConfigDTO alertaConfigDTO = alertaConfigMapper.toDto(alertaConfig);

        restAlertaConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alertaConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUmbralIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alertaConfig.setUmbral(null);

        // Create the AlertaConfig, which fails.
        AlertaConfigDTO alertaConfigDTO = alertaConfigMapper.toDto(alertaConfig);

        restAlertaConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alertaConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alertaConfig.setActiva(null);

        // Create the AlertaConfig, which fails.
        AlertaConfigDTO alertaConfigDTO = alertaConfigMapper.toDto(alertaConfig);

        restAlertaConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alertaConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlertaConfigs() throws Exception {
        // Initialize the database
        insertedAlertaConfig = alertaConfigRepository.saveAndFlush(alertaConfig);

        // Get all the alertaConfigList
        restAlertaConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alertaConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].campoMonitorizado").value(hasItem(DEFAULT_CAMPO_MONITORIZADO)))
            .andExpect(jsonPath("$.[*].operador").value(hasItem(DEFAULT_OPERADOR)))
            .andExpect(jsonPath("$.[*].umbral").value(hasItem(DEFAULT_UMBRAL)))
            .andExpect(jsonPath("$.[*].activa").value(hasItem(DEFAULT_ACTIVA)))
            .andExpect(jsonPath("$.[*].nivelSeveridad").value(hasItem(DEFAULT_NIVEL_SEVERIDAD)))
            .andExpect(jsonPath("$.[*].canalNotificacion").value(hasItem(DEFAULT_CANAL_NOTIFICACION)))
            .andExpect(jsonPath("$.[*].cooldownMinutos").value(hasItem(DEFAULT_COOLDOWN_MINUTOS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAlertaConfigsWithEagerRelationshipsIsEnabled() throws Exception {
        when(alertaConfigServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAlertaConfigMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(alertaConfigServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAlertaConfigsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(alertaConfigServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAlertaConfigMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(alertaConfigRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAlertaConfig() throws Exception {
        // Initialize the database
        insertedAlertaConfig = alertaConfigRepository.saveAndFlush(alertaConfig);

        // Get the alertaConfig
        restAlertaConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, alertaConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alertaConfig.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.campoMonitorizado").value(DEFAULT_CAMPO_MONITORIZADO))
            .andExpect(jsonPath("$.operador").value(DEFAULT_OPERADOR))
            .andExpect(jsonPath("$.umbral").value(DEFAULT_UMBRAL))
            .andExpect(jsonPath("$.activa").value(DEFAULT_ACTIVA))
            .andExpect(jsonPath("$.nivelSeveridad").value(DEFAULT_NIVEL_SEVERIDAD))
            .andExpect(jsonPath("$.canalNotificacion").value(DEFAULT_CANAL_NOTIFICACION))
            .andExpect(jsonPath("$.cooldownMinutos").value(DEFAULT_COOLDOWN_MINUTOS));
    }

    @Test
    @Transactional
    void getNonExistingAlertaConfig() throws Exception {
        // Get the alertaConfig
        restAlertaConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlertaConfig() throws Exception {
        // Initialize the database
        insertedAlertaConfig = alertaConfigRepository.saveAndFlush(alertaConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alertaConfig
        AlertaConfig updatedAlertaConfig = alertaConfigRepository.findById(alertaConfig.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlertaConfig are not directly saved in db
        em.detach(updatedAlertaConfig);
        updatedAlertaConfig
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .campoMonitorizado(UPDATED_CAMPO_MONITORIZADO)
            .operador(UPDATED_OPERADOR)
            .umbral(UPDATED_UMBRAL)
            .activa(UPDATED_ACTIVA)
            .nivelSeveridad(UPDATED_NIVEL_SEVERIDAD)
            .canalNotificacion(UPDATED_CANAL_NOTIFICACION)
            .cooldownMinutos(UPDATED_COOLDOWN_MINUTOS);
        AlertaConfigDTO alertaConfigDTO = alertaConfigMapper.toDto(updatedAlertaConfig);

        restAlertaConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alertaConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alertaConfigDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlertaConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlertaConfigToMatchAllProperties(updatedAlertaConfig);
    }

    @Test
    @Transactional
    void putNonExistingAlertaConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alertaConfig.setId(longCount.incrementAndGet());

        // Create the AlertaConfig
        AlertaConfigDTO alertaConfigDTO = alertaConfigMapper.toDto(alertaConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlertaConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alertaConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alertaConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlertaConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlertaConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alertaConfig.setId(longCount.incrementAndGet());

        // Create the AlertaConfig
        AlertaConfigDTO alertaConfigDTO = alertaConfigMapper.toDto(alertaConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlertaConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alertaConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlertaConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlertaConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alertaConfig.setId(longCount.incrementAndGet());

        // Create the AlertaConfig
        AlertaConfigDTO alertaConfigDTO = alertaConfigMapper.toDto(alertaConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlertaConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alertaConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlertaConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlertaConfigWithPatch() throws Exception {
        // Initialize the database
        insertedAlertaConfig = alertaConfigRepository.saveAndFlush(alertaConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alertaConfig using partial update
        AlertaConfig partialUpdatedAlertaConfig = new AlertaConfig();
        partialUpdatedAlertaConfig.setId(alertaConfig.getId());

        partialUpdatedAlertaConfig.nombre(UPDATED_NOMBRE).activa(UPDATED_ACTIVA);

        restAlertaConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlertaConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlertaConfig))
            )
            .andExpect(status().isOk());

        // Validate the AlertaConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlertaConfigUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlertaConfig, alertaConfig),
            getPersistedAlertaConfig(alertaConfig)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlertaConfigWithPatch() throws Exception {
        // Initialize the database
        insertedAlertaConfig = alertaConfigRepository.saveAndFlush(alertaConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alertaConfig using partial update
        AlertaConfig partialUpdatedAlertaConfig = new AlertaConfig();
        partialUpdatedAlertaConfig.setId(alertaConfig.getId());

        partialUpdatedAlertaConfig
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .campoMonitorizado(UPDATED_CAMPO_MONITORIZADO)
            .operador(UPDATED_OPERADOR)
            .umbral(UPDATED_UMBRAL)
            .activa(UPDATED_ACTIVA)
            .nivelSeveridad(UPDATED_NIVEL_SEVERIDAD)
            .canalNotificacion(UPDATED_CANAL_NOTIFICACION)
            .cooldownMinutos(UPDATED_COOLDOWN_MINUTOS);

        restAlertaConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlertaConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlertaConfig))
            )
            .andExpect(status().isOk());

        // Validate the AlertaConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlertaConfigUpdatableFieldsEquals(partialUpdatedAlertaConfig, getPersistedAlertaConfig(partialUpdatedAlertaConfig));
    }

    @Test
    @Transactional
    void patchNonExistingAlertaConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alertaConfig.setId(longCount.incrementAndGet());

        // Create the AlertaConfig
        AlertaConfigDTO alertaConfigDTO = alertaConfigMapper.toDto(alertaConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlertaConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alertaConfigDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alertaConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlertaConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlertaConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alertaConfig.setId(longCount.incrementAndGet());

        // Create the AlertaConfig
        AlertaConfigDTO alertaConfigDTO = alertaConfigMapper.toDto(alertaConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlertaConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alertaConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlertaConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlertaConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alertaConfig.setId(longCount.incrementAndGet());

        // Create the AlertaConfig
        AlertaConfigDTO alertaConfigDTO = alertaConfigMapper.toDto(alertaConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlertaConfigMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alertaConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlertaConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlertaConfig() throws Exception {
        // Initialize the database
        insertedAlertaConfig = alertaConfigRepository.saveAndFlush(alertaConfig);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alertaConfig
        restAlertaConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, alertaConfig.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alertaConfigRepository.count();
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

    protected AlertaConfig getPersistedAlertaConfig(AlertaConfig alertaConfig) {
        return alertaConfigRepository.findById(alertaConfig.getId()).orElseThrow();
    }

    protected void assertPersistedAlertaConfigToMatchAllProperties(AlertaConfig expectedAlertaConfig) {
        assertAlertaConfigAllPropertiesEquals(expectedAlertaConfig, getPersistedAlertaConfig(expectedAlertaConfig));
    }

    protected void assertPersistedAlertaConfigToMatchUpdatableProperties(AlertaConfig expectedAlertaConfig) {
        assertAlertaConfigAllUpdatablePropertiesEquals(expectedAlertaConfig, getPersistedAlertaConfig(expectedAlertaConfig));
    }
}
