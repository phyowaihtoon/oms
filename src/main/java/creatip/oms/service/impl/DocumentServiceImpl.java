package creatip.oms.service.impl;

import creatip.oms.domain.Document;
import creatip.oms.repository.DocumentRepository;
import creatip.oms.service.DocumentService;
import creatip.oms.service.dto.DocumentDTO;
import creatip.oms.service.mapper.DocumentMapper;
import creatip.oms.service.message.BaseMessage;
import creatip.oms.util.ResponseCode;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Document}.
 */
@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

    private final Logger log = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private final DocumentRepository documentRepository;

    private final DocumentMapper documentMapper;

    public DocumentServiceImpl(DocumentRepository documentRepository, DocumentMapper documentMapper) {
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
    }

    @Override
    public DocumentDTO save(DocumentDTO documentDTO) {
        log.debug("Request to save Document : {}", documentDTO);
        Document document = documentMapper.toEntity(documentDTO);
        document = documentRepository.save(document);
        return documentMapper.toDto(document);
    }

    @Override
    public Optional<DocumentDTO> partialUpdate(DocumentDTO documentDTO) {
        log.debug("Request to partially update Document : {}", documentDTO);

        return documentRepository
            .findById(documentDTO.getId())
            .map(
                existingDocument -> {
                    documentMapper.partialUpdate(existingDocument, documentDTO);
                    return existingDocument;
                }
            )
            .map(documentRepository::save)
            .map(documentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentDTO> findAll() {
        log.debug("Request to get all Documents");
        return documentRepository.findAll().stream().map(documentMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentDTO> findOne(Long id) {
        log.debug("Request to get Document : {}", id);
        return documentRepository.findById(id).map(documentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Document : {}", id);
        documentRepository.deleteById(id);
    }

    @Override
    public BaseMessage findbyFileName(String filename) {
        BaseMessage replyMessage = new BaseMessage();
        List<Document> doc = documentRepository.findByFileName(filename);

        replyMessage.setCode(ResponseCode.ERROR_E00);
        if (doc.size() > 0) {
            replyMessage.setMessage("Document is already stored in server. Can' be deleted");
        } else {
            replyMessage.setCode(ResponseCode.SUCCESS);
            replyMessage.setMessage("Document is not in database yet.");
        }

        return replyMessage;
    }

    @Override
    public BaseMessage deleteFileById(Long id) {
        BaseMessage replyMessage = new BaseMessage();
        documentRepository.updateFlagById(id);
        replyMessage.setCode(ResponseCode.ERROR_E00);
        replyMessage.setMessage("Document is already stored in server. Can' be deleted");
        return replyMessage;
    }
}