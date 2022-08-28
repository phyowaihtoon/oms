package com.hmm.dms.service;

import com.hmm.dms.service.dto.DocumentHeaderDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DocumentInquiryService {
    public Page<DocumentHeaderDTO> searchDocumentHeaderByMetaData(DocumentHeaderDTO dto, Pageable pageable);

    public DocumentHeaderDTO getDocumentsById(Long id);
}
