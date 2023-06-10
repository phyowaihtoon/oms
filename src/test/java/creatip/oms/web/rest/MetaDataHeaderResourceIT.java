package creatip.oms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import creatip.oms.IntegrationTest;
import creatip.oms.domain.MetaDataHeader;
import creatip.oms.repository.MetaDataHeaderRepository;
import creatip.oms.web.rest.MetaDataHeaderResource;
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
 * Integration tests for the {@link MetaDataHeaderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MetaDataHeaderResourceIT {

    private static final String DEFAULT_DOC_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_DOC_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/meta-data-headers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MetaDataHeaderRepository metaDataHeaderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMetaDataHeaderMockMvc;

    private MetaDataHeader metaDataHeader;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MetaDataHeader createEntity(EntityManager em) {
        MetaDataHeader metaDataHeader = new MetaDataHeader().docTitle(DEFAULT_DOC_TITLE);
        return metaDataHeader;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MetaDataHeader createUpdatedEntity(EntityManager em) {
        MetaDataHeader metaDataHeader = new MetaDataHeader().docTitle(UPDATED_DOC_TITLE);
        return metaDataHeader;
    }

    @BeforeEach
    public void initTest() {
        metaDataHeader = createEntity(em);
    }

    @Test
    @Transactional
    void createMetaDataHeader() throws Exception {
        int databaseSizeBeforeCreate = metaDataHeaderRepository.findAll().size();
        // Create the MetaDataHeader
        restMetaDataHeaderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(metaDataHeader))
            )
            .andExpect(status().isCreated());

        // Validate the MetaDataHeader in the database
        List<MetaDataHeader> metaDataHeaderList = metaDataHeaderRepository.findAll();
        assertThat(metaDataHeaderList).hasSize(databaseSizeBeforeCreate + 1);
        MetaDataHeader testMetaDataHeader = metaDataHeaderList.get(metaDataHeaderList.size() - 1);
        assertThat(testMetaDataHeader.getDocTitle()).isEqualTo(DEFAULT_DOC_TITLE);
    }

    @Test
    @Transactional
    void createMetaDataHeaderWithExistingId() throws Exception {
        // Create the MetaDataHeader with an existing ID
        metaDataHeader.setId(1L);

        int databaseSizeBeforeCreate = metaDataHeaderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMetaDataHeaderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(metaDataHeader))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaDataHeader in the database
        List<MetaDataHeader> metaDataHeaderList = metaDataHeaderRepository.findAll();
        assertThat(metaDataHeaderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = metaDataHeaderRepository.findAll().size();
        // set the field null
        metaDataHeader.setDocTitle(null);

        // Create the MetaDataHeader, which fails.

        restMetaDataHeaderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(metaDataHeader))
            )
            .andExpect(status().isBadRequest());

        List<MetaDataHeader> metaDataHeaderList = metaDataHeaderRepository.findAll();
        assertThat(metaDataHeaderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMetaDataHeaders() throws Exception {
        // Initialize the database
        metaDataHeaderRepository.saveAndFlush(metaDataHeader);

        // Get all the metaDataHeaderList
        restMetaDataHeaderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metaDataHeader.getId().intValue())))
            .andExpect(jsonPath("$.[*].docTitle").value(hasItem(DEFAULT_DOC_TITLE)));
    }

    @Test
    @Transactional
    void getMetaDataHeader() throws Exception {
        // Initialize the database
        metaDataHeaderRepository.saveAndFlush(metaDataHeader);

        // Get the metaDataHeader
        restMetaDataHeaderMockMvc
            .perform(get(ENTITY_API_URL_ID, metaDataHeader.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(metaDataHeader.getId().intValue()))
            .andExpect(jsonPath("$.docTitle").value(DEFAULT_DOC_TITLE));
    }

    @Test
    @Transactional
    void getNonExistingMetaDataHeader() throws Exception {
        // Get the metaDataHeader
        restMetaDataHeaderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMetaDataHeader() throws Exception {
        // Initialize the database
        metaDataHeaderRepository.saveAndFlush(metaDataHeader);

        int databaseSizeBeforeUpdate = metaDataHeaderRepository.findAll().size();

        // Update the metaDataHeader
        MetaDataHeader updatedMetaDataHeader = metaDataHeaderRepository.findById(metaDataHeader.getId()).get();
        // Disconnect from session so that the updates on updatedMetaDataHeader are not directly saved in db
        em.detach(updatedMetaDataHeader);
        updatedMetaDataHeader.docTitle(UPDATED_DOC_TITLE);

        restMetaDataHeaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMetaDataHeader.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMetaDataHeader))
            )
            .andExpect(status().isOk());

        // Validate the MetaDataHeader in the database
        List<MetaDataHeader> metaDataHeaderList = metaDataHeaderRepository.findAll();
        assertThat(metaDataHeaderList).hasSize(databaseSizeBeforeUpdate);
        MetaDataHeader testMetaDataHeader = metaDataHeaderList.get(metaDataHeaderList.size() - 1);
        assertThat(testMetaDataHeader.getDocTitle()).isEqualTo(UPDATED_DOC_TITLE);
    }

    @Test
    @Transactional
    void putNonExistingMetaDataHeader() throws Exception {
        int databaseSizeBeforeUpdate = metaDataHeaderRepository.findAll().size();
        metaDataHeader.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetaDataHeaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, metaDataHeader.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(metaDataHeader))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaDataHeader in the database
        List<MetaDataHeader> metaDataHeaderList = metaDataHeaderRepository.findAll();
        assertThat(metaDataHeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMetaDataHeader() throws Exception {
        int databaseSizeBeforeUpdate = metaDataHeaderRepository.findAll().size();
        metaDataHeader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaDataHeaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(metaDataHeader))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaDataHeader in the database
        List<MetaDataHeader> metaDataHeaderList = metaDataHeaderRepository.findAll();
        assertThat(metaDataHeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMetaDataHeader() throws Exception {
        int databaseSizeBeforeUpdate = metaDataHeaderRepository.findAll().size();
        metaDataHeader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaDataHeaderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(metaDataHeader)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MetaDataHeader in the database
        List<MetaDataHeader> metaDataHeaderList = metaDataHeaderRepository.findAll();
        assertThat(metaDataHeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMetaDataHeaderWithPatch() throws Exception {
        // Initialize the database
        metaDataHeaderRepository.saveAndFlush(metaDataHeader);

        int databaseSizeBeforeUpdate = metaDataHeaderRepository.findAll().size();

        // Update the metaDataHeader using partial update
        MetaDataHeader partialUpdatedMetaDataHeader = new MetaDataHeader();
        partialUpdatedMetaDataHeader.setId(metaDataHeader.getId());

        partialUpdatedMetaDataHeader.docTitle(UPDATED_DOC_TITLE);

        restMetaDataHeaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMetaDataHeader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMetaDataHeader))
            )
            .andExpect(status().isOk());

        // Validate the MetaDataHeader in the database
        List<MetaDataHeader> metaDataHeaderList = metaDataHeaderRepository.findAll();
        assertThat(metaDataHeaderList).hasSize(databaseSizeBeforeUpdate);
        MetaDataHeader testMetaDataHeader = metaDataHeaderList.get(metaDataHeaderList.size() - 1);
        assertThat(testMetaDataHeader.getDocTitle()).isEqualTo(UPDATED_DOC_TITLE);
    }

    @Test
    @Transactional
    void fullUpdateMetaDataHeaderWithPatch() throws Exception {
        // Initialize the database
        metaDataHeaderRepository.saveAndFlush(metaDataHeader);

        int databaseSizeBeforeUpdate = metaDataHeaderRepository.findAll().size();

        // Update the metaDataHeader using partial update
        MetaDataHeader partialUpdatedMetaDataHeader = new MetaDataHeader();
        partialUpdatedMetaDataHeader.setId(metaDataHeader.getId());

        partialUpdatedMetaDataHeader.docTitle(UPDATED_DOC_TITLE);

        restMetaDataHeaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMetaDataHeader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMetaDataHeader))
            )
            .andExpect(status().isOk());

        // Validate the MetaDataHeader in the database
        List<MetaDataHeader> metaDataHeaderList = metaDataHeaderRepository.findAll();
        assertThat(metaDataHeaderList).hasSize(databaseSizeBeforeUpdate);
        MetaDataHeader testMetaDataHeader = metaDataHeaderList.get(metaDataHeaderList.size() - 1);
        assertThat(testMetaDataHeader.getDocTitle()).isEqualTo(UPDATED_DOC_TITLE);
    }

    @Test
    @Transactional
    void patchNonExistingMetaDataHeader() throws Exception {
        int databaseSizeBeforeUpdate = metaDataHeaderRepository.findAll().size();
        metaDataHeader.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetaDataHeaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, metaDataHeader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(metaDataHeader))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaDataHeader in the database
        List<MetaDataHeader> metaDataHeaderList = metaDataHeaderRepository.findAll();
        assertThat(metaDataHeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMetaDataHeader() throws Exception {
        int databaseSizeBeforeUpdate = metaDataHeaderRepository.findAll().size();
        metaDataHeader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaDataHeaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(metaDataHeader))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaDataHeader in the database
        List<MetaDataHeader> metaDataHeaderList = metaDataHeaderRepository.findAll();
        assertThat(metaDataHeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMetaDataHeader() throws Exception {
        int databaseSizeBeforeUpdate = metaDataHeaderRepository.findAll().size();
        metaDataHeader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaDataHeaderMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(metaDataHeader))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MetaDataHeader in the database
        List<MetaDataHeader> metaDataHeaderList = metaDataHeaderRepository.findAll();
        assertThat(metaDataHeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMetaDataHeader() throws Exception {
        // Initialize the database
        metaDataHeaderRepository.saveAndFlush(metaDataHeader);

        int databaseSizeBeforeDelete = metaDataHeaderRepository.findAll().size();

        // Delete the metaDataHeader
        restMetaDataHeaderMockMvc
            .perform(delete(ENTITY_API_URL_ID, metaDataHeader.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MetaDataHeader> metaDataHeaderList = metaDataHeaderRepository.findAll();
        assertThat(metaDataHeaderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
