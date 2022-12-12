package com.hmm.dms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hmm.dms.IntegrationTest;
import com.hmm.dms.domain.CodeDefinition;
import com.hmm.dms.repository.CodeDefinitionRepository;
import com.hmm.dms.service.dto.CodeDefinitionDTO;
import com.hmm.dms.service.mapper.CodeDefinitionMapper;
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
 * Integration tests for the {@link CodeDefinitionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CodeDefinitionResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DEFINITION = "AAAAAAAAAA";
    private static final String UPDATED_DEFINITION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/code-definitions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CodeDefinitionRepository codeDefinitionRepository;

    @Autowired
    private CodeDefinitionMapper codeDefinitionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCodeDefinitionMockMvc;

    private CodeDefinition codeDefinition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CodeDefinition createEntity(EntityManager em) {
        CodeDefinition codeDefinition = new CodeDefinition().code(DEFAULT_CODE).definition(DEFAULT_DEFINITION);
        return codeDefinition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CodeDefinition createUpdatedEntity(EntityManager em) {
        CodeDefinition codeDefinition = new CodeDefinition().code(UPDATED_CODE).definition(UPDATED_DEFINITION);
        return codeDefinition;
    }

    @BeforeEach
    public void initTest() {
        codeDefinition = createEntity(em);
    }

    @Test
    @Transactional
    void createCodeDefinition() throws Exception {
        int databaseSizeBeforeCreate = codeDefinitionRepository.findAll().size();
        // Create the CodeDefinition
        CodeDefinitionDTO codeDefinitionDTO = codeDefinitionMapper.toDto(codeDefinition);
        restCodeDefinitionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(codeDefinitionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CodeDefinition in the database
        List<CodeDefinition> codeDefinitionList = codeDefinitionRepository.findAll();
        assertThat(codeDefinitionList).hasSize(databaseSizeBeforeCreate + 1);
        CodeDefinition testCodeDefinition = codeDefinitionList.get(codeDefinitionList.size() - 1);
        assertThat(testCodeDefinition.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCodeDefinition.getDefinition()).isEqualTo(DEFAULT_DEFINITION);
    }

    @Test
    @Transactional
    void createCodeDefinitionWithExistingId() throws Exception {
        // Create the CodeDefinition with an existing ID
        codeDefinition.setId(1L);
        CodeDefinitionDTO codeDefinitionDTO = codeDefinitionMapper.toDto(codeDefinition);

        int databaseSizeBeforeCreate = codeDefinitionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCodeDefinitionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(codeDefinitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeDefinition in the database
        List<CodeDefinition> codeDefinitionList = codeDefinitionRepository.findAll();
        assertThat(codeDefinitionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = codeDefinitionRepository.findAll().size();
        // set the field null
        codeDefinition.setCode(null);

        // Create the CodeDefinition, which fails.
        CodeDefinitionDTO codeDefinitionDTO = codeDefinitionMapper.toDto(codeDefinition);

        restCodeDefinitionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(codeDefinitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<CodeDefinition> codeDefinitionList = codeDefinitionRepository.findAll();
        assertThat(codeDefinitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDefinitionIsRequired() throws Exception {
        int databaseSizeBeforeTest = codeDefinitionRepository.findAll().size();
        // set the field null
        codeDefinition.setDefinition(null);

        // Create the CodeDefinition, which fails.
        CodeDefinitionDTO codeDefinitionDTO = codeDefinitionMapper.toDto(codeDefinition);

        restCodeDefinitionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(codeDefinitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<CodeDefinition> codeDefinitionList = codeDefinitionRepository.findAll();
        assertThat(codeDefinitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCodeDefinitions() throws Exception {
        // Initialize the database
        codeDefinitionRepository.saveAndFlush(codeDefinition);

        // Get all the codeDefinitionList
        restCodeDefinitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(codeDefinition.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].definition").value(hasItem(DEFAULT_DEFINITION)));
    }

    @Test
    @Transactional
    void getCodeDefinition() throws Exception {
        // Initialize the database
        codeDefinitionRepository.saveAndFlush(codeDefinition);

        // Get the codeDefinition
        restCodeDefinitionMockMvc
            .perform(get(ENTITY_API_URL_ID, codeDefinition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(codeDefinition.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.definition").value(DEFAULT_DEFINITION));
    }

    @Test
    @Transactional
    void getNonExistingCodeDefinition() throws Exception {
        // Get the codeDefinition
        restCodeDefinitionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCodeDefinition() throws Exception {
        // Initialize the database
        codeDefinitionRepository.saveAndFlush(codeDefinition);

        int databaseSizeBeforeUpdate = codeDefinitionRepository.findAll().size();

        // Update the codeDefinition
        CodeDefinition updatedCodeDefinition = codeDefinitionRepository.findById(codeDefinition.getId()).get();
        // Disconnect from session so that the updates on updatedCodeDefinition are not directly saved in db
        em.detach(updatedCodeDefinition);
        updatedCodeDefinition.code(UPDATED_CODE).definition(UPDATED_DEFINITION);
        CodeDefinitionDTO codeDefinitionDTO = codeDefinitionMapper.toDto(updatedCodeDefinition);

        restCodeDefinitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, codeDefinitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(codeDefinitionDTO))
            )
            .andExpect(status().isOk());

        // Validate the CodeDefinition in the database
        List<CodeDefinition> codeDefinitionList = codeDefinitionRepository.findAll();
        assertThat(codeDefinitionList).hasSize(databaseSizeBeforeUpdate);
        CodeDefinition testCodeDefinition = codeDefinitionList.get(codeDefinitionList.size() - 1);
        assertThat(testCodeDefinition.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCodeDefinition.getDefinition()).isEqualTo(UPDATED_DEFINITION);
    }

    @Test
    @Transactional
    void putNonExistingCodeDefinition() throws Exception {
        int databaseSizeBeforeUpdate = codeDefinitionRepository.findAll().size();
        codeDefinition.setId(count.incrementAndGet());

        // Create the CodeDefinition
        CodeDefinitionDTO codeDefinitionDTO = codeDefinitionMapper.toDto(codeDefinition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCodeDefinitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, codeDefinitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(codeDefinitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeDefinition in the database
        List<CodeDefinition> codeDefinitionList = codeDefinitionRepository.findAll();
        assertThat(codeDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCodeDefinition() throws Exception {
        int databaseSizeBeforeUpdate = codeDefinitionRepository.findAll().size();
        codeDefinition.setId(count.incrementAndGet());

        // Create the CodeDefinition
        CodeDefinitionDTO codeDefinitionDTO = codeDefinitionMapper.toDto(codeDefinition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeDefinitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(codeDefinitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeDefinition in the database
        List<CodeDefinition> codeDefinitionList = codeDefinitionRepository.findAll();
        assertThat(codeDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCodeDefinition() throws Exception {
        int databaseSizeBeforeUpdate = codeDefinitionRepository.findAll().size();
        codeDefinition.setId(count.incrementAndGet());

        // Create the CodeDefinition
        CodeDefinitionDTO codeDefinitionDTO = codeDefinitionMapper.toDto(codeDefinition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeDefinitionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(codeDefinitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CodeDefinition in the database
        List<CodeDefinition> codeDefinitionList = codeDefinitionRepository.findAll();
        assertThat(codeDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCodeDefinitionWithPatch() throws Exception {
        // Initialize the database
        codeDefinitionRepository.saveAndFlush(codeDefinition);

        int databaseSizeBeforeUpdate = codeDefinitionRepository.findAll().size();

        // Update the codeDefinition using partial update
        CodeDefinition partialUpdatedCodeDefinition = new CodeDefinition();
        partialUpdatedCodeDefinition.setId(codeDefinition.getId());

        restCodeDefinitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCodeDefinition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCodeDefinition))
            )
            .andExpect(status().isOk());

        // Validate the CodeDefinition in the database
        List<CodeDefinition> codeDefinitionList = codeDefinitionRepository.findAll();
        assertThat(codeDefinitionList).hasSize(databaseSizeBeforeUpdate);
        CodeDefinition testCodeDefinition = codeDefinitionList.get(codeDefinitionList.size() - 1);
        assertThat(testCodeDefinition.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCodeDefinition.getDefinition()).isEqualTo(DEFAULT_DEFINITION);
    }

    @Test
    @Transactional
    void fullUpdateCodeDefinitionWithPatch() throws Exception {
        // Initialize the database
        codeDefinitionRepository.saveAndFlush(codeDefinition);

        int databaseSizeBeforeUpdate = codeDefinitionRepository.findAll().size();

        // Update the codeDefinition using partial update
        CodeDefinition partialUpdatedCodeDefinition = new CodeDefinition();
        partialUpdatedCodeDefinition.setId(codeDefinition.getId());

        partialUpdatedCodeDefinition.code(UPDATED_CODE).definition(UPDATED_DEFINITION);

        restCodeDefinitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCodeDefinition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCodeDefinition))
            )
            .andExpect(status().isOk());

        // Validate the CodeDefinition in the database
        List<CodeDefinition> codeDefinitionList = codeDefinitionRepository.findAll();
        assertThat(codeDefinitionList).hasSize(databaseSizeBeforeUpdate);
        CodeDefinition testCodeDefinition = codeDefinitionList.get(codeDefinitionList.size() - 1);
        assertThat(testCodeDefinition.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCodeDefinition.getDefinition()).isEqualTo(UPDATED_DEFINITION);
    }

    @Test
    @Transactional
    void patchNonExistingCodeDefinition() throws Exception {
        int databaseSizeBeforeUpdate = codeDefinitionRepository.findAll().size();
        codeDefinition.setId(count.incrementAndGet());

        // Create the CodeDefinition
        CodeDefinitionDTO codeDefinitionDTO = codeDefinitionMapper.toDto(codeDefinition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCodeDefinitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, codeDefinitionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(codeDefinitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeDefinition in the database
        List<CodeDefinition> codeDefinitionList = codeDefinitionRepository.findAll();
        assertThat(codeDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCodeDefinition() throws Exception {
        int databaseSizeBeforeUpdate = codeDefinitionRepository.findAll().size();
        codeDefinition.setId(count.incrementAndGet());

        // Create the CodeDefinition
        CodeDefinitionDTO codeDefinitionDTO = codeDefinitionMapper.toDto(codeDefinition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeDefinitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(codeDefinitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeDefinition in the database
        List<CodeDefinition> codeDefinitionList = codeDefinitionRepository.findAll();
        assertThat(codeDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCodeDefinition() throws Exception {
        int databaseSizeBeforeUpdate = codeDefinitionRepository.findAll().size();
        codeDefinition.setId(count.incrementAndGet());

        // Create the CodeDefinition
        CodeDefinitionDTO codeDefinitionDTO = codeDefinitionMapper.toDto(codeDefinition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeDefinitionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(codeDefinitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CodeDefinition in the database
        List<CodeDefinition> codeDefinitionList = codeDefinitionRepository.findAll();
        assertThat(codeDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCodeDefinition() throws Exception {
        // Initialize the database
        codeDefinitionRepository.saveAndFlush(codeDefinition);

        int databaseSizeBeforeDelete = codeDefinitionRepository.findAll().size();

        // Delete the codeDefinition
        restCodeDefinitionMockMvc
            .perform(delete(ENTITY_API_URL_ID, codeDefinition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CodeDefinition> codeDefinitionList = codeDefinitionRepository.findAll();
        assertThat(codeDefinitionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
