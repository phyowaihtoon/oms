package creatip.oms.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import creatip.oms.repository.DocumentHeaderRepository;
import creatip.oms.service.DocumentHeaderService;
import creatip.oms.service.DocumentService;
import creatip.oms.service.MetaDataService;
import creatip.oms.service.RepositoryService;
import creatip.oms.service.dto.DocumentHeaderDTO;
import creatip.oms.service.dto.MetaDataHeaderDTO;
import creatip.oms.service.dto.RepositoryHeaderDTO;
import creatip.oms.service.message.BaseMessage;
import creatip.oms.service.message.ReplyMessage;
import creatip.oms.service.message.UploadFailedException;
import creatip.oms.util.ResponseCode;
import creatip.oms.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link creatip.oms.domain.Document}.
 */
@RestController
@RequestMapping("/apiresource/v1")
public class DmsWebResource {

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Logger log = LoggerFactory.getLogger(RepositoryResource.class);

    private final RepositoryService repositoryService;

    private final MetaDataService metaDataService;

    private final DocumentService documentService;

    private final DocumentHeaderService documentHeaderService;

    private final DocumentHeaderRepository documentHeaderRepository;

    private ObjectMapper objectMapper;

    public DmsWebResource(
        RepositoryService repositoryService,
        MetaDataService metaDataService,
        DocumentService documentService,
        DocumentHeaderService documentHeaderService,
        DocumentHeaderRepository documentHeaderRepository
    ) {
        this.repositoryService = repositoryService;
        this.metaDataService = metaDataService;
        this.documentService = documentService;
        this.documentHeaderService = documentHeaderService;
        this.documentHeaderRepository = documentHeaderRepository;
    }

    /**
     * {@code GET  /getAllRepository} : get all the repository.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of repository in body.
     */
    @GetMapping("/getAllRepositories")
    public ResponseEntity<List<RepositoryHeaderDTO>> getAllRepositories() {
        log.debug("REST request to get all of Repositories");
        List<RepositoryHeaderDTO> data = repositoryService.findAllRepository();
        return ResponseEntity.ok().body(data);
    }

    /**
     * {@code GET  /getRepositoryById} : get the "repository id" Repository.
     *
     * @param repositoryId the id of the RepositoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the repositoryDTO, or with status {@code 404 (Not Found)}.
     */
    //@GetMapping("/repository/{id}")
    @RequestMapping(value = "/getRepositoryById", method = RequestMethod.GET)
    public ResponseEntity<RepositoryHeaderDTO> getRepository(@RequestParam(value = "repositoryId") Long id) {
        log.debug("REST request to get Repository : {}", id);
        Optional<RepositoryHeaderDTO> dto = repositoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dto);
    }

    /**
     * {@code GET  /getAllMetaData} : get all the metadata.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of metadata in body.
     */
    @GetMapping("/getAllMetaData")
    public ResponseEntity<List<MetaDataHeaderDTO>> getAllMetaData() {
        log.debug("REST request to get all of MetaData");
        List<MetaDataHeaderDTO> data = metaDataService.findAllMetaData();
        return ResponseEntity.ok().body(data);
    }

    /**
     * {@code GET  /getMetaDataById} : get the "metadata id" Repository.
     *
     * @param repositoryId the id of the MetaDataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the metadataDTO, or with status {@code 404 (Not Found)}.
     */
    @RequestMapping(value = "/getMetaDataById", method = RequestMethod.GET)
    public ResponseEntity<MetaDataHeaderDTO> getMetaData(@RequestParam(value = "metadataId") Long id) {
        log.debug("REST request to get Repository : {}", id);
        Optional<MetaDataHeaderDTO> dto = metaDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dto);
    }

    @GetMapping("/deleteDocumentById")
    public ResponseEntity<BaseMessage> checkFileExist(@RequestParam(value = "documentId") Long id) {
        log.debug("REST request to get file info");
        BaseMessage result = documentService.deleteFileById(id);
        return ResponseEntity.ok().body(result);
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
        String ENTITY_NAME = "document";
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
        String ENTITY_NAME = "document";
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
}
