package com.hmm.dms.web.rest;

import com.hmm.dms.repository.DocumentHeaderRepository;
import com.hmm.dms.service.DocumentHeaderService;
import com.hmm.dms.service.dto.DocumentHeaderDTO;
import com.hmm.dms.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class DocumentHeaderResource {

    private final Logger log = LoggerFactory.getLogger(DocumentResource.class);

    private static final String ENTITY_NAME = "document_header";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentHeaderService documenHeadertService;

    private final DocumentHeaderRepository documenHeadertRepository;

    public DocumentHeaderResource(DocumentHeaderService documentHeaderService, DocumentHeaderRepository documentHeaderRepository) {
        this.documenHeadertService = documentHeaderService;
        this.documenHeadertRepository = documentHeaderRepository;
    }

    @PostMapping("/documentHeader")
    public ResponseEntity<DocumentHeaderDTO> createDocument(@Valid @RequestBody DocumentHeaderDTO documentHeaderDTO)
        throws URISyntaxException {
        log.debug("REST request to save Document : {}", documentHeaderDTO);
        if (documentHeaderDTO.getId() != null) {
            throw new BadRequestAlertException("A new document cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocumentHeaderDTO result = documenHeadertService.save(documentHeaderDTO);
        return ResponseEntity
            .created(new URI("/api/documentHeader/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
