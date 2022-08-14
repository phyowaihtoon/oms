package com.hmm.dms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hmm.dms.IntegrationTest;
import com.hmm.dms.domain.Repository;
import com.hmm.dms.repository.RepositoryDocRepository;
import com.hmm.dms.service.dto.RepositoryDTO;
import com.hmm.dms.service.mapper.RepositoryMapper;
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
 * Integration tests for the {@link RepositoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RepositoryResourceIT {

    private static final Integer DEFAULT_PARENT_ID = 1;
    private static final Integer UPDATED_PARENT_ID = 2;

    private static final String DEFAULT_REPOSITORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REPOSITORY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/repositories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RepositoryDocRepository repositoryRepository;

    @Autowired
    private RepositoryMapper repositoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRepositoryMockMvc;

    private Repository repository;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Repository createEntity(EntityManager em) {
        Repository repository = new Repository()
            .parentID(DEFAULT_PARENT_ID)
            .repositoryName(DEFAULT_REPOSITORY_NAME)
            .description(DEFAULT_DESCRIPTION);
        return repository;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Repository createUpdatedEntity(EntityManager em) {
        Repository repository = new Repository()
            .parentID(UPDATED_PARENT_ID)
            .repositoryName(UPDATED_REPOSITORY_NAME)
            .description(UPDATED_DESCRIPTION);
        return repository;
    }

    @BeforeEach
    public void initTest() {
        repository = createEntity(em);
    }

    @Test
    @Transactional
    void createRepository() throws Exception {
        int databaseSizeBeforeCreate = repositoryRepository.findAll().size();
        // Create the Repository
        RepositoryDTO repositoryDTO = repositoryMapper.toDto(repository);
        restRepositoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(repositoryDTO)))
            .andExpect(status().isCreated());

        // Validate the Repository in the database
        List<Repository> repositoryList = repositoryRepository.findAll();
        assertThat(repositoryList).hasSize(databaseSizeBeforeCreate + 1);
        Repository testRepository = repositoryList.get(repositoryList.size() - 1);
        assertThat(testRepository.getParentID()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(testRepository.getRepositoryName()).isEqualTo(DEFAULT_REPOSITORY_NAME);
        assertThat(testRepository.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createRepositoryWithExistingId() throws Exception {
        // Create the Repository with an existing ID
        repository.setId(1L);
        RepositoryDTO repositoryDTO = repositoryMapper.toDto(repository);

        int databaseSizeBeforeCreate = repositoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRepositoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(repositoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Repository in the database
        List<Repository> repositoryList = repositoryRepository.findAll();
        assertThat(repositoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRepositoryNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = repositoryRepository.findAll().size();
        // set the field null
        repository.setRepositoryName(null);

        // Create the Repository, which fails.
        RepositoryDTO repositoryDTO = repositoryMapper.toDto(repository);

        restRepositoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(repositoryDTO)))
            .andExpect(status().isBadRequest());

        List<Repository> repositoryList = repositoryRepository.findAll();
        assertThat(repositoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRepositories() throws Exception {
        // Initialize the database
        repositoryRepository.saveAndFlush(repository);

        // Get all the repositoryList
        restRepositoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(repository.getId().intValue())))
            .andExpect(jsonPath("$.[*].parentID").value(hasItem(DEFAULT_PARENT_ID)))
            .andExpect(jsonPath("$.[*].repositoryName").value(hasItem(DEFAULT_REPOSITORY_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getRepository() throws Exception {
        // Initialize the database
        repositoryRepository.saveAndFlush(repository);

        // Get the repository
        restRepositoryMockMvc
            .perform(get(ENTITY_API_URL_ID, repository.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(repository.getId().intValue()))
            .andExpect(jsonPath("$.parentID").value(DEFAULT_PARENT_ID))
            .andExpect(jsonPath("$.repositoryName").value(DEFAULT_REPOSITORY_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingRepository() throws Exception {
        // Get the repository
        restRepositoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRepository() throws Exception {
        // Initialize the database
        repositoryRepository.saveAndFlush(repository);

        int databaseSizeBeforeUpdate = repositoryRepository.findAll().size();

        // Update the repository
        Repository updatedRepository = repositoryRepository.findById(repository.getId()).get();
        // Disconnect from session so that the updates on updatedRepository are not directly saved in db
        em.detach(updatedRepository);
        updatedRepository.parentID(UPDATED_PARENT_ID).repositoryName(UPDATED_REPOSITORY_NAME).description(UPDATED_DESCRIPTION);
        RepositoryDTO repositoryDTO = repositoryMapper.toDto(updatedRepository);

        restRepositoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, repositoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(repositoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Repository in the database
        List<Repository> repositoryList = repositoryRepository.findAll();
        assertThat(repositoryList).hasSize(databaseSizeBeforeUpdate);
        Repository testRepository = repositoryList.get(repositoryList.size() - 1);
        assertThat(testRepository.getParentID()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testRepository.getRepositoryName()).isEqualTo(UPDATED_REPOSITORY_NAME);
        assertThat(testRepository.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingRepository() throws Exception {
        int databaseSizeBeforeUpdate = repositoryRepository.findAll().size();
        repository.setId(count.incrementAndGet());

        // Create the Repository
        RepositoryDTO repositoryDTO = repositoryMapper.toDto(repository);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRepositoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, repositoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(repositoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Repository in the database
        List<Repository> repositoryList = repositoryRepository.findAll();
        assertThat(repositoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRepository() throws Exception {
        int databaseSizeBeforeUpdate = repositoryRepository.findAll().size();
        repository.setId(count.incrementAndGet());

        // Create the Repository
        RepositoryDTO repositoryDTO = repositoryMapper.toDto(repository);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepositoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(repositoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Repository in the database
        List<Repository> repositoryList = repositoryRepository.findAll();
        assertThat(repositoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRepository() throws Exception {
        int databaseSizeBeforeUpdate = repositoryRepository.findAll().size();
        repository.setId(count.incrementAndGet());

        // Create the Repository
        RepositoryDTO repositoryDTO = repositoryMapper.toDto(repository);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepositoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(repositoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Repository in the database
        List<Repository> repositoryList = repositoryRepository.findAll();
        assertThat(repositoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRepositoryWithPatch() throws Exception {
        // Initialize the database
        repositoryRepository.saveAndFlush(repository);

        int databaseSizeBeforeUpdate = repositoryRepository.findAll().size();

        // Update the repository using partial update
        Repository partialUpdatedRepository = new Repository();
        partialUpdatedRepository.setId(repository.getId());

        partialUpdatedRepository.repositoryName(UPDATED_REPOSITORY_NAME);

        restRepositoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRepository.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRepository))
            )
            .andExpect(status().isOk());

        // Validate the Repository in the database
        List<Repository> repositoryList = repositoryRepository.findAll();
        assertThat(repositoryList).hasSize(databaseSizeBeforeUpdate);
        Repository testRepository = repositoryList.get(repositoryList.size() - 1);
        assertThat(testRepository.getParentID()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(testRepository.getRepositoryName()).isEqualTo(UPDATED_REPOSITORY_NAME);
        assertThat(testRepository.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateRepositoryWithPatch() throws Exception {
        // Initialize the database
        repositoryRepository.saveAndFlush(repository);

        int databaseSizeBeforeUpdate = repositoryRepository.findAll().size();

        // Update the repository using partial update
        Repository partialUpdatedRepository = new Repository();
        partialUpdatedRepository.setId(repository.getId());

        partialUpdatedRepository.parentID(UPDATED_PARENT_ID).repositoryName(UPDATED_REPOSITORY_NAME).description(UPDATED_DESCRIPTION);

        restRepositoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRepository.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRepository))
            )
            .andExpect(status().isOk());

        // Validate the Repository in the database
        List<Repository> repositoryList = repositoryRepository.findAll();
        assertThat(repositoryList).hasSize(databaseSizeBeforeUpdate);
        Repository testRepository = repositoryList.get(repositoryList.size() - 1);
        assertThat(testRepository.getParentID()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testRepository.getRepositoryName()).isEqualTo(UPDATED_REPOSITORY_NAME);
        assertThat(testRepository.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingRepository() throws Exception {
        int databaseSizeBeforeUpdate = repositoryRepository.findAll().size();
        repository.setId(count.incrementAndGet());

        // Create the Repository
        RepositoryDTO repositoryDTO = repositoryMapper.toDto(repository);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRepositoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, repositoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(repositoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Repository in the database
        List<Repository> repositoryList = repositoryRepository.findAll();
        assertThat(repositoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRepository() throws Exception {
        int databaseSizeBeforeUpdate = repositoryRepository.findAll().size();
        repository.setId(count.incrementAndGet());

        // Create the Repository
        RepositoryDTO repositoryDTO = repositoryMapper.toDto(repository);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepositoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(repositoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Repository in the database
        List<Repository> repositoryList = repositoryRepository.findAll();
        assertThat(repositoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRepository() throws Exception {
        int databaseSizeBeforeUpdate = repositoryRepository.findAll().size();
        repository.setId(count.incrementAndGet());

        // Create the Repository
        RepositoryDTO repositoryDTO = repositoryMapper.toDto(repository);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepositoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(repositoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Repository in the database
        List<Repository> repositoryList = repositoryRepository.findAll();
        assertThat(repositoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRepository() throws Exception {
        // Initialize the database
        repositoryRepository.saveAndFlush(repository);

        int databaseSizeBeforeDelete = repositoryRepository.findAll().size();

        // Delete the repository
        restRepositoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, repository.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Repository> repositoryList = repositoryRepository.findAll();
        assertThat(repositoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
