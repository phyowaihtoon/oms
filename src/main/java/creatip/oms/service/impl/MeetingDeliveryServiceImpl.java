package creatip.oms.service.impl;

import creatip.oms.domain.MeetingAttachment;
import creatip.oms.domain.MeetingDelivery;
import creatip.oms.domain.MeetingReceiver;
import creatip.oms.enumeration.CommonEnum.DeliveryStatus;
import creatip.oms.enumeration.CommonEnum.MeetingStatus;
import creatip.oms.repository.MeetingAttachmentRepository;
import creatip.oms.repository.MeetingDeliveryRepository;
import creatip.oms.repository.MeetingReceiverRepository;
import creatip.oms.service.MeetingDeliveryService;
import creatip.oms.service.dto.MeetingAttachmentDTO;
import creatip.oms.service.dto.MeetingDeliveryDTO;
import creatip.oms.service.dto.MeetingReceiverDTO;
import creatip.oms.service.mapper.MeetingAttachmentMapper;
import creatip.oms.service.mapper.MeetingDeliveryMapper;
import creatip.oms.service.mapper.MeetingReceiverMapper;
import creatip.oms.service.message.MeetingMessage;
import creatip.oms.service.message.ReplyMessage;
import creatip.oms.service.message.SearchCriteriaMessage;
import creatip.oms.service.message.UploadFailedException;
import creatip.oms.util.FTPSessionFactory;
import creatip.oms.util.ResponseCode;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.ftp.session.FtpSession;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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

    private final FTPSessionFactory ftpSessionFactory;

    public MeetingDeliveryServiceImpl(
        MeetingDeliveryRepository meetingDeliveryRepository,
        MeetingAttachmentRepository meetingAttachmentRepository,
        MeetingReceiverRepository meetingReceiverRepository,
        MeetingDeliveryMapper meetingDeliveryMapper,
        MeetingAttachmentMapper meetingAttachmentMapper,
        MeetingReceiverMapper meetingReceiverMapper,
        FTPSessionFactory ftpSessionFactory
    ) {
        this.meetingDeliveryRepository = meetingDeliveryRepository;
        this.meetingAttachmentRepository = meetingAttachmentRepository;
        this.meetingReceiverRepository = meetingReceiverRepository;
        this.meetingDeliveryMapper = meetingDeliveryMapper;
        this.meetingAttachmentMapper = meetingAttachmentMapper;
        this.meetingReceiverMapper = meetingReceiverMapper;
        this.replyMessage = new ReplyMessage<MeetingMessage>();
        this.ftpSessionFactory = ftpSessionFactory;
    }

    @Scheduled(fixedRate = 60000)
    public void updateMeetingSchedule() {
        List<MeetingDelivery> list = meetingDeliveryRepository.findScheduledMeetingList();
        if (list != null && list.size() > 0) {
            for (MeetingDelivery data : list) {
                Instant meetingEndTime = data.getEndDate();
                if (meetingEndTime != null) {
                    Instant currentDateTime = Instant.now();
                    int result = currentDateTime.compareTo(meetingEndTime);
                    if (result >= 0) {
                        log.debug("Meeting Schedule for {} is finished", data.getDescription());
                        data.setMeetingStatus(MeetingStatus.FINISH.value);
                        meetingDeliveryRepository.save(data);
                    }
                }
            }
        }
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
            if (delivery.getDeliveryStatus() == DeliveryStatus.SENT.value && delivery.getSentDate() == null) delivery.setSentDate(
                Instant.now()
            );

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

            List<MeetingAttachmentDTO> savedAttachmentList = new ArrayList<MeetingAttachmentDTO>();

            for (MeetingAttachment attachment : attachmentList) {
                if (attachment.getId() != null && attachment.getId() > 0) {
                    attachment.setHeader(delivery);
                    attachment = meetingAttachmentRepository.save(attachment);
                    savedAttachmentList.add(meetingAttachmentMapper.toDto(attachment));
                }
            }

            // Uploading attachment files
            if (attachedFiles != null && attachedFiles.size() > 0) {
                List<MeetingAttachment> uploadedAttachmentList = saveAndUploadFiles(attachedFiles, delivery);
                if (attachedFiles.size() != uploadedAttachmentList.size()) {
                    throw new UploadFailedException("Upload failed", replyMessage.getCode(), replyMessage.getMessage());
                }
                savedAttachmentList.addAll(meetingAttachmentMapper.toDto(uploadedAttachmentList));
            }

            message.setAttachmentList(savedAttachmentList);

            replyMessage.setCode(ResponseCode.SUCCESS);
            replyMessage.setMessage(
                delivery.getDeliveryStatus() == DeliveryStatus.SENT.value
                    ? "Meeting Invitation has been sent successfully!"
                    : "Meeting Invitation has been saved as draft!"
            );
            replyMessage.setData(message);
        } catch (UploadFailedException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            replyMessage.setCode(ResponseCode.ERROR_E01);
            replyMessage.setMessage(ex.getMessage());
        }

        return replyMessage;
    }

    @Override
    public Optional<MeetingMessage> findOne(Long id) {
        MeetingMessage meetingMessage = null;
        Optional<MeetingDelivery> headerOptional = meetingDeliveryRepository.findById(id);
        if (headerOptional.isPresent()) {
            meetingMessage = new MeetingMessage();
            MeetingDeliveryDTO deliveryDTO = meetingDeliveryMapper.toDto(headerOptional.get());
            List<MeetingReceiver> recList = meetingReceiverRepository.findByHeaderId(id);
            List<MeetingReceiverDTO> recListDTO = meetingReceiverMapper.toDto(recList);
            List<MeetingAttachment> attList = meetingAttachmentRepository.findByHeaderId(id);
            List<MeetingAttachmentDTO> attListDTO = meetingAttachmentMapper.toDto(attList);
            meetingMessage.setMeetingDelivery(deliveryDTO);
            meetingMessage.setAttachmentList(attListDTO);
            meetingMessage.setReceiverList(recListDTO);
            return Optional.of(meetingMessage);
        }
        return Optional.empty();
    }

    @Override
    public Page<MeetingDeliveryDTO> getReceivedMeetingList(SearchCriteriaMessage criteria, Pageable pageable) {
        return meetingDeliveryRepository.findAll(pageable).map(meetingDeliveryMapper::toDto);
    }

    @Override
    public Page<MeetingDeliveryDTO> getInvitedMeetingList(SearchCriteriaMessage criteria, Pageable pageable) {
        return meetingDeliveryRepository.findAll(pageable).map(meetingDeliveryMapper::toDto);
    }

    private List<MeetingAttachment> saveAndUploadFiles(List<MultipartFile> multipartFiles, MeetingDelivery header) {
        List<String> uploadedFileList = new ArrayList<>();
        List<MeetingAttachment> uploadedList = new ArrayList<MeetingAttachment>();
        try {
            FtpSession ftpSession = this.ftpSessionFactory.getSession();
            System.out.println("Connected successfully to FTP Server");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String dateInString = formatter.format(Instant.now());
            String deliveryID = "ID" + header.getId();
            String[] fullDirectory = new String[] { "meeting", dateInString, deliveryID };

            for (MultipartFile file : multipartFiles) {
                String[] docDetailInfo = StringUtils.cleanPath(file.getOriginalFilename()).split("@");
                System.out.println("Document Information :" + file.getOriginalFilename() + ", Length :" + docDetailInfo.length);
                String orgFileName = docDetailInfo[0];

                String directory = "";
                String filePath = "";

                for (int i = 0; i < fullDirectory.length; i++) {
                    directory += "/" + fullDirectory[i];
                    filePath += "//" + fullDirectory[i];

                    boolean isPathExists = ftpSession.exists(directory);

                    if (!isPathExists) {
                        boolean isCreated = ftpSession.mkdir(directory);
                        if (!isCreated) {
                            String forbiddenCharacters = "\\/:*?\"<>|";
                            replyMessage.setCode(ResponseCode.ERROR_E00);
                            replyMessage.setMessage(
                                "Failed to create directory in FTP Server. Repository URL cannot include these characters : " +
                                forbiddenCharacters
                            );
                            return uploadedList;
                        }
                    }
                }

                String fullRemoteFilePath = directory + "/" + orgFileName + "";
                InputStream inputStream = file.getInputStream();
                System.out.println("Start uploading file: [" + fullRemoteFilePath + "]");

                try {
                    ftpSession.write(inputStream, fullRemoteFilePath);
                    inputStream.close();
                    System.out.println("Uploaded successfully: [" + fullRemoteFilePath + "]");
                    uploadedFileList.add(fullRemoteFilePath);

                    MeetingAttachment meetingAttachment = new MeetingAttachment();
                    meetingAttachment.setHeader(header);
                    meetingAttachment.setFilePath(filePath);
                    meetingAttachment.setFileName(orgFileName);
                    meetingAttachment.setDelFlag("N");
                    meetingAttachment = meetingAttachmentRepository.save(meetingAttachment);
                    uploadedList.add(meetingAttachment);
                } catch (IOException ex) {
                    System.out.println("Failed to upload file : [" + fullRemoteFilePath + "]");
                    ex.printStackTrace();
                    replyMessage.setCode(ResponseCode.ERROR_E01);
                    replyMessage.setMessage("Failed to upload file :" + orgFileName + " " + ex.getMessage());
                    // Removing previous uploaded files from FTP Server if failed to upload one file
                    if (uploadedFileList != null && uploadedFileList.size() > 0) removePreviousFiles(uploadedFileList, ftpSession);
                    return null;
                }
            }
        } catch (IllegalStateException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
            replyMessage.setCode(ResponseCode.ERROR_E01);
            replyMessage.setMessage("Cannot connect to FTP Server. [" + ex.getMessage() + "]");
            return uploadedList;
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
            replyMessage.setCode(ResponseCode.ERROR_E01);
            replyMessage.setMessage(ex.getMessage());
            return uploadedList;
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
            replyMessage.setCode(ResponseCode.ERROR_E01);
            replyMessage.setMessage(ex.getMessage());
            return uploadedList;
        }

        return uploadedList;
    }

    private void removePreviousFiles(List<String> uploadedFileList, FtpSession ftpSession) {
        System.out.println("Removing previous uploaded files if failed to upload one file");
        for (String filePath : uploadedFileList) {
            try {
                ftpSession.remove(filePath);
                System.out.println("Removed successfully : " + filePath);
            } catch (Exception ex) {
                System.out.println("Failed to remove : " + filePath);
                ex.printStackTrace();
            }
        }
    }
}
