package com.hmm.dms.service;

import com.hmm.dms.service.dto.DocumentDTO;
import com.hmm.dms.service.message.BaseMessage;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.hmm.dms.domain.Document}.
 */
public interface DocumentService {
    /**
     * Save a document.
     *
     * @param documentDTO the entity to save.
     * @return the persisted entity.
     */
    DocumentDTO save(DocumentDTO documentDTO);

    /**
     * Partially updates a document.
     *
     * @param documentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentDTO> partialUpdate(DocumentDTO documentDTO);

    /**
     * Get all the documents.
     *
     * @return the list of entities.
     */
    List<DocumentDTO> findAll();

    /**
     * Get the "id" document.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentDTO> findOne(Long id);

    /**
     * Delete the "id" document.
     *
     * @param id the id of the entity.
     */

    BaseMessage findbyFileName(String filename);

    void delete(Long id);

    BaseMessage deleteFileById(Long id);
}
