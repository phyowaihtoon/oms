package com.hmm.dms.web.rest;

import com.hmm.dms.service.DocumentInquiryService;
import com.hmm.dms.service.dto.DocumentHeaderDTO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api")
public class DocumentInquiryResource {

    private final Logger log = LoggerFactory.getLogger(DocumentResource.class);

    private final DocumentInquiryService documentInquiryService;

    public DocumentInquiryResource(DocumentInquiryService documentInquiryService) {
        this.documentInquiryService = documentInquiryService;
    }

    @GetMapping("/docinquiry")
    public ResponseEntity<List<DocumentHeaderDTO>> getAllDocuments(Pageable pageable) {
        log.debug("REST request to get all Documents");
        Page<DocumentHeaderDTO> page = documentInquiryService.searchDocumentsByRepoURL(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
