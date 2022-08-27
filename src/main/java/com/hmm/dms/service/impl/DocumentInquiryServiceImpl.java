package com.hmm.dms.service.impl;

import com.hmm.dms.domain.DocumentHeader;
import com.hmm.dms.repository.DocumentHeaderRepository;
import com.hmm.dms.service.DocumentInquiryService;
import com.hmm.dms.service.dto.DocumentHeaderDTO;
import com.hmm.dms.service.mapper.DocumentHeaderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DocumentInquiryServiceImpl implements DocumentInquiryService {

    private final Logger log = LoggerFactory.getLogger(DocumentInquiryServiceImpl.class);

    private final DocumentHeaderRepository documentRepository;

    private final DocumentHeaderMapper documentMapper;

    public DocumentInquiryServiceImpl(DocumentHeaderRepository documentRepository, DocumentHeaderMapper documentMapper) {
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
    }

    @Override
    public Page<DocumentHeaderDTO> searchDocumentsByRepoURL(Long id, String repURL, Pageable pageable) {
        if (repURL == null || repURL.equals("null") || repURL.isEmpty()) repURL = "";
        Page<DocumentHeader> pageWithEntity = this.documentRepository.findAll(id, repURL, pageable);
        return pageWithEntity.map(documentMapper::toDto);
    }
}
