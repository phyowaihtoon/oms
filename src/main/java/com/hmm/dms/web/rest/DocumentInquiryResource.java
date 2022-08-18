package com.hmm.dms.web.rest;

import com.hmm.dms.service.DocumentInquiryService;
import com.hmm.dms.service.dto.DocumentHeaderDTO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DocumentInquiryResource {

    private final Logger log = LoggerFactory.getLogger(DocumentResource.class);

    private final DocumentInquiryService documentInquiryService;

    public DocumentInquiryResource(DocumentInquiryService documentInquiryService) {
        this.documentInquiryService = documentInquiryService;
    }

    @GetMapping("/docinquiry")
    public List<DocumentHeaderDTO> getAllDocuments() {
        log.debug("REST request to get all Documents");
        return documentInquiryService.searchDocumentsByRepoURL();
    }
}
