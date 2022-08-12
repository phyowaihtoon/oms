package com.hmm.dms.web.rest;

import com.hmm.dms.domain.DocumentHeader;
import com.hmm.dms.repository.DocumentHeaderRepository;
import com.hmm.dms.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hmm.dms.domain.DocumentHeader}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DocumentHeaderResource {

    private final Logger log = LoggerFactory.getLogger(DocumentHeaderResource.class);

    private static final String ENTITY_NAME = "documentHeader";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentHeaderRepository documentHeaderRepository;

    public DocumentHeaderResource(DocumentHeaderRepository documentHeaderRepository) {
        this.documentHeaderRepository = documentHeaderRepository;
    }

    /**
     * {@code POST  /document-headers} : Create a new documentHeader.
     *
     * @param documentHeader the documentHeader to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentHeader, or with status {@code 400 (Bad Request)} if the documentHeader has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/document-headers")
    public ResponseEntity<DocumentHeader> createDocumentHeader(@RequestBody DocumentHeader documentHeader) throws URISyntaxException {
        log.debug("REST request to save DocumentHeader : {}", documentHeader);
        if (documentHeader.getId() != null) {
            throw new BadRequestAlertException("A new documentHeader cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocumentHeader result = documentHeaderRepository.save(documentHeader);
        return ResponseEntity
            .created(new URI("/api/document-headers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /document-headers/:id} : Updates an existing documentHeader.
     *
     * @param id the id of the documentHeader to save.
     * @param documentHeader the documentHeader to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentHeader,
     * or with status {@code 400 (Bad Request)} if the documentHeader is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentHeader couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/document-headers/{id}")
    public ResponseEntity<DocumentHeader> updateDocumentHeader(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentHeader documentHeader
    ) throws URISyntaxException {
        log.debug("REST request to update DocumentHeader : {}, {}", id, documentHeader);
        if (documentHeader.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentHeader.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentHeaderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DocumentHeader result = documentHeaderRepository.save(documentHeader);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentHeader.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /document-headers/:id} : Partial updates given fields of an existing documentHeader, field will ignore if it is null
     *
     * @param id the id of the documentHeader to save.
     * @param documentHeader the documentHeader to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentHeader,
     * or with status {@code 400 (Bad Request)} if the documentHeader is not valid,
     * or with status {@code 404 (Not Found)} if the documentHeader is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentHeader couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/document-headers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DocumentHeader> partialUpdateDocumentHeader(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentHeader documentHeader
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocumentHeader partially : {}, {}", id, documentHeader);
        if (documentHeader.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentHeader.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentHeaderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentHeader> result = documentHeaderRepository
            .findById(documentHeader.getId())
            .map(
                existingDocumentHeader -> {
                    if (documentHeader.getMetaDataHeaderId() != null) {
                        existingDocumentHeader.setMetaDataHeaderId(documentHeader.getMetaDataHeaderId());
                    }
                    if (documentHeader.getFieldNames() != null) {
                        existingDocumentHeader.setFieldNames(documentHeader.getFieldNames());
                    }
                    if (documentHeader.getFieldValues() != null) {
                        existingDocumentHeader.setFieldValues(documentHeader.getFieldValues());
                    }

                    return existingDocumentHeader;
                }
            )
            .map(documentHeaderRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentHeader.getId().toString())
        );
    }

    /**
     * {@code GET  /document-headers} : get all the documentHeaders.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentHeaders in body.
     */
    @GetMapping("/document-headers")
    public List<DocumentHeader> getAllDocumentHeaders() {
        log.debug("REST request to get all DocumentHeaders");
        return documentHeaderRepository.findAll();
    }

    /**
     * {@code GET  /document-headers/:id} : get the "id" documentHeader.
     *
     * @param id the id of the documentHeader to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentHeader, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/document-headers/{id}")
    public ResponseEntity<DocumentHeader> getDocumentHeader(@PathVariable Long id) {
        log.debug("REST request to get DocumentHeader : {}", id);
        Optional<DocumentHeader> documentHeader = documentHeaderRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(documentHeader);
    }

    /**
     * {@code DELETE  /document-headers/:id} : delete the "id" documentHeader.
     *
     * @param id the id of the documentHeader to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/document-headers/{id}")
    public ResponseEntity<Void> deleteDocumentHeader(@PathVariable Long id) {
        log.debug("REST request to delete DocumentHeader : {}", id);
        documentHeaderRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
