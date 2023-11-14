package creatip.oms.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import creatip.oms.domain.MeetingAttachment;
import creatip.oms.domain.User;
import creatip.oms.enumeration.CommonEnum.RequestFrom;
import creatip.oms.repository.MeetingAttachmentRepository;
import creatip.oms.repository.MeetingDeliveryRepository;
import creatip.oms.service.ApplicationUserService;
import creatip.oms.service.FTPRepositoryService;
import creatip.oms.service.MeetingDeliveryService;
import creatip.oms.service.UserService;
import creatip.oms.service.dto.ApplicationUserDTO;
import creatip.oms.service.dto.MeetingDeliveryDTO;
import creatip.oms.service.message.BaseMessage;
import creatip.oms.service.message.MeetingMessage;
import creatip.oms.service.message.ReplyMessage;
import creatip.oms.service.message.SearchCriteriaMessage;
import creatip.oms.service.message.UploadFailedException;
import creatip.oms.util.ResponseCode;
import creatip.oms.util.SharedUtils;
import creatip.oms.web.rest.errors.BadRequestAlertException;
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
public class MeetingDeliveryResource {

    private final Logger log = LoggerFactory.getLogger(MeetingDeliveryResource.class);

    private static final String ENTITY_NAME = "MeetingDelivery";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private ObjectMapper objectMapper;

    private final MeetingDeliveryService meetingDeliveryService;
    private final MeetingDeliveryRepository meetingDeliveryRepository;
    private final ApplicationUserService applicationUserService;
    private final UserService userService;
    private final MeetingAttachmentRepository meetingAttachmentRepository;
    private final FTPRepositoryService ftpRepositoryService;

    public MeetingDeliveryResource(
        MeetingDeliveryService meetingDeliveryService,
        MeetingDeliveryRepository meetingDeliveryRepository,
        UserService userService,
        ApplicationUserService applicationUserService,
        MeetingAttachmentRepository meetingAttachmentRepository,
        FTPRepositoryService ftpRepositoryService
    ) {
        this.meetingDeliveryService = meetingDeliveryService;
        this.meetingDeliveryRepository = meetingDeliveryRepository;
        this.userService = userService;
        this.applicationUserService = applicationUserService;
        this.meetingAttachmentRepository = meetingAttachmentRepository;
        this.ftpRepositoryService = ftpRepositoryService;
    }

