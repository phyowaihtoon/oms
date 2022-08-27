package com.hmm.dms.service;

import com.hmm.dms.service.dto.DocumentHeaderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DocumentInquiryService {
    public Page<DocumentHeaderDTO> searchDocumentsByRepoURL(Long id, String repURL, Pageable pageable);
}
