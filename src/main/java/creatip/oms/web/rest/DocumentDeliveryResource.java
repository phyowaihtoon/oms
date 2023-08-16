package creatip.oms.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import creatip.oms.repository.DocumentDeliveryRepository;
import creatip.oms.service.DocumentDeliveryService;
import creatip.oms.service.message.DeliveryMessage;
import creatip.oms.service.message.DeliveryMessage;
import creatip.oms.service.message.ReplyMessage;
import creatip.oms.service.message.UploadFailedException;
import creatip.oms.util.ResponseCode;
import creatip.oms.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;

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

    public DocumentDeliveryResource(
        DocumentDeliveryService documentDeliveryService,
        DocumentDeliveryRepository documentDeliveryRepository
    ) {
        this.documentDeliveryService = documentDeliveryService;
        this.documentDeliveryRepository = documentDeliveryRepository;
    }

    @PostMapping("/delivery")
    public ResponseEntity<ReplyMessage<DeliveryMessage>> saveDocumentDelivery(
        @RequestParam(value = "files", required = false) List<MultipartFile> multipartFiles,
        @RequestParam("delivery") String message
    ) throws URISyntaxException {
        DeliveryMessage deliveryMessage = null;
        ReplyMessage<DeliveryMessage> result = null;

        try {
            this.objectMapper = new ObjectMapper();
            deliveryMessage = this.objectMapper.readValue(message, DeliveryMessage.class);
            log.debug("REST request to save Document Delivery Mapping: {}", deliveryMessage);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
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

        if (deliveryMessage != null && deliveryMessage.getDocumentDelivery().getId() != null) {
            throw new BadRequestAlertException("A new document cannot already have an ID", ENTITY_NAME, "idexists");
        }
        String docHeaderId = "";

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
        if (result != null && result.getCode().equals(ResponseCode.SUCCESS)) {
            docHeaderId = result.getData().getDocumentDelivery().getId().toString();
        }

        return ResponseEntity
            .created(new URI("/api/delivery/" + docHeaderId))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, docHeaderId))
            .body(result);
    }

    @PutMapping("/delivery/{id}")
    public ResponseEntity<ReplyMessage<DeliveryMessage>> updateMeetingDelivery(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestParam(value = "files", required = false) List<MultipartFile> multipartFiles,
        @RequestParam("delivery") String message
    ) throws URISyntaxException {
        DeliveryMessage deliveryMessage = null;
        ReplyMessage<DeliveryMessage> result = null;

        try {
            this.objectMapper = new ObjectMapper();
            deliveryMessage = this.objectMapper.readValue(message, DeliveryMessage.class);
            log.debug("REST request to update Repository : {}, {}", id, deliveryMessage);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
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
}