    @PostMapping("/meeting")
    public ResponseEntity<ReplyMessage<MeetingMessage>> saveMeetingDelivery(
        @RequestParam(value = "files", required = false) List<MultipartFile> multipartFiles,
        @RequestParam("meeting") String message
    ) throws URISyntaxException {
        log.debug("Message request to save Meeting Delivery: {}", message);

        MeetingMessage deliveryMessage = null;
        ReplyMessage<MeetingMessage> result = null;
        User loginUser = userService.getUserWithAuthorities().get();
        ApplicationUserDTO appUserDTO = applicationUserService.findOneByUserID(loginUser.getId());
        if (appUserDTO == null || appUserDTO.getDepartment() == null) {
            String responseMessage = loginUser.getLogin() + " is not linked with any department.";
            log.debug("Message Response : {}", responseMessage);
            result = new ReplyMessage<MeetingMessage>();
            result.setCode(ResponseCode.ERROR_E00);
            result.setMessage(responseMessage);
            return ResponseEntity
                .created(new URI("/api/meeting/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        }

        try {
            this.objectMapper = new ObjectMapper();
            deliveryMessage = this.objectMapper.readValue(message, MeetingMessage.class);
        } catch (JsonProcessingException ex) {
            String responseMessage = "Invalid request ," + ex.getMessage();
            log.debug("Message Response : {}", responseMessage);
            log.error("JsonProcessingException :", ex);
            result = new ReplyMessage<MeetingMessage>();
            result.setCode(ResponseCode.EXCEP_EX);
            result.setMessage(responseMessage);
            return ResponseEntity
                .created(new URI("/api/meeting/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        } catch (Exception ex) {
            String responseMessage = "Invalid request ," + ex.getMessage();
            log.debug("Message Response : {}", responseMessage);
            log.error("Exception :", ex);
            result = new ReplyMessage<MeetingMessage>();
            result.setCode(ResponseCode.EXCEP_EX);
            result.setMessage(responseMessage);
            return ResponseEntity
                .created(new URI("/api/meeting/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        }

        if (deliveryMessage != null && deliveryMessage.getMeetingDelivery().getId() != null) {
            String responseMessage = String.format(
                "Bad Request ,a new record cannot have an ID %s",
                "[" + deliveryMessage.getMeetingDelivery().getId() + "]"
            );
            log.debug("Message Response : {}", responseMessage);
            result = new ReplyMessage<MeetingMessage>();
            result.setCode(ResponseCode.ERROR_E00);
            result.setMessage(responseMessage);
            return ResponseEntity
                .created(new URI("/api/meeting/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        }

        try {
            deliveryMessage.getMeetingDelivery().setSender(appUserDTO.getDepartment());
            result = meetingDeliveryService.save(deliveryMessage, multipartFiles);
        } catch (UploadFailedException ex) {
            log.debug("Message Response : {}", ex.getMessage());
            log.error("UploadFailedException :", ex);
            result = new ReplyMessage<MeetingMessage>();
            result.setCode(ex.getCode());
            result.setMessage(ex.getMessage());
            return ResponseEntity
                .created(new URI("/api/meeting/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        } catch (Exception ex) {
            String errMessage = "Transaction could not be processed. Check details in application logs";
            log.debug("Message Response : {}", errMessage);
            log.error("Exception :", ex);
            result = new ReplyMessage<MeetingMessage>();
            result.setCode(ResponseCode.EXCEP_EX);
            result.setMessage(errMessage);
            return ResponseEntity
                .created(new URI("/api/meeting/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        }

        String docHeaderId = "";
        if (result != null && result.getCode().equals(ResponseCode.SUCCESS)) {
            docHeaderId = result.getData().getMeetingDelivery().getId().toString();
        }

        if (result != null) {
            log.debug("Message Response : {}", result.getMessage());
        }

        return ResponseEntity
            .created(new URI("/api/meeting/" + docHeaderId))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, docHeaderId))
            .body(result);
    }

    @PutMapping("/meeting/{id}")
    public ResponseEntity<ReplyMessage<MeetingMessage>> updateMeetingDelivery(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestParam(value = "files", required = false) List<MultipartFile> multipartFiles,
        @RequestParam("meeting") String message
    ) throws URISyntaxException {
        log.debug("Message request to update Meeting Delivery : {}, {}", id, message);

        MeetingMessage meetingMessage = null;
        ReplyMessage<MeetingMessage> result = null;
        User loginUser = userService.getUserWithAuthorities().get();
        ApplicationUserDTO appUserDTO = applicationUserService.findOneByUserID(loginUser.getId());
        if (appUserDTO == null || appUserDTO.getDepartment() == null) {
            String responseMessage = loginUser.getLogin() + " is not linked with any department.";
            log.debug("Message Response : {}", responseMessage);
            result = new ReplyMessage<MeetingMessage>();
            result.setCode(ResponseCode.ERROR_E00);
            result.setMessage(responseMessage);
            return ResponseEntity
                .created(new URI("/api/meeting/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        }

        try {
            this.objectMapper = new ObjectMapper();
            meetingMessage = this.objectMapper.readValue(message, MeetingMessage.class);
        } catch (JsonProcessingException ex) {
            String responseMessage = "Invalid request ," + ex.getMessage();
            log.debug("Message Response : {}", responseMessage);
            log.error("JsonProcessingException :", ex);
            result = new ReplyMessage<MeetingMessage>();
            result.setCode(ResponseCode.EXCEP_EX);
            result.setMessage(responseMessage);
            return ResponseEntity
                .created(new URI("/api/meeting/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        } catch (Exception ex) {
            String responseMessage = "Invalid request ," + ex.getMessage();
            log.debug("Message Response : {}", responseMessage);
            log.error("Exception :", ex);
            result = new ReplyMessage<MeetingMessage>();
            result.setCode(ResponseCode.EXCEP_EX);
            result.setMessage(responseMessage);
            return ResponseEntity
                .created(new URI("/api/meeting/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        }

        if (meetingMessage == null) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idnull");
        }
        if (meetingMessage.getMeetingDelivery().getId() == null) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, meetingMessage.getMeetingDelivery().getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!meetingDeliveryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        String docHeaderId = id.toString();

        try {
            meetingMessage.getMeetingDelivery().setSender(appUserDTO.getDepartment());
            result = meetingDeliveryService.save(meetingMessage, multipartFiles);
        } catch (UploadFailedException ex) {
            log.debug("Message Response : {}", ex.getMessage());
            log.error("UploadFailedException :", ex);
            result = new ReplyMessage<MeetingMessage>();
            result.setCode(ex.getCode());
            result.setMessage(ex.getMessage());
            return ResponseEntity
                .created(new URI("/api/meeting/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        } catch (Exception ex) {
            String errMessage = "Transaction could not be processed. Check details in application logs";
            log.debug("Message Response : {}", errMessage);
            log.error("Exception :", ex);
            result = new ReplyMessage<MeetingMessage>();
            result.setCode(ResponseCode.EXCEP_EX);
            result.setMessage(errMessage);
            return ResponseEntity
                .created(new URI("/api/meeting/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        }

        if (result != null) {
            log.debug("Message Response : {}", result.getMessage());
        }

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, docHeaderId))
            .body(result);
    }

    @GetMapping("/meeting/{id}")
    public ResponseEntity<MeetingMessage> getMeetingDelivery(@PathVariable Long id) {
        log.debug("REST request to get MeetingDelivery : {}", id);
        Optional<MeetingMessage> departmentDTO = meetingDeliveryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(departmentDTO);
    }

    @GetMapping("/meeting/received")
    public ResponseEntity<List<MeetingDeliveryDTO>> getReceivedMeetingList(@RequestParam("criteria") String criteria, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get MeetingDelivery Received List");
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
            log.error(ex.getMessage());
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (Exception ex) {
            String message = "Invalid request parameter";
            log.debug("Response Message : {}", message);
            log.error(ex.getMessage());
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
            String message = "Invalid Date";
            log.debug("Response Message : {}", message);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        }

        if (
            criteriaMessage.getRequestFrom() == RequestFrom.INQUIRY.value &&
            (!SharedUtils.isDateStringValid(criteriaMessage.getDateFrom()) || !SharedUtils.isDateStringValid(criteriaMessage.getDateTo()))
        ) {
            String message = "Invalid Date";
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

        Page<MeetingDeliveryDTO> page = meetingDeliveryService.getReceivedMeetingList(criteriaMessage, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/meeting/sent")
    public ResponseEntity<List<MeetingDeliveryDTO>> getSentMeetingList(@RequestParam("criteria") String criteria, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get MeetingDelivery Invited List");
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
            log.error(ex.getMessage());
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (Exception ex) {
            String message = "Invalid request parameter";
            log.debug("Response Message : {}", message);
            log.error(ex.getMessage());
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
            String message = "Invalid Date";
            log.debug("Response Message : {}", message);
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        }

        if (
            criteriaMessage.getRequestFrom() == RequestFrom.INQUIRY.value &&
            (!SharedUtils.isDateStringValid(criteriaMessage.getDateFrom()) || !SharedUtils.isDateStringValid(criteriaMessage.getDateTo()))
        ) {
            String message = "Invalid Date";
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

        Page<MeetingDeliveryDTO> page = meetingDeliveryService.getSentMeetingList(criteriaMessage, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/meeting/draft")
    public ResponseEntity<List<MeetingDeliveryDTO>> getMeetingDraftList(@RequestParam("criteria") String criteria, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get MeetingDelivery Draft List");
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
            log.error(ex.getMessage());
            HttpHeaders headers = new HttpHeaders();
            headers.add("message", message);
            return ResponseEntity.badRequest().headers(headers).body(null);
        } catch (Exception ex) {
            String message = "Invalid request parameter";
            log.debug("Response Message : {}", message);
            log.error(ex.getMessage());
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

        Page<MeetingDeliveryDTO> page = meetingDeliveryService.getMeetingDraftList(criteriaMessage, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/meeting/scheduled")
    public List<MeetingDeliveryDTO> getScheduledMeetingList() throws URISyntaxException {
        log.debug("REST request to get MeetingDelivery Scheduled List");

        List<MeetingDeliveryDTO> list = meetingDeliveryService.getScheduledMeetingList();
        return list;
    }

    @GetMapping("/meeting/download/{attchmentId}")
    public ResponseEntity<?> downloadFile(@PathVariable Long attchmentId) {
        log.debug("REST request to download Meeting Attachment file");

        Optional<MeetingAttachment> attachment = meetingAttachmentRepository.findById(attchmentId);
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
        ReplyMessage<ByteArrayResource> replyMessage = ftpRepositoryService.downloadFile(absoluteFilePath);

        if (!replyMessage.getCode().equals(ResponseCode.SUCCESS)) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("code", replyMessage.getCode());
            headers.add("message", replyMessage.getMessage());
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

    @GetMapping("/meeting/preview/{attchmentId}")
    public ResponseEntity<?> previewFile(@PathVariable Long attchmentId) {
        log.debug("REST request to get preview file data of Meeting Attachment");

        Optional<MeetingAttachment> attachment = meetingAttachmentRepository.findById(attchmentId);
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
        ReplyMessage<ByteArrayResource> replyMessage = ftpRepositoryService.getPreviewFileData(absoluteFilePath);

        if (!replyMessage.getCode().equals(ResponseCode.SUCCESS)) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("code", replyMessage.getCode());
            headers.add("message", replyMessage.getMessage());
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

    @PutMapping("/meeting/read/{id}")
    public ResponseEntity<ReplyMessage<String>> markAsRead(@PathVariable(value = "id", required = false) final Long id)
        throws URISyntaxException {
        log.debug("Request to mark Meeting Invitation as read :  ID [{}]", id);
        ReplyMessage<String> replyMessage = null;

        User loginUser = userService.getUserWithAuthorities().get();
        ApplicationUserDTO appUserDTO = applicationUserService.findOneByUserID(loginUser.getId());
        if (appUserDTO == null || appUserDTO.getDepartment() == null) {
            replyMessage = new ReplyMessage<String>();
            String message = loginUser.getLogin() + " is not linked with any department.";
            log.debug("Response Message : {}", message);
            replyMessage.setCode(ResponseCode.ERROR_E00);
            replyMessage.setMessage(message);
            return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .body(replyMessage);
        }

        Long loginDepId = appUserDTO.getDepartment().getId();

        replyMessage = meetingDeliveryService.markAsRead(id, loginDepId);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .body(replyMessage);
    }

    @PutMapping("/meeting/unread/{id}")
    public ResponseEntity<ReplyMessage<String>> markAsUnRead(@PathVariable(value = "id", required = false) final Long id)
        throws URISyntaxException {
        log.debug("Request to mark Meeting Invitation as unread :  ID [{}]", id);
        ReplyMessage<String> replyMessage = null;

        User loginUser = userService.getUserWithAuthorities().get();
        ApplicationUserDTO appUserDTO = applicationUserService.findOneByUserID(loginUser.getId());
        if (appUserDTO == null || appUserDTO.getDepartment() == null) {
            replyMessage = new ReplyMessage<String>();
            String message = loginUser.getLogin() + " is not linked with any department.";
            log.debug("Response Message : {}", message);
            replyMessage.setCode(ResponseCode.ERROR_E00);
            replyMessage.setMessage(message);
            return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .body(replyMessage);
        }

        Long loginDepId = appUserDTO.getDepartment().getId();

        replyMessage = meetingDeliveryService.markAsUnRead(id, loginDepId);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .body(replyMessage);
    }

    @PutMapping("/meeting/deleteAttachment/{id}")
    public ResponseEntity<BaseMessage> deleteAttachment(@PathVariable Long id) {
        log.debug("Request to delete Document Delivery Attachment : {}", id);
        BaseMessage replyMessage = meetingDeliveryService.deleteAttachment(id);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .body(replyMessage);
    }
}
