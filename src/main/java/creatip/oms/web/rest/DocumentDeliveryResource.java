package creatip.oms.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import creatip.oms.domain.User;
import creatip.oms.repository.DocumentDeliveryRepository;
import creatip.oms.service.ApplicationUserService;
import creatip.oms.service.DocumentDeliveryService;
import creatip.oms.service.UserService;
import creatip.oms.service.dto.ApplicationUserDTO;
import creatip.oms.service.dto.DocumentDeliveryDTO;
import creatip.oms.service.message.DeliveryMessage;
import creatip.oms.service.message.ReplyMessage;
import creatip.oms.service.message.SearchCriteriaMessage;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
public class DocumentDeliveryResource {

    private final Logger log = LoggerFactory.getLogger(DocumentDeliveryResource.class);

    private static final String ENTITY_NAME = "DocumentDelivery";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private ObjectMapper objectMapper;

    private final DocumentDeliveryService documentDeliveryService;
    private final DocumentDeliveryRepository documentDeliveryRepository;
    private final ApplicationUserService applicationUserService;
    private final UserService userService;

    public DocumentDeliveryResource(
        DocumentDeliveryService documentDeliveryService,
        DocumentDeliveryRepository documentDeliveryRepository,
        UserService userService,
        ApplicationUserService applicationUserService
    ) {
        this.documentDeliveryService = documentDeliveryService;
        this.documentDeliveryRepository = documentDeliveryRepository;
        this.userService = userService;
        this.applicationUserService = applicationUserService;
    }

