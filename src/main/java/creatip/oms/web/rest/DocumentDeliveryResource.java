package creatip.oms.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import creatip.oms.domain.DocumentAttachment;
import creatip.oms.domain.User;
import creatip.oms.enumeration.CommonEnum.RequestFrom;
import creatip.oms.repository.DocumentAttachmentRepository;
import creatip.oms.repository.DocumentDeliveryRepository;
import creatip.oms.service.ApplicationUserService;
import creatip.oms.service.DocumentDeliveryService;
import creatip.oms.service.FTPRepositoryService;
import creatip.oms.service.UserService;
import creatip.oms.service.dto.ApplicationUserDTO;
import creatip.oms.service.dto.DocumentDeliveryDTO;
import creatip.oms.service.message.DeliveryMessage;
import creatip.oms.service.message.ReplyMessage;
import creatip.oms.service.message.SearchCriteriaMessage;
import creatip.oms.service.message.UploadFailedException;
import creatip.oms.util.ResponseCode;
import creatip.oms.util.SharedUtils;
import creatip.oms.web.rest.errors.BadRequestAlertException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    private final FTPRepositoryService ftpRepositoryService;
    private final DocumentAttachmentRepository documentAttachmentRepository;

    public DocumentDeliveryResource(
        DocumentDeliveryService documentDeliveryService,
        DocumentDeliveryRepository documentDeliveryRepository,
        UserService userService,
        ApplicationUserService applicationUserService,
        FTPRepositoryService ftpRepositoryService,
        DocumentAttachmentRepository documentAttachmentRepository
    ) {
        this.documentDeliveryService = documentDeliveryService;
        this.documentDeliveryRepository = documentDeliveryRepository;
        this.userService = userService;
        this.applicationUserService = applicationUserService;
        this.ftpRepositoryService = ftpRepositoryService;
        this.documentAttachmentRepository = documentAttachmentRepository;
    }

    @PostMapping("/delivery")
    public ResponseEntity<ReplyMessage<DeliveryMessage>> saveDocumentDelivery(
        @RequestParam(value = "files", required = false) List<MultipartFile> multipartFiles,
        @RequestParam("delivery") String message
    ) throws URISyntaxException {
        log.debug("Message request to save Document Delivery: {}", message);

        DeliveryMessage deliveryMessage = null;
        ReplyMessage<DeliveryMessage> result = null;
        User loginUser = userService.getUserWithAuthorities().get();
        ApplicationUserDTO appUserDTO = applicationUserService.findOneByUserID(loginUser.getId());
        if (appUserDTO == null || appUserDTO.getDepartment() == null) {
            String responseMessage = loginUser.getLogin() + " is not linked with any department.";
            log.debug("Message Response : {}", responseMessage);
            result = new ReplyMessage<DeliveryMessage>();
            result.setCode(ResponseCode.ERROR_E00);
            result.setMessage(responseMessage);
            return ResponseEntity
                .created(new URI("/api/delivery/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        }

        try {
            this.objectMapper = new ObjectMapper();
            deliveryMessage = this.objectMapper.readValue(message, DeliveryMessage.class);
        } catch (JsonProcessingException ex) {
            String responseMessage = "Invalid request ," + ex.getMessage();
            log.debug("Message Response : {}", responseMessage);
            log.error("Exception :", ex);
            result = new ReplyMessage<DeliveryMessage>();
            result.setCode(ResponseCode.ERROR_E01);
            result.setMessage(responseMessage);
            return ResponseEntity
                .created(new URI("/api/delivery/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        } catch (Exception ex) {
            String responseMessage = "Invalid request ," + ex.getMessage();
            log.debug("Message Response : {}", responseMessage);
            log.error("Exception :", ex);
            result = new ReplyMessage<DeliveryMessage>();
            result.setCode(ResponseCode.ERROR_E01);
            result.setMessage(responseMessage);
            return ResponseEntity
                .created(new URI("/api/delivery/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        }

        if (deliveryMessage != null && deliveryMessage.getDocumentDelivery().getId() != null) {
            String responseMessage = String.format(
                "Bad Request ,a new record cannot have an ID %s",
                "[" + deliveryMessage.getDocumentDelivery().getId() + "]"
            );
            log.debug("Message Response : {}", responseMessage);
            result = new ReplyMessage<DeliveryMessage>();
            result.setCode(ResponseCode.ERROR_E00);
            result.setMessage(responseMessage);
            return ResponseEntity
                .created(new URI("/api/delivery/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        }

        try {
            deliveryMessage.getDocumentDelivery().setSender(appUserDTO.getDepartment());
            result = documentDeliveryService.save(deliveryMessage, multipartFiles);
        } catch (UploadFailedException ex) {
            log.debug("Message Response : {}", ex.getMessage());
            log.error("Exception :", ex);
            ReplyMessage<DeliveryMessage> uploadFailedMessage = new ReplyMessage<DeliveryMessage>();
            uploadFailedMessage.setCode(ex.getCode());
            uploadFailedMessage.setMessage(ex.getMessage());
            return ResponseEntity
                .created(new URI("/api/delivery/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(uploadFailedMessage);
        }

        String docHeaderId = "";
        if (result != null && result.getCode().equals(ResponseCode.SUCCESS)) {
            docHeaderId = result.getData().getDocumentDelivery().getId().toString();
        }

        if (result != null) {
            log.debug("Message Response : {}", result.getMessage());
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
        log.debug("Message request to update DocumentDelivery : {}, {}", id, message);

        DeliveryMessage deliveryMessage = null;
        ReplyMessage<DeliveryMessage> result = null;

        try {
            this.objectMapper = new ObjectMapper();
            deliveryMessage = this.objectMapper.readValue(message, DeliveryMessage.class);
        } catch (JsonProcessingException ex) {
            String responseMessage = "Invalid request ," + ex.getMessage();
            log.debug("Message Response : {}", responseMessage);
            log.error("Exception :{}", ex);
            result = new ReplyMessage<DeliveryMessage>();
            result.setCode(ResponseCode.ERROR_E01);
            result.setMessage(responseMessage);
            return ResponseEntity
                .created(new URI("/api/delivery/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        } catch (Exception ex) {
            String responseMessage = "Invalid request ," + ex.getMessage();
            log.debug("Message Response : {}", responseMessage);
            log.error("Exception :{}", ex);
            result = new ReplyMessage<DeliveryMessage>();
            result.setCode(ResponseCode.ERROR_E01);
            result.setMessage(responseMessage);
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
        } catch (UploadFailedException ex) {
            log.debug("Message Response : {}", ex.getMessage());
            log.error("Exception : {}", ex);
            ReplyMessage<DeliveryMessage> uploadFailedMessage = new ReplyMessage<DeliveryMessage>();
            uploadFailedMessage.setCode(ex.getCode());
            uploadFailedMessage.setMessage(ex.getMessage());
            return ResponseEntity
                .created(new URI("/api/delivery/" + docHeaderId))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, docHeaderId))
                .body(uploadFailedMessage);
        }

        if (result != null) {
            log.debug("Message Response : {}", result.getMessage());
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
        log.debug("Message request to get DocumentDelivery Received List");
        log.debug("SearchCriteriaMessage :{} ", criteria);

        SearchCriteriaMessage criteriaMessage = null;
        try {
            this.objectMapper = new ObjectMapper();
            criteriaMessage = this.objectMapper.readValue(criteria, SearchCriteriaMessage.class);
        } catch (MismatchedInputException ex) {
            String message = "Invalid request parameter";
            log.debug("Response Message : {}", message);
            log.error(ex.getMessage());
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (JsonProcessingException ex) {
            String message = "Invalid request parameter";
            log.debug("Response Message : {}", message);
            log.error("Exception :{}", ex);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (Exception ex) {
            String message = "Invalid request parameter";
            log.debug("Response Message : {}", message);
            log.error("Exception :{}", ex);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        }

        if (!RequestFrom.isValid(criteriaMessage.getRequestFrom())) {
            String message = "Invalid request parameter";
            log.debug("Response Message : {}", message);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        }

        if (
            criteriaMessage.getRequestFrom() == RequestFrom.DASHBOARD.value && !SharedUtils.isDateStringValid(criteriaMessage.getDateOn())
        ) {
            String message = String.format("Invalid Date, %s", criteriaMessage.getDateOn());
            log.debug("Response Message : {}", message);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        }

        if (
            criteriaMessage.getRequestFrom() == RequestFrom.INQUIRY.value &&
            (!SharedUtils.isDateStringValid(criteriaMessage.getDateFrom()) || !SharedUtils.isDateStringValid(criteriaMessage.getDateTo()))
        ) {
            String message = String.format("Invalid Date, %s", criteriaMessage.getDateFrom() + " , " + criteriaMessage.getDateTo());
            log.debug("Response Message : {}", message);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        }

        User loginUser = userService.getUserWithAuthorities().get();
        ApplicationUserDTO appUserDTO = applicationUserService.findOneByUserID(loginUser.getId());
        if (appUserDTO == null || appUserDTO.getDepartment() == null) {
            String message = loginUser.getLogin() + " is not linked with any department.";
            log.debug("Response Message : {}", message);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        }

        criteriaMessage.setReceiverId(appUserDTO.getDepartment().getId());

        Page<DocumentDeliveryDTO> page = documentDeliveryService.getReceivedDeliveryList(criteriaMessage, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/delivery/sent")
    public ResponseEntity<List<DocumentDeliveryDTO>> getSentDeliveryList(@RequestParam("criteria") String criteria, Pageable pageable)
        throws URISyntaxException {
        log.debug("Message request to get DocumentDelivery Sent List");
        log.debug("SearchCriteriaMessage :{} ", criteria);

        SearchCriteriaMessage criteriaMessage = null;
        try {
            this.objectMapper = new ObjectMapper();
            criteriaMessage = this.objectMapper.readValue(criteria, SearchCriteriaMessage.class);
        } catch (JsonProcessingException ex) {
            String message = "Invalid request parameter";
            log.debug("Response Message : {}", message);
            log.error("Exception :{}", ex);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (Exception ex) {
            String message = "Invalid request parameter";
            log.debug("Response Message : {}", message);
            log.error("Exception :{}", ex);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        }

        if (!RequestFrom.isValid(criteriaMessage.getRequestFrom())) {
            String message = "Invalid request parameter";
            log.debug("Response Message : {}", message);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        }

        if (
            criteriaMessage.getRequestFrom() == RequestFrom.DASHBOARD.value && !SharedUtils.isDateStringValid(criteriaMessage.getDateOn())
        ) {
            String message = String.format("Invalid Date, %s", criteriaMessage.getDateOn());
            log.debug("Response Message : {}", message);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        }

        if (
            criteriaMessage.getRequestFrom() == RequestFrom.INQUIRY.value &&
            (!SharedUtils.isDateStringValid(criteriaMessage.getDateFrom()) || !SharedUtils.isDateStringValid(criteriaMessage.getDateTo()))
        ) {
            String message = String.format("Invalid Date, %s", criteriaMessage.getDateFrom() + " , " + criteriaMessage.getDateTo());
            log.debug("Response Message : {}", message);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        }

        User loginUser = userService.getUserWithAuthorities().get();
        ApplicationUserDTO appUserDTO = applicationUserService.findOneByUserID(loginUser.getId());
        if (appUserDTO == null || appUserDTO.getDepartment() == null) {
            String message = loginUser.getLogin() + " is not linked with any department.";
            log.debug("Response Message : {}", message);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        }

        criteriaMessage.setSenderId(appUserDTO.getDepartment().getId());

        Page<DocumentDeliveryDTO> page = documentDeliveryService.getSentDeliveryList(criteriaMessage, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/delivery/draft")
    public ResponseEntity<List<DocumentDeliveryDTO>> getDeliveryDraftList(@RequestParam("criteria") String criteria, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get DocumentDelivery Draft List");
        log.debug("SearchCriteriaMessage :{} ", criteria);

        SearchCriteriaMessage criteriaMessage = null;
        try {
            this.objectMapper = new ObjectMapper();
            criteriaMessage = this.objectMapper.readValue(criteria, SearchCriteriaMessage.class);
        } catch (JsonProcessingException ex) {
            String message = "Invalid request parameter";
            log.debug("Response Message : {}", message);
            log.error("Exception :{}", ex);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (Exception ex) {
            String message = "Invalid request parameter";
            log.debug("Response Message : {}", message);
            log.error("Exception :{}", ex);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        }

        if (!RequestFrom.isValid(criteriaMessage.getRequestFrom())) {
            String message = "Invalid request parameter";
            log.debug("Response Message : {}", message);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        }

        User loginUser = userService.getUserWithAuthorities().get();
        ApplicationUserDTO appUserDTO = applicationUserService.findOneByUserID(loginUser.getId());
        if (appUserDTO == null || appUserDTO.getDepartment() == null) {
            String message = loginUser.getLogin() + " is not linked with any department.";
            log.debug("Response Message : {}", message);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        }

        criteriaMessage.setSenderId(appUserDTO.getDepartment().getId());

        Page<DocumentDeliveryDTO> page = documentDeliveryService.getDeliveryDraftList(criteriaMessage, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/delivery/download/{attchmentId}")
    public ResponseEntity<?> downloadFile(@PathVariable Long attchmentId) {
        log.debug("REST request to download Document Delivery Attachment file");

        Optional<DocumentAttachment> attachment = documentAttachmentRepository.findById(attchmentId);
        if (!attachment.isPresent()) {
            String message = String.format("Invalid Attachment ID :%s", attchmentId);
            log.debug("Response Message : {}", message);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        }

        String filePath = attachment.get().getFilePath();
        String fileName = attachment.get().getFileName();
        int dot = fileName.lastIndexOf('.');
        String extension = (dot == -1) ? "" : fileName.substring(dot + 1);
        if (extension == null || extension.isEmpty()) {
            String message = String.format("Invalid file extension :%s", fileName);
            log.debug("Response Message : {}", message);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        }

        String absoluteFilePath = filePath + "//" + fileName;
        ReplyMessage<ByteArrayResource> replyMessage = null;
        try {
            log.debug("Start Downloading.....{}", absoluteFilePath);
            replyMessage = ftpRepositoryService.downloadFile(absoluteFilePath);
            log.debug("End Downloading.....{}", absoluteFilePath);
        } catch (IOException ex) {
            String message = "Failed to download: [" + absoluteFilePath + "]";
            log.debug("Response Message : {}", message);
            log.error("Exception :{}", ex);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (Exception ex) {
            String message = "Failed to download: [" + absoluteFilePath + "]";
            log.debug("Response Message : {}", message);
            log.error("Exception :{}", ex);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        }

        /* Giving file name "abc" is to avoid character encoding issue for Myanmar font */
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=abc" + extension);
        header.setContentType(new MediaType("application", extension, StandardCharsets.UTF_8));
        return ResponseEntity
            .ok()
            .headers(header)
            //.contentLength(file.length())
            .body(replyMessage.getData());
    }

    @GetMapping("/delivery/preview/{attchmentId}")
    public ResponseEntity<?> previewFile(@PathVariable Long attchmentId) {
        log.debug("REST request to get preview file data of Document Delivery Attachment");

        Optional<DocumentAttachment> attachment = documentAttachmentRepository.findById(attchmentId);
        if (!attachment.isPresent()) {
            String message = String.format("Invalid Attachment ID :%s", attchmentId);
            log.debug("Response Message : {}", message);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        }

        String filePath = attachment.get().getFilePath();
        String fileName = attachment.get().getFileName();
        int dot = fileName.lastIndexOf('.');
        String extension = (dot == -1) ? "" : fileName.substring(dot + 1);
        if (extension == null || extension.isEmpty()) {
            String message = String.format("Invalid file extension :%s", fileName);
            log.debug("Response Message : {}", message);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        }

        String absoluteFilePath = filePath + "//" + fileName;
        ReplyMessage<ByteArrayResource> replyMessage = null;
        try {
            log.debug("Start Downloading.....{}", absoluteFilePath);
            replyMessage = ftpRepositoryService.getPreviewFileData(absoluteFilePath);
            log.debug("End Downloading.....{}", absoluteFilePath);
        } catch (IOException ex) {
            String message = "Failed to download: [" + absoluteFilePath + "]";
            log.debug("Response Message : {}", message);
            log.error("Exception :{}", ex);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (Exception ex) {
            String message = "Failed to download: [" + absoluteFilePath + "]";
            log.debug("Response Message : {}", message);
            log.error("Exception :{}", ex);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        }

        /* Giving file name "abc" is to avoid character encoding issue for Myanmar font */
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=abc" + extension);
        header.setContentType(new MediaType("application", extension, StandardCharsets.UTF_8));
        return ResponseEntity
            .ok()
            .headers(header)
            //.contentLength(file.length())
            .body(replyMessage.getData());
    }
}
