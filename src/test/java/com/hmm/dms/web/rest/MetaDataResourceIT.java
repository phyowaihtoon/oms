package com.hmm.dms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hmm.dms.IntegrationTest;
import com.hmm.dms.domain.MetaData;
import com.hmm.dms.repository.MetaDataRepository;
import com.hmm.dms.service.dto.MetaDataDTO;
import com.hmm.dms.service.mapper.MetaDataMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MetaDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MetaDataResourceIT {

    private static final Long DEFAULT_HEADER_ID = 1L;
    private static final Long UPDATED_HEADER_ID = 2L;

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_IS_REQUIRED = "AAAAAAAAAA";
    private static final String UPDATED_IS_REQUIRED = "BBBBBBBBBB";

    private static final Integer DEFAULT_FIELD_ORDER = 1;
    private static final Integer UPDATED_FIELD_ORDER = 2;

    private static final String DEFAULT_FIELD_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/meta-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MetaDataRepository metaDataRepository;

    @Autowired
    private MetaDataMapper metaDataMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMetaDataMockMvc;

    private MetaData metaData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MetaData createEntity(EntityManager em) {
        MetaData metaData = new MetaData()
            .headerId(DEFAULT_HEADER_ID)
            .fieldName(DEFAULT_FIELD_NAME)
            .fieldType(DEFAULT_FIELD_TYPE)
            .isRequired(DEFAULT_IS_REQUIRED)
            .fieldOrder(DEFAULT_FIELD_ORDER)
            .fieldValue(DEFAULT_FIELD_VALUE);
        return metaData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MetaData createUpdatedEntity(EntityManager em) {
        MetaData metaData = new MetaData()
            .headerId(UPDATED_HEADER_ID)
            .fieldName(UPDATED_FIELD_NAME)
            .fieldType(UPDATED_FIELD_TYPE)
            .isRequired(UPDATED_IS_REQUIRED)
            .fieldOrder(UPDATED_FIELD_ORDER)
            .fieldValue(UPDATED_FIELD_VALUE);
        return metaData;
    }

    @BeforeEach
    public void initTest() {
        metaData = createEntity(em);
    }

    @Test
    @Transactional
    void createMetaData() throws Exception {
        int databaseSizeBeforeCreate = metaDataRepository.findAll().size();
        // Create the MetaData
        MetaDataDTO metaDataDTO = metaDataMapper.toDto(metaData);
        restMetaDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(metaDataDTO)))
            .andExpect(status().isCreated());

        // Validate the MetaData in the database
        List<MetaData> metaDataList = metaDataRepository.findAll();
        assertThat(metaDataList).hasSize(databaseSizeBeforeCreate + 1);
        MetaData testMetaData = metaDataList.get(metaDataList.size() - 1);
        assertThat(testMetaData.getHeaderId()).isEqualTo(DEFAULT_HEADER_ID);
        assertThat(testMetaData.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
        assertThat(testMetaData.getFieldType()).isEqualTo(DEFAULT_FIELD_TYPE);
        assertThat(testMetaData.getIsRequired()).isEqualTo(DEFAULT_IS_REQUIRED);
        assertThat(testMetaData.getFieldOrder()).isEqualTo(DEFAULT_FIELD_ORDER);
        assertThat(testMetaData.getFieldValue()).isEqualTo(DEFAULT_FIELD_VALUE);
    }

    @Test
    @Transactional
    void createMetaDataWithExistingId() throws Exception {
        // Create the MetaData with an existing ID
        metaData.setId(1L);
        MetaDataDTO metaDataDTO = metaDataMapper.toDto(metaData);

        int databaseSizeBeforeCreate = metaDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMetaDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(metaDataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MetaData in the database
        List<MetaData> metaDataList = metaDataRepository.findAll();
        assertThat(metaDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkHeaderIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = metaDataRepository.findAll().size();
        // set the field null
        metaData.setHeaderId(null);

        // Create the MetaData, which fails.
        MetaDataDTO metaDataDTO = metaDataMapper.toDto(metaData);

        restMetaDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(metaDataDTO)))
            .andExpect(status().isBadRequest());

        List<MetaData> metaDataList = metaDataRepository.findAll();
        assertThat(metaDataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFieldNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = metaDataRepository.findAll().size();
        // set the field null
        metaData.setFieldName(null);

        // Create the MetaData, which fails.
        MetaDataDTO metaDataDTO = metaDataMapper.toDto(metaData);

        restMetaDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(metaDataDTO)))
            .andExpect(status().isBadRequest());

        List<MetaData> metaDataList = metaDataRepository.findAll();
        assertThat(metaDataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFieldTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = metaDataRepository.findAll().size();
        // set the field null
        metaData.setFieldType(null);

        // Create the MetaData, which fails.
        MetaDataDTO metaDataDTO = metaDataMapper.toDto(metaData);

        restMetaDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(metaDataDTO)))
            .andExpect(status().isBadRequest());

        List<MetaData> metaDataList = metaDataRepository.findAll();
        assertThat(metaDataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsRequiredIsRequired() throws Exception {
        int databaseSizeBeforeTest = metaDataRepository.findAll().size();
        // set the field null
        metaData.setIsRequired(null);

        // Create the MetaData, which fails.
        MetaDataDTO metaDataDTO = metaDataMapper.toDto(metaData);

        restMetaDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(metaDataDTO)))
            .andExpect(status().isBadRequest());

        List<MetaData> metaDataList = metaDataRepository.findAll();
        assertThat(metaDataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMetaData() throws Exception {
        // Initialize the database
        metaDataRepository.saveAndFlush(metaData);

        // Get all the metaDataList
        restMetaDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metaData.getId().intValue())))
            .andExpect(jsonPath("$.[*].headerId").value(hasItem(DEFAULT_HEADER_ID.intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)))
            .andExpect(jsonPath("$.[*].fieldType").value(hasItem(DEFAULT_FIELD_TYPE)))
            .andExpect(jsonPath("$.[*].isRequired").value(hasItem(DEFAULT_IS_REQUIRED)))
            .andExpect(jsonPath("$.[*].fieldOrder").value(hasItem(DEFAULT_FIELD_ORDER)))
            .andExpect(jsonPath("$.[*].fieldValue").value(hasItem(DEFAULT_FIELD_VALUE)));
    }

    @Test
    @Transactional
    void getMetaData() throws Exception {
        // Initialize the database
        metaDataRepository.saveAndFlush(metaData);

        // Get the metaData
        restMetaDataMockMvc
            .perform(get(ENTITY_API_URL_ID, metaData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(metaData.getId().intValue()))
            .andExpect(jsonPath("$.headerId").value(DEFAULT_HEADER_ID.intValue()))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME))
            .andExpect(jsonPath("$.fieldType").value(DEFAULT_FIELD_TYPE))
            .andExpect(jsonPath("$.isRequired").value(DEFAULT_IS_REQUIRED))
            .andExpect(jsonPath("$.fieldOrder").value(DEFAULT_FIELD_ORDER))
            .andExpect(jsonPath("$.fieldValue").value(DEFAULT_FIELD_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingMetaData() throws Exception {
        // Get the metaData
        restMetaDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMetaData() throws Exception {
        // Initialize the database
        metaDataRepository.saveAndFlush(metaData);

        int databaseSizeBeforeUpdate = metaDataRepository.findAll().size();

        // Update the metaData
        MetaData updatedMetaData = metaDataRepository.findById(metaData.getId()).get();
        // Disconnect from session so that the updates on updatedMetaData are not directly saved in db
        em.detach(updatedMetaData);
        updatedMetaData
            .headerId(UPDATED_HEADER_ID)
            .fieldName(UPDATED_FIELD_NAME)
            .fieldType(UPDATED_FIELD_TYPE)
            .isRequired(UPDATED_IS_REQUIRED)
            .fieldOrder(UPDATED_FIELD_ORDER)
            .fieldValue(UPDATED_FIELD_VALUE);
        MetaDataDTO metaDataDTO = metaDataMapper.toDto(updatedMetaData);

        restMetaDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, metaDataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(metaDataDTO))
            )
            .andExpect(status().isOk());

        // Validate the MetaData in the database
        List<MetaData> metaDataList = metaDataRepository.findAll();
        assertThat(metaDataList).hasSize(databaseSizeBeforeUpdate);
        MetaData testMetaData = metaDataList.get(metaDataList.size() - 1);
        assertThat(testMetaData.getHeaderId()).isEqualTo(UPDATED_HEADER_ID);
        assertThat(testMetaData.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
        assertThat(testMetaData.getFieldType()).isEqualTo(UPDATED_FIELD_TYPE);
        assertThat(testMetaData.getIsRequired()).isEqualTo(UPDATED_IS_REQUIRED);
        assertThat(testMetaData.getFieldOrder()).isEqualTo(UPDATED_FIELD_ORDER);
        assertThat(testMetaData.getFieldValue()).isEqualTo(UPDATED_FIELD_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingMetaData() throws Exception {
        int databaseSizeBeforeUpdate = metaDataRepository.findAll().size();
        metaData.setId(count.incrementAndGet());

        // Create the MetaData
        MetaDataDTO metaDataDTO = metaDataMapper.toDto(metaData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetaDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, metaDataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(metaDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaData in the database
        List<MetaData> metaDataList = metaDataRepository.findAll();
        assertThat(metaDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMetaData() throws Exception {
        int databaseSizeBeforeUpdate = metaDataRepository.findAll().size();
        metaData.setId(count.incrementAndGet());

        // Create the MetaData
        MetaDataDTO metaDataDTO = metaDataMapper.toDto(metaData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(metaDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaData in the database
        List<MetaData> metaDataList = metaDataRepository.findAll();
        assertThat(metaDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMetaData() throws Exception {
        int databaseSizeBeforeUpdate = metaDataRepository.findAll().size();
        metaData.setId(count.incrementAndGet());

        // Create the MetaData
        MetaDataDTO metaDataDTO = metaDataMapper.toDto(metaData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaDataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(metaDataDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MetaData in the database
        List<MetaData> metaDataList = metaDataRepository.findAll();
        assertThat(metaDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMetaDataWithPatch() throws Exception {
        // Initialize the database
        metaDataRepository.saveAndFlush(metaData);

        int databaseSizeBeforeUpdate = metaDataRepository.findAll().size();

        // Update the metaData using partial update
        MetaData partialUpdatedMetaData = new MetaData();
        partialUpdatedMetaData.setId(metaData.getId());

        partialUpdatedMetaData
            .headerId(UPDATED_HEADER_ID)
            .fieldType(UPDATED_FIELD_TYPE)
            .fieldOrder(UPDATED_FIELD_ORDER)
            .fieldValue(UPDATED_FIELD_VALUE);

        restMetaDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMetaData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMetaData))
            )
            .andExpect(status().isOk());

        // Validate the MetaData in the database
        List<MetaData> metaDataList = metaDataRepository.findAll();
        assertThat(metaDataList).hasSize(databaseSizeBeforeUpdate);
        MetaData testMetaData = metaDataList.get(metaDataList.size() - 1);
        assertThat(testMetaData.getHeaderId()).isEqualTo(UPDATED_HEADER_ID);
        assertThat(testMetaData.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
        assertThat(testMetaData.getFieldType()).isEqualTo(UPDATED_FIELD_TYPE);
        assertThat(testMetaData.getIsRequired()).isEqualTo(DEFAULT_IS_REQUIRED);
        assertThat(testMetaData.getFieldOrder()).isEqualTo(UPDATED_FIELD_ORDER);
        assertThat(testMetaData.getFieldValue()).isEqualTo(UPDATED_FIELD_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateMetaDataWithPatch() throws Exception {
        // Initialize the database
        metaDataRepository.saveAndFlush(metaData);

        int databaseSizeBeforeUpdate = metaDataRepository.findAll().size();

        // Update the metaData using partial update
        MetaData partialUpdatedMetaData = new MetaData();
        partialUpdatedMetaData.setId(metaData.getId());

        partialUpdatedMetaData
            .headerId(UPDATED_HEADER_ID)
            .fieldName(UPDATED_FIELD_NAME)
            .fieldType(UPDATED_FIELD_TYPE)
            .isRequired(UPDATED_IS_REQUIRED)
            .fieldOrder(UPDATED_FIELD_ORDER)
            .fieldValue(UPDATED_FIELD_VALUE);

        restMetaDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMetaData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMetaData))
            )
            .andExpect(status().isOk());

        // Validate the MetaData in the database
        List<MetaData> metaDataList = metaDataRepository.findAll();
        assertThat(metaDataList).hasSize(databaseSizeBeforeUpdate);
        MetaData testMetaData = metaDataList.get(metaDataList.size() - 1);
        assertThat(testMetaData.getHeaderId()).isEqualTo(UPDATED_HEADER_ID);
        assertThat(testMetaData.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
        assertThat(testMetaData.getFieldType()).isEqualTo(UPDATED_FIELD_TYPE);
        assertThat(testMetaData.getIsRequired()).isEqualTo(UPDATED_IS_REQUIRED);
        assertThat(testMetaData.getFieldOrder()).isEqualTo(UPDATED_FIELD_ORDER);
        assertThat(testMetaData.getFieldValue()).isEqualTo(UPDATED_FIELD_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingMetaData() throws Exception {
        int databaseSizeBeforeUpdate = metaDataRepository.findAll().size();
        metaData.setId(count.incrementAndGet());

        // Create the MetaData
        MetaDataDTO metaDataDTO = metaDataMapper.toDto(metaData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetaDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, metaDataDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(metaDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaData in the database
        List<MetaData> metaDataList = metaDataRepository.findAll();
        assertThat(metaDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMetaData() throws Exception {
        int databaseSizeBeforeUpdate = metaDataRepository.findAll().size();
        metaData.setId(count.incrementAndGet());

        // Create the MetaData
        MetaDataDTO metaDataDTO = metaDataMapper.toDto(metaData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(metaDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaData in the database
        List<MetaData> metaDataList = metaDataRepository.findAll();
        assertThat(metaDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMetaData() throws Exception {
        int databaseSizeBeforeUpdate = metaDataRepository.findAll().size();
        metaData.setId(count.incrementAndGet());

        // Create the MetaData
        MetaDataDTO metaDataDTO = metaDataMapper.toDto(metaData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaDataMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(metaDataDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MetaData in the database
        List<MetaData> metaDataList = metaDataRepository.findAll();
        assertThat(metaDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMetaData() throws Exception {
        // Initialize the database
        metaDataRepository.saveAndFlush(metaData);

        int databaseSizeBeforeDelete = metaDataRepository.findAll().size();

        // Delete the metaData
        restMetaDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, metaData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MetaData> metaDataList = metaDataRepository.findAll();
        assertThat(metaDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
