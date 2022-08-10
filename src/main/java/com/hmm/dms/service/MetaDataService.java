package com.hmm.dms.service;

import com.hmm.dms.service.dto.MetaDataDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.hmm.dms.domain.MetaData}.
 */
public interface MetaDataService {
    /**
     * Save a metaData.
     *
     * @param metaDataDTO the entity to save.
     * @return the persisted entity.
     */
    MetaDataDTO save(MetaDataDTO metaDataDTO);

    /**
     * Partially updates a metaData.
     *
     * @param metaDataDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MetaDataDTO> partialUpdate(MetaDataDTO metaDataDTO);

    /**
     * Get all the metaData.
     *
     * @return the list of entities.
     */
    List<MetaDataDTO> findAll();

    /**
     * Get the "id" metaData.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MetaDataDTO> findOne(Long id);

    /**
     * Delete the "id" metaData.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
