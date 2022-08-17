package com.hmm.dms.service.impl;

import com.hmm.dms.service.DocumentInquiryService;
import com.hmm.dms.service.dto.DocumentHeaderDTO;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DocumentInquiryServiceImpl implements DocumentInquiryService {

    @Override
    public List<DocumentHeaderDTO> searchDocumentsByRepoURL() {
        return null;
    }
}
