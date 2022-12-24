package com.hmm.dms.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmm.dms.repository.DocumentHeaderRepository;
import com.hmm.dms.service.DocumentHeaderService;
import com.hmm.dms.service.DocumentService;
import com.hmm.dms.service.dto.DocumentHeaderDTO;
import com.hmm.dms.service.message.BaseMessage;
import com.hmm.dms.service.message.DocumentInquiryMessage;
import com.hmm.dms.service.message.ReplyMessage;
import com.hmm.dms.service.message.UploadFailedException;
import com.hmm.dms.util.ResponseCode;
import com.hmm.dms.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link com.hmm.dms.domain.Document}.
 */
@RestController
@RequestMapping("/api")
public class DocumentResource {

    private final Logger log = LoggerFactory.getLogger(DocumentResource.class);
    private static final String ENTITY_NAME = "document";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentHeaderService documentHeaderService;
    private final DocumentService documentService;
    private final DocumentHeaderRepository documentHeaderRepository;
    private ObjectMapper objectMapper;

    public DocumentResource(
        DocumentHeaderService documentHeaderService,
        DocumentHeaderRepository documentHeaderRepository,
        DocumentService documenService
    ) {
        this.documentHeaderService = documentHeaderService;
        this.documentHeaderRepository = documentHeaderRepository;
        this.documentService = documenService;
    }

    /**
     * {@code POST  /documents} : Create a new document.
     *
     * @param documentDTO the documentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentDTO, or with status {@code 400 (Bad Request)} if the document has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/documents")
    public ResponseEntity<ReplyMessage<DocumentHeaderDTO>> createDocument(
        @RequestParam(value = "files", required = false) List<MultipartFile> multipartFiles,
        @RequestParam("documentHeaderData") String docHeaderInStr
    ) throws URISyntaxException {
        DocumentHeaderDTO documentHeaderDTO = null;
        ReplyMessage<DocumentHeaderDTO> result = null;

        try {
            this.objectMapper = new ObjectMapper();
            documentHeaderDTO = this.objectMapper.readValue(docHeaderInStr, DocumentHeaderDTO.class);
            log.debug("REST request to save Document Mapping: {}", documentHeaderDTO);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            result = new ReplyMessage<DocumentHeaderDTO>();
            result.setCode(ResponseCode.ERROR_E01);
            result.setMessage("Unrecognized Field while parsing string to object");
            return ResponseEntity
                .created(new URI("/api/documentHeader/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = new ReplyMessage<DocumentHeaderDTO>();
            result.setCode(ResponseCode.ERROR_E01);
            result.setMessage("Unrecognized Field while parsing string to object");
            return ResponseEntity
                .created(new URI("/api/documentHeader/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        }

        if (documentHeaderDTO.getId() != null) {
            throw new BadRequestAlertException("A new document cannot already have an ID", ENTITY_NAME, "idexists");
        }
        String docHeaderId = "";

        try {
            result = documentHeaderService.saveAndUploadDocuments(multipartFiles, documentHeaderDTO);
        } catch (UploadFailedException e) {
            ReplyMessage<DocumentHeaderDTO> uploadFailedMessage = new ReplyMessage<DocumentHeaderDTO>();
            uploadFailedMessage.setCode(e.getCode());
            uploadFailedMessage.setMessage(e.getMessage());
            return ResponseEntity
                .created(new URI("/api/documentHeader/" + docHeaderId))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, docHeaderId))
                .body(uploadFailedMessage);
        }
        if (result != null && result.getCode().equals(ResponseCode.SUCCESS)) {
            docHeaderId = result.getData().getId().toString();
        }

        return ResponseEntity
            .created(new URI("/api/documentHeader/" + docHeaderId))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, docHeaderId))
            .body(result);
    }

    /**
     * {@code PUT  /documents/:id} : Updates an existing document.
     *
     * @param id the id of the documentDTO to save.
     * @param documentDTO the documentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentDTO,
     * or with status {@code 400 (Bad Request)} if the documentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/documents/{id}")
    public ResponseEntity<ReplyMessage<DocumentHeaderDTO>> updateDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestParam(value = "files", required = false) List<MultipartFile> multipartFiles,
        @RequestParam("documentHeaderData") String docHeaderInStr
    ) throws URISyntaxException {
        DocumentHeaderDTO documentHeaderDTO = null;
        ReplyMessage<DocumentHeaderDTO> result = null;

        try {
            this.objectMapper = new ObjectMapper();
            documentHeaderDTO = this.objectMapper.readValue(docHeaderInStr, DocumentHeaderDTO.class);
            log.debug("REST request to update Repository : {}, {}", id, documentHeaderDTO);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            result = new ReplyMessage<DocumentHeaderDTO>();
            result.setCode(ResponseCode.ERROR_E01);
            result.setMessage("Unrecognized Field while parsing string to object");
            return ResponseEntity
                .created(new URI("/api/documentHeader/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = new ReplyMessage<DocumentHeaderDTO>();
            result.setCode(ResponseCode.ERROR_E01);
            result.setMessage("Unrecognized Field while parsing string to object");
            return ResponseEntity
                .created(new URI("/api/documentHeader/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        }

        if (documentHeaderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentHeaderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentHeaderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        String docHeaderId = id.toString();

        try {
            result = documentHeaderService.saveAndUploadDocuments(multipartFiles, documentHeaderDTO);
        } catch (UploadFailedException e) {
            ReplyMessage<DocumentHeaderDTO> uploadFailedMessage = new ReplyMessage<DocumentHeaderDTO>();
            uploadFailedMessage.setCode(e.getCode());
            uploadFailedMessage.setMessage(e.getMessage());
            return ResponseEntity
                .created(new URI("/api/documentHeader/" + docHeaderId))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, docHeaderId))
                .body(uploadFailedMessage);
        }
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, docHeaderId))
            .body(result);
    }

    @PatchMapping(value = "/documents/{id}")
    public ResponseEntity<ReplyMessage<DocumentInquiryMessage>> partialUpdateDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DocumentInquiryMessage approvalInfo
    ) throws URISyntaxException {
        ReplyMessage<DocumentInquiryMessage> result = documentHeaderService.partialUpdate(approvalInfo, id);
        String docHeaderId = id.toString();
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, docHeaderId))
            .body(result);
    }

    @PostMapping(value = "/documents/restore")
    public ResponseEntity<BaseMessage> restoreDocument(@Valid @RequestBody DocumentHeaderDTO headerDTO) throws URISyntaxException {
        BaseMessage result = documentHeaderService.restoreDocument(headerDTO);
        String docHeaderId = headerDTO.toString();
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, docHeaderId))
            .body(result);
    }

    @GetMapping("/documents/deleteFileById/{id}")
    public ResponseEntity<BaseMessage> checkFileExist(@PathVariable(value = "id", required = false) final Long id) {
        log.debug("REST request to get file info");
        BaseMessage result = documentService.deleteFileById(id);
        String docId = id.toString();
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, docId)).body(result);
    }
}
