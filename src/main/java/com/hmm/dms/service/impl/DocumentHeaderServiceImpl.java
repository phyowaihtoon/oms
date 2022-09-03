package com.hmm.dms.service.impl;

import com.hmm.dms.domain.Document;
import com.hmm.dms.domain.DocumentHeader;
import com.hmm.dms.repository.DocumentHeaderRepository;
import com.hmm.dms.service.DocumentHeaderService;
import com.hmm.dms.service.dto.DocumentHeaderDTO;
import com.hmm.dms.service.mapper.DocumentHeaderMapper;
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
public class DocumentHeaderServiceImpl implements DocumentHeaderService {

    private final Logger log = LoggerFactory.getLogger(DocumentHeaderServiceImpl.class);

    private final DocumentHeaderRepository documentHeaderRepository;

    private final DocumentHeaderMapper documentHeaderMapper;

    public DocumentHeaderServiceImpl(DocumentHeaderRepository documentHeaderRepository, DocumentHeaderMapper documentHeaderMapper) {
        this.documentHeaderRepository = documentHeaderRepository;
        this.documentHeaderMapper = documentHeaderMapper;
    }

    @Override
    public DocumentHeaderDTO save(DocumentHeaderDTO documentHeaderDTO) {
        log.debug("Request to save Document : {}", documentHeaderDTO);
        DocumentHeader documentHeader = documentHeaderMapper.toEntity(documentHeaderDTO);
        documentHeader = documentHeaderRepository.save(documentHeader);
        return documentHeaderMapper.toDto(documentHeader);
    }

    @Override
    public Optional<DocumentHeaderDTO> partialUpdate(DocumentHeaderDTO documentHeaderDTO) {
        log.debug("Request to partially update Document : {}", documentHeaderDTO);

        return documentHeaderRepository
            .findById(documentHeaderDTO.getId())
            .map(
                existingDocument -> {
                    documentHeaderMapper.partialUpdate(existingDocument, documentHeaderDTO);
                    return existingDocument;
                }
            )
            .map(documentHeaderRepository::save)
            .map(documentHeaderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentHeaderDTO> findAll() {
        log.debug("Request to get all Documents");
        return documentHeaderRepository
            .findAll()
            .stream()
            .map(documentHeaderMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentHeaderDTO> findOne(Long id) {
        log.debug("Request to get Document : {}", id);
        return documentHeaderRepository.findById(id).map(documentHeaderMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Document : {}", id);
        documentHeaderRepository.deleteById(id);
    }
}
