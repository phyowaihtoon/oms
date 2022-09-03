package com.hmm.dms.service;

import com.hmm.dms.service.dto.DocumentHeaderDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.hmm.dms.domain.Document}.
 */
public interface DocumentHeaderService {
    /**
     * Save a document.
     *
     * @param documentDTO the entity to save.
     * @return the persisted entity.
     */
    DocumentHeaderDTO save(DocumentHeaderDTO documentHeaderDTO);

    /**
     * Partially updates a document.
     *
     * @param documentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentHeaderDTO> partialUpdate(DocumentHeaderDTO documentHeaderDTO);

    /**
     * Get all the documents.
     *
     * @return the list of entities.
     */
    List<DocumentHeaderDTO> findAll();

    /**
     * Get the "id" document.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentHeaderDTO> findOne(Long id);

    /**
     * Delete the "id" document.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
