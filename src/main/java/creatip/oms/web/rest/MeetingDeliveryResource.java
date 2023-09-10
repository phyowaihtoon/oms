package creatip.oms.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import creatip.oms.domain.User;
import creatip.oms.repository.MeetingDeliveryRepository;
import creatip.oms.service.ApplicationUserService;
import creatip.oms.service.MeetingDeliveryService;
import creatip.oms.service.UserService;
import creatip.oms.service.dto.ApplicationUserDTO;
import creatip.oms.service.dto.MeetingDeliveryDTO;
import creatip.oms.service.message.MeetingMessage;
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

    public MeetingDeliveryResource(
        MeetingDeliveryService meetingDeliveryService,
        MeetingDeliveryRepository meetingDeliveryRepository,
        UserService userService,
        ApplicationUserService applicationUserService
    ) {
        this.meetingDeliveryService = meetingDeliveryService;
        this.meetingDeliveryRepository = meetingDeliveryRepository;
        this.userService = userService;
        this.applicationUserService = applicationUserService;
    }

    @PostMapping("/meeting")
    public ResponseEntity<ReplyMessage<MeetingMessage>> saveMeetingDelivery(
        @RequestParam(value = "files", required = false) List<MultipartFile> multipartFiles,
        @RequestParam("meeting") String message
    ) throws URISyntaxException {
        MeetingMessage deliveryMessage = null;
        ReplyMessage<MeetingMessage> result = null;
        User loginUser = userService.getUserWithAuthorities().get();
        ApplicationUserDTO appUserDTO = applicationUserService.findOneByUserID(loginUser.getId());
        if (appUserDTO == null || appUserDTO.getDepartment() == null) {
            result = new ReplyMessage<MeetingMessage>();
            result.setCode(ResponseCode.ERROR_E01);
            result.setMessage(loginUser.getLogin() + " is not linked with any department.");
            return ResponseEntity
                .created(new URI("/api/delivery/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        }

        try {
            this.objectMapper = new ObjectMapper();
            deliveryMessage = this.objectMapper.readValue(message, MeetingMessage.class);
            log.debug("REST request to save MeetingDelivery: {}", deliveryMessage);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            result = new ReplyMessage<MeetingMessage>();
            result.setCode(ResponseCode.ERROR_E01);
            result.setMessage("Unrecognized field included in the request message");
            return ResponseEntity
                .created(new URI("/api/meeting/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = new ReplyMessage<MeetingMessage>();
            result.setCode(ResponseCode.ERROR_E01);
            result.setMessage("Unrecognized Field while parsing string to object");
            return ResponseEntity
                .created(new URI("/api/meeting/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        }

        if (deliveryMessage != null && deliveryMessage.getMeetingDelivery().getId() != null) {
            throw new BadRequestAlertException("A new document cannot already have an ID", ENTITY_NAME, "idexists");
        }

        try {
            deliveryMessage.getMeetingDelivery().setSender(appUserDTO.getDepartment());
            result = meetingDeliveryService.save(deliveryMessage, multipartFiles);
        } catch (UploadFailedException e) {
            ReplyMessage<MeetingMessage> uploadFailedMessage = new ReplyMessage<MeetingMessage>();
            uploadFailedMessage.setCode(e.getCode());
            uploadFailedMessage.setMessage(e.getMessage());
            return ResponseEntity
                .created(new URI("/api/meeting/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(uploadFailedMessage);
        }

        String docHeaderId = "";
        if (result != null && result.getCode().equals(ResponseCode.SUCCESS)) {
            docHeaderId = result.getData().getMeetingDelivery().getId().toString();
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
        MeetingMessage meetingMessage = null;
        ReplyMessage<MeetingMessage> result = null;

        try {
            this.objectMapper = new ObjectMapper();
            meetingMessage = this.objectMapper.readValue(message, MeetingMessage.class);
            log.debug("REST request to update MeetingDelivery : {}, {}", id, meetingMessage);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            result = new ReplyMessage<MeetingMessage>();
            result.setCode(ResponseCode.ERROR_E01);
            result.setMessage("Unrecognized Field while parsing string to object");
            return ResponseEntity
                .created(new URI("/api/meeting/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = new ReplyMessage<MeetingMessage>();
            result.setCode(ResponseCode.ERROR_E01);
            result.setMessage("Unrecognized Field while parsing string to object");
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
            result = meetingDeliveryService.save(meetingMessage, multipartFiles);
        } catch (UploadFailedException e) {
            ReplyMessage<MeetingMessage> uploadFailedMessage = new ReplyMessage<MeetingMessage>();
            uploadFailedMessage.setCode(e.getCode());
            uploadFailedMessage.setMessage(e.getMessage());
            return ResponseEntity
                .created(new URI("/api/meeting/" + docHeaderId))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, docHeaderId))
                .body(uploadFailedMessage);
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
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return ResponseEntity
                .created(new URI("/api/meeting/received/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity
                .created(new URI("/api/meeting/received/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(null);
        }

        Page<MeetingDeliveryDTO> page = meetingDeliveryService.getReceivedMeetingList(criteriaMessage, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/meeting/sent")
    public ResponseEntity<List<MeetingDeliveryDTO>> getInvitedMeetingList(@RequestParam("criteria") String criteria, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get MeetingDelivery Invited List");
        log.debug("SearchCriteriaMessage :{} ", criteria);
        SearchCriteriaMessage criteriaMessage = null;
        try {
            this.objectMapper = new ObjectMapper();
            criteriaMessage = this.objectMapper.readValue(criteria, SearchCriteriaMessage.class);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return ResponseEntity
                .created(new URI("/api/meeting/sent/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity
                .created(new URI("/api/meeting/sent/"))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                .body(null);
        }

        Page<MeetingDeliveryDTO> page = meetingDeliveryService.getInvitedMeetingList(criteriaMessage, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
