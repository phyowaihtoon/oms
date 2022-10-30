package com.hmm.dms.service;

import com.hmm.dms.service.dto.DocumentHeaderDTO;
import com.hmm.dms.service.message.ReplyMessage;
import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

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
    ReplyMessage<DocumentHeaderDTO> saveAndUploadDocuments(List<MultipartFile> multipartFiles, DocumentHeaderDTO documentHeaderDTO);

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
