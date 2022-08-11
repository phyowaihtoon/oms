package com.hmm.dms.service;

import com.hmm.dms.service.dto.RoleMenuMapDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.hmm.dms.domain.RoleMenuMap}.
 */
public interface RoleMenuMapService {
    /**
     * Save a roleMenuMap.
     *
     * @param roleMenuMapDTO the entity to save.
     * @return the persisted entity.
     */
    RoleMenuMapDTO save(RoleMenuMapDTO roleMenuMapDTO);

    /**
     * Partially updates a roleMenuMap.
     *
     * @param roleMenuMapDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RoleMenuMapDTO> partialUpdate(RoleMenuMapDTO roleMenuMapDTO);

    /**
     * Get all the roleMenuMaps.
     *
     * @return the list of entities.
     */
    List<RoleMenuMapDTO> findAll();

    /**
     * Get the "id" roleMenuMap.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RoleMenuMapDTO> findOne(Long id);

    /**
     * Delete the "id" roleMenuMap.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
