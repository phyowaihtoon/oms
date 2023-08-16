package creatip.oms.service.impl;

import creatip.oms.domain.MeetingAttachment;
import creatip.oms.domain.MeetingDelivery;
import creatip.oms.domain.MeetingReceiver;
import creatip.oms.repository.MeetingAttachmentRepository;
import creatip.oms.repository.MeetingDeliveryRepository;
import creatip.oms.repository.MeetingReceiverRepository;
import creatip.oms.service.MeetingDeliveryService;
import creatip.oms.service.mapper.MeetingAttachmentMapper;
import creatip.oms.service.mapper.MeetingDeliveryMapper;
import creatip.oms.service.mapper.MeetingReceiverMapper;
import creatip.oms.service.message.MeetingMessage;
import creatip.oms.service.message.ReplyMessage;
import creatip.oms.service.message.UploadFailedException;
import creatip.oms.util.ResponseCode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class MeetingDeliveryServiceImpl implements MeetingDeliveryService {

    private final Logger log = LoggerFactory.getLogger(MeetingDeliveryServiceImpl.class);

    private final MeetingDeliveryRepository meetingDeliveryRepository;
    private final MeetingAttachmentRepository meetingAttachmentRepository;
    private final MeetingReceiverRepository meetingReceiverRepository;

    private final MeetingDeliveryMapper meetingDeliveryMapper;
    private final MeetingAttachmentMapper meetingAttachmentMapper;
    private final MeetingReceiverMapper meetingReceiverMapper;

    private ReplyMessage<MeetingMessage> replyMessage;

    public MeetingDeliveryServiceImpl(
        MeetingDeliveryRepository meetingDeliveryRepository,
        MeetingAttachmentRepository meetingAttachmentRepository,
        MeetingReceiverRepository meetingReceiverRepository,
        MeetingDeliveryMapper meetingDeliveryMapper,
        MeetingAttachmentMapper meetingAttachmentMapper,
        MeetingReceiverMapper meetingReceiverMapper
    ) {
        this.meetingDeliveryRepository = meetingDeliveryRepository;
        this.meetingAttachmentRepository = meetingAttachmentRepository;
        this.meetingReceiverRepository = meetingReceiverRepository;
        this.meetingDeliveryMapper = meetingDeliveryMapper;
        this.meetingAttachmentMapper = meetingAttachmentMapper;
        this.meetingReceiverMapper = meetingReceiverMapper;
        this.replyMessage = new ReplyMessage<MeetingMessage>();
    }

    @Override
    @Transactional(rollbackFor = UploadFailedException.class)
    public ReplyMessage<MeetingMessage> save(MeetingMessage message, List<MultipartFile> attachedFiles) throws UploadFailedException {
        log.debug("Request to save Meeting Delivery : {}", message);
        if (message != null && message.getAttachmentList() != null && message.getAttachmentList().size() == 0) {
            replyMessage.setCode(ResponseCode.ERROR_E00);
            replyMessage.setMessage("There is no attached document.");
            return replyMessage;
        }

        try {
            log.debug("Saving Meeting Delivery: {}", message);

            MeetingDelivery delivery = meetingDeliveryMapper.toEntity(message.getMeetingDelivery());
            delivery = meetingDeliveryRepository.save(delivery);
            message.setMeetingDelivery(meetingDeliveryMapper.toDto(delivery));

            log.debug("Saving Meeting Receiver");
            List<MeetingReceiver> receiverList = message
                .getReceiverList()
                .stream()
                .map(meetingReceiverMapper::toEntity)
                .collect(Collectors.toList());

            List<MeetingReceiver> savedReceiverList = new ArrayList<MeetingReceiver>();

            for (MeetingReceiver reciever : receiverList) {
                reciever.setHeader(delivery);
                reciever = meetingReceiverRepository.save(reciever);
                savedReceiverList.add(reciever);
            }

            message.setReceiverList(meetingReceiverMapper.toDto(savedReceiverList));

            log.debug("Saving Meeting Attachment");
            List<MeetingAttachment> attachmentList = message
                .getAttachmentList()
                .stream()
                .map(meetingAttachmentMapper::toEntity)
                .collect(Collectors.toList());

            List<MeetingAttachment> savedAttachmentList = new ArrayList<MeetingAttachment>();

            for (MeetingAttachment attachment : attachmentList) {
                attachment.setHeader(delivery);
                attachment = meetingAttachmentRepository.save(attachment);
                savedAttachmentList.add(attachment);
            }

            message.setAttachmentList(meetingAttachmentMapper.toDto(savedAttachmentList));

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
            replyMessage.setMessage("Meeting Mapping is successfully saved");
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
}
