package com.hmm.dms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hmm.dms.IntegrationTest;
import com.hmm.dms.domain.RoleMenuMap;
import com.hmm.dms.repository.RoleMenuMapRepository;
import com.hmm.dms.service.dto.RoleMenuMapDTO;
import com.hmm.dms.service.mapper.RoleMenuMapMapper;
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
 * Integration tests for the {@link RoleMenuMapResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoleMenuMapResourceIT {

    private static final String DEFAULT_ROLE_ID = "AAAAAAAAAA";
    private static final String UPDATED_ROLE_ID = "BBBBBBBBBB";

    private static final Long DEFAULT_MENU_ID = 1L;
    private static final Long UPDATED_MENU_ID = 2L;

    private static final String DEFAULT_ALLOWED = "A";
    private static final String UPDATED_ALLOWED = "B";

    private static final String ENTITY_API_URL = "/api/role-menu-maps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RoleMenuMapRepository roleMenuMapRepository;

    @Autowired
    private RoleMenuMapMapper roleMenuMapMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoleMenuMapMockMvc;

    private RoleMenuMap roleMenuMap;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoleMenuMap createEntity(EntityManager em) {
        RoleMenuMap roleMenuMap = new RoleMenuMap().roleId(DEFAULT_ROLE_ID).menuId(DEFAULT_MENU_ID).allowed(DEFAULT_ALLOWED);
        return roleMenuMap;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoleMenuMap createUpdatedEntity(EntityManager em) {
        RoleMenuMap roleMenuMap = new RoleMenuMap().roleId(UPDATED_ROLE_ID).menuId(UPDATED_MENU_ID).allowed(UPDATED_ALLOWED);
        return roleMenuMap;
    }

    @BeforeEach
    public void initTest() {
        roleMenuMap = createEntity(em);
    }

    @Test
    @Transactional
    void createRoleMenuMap() throws Exception {
        int databaseSizeBeforeCreate = roleMenuMapRepository.findAll().size();
        // Create the RoleMenuMap
        RoleMenuMapDTO roleMenuMapDTO = roleMenuMapMapper.toDto(roleMenuMap);
        restRoleMenuMapMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roleMenuMapDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RoleMenuMap in the database
        List<RoleMenuMap> roleMenuMapList = roleMenuMapRepository.findAll();
        assertThat(roleMenuMapList).hasSize(databaseSizeBeforeCreate + 1);
        RoleMenuMap testRoleMenuMap = roleMenuMapList.get(roleMenuMapList.size() - 1);
        assertThat(testRoleMenuMap.getRoleId()).isEqualTo(DEFAULT_ROLE_ID);
        assertThat(testRoleMenuMap.getMenuId()).isEqualTo(DEFAULT_MENU_ID);
        assertThat(testRoleMenuMap.getAllowed()).isEqualTo(DEFAULT_ALLOWED);
    }

    @Test
    @Transactional
    void createRoleMenuMapWithExistingId() throws Exception {
        // Create the RoleMenuMap with an existing ID
        roleMenuMap.setId(1L);
        RoleMenuMapDTO roleMenuMapDTO = roleMenuMapMapper.toDto(roleMenuMap);

        int databaseSizeBeforeCreate = roleMenuMapRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoleMenuMapMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roleMenuMapDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleMenuMap in the database
        List<RoleMenuMap> roleMenuMapList = roleMenuMapRepository.findAll();
        assertThat(roleMenuMapList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRoleIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = roleMenuMapRepository.findAll().size();
        // set the field null
        roleMenuMap.setRoleId(null);

        // Create the RoleMenuMap, which fails.
        RoleMenuMapDTO roleMenuMapDTO = roleMenuMapMapper.toDto(roleMenuMap);

        restRoleMenuMapMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roleMenuMapDTO))
            )
            .andExpect(status().isBadRequest());

        List<RoleMenuMap> roleMenuMapList = roleMenuMapRepository.findAll();
        assertThat(roleMenuMapList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMenuIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = roleMenuMapRepository.findAll().size();
        // set the field null
        roleMenuMap.setMenuId(null);

        // Create the RoleMenuMap, which fails.
        RoleMenuMapDTO roleMenuMapDTO = roleMenuMapMapper.toDto(roleMenuMap);

        restRoleMenuMapMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roleMenuMapDTO))
            )
            .andExpect(status().isBadRequest());

        List<RoleMenuMap> roleMenuMapList = roleMenuMapRepository.findAll();
        assertThat(roleMenuMapList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAllowedIsRequired() throws Exception {
        int databaseSizeBeforeTest = roleMenuMapRepository.findAll().size();
        // set the field null
        roleMenuMap.setAllowed(null);

        // Create the RoleMenuMap, which fails.
        RoleMenuMapDTO roleMenuMapDTO = roleMenuMapMapper.toDto(roleMenuMap);

        restRoleMenuMapMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roleMenuMapDTO))
            )
            .andExpect(status().isBadRequest());

        List<RoleMenuMap> roleMenuMapList = roleMenuMapRepository.findAll();
        assertThat(roleMenuMapList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRoleMenuMaps() throws Exception {
        // Initialize the database
        roleMenuMapRepository.saveAndFlush(roleMenuMap);

        // Get all the roleMenuMapList
        restRoleMenuMapMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roleMenuMap.getId().intValue())))
            .andExpect(jsonPath("$.[*].roleId").value(hasItem(DEFAULT_ROLE_ID)))
            .andExpect(jsonPath("$.[*].menuId").value(hasItem(DEFAULT_MENU_ID.intValue())))
            .andExpect(jsonPath("$.[*].allowed").value(hasItem(DEFAULT_ALLOWED)));
    }

    @Test
    @Transactional
    void getRoleMenuMap() throws Exception {
        // Initialize the database
        roleMenuMapRepository.saveAndFlush(roleMenuMap);

        // Get the roleMenuMap
        restRoleMenuMapMockMvc
            .perform(get(ENTITY_API_URL_ID, roleMenuMap.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roleMenuMap.getId().intValue()))
            .andExpect(jsonPath("$.roleId").value(DEFAULT_ROLE_ID))
            .andExpect(jsonPath("$.menuId").value(DEFAULT_MENU_ID.intValue()))
            .andExpect(jsonPath("$.allowed").value(DEFAULT_ALLOWED));
    }

    @Test
    @Transactional
    void getNonExistingRoleMenuMap() throws Exception {
        // Get the roleMenuMap
        restRoleMenuMapMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRoleMenuMap() throws Exception {
        // Initialize the database
        roleMenuMapRepository.saveAndFlush(roleMenuMap);

        int databaseSizeBeforeUpdate = roleMenuMapRepository.findAll().size();

        // Update the roleMenuMap
        RoleMenuMap updatedRoleMenuMap = roleMenuMapRepository.findById(roleMenuMap.getId()).get();
        // Disconnect from session so that the updates on updatedRoleMenuMap are not directly saved in db
        em.detach(updatedRoleMenuMap);
        updatedRoleMenuMap.roleId(UPDATED_ROLE_ID).menuId(UPDATED_MENU_ID).allowed(UPDATED_ALLOWED);
        RoleMenuMapDTO roleMenuMapDTO = roleMenuMapMapper.toDto(updatedRoleMenuMap);

        restRoleMenuMapMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roleMenuMapDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roleMenuMapDTO))
            )
            .andExpect(status().isOk());

        // Validate the RoleMenuMap in the database
        List<RoleMenuMap> roleMenuMapList = roleMenuMapRepository.findAll();
        assertThat(roleMenuMapList).hasSize(databaseSizeBeforeUpdate);
        RoleMenuMap testRoleMenuMap = roleMenuMapList.get(roleMenuMapList.size() - 1);
        assertThat(testRoleMenuMap.getRoleId()).isEqualTo(UPDATED_ROLE_ID);
        assertThat(testRoleMenuMap.getMenuId()).isEqualTo(UPDATED_MENU_ID);
        assertThat(testRoleMenuMap.getAllowed()).isEqualTo(UPDATED_ALLOWED);
    }

    @Test
    @Transactional
    void putNonExistingRoleMenuMap() throws Exception {
        int databaseSizeBeforeUpdate = roleMenuMapRepository.findAll().size();
        roleMenuMap.setId(count.incrementAndGet());

        // Create the RoleMenuMap
        RoleMenuMapDTO roleMenuMapDTO = roleMenuMapMapper.toDto(roleMenuMap);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleMenuMapMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roleMenuMapDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roleMenuMapDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleMenuMap in the database
        List<RoleMenuMap> roleMenuMapList = roleMenuMapRepository.findAll();
        assertThat(roleMenuMapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoleMenuMap() throws Exception {
        int databaseSizeBeforeUpdate = roleMenuMapRepository.findAll().size();
        roleMenuMap.setId(count.incrementAndGet());

        // Create the RoleMenuMap
        RoleMenuMapDTO roleMenuMapDTO = roleMenuMapMapper.toDto(roleMenuMap);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleMenuMapMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roleMenuMapDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleMenuMap in the database
        List<RoleMenuMap> roleMenuMapList = roleMenuMapRepository.findAll();
        assertThat(roleMenuMapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoleMenuMap() throws Exception {
        int databaseSizeBeforeUpdate = roleMenuMapRepository.findAll().size();
        roleMenuMap.setId(count.incrementAndGet());

        // Create the RoleMenuMap
        RoleMenuMapDTO roleMenuMapDTO = roleMenuMapMapper.toDto(roleMenuMap);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleMenuMapMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roleMenuMapDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoleMenuMap in the database
        List<RoleMenuMap> roleMenuMapList = roleMenuMapRepository.findAll();
        assertThat(roleMenuMapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoleMenuMapWithPatch() throws Exception {
        // Initialize the database
        roleMenuMapRepository.saveAndFlush(roleMenuMap);

        int databaseSizeBeforeUpdate = roleMenuMapRepository.findAll().size();

        // Update the roleMenuMap using partial update
        RoleMenuMap partialUpdatedRoleMenuMap = new RoleMenuMap();
        partialUpdatedRoleMenuMap.setId(roleMenuMap.getId());

        restRoleMenuMapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoleMenuMap.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoleMenuMap))
            )
            .andExpect(status().isOk());

        // Validate the RoleMenuMap in the database
        List<RoleMenuMap> roleMenuMapList = roleMenuMapRepository.findAll();
        assertThat(roleMenuMapList).hasSize(databaseSizeBeforeUpdate);
        RoleMenuMap testRoleMenuMap = roleMenuMapList.get(roleMenuMapList.size() - 1);
        assertThat(testRoleMenuMap.getRoleId()).isEqualTo(DEFAULT_ROLE_ID);
        assertThat(testRoleMenuMap.getMenuId()).isEqualTo(DEFAULT_MENU_ID);
        assertThat(testRoleMenuMap.getAllowed()).isEqualTo(DEFAULT_ALLOWED);
    }

    @Test
    @Transactional
    void fullUpdateRoleMenuMapWithPatch() throws Exception {
        // Initialize the database
        roleMenuMapRepository.saveAndFlush(roleMenuMap);

        int databaseSizeBeforeUpdate = roleMenuMapRepository.findAll().size();

        // Update the roleMenuMap using partial update
        RoleMenuMap partialUpdatedRoleMenuMap = new RoleMenuMap();
        partialUpdatedRoleMenuMap.setId(roleMenuMap.getId());

        partialUpdatedRoleMenuMap.roleId(UPDATED_ROLE_ID).menuId(UPDATED_MENU_ID).allowed(UPDATED_ALLOWED);

        restRoleMenuMapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoleMenuMap.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoleMenuMap))
            )
            .andExpect(status().isOk());

        // Validate the RoleMenuMap in the database
        List<RoleMenuMap> roleMenuMapList = roleMenuMapRepository.findAll();
        assertThat(roleMenuMapList).hasSize(databaseSizeBeforeUpdate);
        RoleMenuMap testRoleMenuMap = roleMenuMapList.get(roleMenuMapList.size() - 1);
        assertThat(testRoleMenuMap.getRoleId()).isEqualTo(UPDATED_ROLE_ID);
        assertThat(testRoleMenuMap.getMenuId()).isEqualTo(UPDATED_MENU_ID);
        assertThat(testRoleMenuMap.getAllowed()).isEqualTo(UPDATED_ALLOWED);
    }

    @Test
    @Transactional
    void patchNonExistingRoleMenuMap() throws Exception {
        int databaseSizeBeforeUpdate = roleMenuMapRepository.findAll().size();
        roleMenuMap.setId(count.incrementAndGet());

        // Create the RoleMenuMap
        RoleMenuMapDTO roleMenuMapDTO = roleMenuMapMapper.toDto(roleMenuMap);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleMenuMapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roleMenuMapDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roleMenuMapDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleMenuMap in the database
        List<RoleMenuMap> roleMenuMapList = roleMenuMapRepository.findAll();
        assertThat(roleMenuMapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoleMenuMap() throws Exception {
        int databaseSizeBeforeUpdate = roleMenuMapRepository.findAll().size();
        roleMenuMap.setId(count.incrementAndGet());

        // Create the RoleMenuMap
        RoleMenuMapDTO roleMenuMapDTO = roleMenuMapMapper.toDto(roleMenuMap);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleMenuMapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roleMenuMapDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleMenuMap in the database
        List<RoleMenuMap> roleMenuMapList = roleMenuMapRepository.findAll();
        assertThat(roleMenuMapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoleMenuMap() throws Exception {
        int databaseSizeBeforeUpdate = roleMenuMapRepository.findAll().size();
        roleMenuMap.setId(count.incrementAndGet());

        // Create the RoleMenuMap
        RoleMenuMapDTO roleMenuMapDTO = roleMenuMapMapper.toDto(roleMenuMap);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleMenuMapMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(roleMenuMapDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoleMenuMap in the database
        List<RoleMenuMap> roleMenuMapList = roleMenuMapRepository.findAll();
        assertThat(roleMenuMapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoleMenuMap() throws Exception {
        // Initialize the database
        roleMenuMapRepository.saveAndFlush(roleMenuMap);

        int databaseSizeBeforeDelete = roleMenuMapRepository.findAll().size();

        // Delete the roleMenuMap
        restRoleMenuMapMockMvc
            .perform(delete(ENTITY_API_URL_ID, roleMenuMap.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RoleMenuMap> roleMenuMapList = roleMenuMapRepository.findAll();
        assertThat(roleMenuMapList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
