package creatip.oms.service;

import creatip.oms.service.dto.DocumentHeaderDTO;
import creatip.oms.service.message.BaseMessage;
import creatip.oms.service.message.DocumentInquiryMessage;
import creatip.oms.service.message.ReplyMessage;
import creatip.oms.service.message.UploadFailedException;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for managing {@link creatip.oms.domain.Document}.
 */
public interface DocumentHeaderService {
    /**
     * Save a document.
     *
     * @param documentDTO the entity to save.
     * @return the persisted entity.
     */
    ReplyMessage<DocumentHeaderDTO> saveAndUploadDocuments(List<MultipartFile> multipartFiles, DocumentHeaderDTO documentHeaderDTO)
        throws UploadFailedException;

    /**
     * Partially updates a document.
     *
     * @param documentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentHeaderDTO> partialUpdate(DocumentHeaderDTO documentHeaderDTO);

    ReplyMessage<DocumentInquiryMessage> partialUpdate(DocumentInquiryMessage approvalInfo, Long id);

    BaseMessage restoreDocument(DocumentHeaderDTO data);
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
