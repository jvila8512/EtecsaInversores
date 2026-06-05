package com.etecsa.monitor.web.rest;

import static com.etecsa.monitor.domain.ModeloInversorAsserts.*;
import static com.etecsa.monitor.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.etecsa.monitor.IntegrationTest;
import com.etecsa.monitor.domain.ModeloInversor;
import com.etecsa.monitor.repository.ModeloInversorRepository;
import com.etecsa.monitor.service.ModeloInversorService;
import com.etecsa.monitor.service.dto.ModeloInversorDTO;
import com.etecsa.monitor.service.mapper.ModeloInversorMapper;
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
 * Integration tests for the {@link ModeloInversorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ModeloInversorResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CAPACIDAD_VA = 1;
    private static final Integer UPDATED_CAPACIDAD_VA = 2;

    private static final Integer DEFAULT_VOLTAJE_SISTEMA_NOM = 1;
    private static final Integer UPDATED_VOLTAJE_SISTEMA_NOM = 2;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/modelo-inversors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ModeloInversorRepository modeloInversorRepository;

    @Mock
    private ModeloInversorRepository modeloInversorRepositoryMock;

    @Autowired
    private ModeloInversorMapper modeloInversorMapper;

    @Mock
    private ModeloInversorService modeloInversorServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restModeloInversorMockMvc;

    private ModeloInversor modeloInversor;

    private ModeloInversor insertedModeloInversor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModeloInversor createEntity() {
        return new ModeloInversor()
            .nombre(DEFAULT_NOMBRE)
            .capacidadVa(DEFAULT_CAPACIDAD_VA)
            .voltajeSistemaNom(DEFAULT_VOLTAJE_SISTEMA_NOM)
            .descripcion(DEFAULT_DESCRIPCION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModeloInversor createUpdatedEntity() {
        return new ModeloInversor()
            .nombre(UPDATED_NOMBRE)
            .capacidadVa(UPDATED_CAPACIDAD_VA)
            .voltajeSistemaNom(UPDATED_VOLTAJE_SISTEMA_NOM)
            .descripcion(UPDATED_DESCRIPCION);
    }

    @BeforeEach
    void initTest() {
        modeloInversor = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedModeloInversor != null) {
            modeloInversorRepository.delete(insertedModeloInversor);
            insertedModeloInversor = null;
        }
    }

    @Test
    @Transactional
    void createModeloInversor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ModeloInversor
        ModeloInversorDTO modeloInversorDTO = modeloInversorMapper.toDto(modeloInversor);
        var returnedModeloInversorDTO = om.readValue(
            restModeloInversorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(modeloInversorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ModeloInversorDTO.class
        );

        // Validate the ModeloInversor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedModeloInversor = modeloInversorMapper.toEntity(returnedModeloInversorDTO);
        assertModeloInversorUpdatableFieldsEquals(returnedModeloInversor, getPersistedModeloInversor(returnedModeloInversor));

        insertedModeloInversor = returnedModeloInversor;
    }

    @Test
    @Transactional
    void createModeloInversorWithExistingId() throws Exception {
        // Create the ModeloInversor with an existing ID
        modeloInversor.setId(1L);
        ModeloInversorDTO modeloInversorDTO = modeloInversorMapper.toDto(modeloInversor);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restModeloInversorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(modeloInversorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ModeloInversor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        modeloInversor.setNombre(null);

        // Create the ModeloInversor, which fails.
        ModeloInversorDTO modeloInversorDTO = modeloInversorMapper.toDto(modeloInversor);

        restModeloInversorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(modeloInversorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCapacidadVaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        modeloInversor.setCapacidadVa(null);

        // Create the ModeloInversor, which fails.
        ModeloInversorDTO modeloInversorDTO = modeloInversorMapper.toDto(modeloInversor);

        restModeloInversorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(modeloInversorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVoltajeSistemaNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        modeloInversor.setVoltajeSistemaNom(null);

        // Create the ModeloInversor, which fails.
        ModeloInversorDTO modeloInversorDTO = modeloInversorMapper.toDto(modeloInversor);

        restModeloInversorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(modeloInversorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllModeloInversors() throws Exception {
        // Initialize the database
        insertedModeloInversor = modeloInversorRepository.saveAndFlush(modeloInversor);

        // Get all the modeloInversorList
        restModeloInversorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(modeloInversor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].capacidadVa").value(hasItem(DEFAULT_CAPACIDAD_VA)))
            .andExpect(jsonPath("$.[*].voltajeSistemaNom").value(hasItem(DEFAULT_VOLTAJE_SISTEMA_NOM)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllModeloInversorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(modeloInversorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restModeloInversorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(modeloInversorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllModeloInversorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(modeloInversorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restModeloInversorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(modeloInversorRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getModeloInversor() throws Exception {
        // Initialize the database
        insertedModeloInversor = modeloInversorRepository.saveAndFlush(modeloInversor);

        // Get the modeloInversor
        restModeloInversorMockMvc
            .perform(get(ENTITY_API_URL_ID, modeloInversor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(modeloInversor.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.capacidadVa").value(DEFAULT_CAPACIDAD_VA))
            .andExpect(jsonPath("$.voltajeSistemaNom").value(DEFAULT_VOLTAJE_SISTEMA_NOM))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingModeloInversor() throws Exception {
        // Get the modeloInversor
        restModeloInversorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingModeloInversor() throws Exception {
        // Initialize the database
        insertedModeloInversor = modeloInversorRepository.saveAndFlush(modeloInversor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the modeloInversor
        ModeloInversor updatedModeloInversor = modeloInversorRepository.findById(modeloInversor.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedModeloInversor are not directly saved in db
        em.detach(updatedModeloInversor);
        updatedModeloInversor
            .nombre(UPDATED_NOMBRE)
            .capacidadVa(UPDATED_CAPACIDAD_VA)
            .voltajeSistemaNom(UPDATED_VOLTAJE_SISTEMA_NOM)
            .descripcion(UPDATED_DESCRIPCION);
        ModeloInversorDTO modeloInversorDTO = modeloInversorMapper.toDto(updatedModeloInversor);

        restModeloInversorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, modeloInversorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(modeloInversorDTO))
            )
            .andExpect(status().isOk());

        // Validate the ModeloInversor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedModeloInversorToMatchAllProperties(updatedModeloInversor);
    }

    @Test
    @Transactional
    void putNonExistingModeloInversor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        modeloInversor.setId(longCount.incrementAndGet());

        // Create the ModeloInversor
        ModeloInversorDTO modeloInversorDTO = modeloInversorMapper.toDto(modeloInversor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModeloInversorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, modeloInversorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(modeloInversorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ModeloInversor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchModeloInversor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        modeloInversor.setId(longCount.incrementAndGet());

        // Create the ModeloInversor
        ModeloInversorDTO modeloInversorDTO = modeloInversorMapper.toDto(modeloInversor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModeloInversorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(modeloInversorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ModeloInversor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamModeloInversor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        modeloInversor.setId(longCount.incrementAndGet());

        // Create the ModeloInversor
        ModeloInversorDTO modeloInversorDTO = modeloInversorMapper.toDto(modeloInversor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModeloInversorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(modeloInversorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ModeloInversor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateModeloInversorWithPatch() throws Exception {
        // Initialize the database
        insertedModeloInversor = modeloInversorRepository.saveAndFlush(modeloInversor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the modeloInversor using partial update
        ModeloInversor partialUpdatedModeloInversor = new ModeloInversor();
        partialUpdatedModeloInversor.setId(modeloInversor.getId());

        partialUpdatedModeloInversor.nombre(UPDATED_NOMBRE).voltajeSistemaNom(UPDATED_VOLTAJE_SISTEMA_NOM);

        restModeloInversorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModeloInversor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedModeloInversor))
            )
            .andExpect(status().isOk());

        // Validate the ModeloInversor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertModeloInversorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedModeloInversor, modeloInversor),
            getPersistedModeloInversor(modeloInversor)
        );
    }

    @Test
    @Transactional
    void fullUpdateModeloInversorWithPatch() throws Exception {
        // Initialize the database
        insertedModeloInversor = modeloInversorRepository.saveAndFlush(modeloInversor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the modeloInversor using partial update
        ModeloInversor partialUpdatedModeloInversor = new ModeloInversor();
        partialUpdatedModeloInversor.setId(modeloInversor.getId());

        partialUpdatedModeloInversor
            .nombre(UPDATED_NOMBRE)
            .capacidadVa(UPDATED_CAPACIDAD_VA)
            .voltajeSistemaNom(UPDATED_VOLTAJE_SISTEMA_NOM)
            .descripcion(UPDATED_DESCRIPCION);

        restModeloInversorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModeloInversor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedModeloInversor))
            )
            .andExpect(status().isOk());

        // Validate the ModeloInversor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertModeloInversorUpdatableFieldsEquals(partialUpdatedModeloInversor, getPersistedModeloInversor(partialUpdatedModeloInversor));
    }

    @Test
    @Transactional
    void patchNonExistingModeloInversor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        modeloInversor.setId(longCount.incrementAndGet());

        // Create the ModeloInversor
        ModeloInversorDTO modeloInversorDTO = modeloInversorMapper.toDto(modeloInversor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModeloInversorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, modeloInversorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(modeloInversorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ModeloInversor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchModeloInversor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        modeloInversor.setId(longCount.incrementAndGet());

        // Create the ModeloInversor
        ModeloInversorDTO modeloInversorDTO = modeloInversorMapper.toDto(modeloInversor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModeloInversorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(modeloInversorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ModeloInversor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamModeloInversor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        modeloInversor.setId(longCount.incrementAndGet());

        // Create the ModeloInversor
        ModeloInversorDTO modeloInversorDTO = modeloInversorMapper.toDto(modeloInversor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModeloInversorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(modeloInversorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ModeloInversor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteModeloInversor() throws Exception {
        // Initialize the database
        insertedModeloInversor = modeloInversorRepository.saveAndFlush(modeloInversor);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the modeloInversor
        restModeloInversorMockMvc
            .perform(delete(ENTITY_API_URL_ID, modeloInversor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return modeloInversorRepository.count();
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

    protected ModeloInversor getPersistedModeloInversor(ModeloInversor modeloInversor) {
        return modeloInversorRepository.findById(modeloInversor.getId()).orElseThrow();
    }

    protected void assertPersistedModeloInversorToMatchAllProperties(ModeloInversor expectedModeloInversor) {
        assertModeloInversorAllPropertiesEquals(expectedModeloInversor, getPersistedModeloInversor(expectedModeloInversor));
    }

    protected void assertPersistedModeloInversorToMatchUpdatableProperties(ModeloInversor expectedModeloInversor) {
        assertModeloInversorAllUpdatablePropertiesEquals(expectedModeloInversor, getPersistedModeloInversor(expectedModeloInversor));
    }
}
