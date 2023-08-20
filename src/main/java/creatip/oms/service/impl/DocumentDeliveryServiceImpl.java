package creatip.oms.service.impl;

import creatip.oms.domain.DocumentAttachment;
import creatip.oms.domain.DocumentDelivery;
import creatip.oms.domain.DocumentReceiver;
import creatip.oms.enumeration.CommonEnum.DeliveryStatus;
import creatip.oms.repository.DocumentAttachmentRepository;
import creatip.oms.repository.DocumentDeliveryRepository;
import creatip.oms.repository.DocumentReceiverRepository;
import creatip.oms.service.DocumentDeliveryService;
import creatip.oms.service.dto.DocumentDeliveryDTO;
import creatip.oms.service.mapper.DocumentAttachmentMapper;
import creatip.oms.service.mapper.DocumentDeliveryMapper;
import creatip.oms.service.mapper.DocumentReceiverMapper;
import creatip.oms.service.message.DeliveryMessage;
import creatip.oms.service.message.NotificationMessage;
import creatip.oms.service.message.ReplyMessage;
import creatip.oms.service.message.UploadFailedException;
import creatip.oms.util.ResponseCode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class DocumentDeliveryServiceImpl implements DocumentDeliveryService {

    private final Logger log = LoggerFactory.getLogger(DocumentDeliveryServiceImpl.class);

    private final DocumentDeliveryRepository documentDeliveryRepository;
    private final DocumentAttachmentRepository documentAttachmentRepository;
    private final DocumentReceiverRepository documentReceiverRepository;

    private final DocumentDeliveryMapper documentDeliveryMapper;
    private final DocumentAttachmentMapper documentAttachmentMapper;
    private final DocumentReceiverMapper documentReceiverMapper;

    private ReplyMessage<DeliveryMessage> replyMessage;

    public DocumentDeliveryServiceImpl(
        DocumentDeliveryRepository documentDeliveryRepository,
        DocumentAttachmentRepository documentAttachmentRepository,
        DocumentReceiverRepository documentReceiverRepository,
        DocumentDeliveryMapper documentDeliveryMapper,
        DocumentAttachmentMapper documentAttachmentMapper,
        DocumentReceiverMapper documentReceiverMapper
    ) {
        this.documentDeliveryRepository = documentDeliveryRepository;
        this.documentAttachmentRepository = documentAttachmentRepository;
        this.documentReceiverRepository = documentReceiverRepository;
        this.documentDeliveryMapper = documentDeliveryMapper;
        this.documentAttachmentMapper = documentAttachmentMapper;
        this.documentReceiverMapper = documentReceiverMapper;
        this.replyMessage = new ReplyMessage<DeliveryMessage>();
    }

    @Override
    @Transactional(rollbackFor = UploadFailedException.class)
    public ReplyMessage<DeliveryMessage> save(DeliveryMessage message, List<MultipartFile> attachedFiles) throws UploadFailedException {
        log.debug("Request to save Document Delivery : {}", message);
        if (message != null && message.getAttachmentList() != null && message.getAttachmentList().size() == 0) {
            replyMessage.setCode(ResponseCode.ERROR_E00);
            replyMessage.setMessage("There is no attached document.");
            return replyMessage;
        }

        try {
            log.debug("Saving Document Delivery: {}", message);

            DocumentDelivery delivery = documentDeliveryMapper.toEntity(message.getDocumentDelivery());
            if (delivery.getDeliveryStatus() == DeliveryStatus.SENT.value && delivery.getSentDate() == null) delivery.setSentDate(
                Instant.now()
            );

            delivery = documentDeliveryRepository.save(delivery);
            message.setDocumentDelivery(documentDeliveryMapper.toDto(delivery));

            log.debug("Saving Document Receiver");
            List<DocumentReceiver> receiverList = message
                .getReceiverList()
                .stream()
                .map(documentReceiverMapper::toEntity)
                .collect(Collectors.toList());

            List<DocumentReceiver> savedReceiverList = new ArrayList<DocumentReceiver>();

            for (DocumentReceiver reciever : receiverList) {
                reciever.setHeader(delivery);
                reciever = documentReceiverRepository.save(reciever);
                savedReceiverList.add(reciever);
            }

            message.setReceiverList(documentReceiverMapper.toDto(savedReceiverList));

            log.debug("Saving Document Attachment");
            List<DocumentAttachment> attachmentList = message
                .getAttachmentList()
                .stream()
                .map(documentAttachmentMapper::toEntity)
                .collect(Collectors.toList());

            List<DocumentAttachment> savedAttachmentList = new ArrayList<DocumentAttachment>();

            for (DocumentAttachment document : attachmentList) {
                document.setHeader(delivery);
                document = documentAttachmentRepository.save(document);
                savedAttachmentList.add(document);
            }

            message.setAttachmentList(documentAttachmentMapper.toDto(savedAttachmentList));

            /*
             * New uploaded document information will be saved and uploaded separately in
             * this method
             */
            /*
             * if (attachedFiles != null && attachedFiles.size() > 0) { if
             * (!saveAndUploadFiles(multipartFiles, documentHeader.getId(),
             * documentDTOList)) { throw new UploadFailedException("Upload failed",
             * replyMessage.getCode(), replyMessage.getMessage()); } }
             */

            replyMessage.setCode(ResponseCode.SUCCESS);
            replyMessage.setMessage("Document Mapping is successfully saved");
            replyMessage.setData(message);
        }/*
         * catch (UploadFailedException ex) { throw ex; }
         */ catch (Exception ex) {
            ex.printStackTrace();
            replyMessage.setCode(ResponseCode.ERROR_E01);
            replyMessage.setMessage(ex.getMessage());
        }

        return replyMessage;
    }

    @Override
    public Page<DocumentDeliveryDTO> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<DocumentDeliveryDTO> findOne(Long id) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public List<NotificationMessage> getNotification(Long departmentId) {
        List<NotificationMessage> notiList = new ArrayList<NotificationMessage>();
        List<DocumentReceiver> list = documentReceiverRepository.findUnReadMailByRecieverId(departmentId);
        for (DocumentReceiver receiver : list) {
            NotificationMessage notiMessage = new NotificationMessage();
            notiMessage.setReferenceNo(receiver.getHeader().getReferenceNo());
            notiMessage.setSubject(receiver.getHeader().getSubject());
            notiList.add(notiMessage);
        }
        return notiList;
    }
}
