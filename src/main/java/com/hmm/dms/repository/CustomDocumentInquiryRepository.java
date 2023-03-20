package com.hmm.dms.repository;

import com.hmm.dms.domain.DocumentHeader;
import com.hmm.dms.service.message.DocumentInquiryMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomDocumentInquiryRepository {
    Page<DocumentHeader> findDocumentHeaderByMetaData(DocumentInquiryMessage dto, Pageable pageable);
}
