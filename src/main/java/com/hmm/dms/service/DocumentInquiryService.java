package com.hmm.dms.service;

import com.hmm.dms.service.dto.DocumentHeaderDTO;
import java.util.List;

public interface DocumentInquiryService {
    public List<DocumentHeaderDTO> searchDocumentsByRepoURL();
}
