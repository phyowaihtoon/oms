package com.hmm.dms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hmm.dms.IntegrationTest;
import com.hmm.dms.domain.DocumentHeader;
import com.hmm.dms.repository.DocumentHeaderRepository;
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
 * Integration tests for the {@link DocumentHeaderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentHeaderResourceIT {

    private static final Long DEFAULT_META_DATA_HEADER_ID = 1L;
    private static final Long UPDATED_META_DATA_HEADER_ID = 2L;

    private static final String DEFAULT_FIELD_NAMES = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAMES = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_VALUES = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_VALUES = "BBBBBBBBBB";

    private static final String DEFAULT_REPOSITORY_URL = "AAAAAAAAAA";
    private static final String UPDATED_REPOSITORY_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/document-headers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocumentHeaderRepository documentHeaderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentHeaderMockMvc;

    private DocumentHeader documentHeader;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentHeader createEntity(EntityManager em) {
        DocumentHeader documentHeader = new DocumentHeader()
            .metaDataHeaderId(DEFAULT_META_DATA_HEADER_ID)
            .fieldNames(DEFAULT_FIELD_NAMES)
            .fieldValues(DEFAULT_FIELD_VALUES)
            .repositoryURL(DEFAULT_REPOSITORY_URL);
        return documentHeader;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentHeader createUpdatedEntity(EntityManager em) {
        DocumentHeader documentHeader = new DocumentHeader()
            .metaDataHeaderId(UPDATED_META_DATA_HEADER_ID)
            .fieldNames(UPDATED_FIELD_NAMES)
            .fieldValues(UPDATED_FIELD_VALUES)
            .repositoryURL(UPDATED_REPOSITORY_URL);
        return documentHeader;
    }

    @BeforeEach
    public void initTest() {
        documentHeader = createEntity(em);
    }

    @Test
    @Transactional
    void createDocumentHeader() throws Exception {
        int databaseSizeBeforeCreate = documentHeaderRepository.findAll().size();
        // Create the DocumentHeader
        restDocumentHeaderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentHeader))
            )
            .andExpect(status().isCreated());

        // Validate the DocumentHeader in the database
        List<DocumentHeader> documentHeaderList = documentHeaderRepository.findAll();
        assertThat(documentHeaderList).hasSize(databaseSizeBeforeCreate + 1);
        DocumentHeader testDocumentHeader = documentHeaderList.get(documentHeaderList.size() - 1);
        assertThat(testDocumentHeader.getMetaDataHeaderId()).isEqualTo(DEFAULT_META_DATA_HEADER_ID);
        assertThat(testDocumentHeader.getFieldNames()).isEqualTo(DEFAULT_FIELD_NAMES);
        assertThat(testDocumentHeader.getFieldValues()).isEqualTo(DEFAULT_FIELD_VALUES);
        assertThat(testDocumentHeader.getRepositoryURL()).isEqualTo(DEFAULT_REPOSITORY_URL);
    }

    @Test
    @Transactional
    void createDocumentHeaderWithExistingId() throws Exception {
        // Create the DocumentHeader with an existing ID
        documentHeader.setId(1L);

        int databaseSizeBeforeCreate = documentHeaderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentHeaderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentHeader))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentHeader in the database
        List<DocumentHeader> documentHeaderList = documentHeaderRepository.findAll();
        assertThat(documentHeaderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDocumentHeaders() throws Exception {
        // Initialize the database
        documentHeaderRepository.saveAndFlush(documentHeader);

        // Get all the documentHeaderList
        restDocumentHeaderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentHeader.getId().intValue())))
            .andExpect(jsonPath("$.[*].metaDataHeaderId").value(hasItem(DEFAULT_META_DATA_HEADER_ID.intValue())))
            .andExpect(jsonPath("$.[*].fieldNames").value(hasItem(DEFAULT_FIELD_NAMES)))
            .andExpect(jsonPath("$.[*].fieldValues").value(hasItem(DEFAULT_FIELD_VALUES)))
            .andExpect(jsonPath("$.[*].repositoryURL").value(hasItem(DEFAULT_REPOSITORY_URL)));
    }

    @Test
    @Transactional
    void getDocumentHeader() throws Exception {
        // Initialize the database
        documentHeaderRepository.saveAndFlush(documentHeader);

        // Get the documentHeader
        restDocumentHeaderMockMvc
            .perform(get(ENTITY_API_URL_ID, documentHeader.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentHeader.getId().intValue()))
            .andExpect(jsonPath("$.metaDataHeaderId").value(DEFAULT_META_DATA_HEADER_ID.intValue()))
            .andExpect(jsonPath("$.fieldNames").value(DEFAULT_FIELD_NAMES))
            .andExpect(jsonPath("$.fieldValues").value(DEFAULT_FIELD_VALUES))
            .andExpect(jsonPath("$.repositoryURL").value(DEFAULT_REPOSITORY_URL));
    }

    @Test
    @Transactional
    void getNonExistingDocumentHeader() throws Exception {
        // Get the documentHeader
        restDocumentHeaderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDocumentHeader() throws Exception {
        // Initialize the database
        documentHeaderRepository.saveAndFlush(documentHeader);

        int databaseSizeBeforeUpdate = documentHeaderRepository.findAll().size();

        // Update the documentHeader
        DocumentHeader updatedDocumentHeader = documentHeaderRepository.findById(documentHeader.getId()).get();
        // Disconnect from session so that the updates on updatedDocumentHeader are not directly saved in db
        em.detach(updatedDocumentHeader);
        updatedDocumentHeader
            .metaDataHeaderId(UPDATED_META_DATA_HEADER_ID)
            .fieldNames(UPDATED_FIELD_NAMES)
            .fieldValues(UPDATED_FIELD_VALUES)
            .repositoryURL(UPDATED_REPOSITORY_URL);

        restDocumentHeaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDocumentHeader.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDocumentHeader))
            )
            .andExpect(status().isOk());

        // Validate the DocumentHeader in the database
        List<DocumentHeader> documentHeaderList = documentHeaderRepository.findAll();
        assertThat(documentHeaderList).hasSize(databaseSizeBeforeUpdate);
        DocumentHeader testDocumentHeader = documentHeaderList.get(documentHeaderList.size() - 1);
        assertThat(testDocumentHeader.getMetaDataHeaderId()).isEqualTo(UPDATED_META_DATA_HEADER_ID);
        assertThat(testDocumentHeader.getFieldNames()).isEqualTo(UPDATED_FIELD_NAMES);
        assertThat(testDocumentHeader.getFieldValues()).isEqualTo(UPDATED_FIELD_VALUES);
        assertThat(testDocumentHeader.getRepositoryURL()).isEqualTo(UPDATED_REPOSITORY_URL);
    }

    @Test
    @Transactional
    void putNonExistingDocumentHeader() throws Exception {
        int databaseSizeBeforeUpdate = documentHeaderRepository.findAll().size();
        documentHeader.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentHeaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentHeader.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentHeader))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentHeader in the database
        List<DocumentHeader> documentHeaderList = documentHeaderRepository.findAll();
        assertThat(documentHeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentHeader() throws Exception {
        int databaseSizeBeforeUpdate = documentHeaderRepository.findAll().size();
        documentHeader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentHeaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentHeader))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentHeader in the database
        List<DocumentHeader> documentHeaderList = documentHeaderRepository.findAll();
        assertThat(documentHeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentHeader() throws Exception {
        int databaseSizeBeforeUpdate = documentHeaderRepository.findAll().size();
        documentHeader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentHeaderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentHeader)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentHeader in the database
        List<DocumentHeader> documentHeaderList = documentHeaderRepository.findAll();
        assertThat(documentHeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentHeaderWithPatch() throws Exception {
        // Initialize the database
        documentHeaderRepository.saveAndFlush(documentHeader);

        int databaseSizeBeforeUpdate = documentHeaderRepository.findAll().size();

        // Update the documentHeader using partial update
        DocumentHeader partialUpdatedDocumentHeader = new DocumentHeader();
        partialUpdatedDocumentHeader.setId(documentHeader.getId());

        partialUpdatedDocumentHeader.fieldNames(UPDATED_FIELD_NAMES).fieldValues(UPDATED_FIELD_VALUES);

        restDocumentHeaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentHeader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentHeader))
            )
            .andExpect(status().isOk());

        // Validate the DocumentHeader in the database
        List<DocumentHeader> documentHeaderList = documentHeaderRepository.findAll();
        assertThat(documentHeaderList).hasSize(databaseSizeBeforeUpdate);
        DocumentHeader testDocumentHeader = documentHeaderList.get(documentHeaderList.size() - 1);
        assertThat(testDocumentHeader.getMetaDataHeaderId()).isEqualTo(DEFAULT_META_DATA_HEADER_ID);
        assertThat(testDocumentHeader.getFieldNames()).isEqualTo(UPDATED_FIELD_NAMES);
        assertThat(testDocumentHeader.getFieldValues()).isEqualTo(UPDATED_FIELD_VALUES);
        assertThat(testDocumentHeader.getRepositoryURL()).isEqualTo(DEFAULT_REPOSITORY_URL);
    }

    @Test
    @Transactional
    void fullUpdateDocumentHeaderWithPatch() throws Exception {
        // Initialize the database
        documentHeaderRepository.saveAndFlush(documentHeader);

        int databaseSizeBeforeUpdate = documentHeaderRepository.findAll().size();

        // Update the documentHeader using partial update
        DocumentHeader partialUpdatedDocumentHeader = new DocumentHeader();
        partialUpdatedDocumentHeader.setId(documentHeader.getId());

        partialUpdatedDocumentHeader
            .metaDataHeaderId(UPDATED_META_DATA_HEADER_ID)
            .fieldNames(UPDATED_FIELD_NAMES)
            .fieldValues(UPDATED_FIELD_VALUES)
            .repositoryURL(UPDATED_REPOSITORY_URL);

        restDocumentHeaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentHeader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentHeader))
            )
            .andExpect(status().isOk());

        // Validate the DocumentHeader in the database
        List<DocumentHeader> documentHeaderList = documentHeaderRepository.findAll();
        assertThat(documentHeaderList).hasSize(databaseSizeBeforeUpdate);
        DocumentHeader testDocumentHeader = documentHeaderList.get(documentHeaderList.size() - 1);
        assertThat(testDocumentHeader.getMetaDataHeaderId()).isEqualTo(UPDATED_META_DATA_HEADER_ID);
        assertThat(testDocumentHeader.getFieldNames()).isEqualTo(UPDATED_FIELD_NAMES);
        assertThat(testDocumentHeader.getFieldValues()).isEqualTo(UPDATED_FIELD_VALUES);
        assertThat(testDocumentHeader.getRepositoryURL()).isEqualTo(UPDATED_REPOSITORY_URL);
    }

    @Test
    @Transactional
    void patchNonExistingDocumentHeader() throws Exception {
        int databaseSizeBeforeUpdate = documentHeaderRepository.findAll().size();
        documentHeader.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentHeaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentHeader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentHeader))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentHeader in the database
        List<DocumentHeader> documentHeaderList = documentHeaderRepository.findAll();
        assertThat(documentHeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentHeader() throws Exception {
        int databaseSizeBeforeUpdate = documentHeaderRepository.findAll().size();
        documentHeader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentHeaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentHeader))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentHeader in the database
        List<DocumentHeader> documentHeaderList = documentHeaderRepository.findAll();
        assertThat(documentHeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentHeader() throws Exception {
        int databaseSizeBeforeUpdate = documentHeaderRepository.findAll().size();
        documentHeader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentHeaderMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(documentHeader))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentHeader in the database
        List<DocumentHeader> documentHeaderList = documentHeaderRepository.findAll();
        assertThat(documentHeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentHeader() throws Exception {
        // Initialize the database
        documentHeaderRepository.saveAndFlush(documentHeader);

        int databaseSizeBeforeDelete = documentHeaderRepository.findAll().size();

        // Delete the documentHeader
        restDocumentHeaderMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentHeader.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocumentHeader> documentHeaderList = documentHeaderRepository.findAll();
        assertThat(documentHeaderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