    @PostMapping("/delivery")
    public ResponseEntity<ReplyMessage<DeliveryMessage>> saveDocumentDelivery(
        @RequestParam(value = "files", required = false) List<MultipartFile> multipartFiles,
        @RequestParam("delivery") String message
    ) throws URISyntaxException {
        DeliveryMessage deliveryMessage = null;
        ReplyMessage<DeliveryMessage> result = null;
        User loginUser = userService.getUserWithAuthorities().get();
        ApplicationUserDTO appUserDTO = applicationUserService.findOneByUserID(loginUser.getId());
        if (appUserDTO == null || appUserDTO.getDepartment() == null) {
            result = new ReplyMessage<DeliveryMessage>();
            result.setCode(ResponseCode.ERROR_E01);
            result.setMessage(loginUser.getLogin() + " is not linked with any department.");
            return ResponseEntity
                .created(new URI("/api/delivery/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        }

        try {
            this.objectMapper = new ObjectMapper();
            deliveryMessage = this.objectMapper.readValue(message, DeliveryMessage.class);
            log.debug("REST request to save Document Delivery: {}", deliveryMessage);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            result = new ReplyMessage<DeliveryMessage>();
            result.setCode(ResponseCode.ERROR_E01);
            result.setMessage("Unrecognized field included in the request message");
            return ResponseEntity
                .created(new URI("/api/delivery/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = new ReplyMessage<DeliveryMessage>();
            result.setCode(ResponseCode.ERROR_E01);
            result.setMessage("Unrecognized Field while parsing string to object");
            return ResponseEntity
                .created(new URI("/api/delivery/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        }

        if (deliveryMessage != null && deliveryMessage.getDocumentDelivery().getId() != null) {
            throw new BadRequestAlertException("A new document cannot already have an ID", ENTITY_NAME, "idexists");
        }

        try {
            deliveryMessage.getDocumentDelivery().setSender(appUserDTO.getDepartment());
            result = documentDeliveryService.save(deliveryMessage, multipartFiles);
        } catch (UploadFailedException e) {
            ReplyMessage<DeliveryMessage> uploadFailedMessage = new ReplyMessage<DeliveryMessage>();
            uploadFailedMessage.setCode(e.getCode());
            uploadFailedMessage.setMessage(e.getMessage());
            return ResponseEntity
                .created(new URI("/api/delivery/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(uploadFailedMessage);
        }

        String docHeaderId = "";
        if (result != null && result.getCode().equals(ResponseCode.SUCCESS)) {
            docHeaderId = result.getData().getDocumentDelivery().getId().toString();
        }

        return ResponseEntity
            .created(new URI("/api/delivery/" + docHeaderId))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, docHeaderId))
            .body(result);
    }

    @PutMapping("/delivery/{id}")
    public ResponseEntity<ReplyMessage<DeliveryMessage>> updateDocumentDelivery(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestParam(value = "files", required = false) List<MultipartFile> multipartFiles,
        @RequestParam("delivery") String message
    ) throws URISyntaxException {
        DeliveryMessage deliveryMessage = null;
        ReplyMessage<DeliveryMessage> result = null;

        try {
            this.objectMapper = new ObjectMapper();
            deliveryMessage = this.objectMapper.readValue(message, DeliveryMessage.class);
            log.debug("REST request to update DocumentDelivery : {}, {}", id, deliveryMessage);
        } catch (JsonProcessingException ex) {
            log.debug("Unrecognized Field while parsing string to object named DeliveryMessage");
            log.debug("JsonProcessingException :{}", ex.getMessage());

            result = new ReplyMessage<DeliveryMessage>();
            result.setCode(ResponseCode.ERROR_E01);
            result.setMessage("Unrecognized Field while parsing string to object");
            return ResponseEntity
                .created(new URI("/api/delivery/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = new ReplyMessage<DeliveryMessage>();
            result.setCode(ResponseCode.ERROR_E01);
            result.setMessage("Unrecognized Field while parsing string to object");
            return ResponseEntity
                .created(new URI("/api/delivery/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        }

        if (deliveryMessage == null) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idnull");
        }
        if (deliveryMessage.getDocumentDelivery().getId() == null) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deliveryMessage.getDocumentDelivery().getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentDeliveryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        String docHeaderId = id.toString();

        try {
            result = documentDeliveryService.save(deliveryMessage, multipartFiles);
        } catch (UploadFailedException e) {
            ReplyMessage<DeliveryMessage> uploadFailedMessage = new ReplyMessage<DeliveryMessage>();
            uploadFailedMessage.setCode(e.getCode());
            uploadFailedMessage.setMessage(e.getMessage());
            return ResponseEntity
                .created(new URI("/api/delivery/" + docHeaderId))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, docHeaderId))
                .body(uploadFailedMessage);
        }
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, docHeaderId))
            .body(result);
    }

    @GetMapping("/delivery/{id}")
    public ResponseEntity<DeliveryMessage> getDocumentDelivery(@PathVariable Long id) {
        log.debug("REST request to get DocumentDelivery : {}", id);
        Optional<DeliveryMessage> departmentDTO = documentDeliveryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(departmentDTO);
    }

    @GetMapping("/delivery/received")
    public ResponseEntity<List<DocumentDeliveryDTO>> getReceivedDeliveryList(@RequestParam("criteria") String criteria, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get DocumentDelivery Received List");
        log.debug("SearchCriteriaMessage :{} ", criteria);
        SearchCriteriaMessage criteriaMessage = null;
        try {
            this.objectMapper = new ObjectMapper();
            criteriaMessage = this.objectMapper.readValue(criteria, SearchCriteriaMessage.class);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return ResponseEntity
                .created(new URI("/api/delivery/received/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity
                .created(new URI("/api/delivery/received/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(null);
        }

        Page<DocumentDeliveryDTO> page = documentDeliveryService.getReceivedDeliveryList(criteriaMessage, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/delivery/sent")
    public ResponseEntity<List<DocumentDeliveryDTO>> getSentDeliveryList(@RequestParam("criteria") String criteria, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get DocumentDelivery Sent List");
        log.debug("SearchCriteriaMessage :{} ", criteria);
        SearchCriteriaMessage criteriaMessage = null;
        try {
            this.objectMapper = new ObjectMapper();
            criteriaMessage = this.objectMapper.readValue(criteria, SearchCriteriaMessage.class);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return ResponseEntity
                .created(new URI("/api/delivery/sent/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity
                .created(new URI("/api/delivery/sent/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(null);
        }

        Page<DocumentDeliveryDTO> page = documentDeliveryService.getSentDeliveryList(criteriaMessage, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
