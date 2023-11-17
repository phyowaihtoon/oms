package creatip.oms.service.impl;

import creatip.oms.domain.DocumentAttachment;
import creatip.oms.domain.DocumentDelivery;
import creatip.oms.domain.DocumentReceiver;
import creatip.oms.enumeration.CommonEnum.DeliveryStatus;
import creatip.oms.enumeration.CommonEnum.RequestFrom;
import creatip.oms.enumeration.CommonEnum.ViewStatus;
import creatip.oms.repository.DocumentAttachmentRepository;
import creatip.oms.repository.DocumentDeliveryRepository;
import creatip.oms.repository.DocumentReceiverRepository;
import creatip.oms.service.DocumentDeliveryService;
import creatip.oms.service.dto.DocumentAttachmentDTO;
import creatip.oms.service.dto.DocumentDeliveryDTO;
import creatip.oms.service.dto.DocumentReceiverDTO;
import creatip.oms.service.mapper.DocumentAttachmentMapper;
import creatip.oms.service.mapper.DocumentDeliveryMapper;
import creatip.oms.service.mapper.DocumentReceiverMapper;
import creatip.oms.service.message.BaseMessage;
import creatip.oms.service.message.DeliveryMessage;
import creatip.oms.service.message.NotificationMessage;
import creatip.oms.service.message.ReplyMessage;
import creatip.oms.service.message.SearchCriteriaMessage;
import creatip.oms.service.message.UploadFailedException;
import creatip.oms.util.FTPSessionFactory;
import creatip.oms.util.ResponseCode;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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

    private final FTPSessionFactory ftpSessionFactory;

    public DocumentDeliveryServiceImpl(
        DocumentDeliveryRepository documentDeliveryRepository,
        DocumentAttachmentRepository documentAttachmentRepository,
        DocumentReceiverRepository documentReceiverRepository,
        DocumentDeliveryMapper documentDeliveryMapper,
        DocumentAttachmentMapper documentAttachmentMapper,
        DocumentReceiverMapper documentReceiverMapper,
        FTPSessionFactory ftpSessionFactory
    ) {
        this.documentDeliveryRepository = documentDeliveryRepository;
        this.documentAttachmentRepository = documentAttachmentRepository;
        this.documentReceiverRepository = documentReceiverRepository;
        this.documentDeliveryMapper = documentDeliveryMapper;
        this.documentAttachmentMapper = documentAttachmentMapper;
        this.documentReceiverMapper = documentReceiverMapper;
        this.replyMessage = new ReplyMessage<DeliveryMessage>();
        this.ftpSessionFactory = ftpSessionFactory;
    }

    @Override
    @Transactional(rollbackFor = UploadFailedException.class)
    public ReplyMessage<DeliveryMessage> save(DeliveryMessage message, List<MultipartFile> attachedFiles) throws UploadFailedException {
        if (message != null && message.getAttachmentList() != null && message.getAttachmentList().size() == 0) {
            replyMessage.setCode(ResponseCode.ERROR_E00);
            replyMessage.setMessage("Please, attach document");
            return replyMessage;
        }

        try {
            log.debug("Saving Document Delivery");

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

            documentReceiverRepository.deleteByHeaderId(delivery.getId());

            for (DocumentReceiver reciever : receiverList) {
                reciever.setHeader(delivery);
                reciever.setStatus(ViewStatus.UNREAD.value);
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

            List<DocumentAttachmentDTO> savedAttachmentList = new ArrayList<DocumentAttachmentDTO>();
            for (DocumentAttachment document : attachmentList) {
                if (document.getId() != null && document.getId() > 0) {
                    document.setHeader(delivery);
                    document = documentAttachmentRepository.save(document);
                    savedAttachmentList.add(documentAttachmentMapper.toDto(document));
                }
            }

            // Uploading attachment files
            if (attachedFiles != null && attachedFiles.size() > 0) {
                log.debug("Trying to upload {} files", attachedFiles.size());
                List<DocumentAttachment> uploadedAttachmentList = saveAndUploadFiles(attachedFiles, delivery);
                if (attachedFiles.size() != uploadedAttachmentList.size()) {
                    throw new UploadFailedException("Upload failed", replyMessage.getCode(), replyMessage.getMessage());
                }
                log.debug("{} out of {} files were uploaded successfully", uploadedAttachmentList.size(), attachedFiles.size());
                savedAttachmentList.addAll(documentAttachmentMapper.toDto(uploadedAttachmentList));
            }

            message.setAttachmentList(savedAttachmentList);

            replyMessage.setCode(ResponseCode.SUCCESS);
            replyMessage.setMessage(
                delivery.getDeliveryStatus() == DeliveryStatus.SENT.value
                    ? "Document has been delivered successfully!"
                    : "Document has been saved as draft!"
            );
            replyMessage.setData(message);
        } catch (UploadFailedException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        }

        return replyMessage;
    }

    @Override
    public Optional<DeliveryMessage> findOne(Long id) {
        DeliveryMessage deliveryMessage = null;
        Optional<DocumentDelivery> headerOptional = documentDeliveryRepository.findById(id);
        if (headerOptional.isPresent()) {
            deliveryMessage = new DeliveryMessage();
            DocumentDeliveryDTO deliveryDTO = documentDeliveryMapper.toDto(headerOptional.get());
            List<DocumentReceiver> recList = documentReceiverRepository.findByHeaderId(id);
            List<DocumentReceiverDTO> recListDTO = documentReceiverMapper.toDto(recList);
            List<DocumentAttachment> attList = documentAttachmentRepository.findByHeaderIdAndDelFlag(id, "N");
            List<DocumentAttachmentDTO> attListDTO = documentAttachmentMapper.toDto(attList);
            deliveryMessage.setDocumentDelivery(deliveryDTO);
            deliveryMessage.setAttachmentList(attListDTO);
            deliveryMessage.setReceiverList(recListDTO);
            return Optional.of(deliveryMessage);
        }
        return Optional.empty();
    }

    @Override
    public List<NotificationMessage> getNotification(Long departmentId) {
        List<NotificationMessage> notiList = new ArrayList<NotificationMessage>();
        List<DocumentReceiver> list = documentReceiverRepository.findUnReadMailByRecieverId(departmentId);
        for (DocumentReceiver receiver : list) {
            NotificationMessage notiMessage = new NotificationMessage();
            DocumentDeliveryDTO deliveryDTO = documentDeliveryMapper.toDto(receiver.getHeader());
            notiMessage.setId(deliveryDTO.getId());
            notiMessage.setReferenceNo(deliveryDTO.getReferenceNo());
            notiMessage.setSubject(deliveryDTO.getSubject());
            notiMessage.setSenderName(deliveryDTO.getSender().getDepartmentName());
            notiList.add(notiMessage);
        }
        return notiList;
    }

    private List<DocumentAttachment> saveAndUploadFiles(List<MultipartFile> multipartFiles, DocumentDelivery header) {
        List<String> uploadedFileList = new ArrayList<>();
        List<DocumentAttachment> uploadedList = new ArrayList<DocumentAttachment>();
        try {
            FtpSession ftpSession = this.ftpSessionFactory.getSession();
            log.debug("Connected successfully to FTP Server : {}", ftpSession.getHostPort());

            Instant currentInstant = Instant.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneId.systemDefault()); // Adjust to your desired time zone
            String dateInString = formatter.format(currentInstant);

            String deliveryID = "ID" + header.getId();
            String[] fullDirectory = new String[] { "delivery", dateInString, deliveryID };

            for (MultipartFile file : multipartFiles) {
                String[] docDetailInfo = StringUtils.cleanPath(file.getOriginalFilename()).split("@");
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
                log.debug("Start uploading file: {}", fullRemoteFilePath);

                try {
                    ftpSession.write(inputStream, fullRemoteFilePath);
                    inputStream.close();
                    uploadedFileList.add(fullRemoteFilePath);

                    DocumentAttachment documentAttachment = new DocumentAttachment();
                    documentAttachment.setHeader(header);
                    documentAttachment.setFilePath(filePath);
                    documentAttachment.setFileName(orgFileName);
                    documentAttachment.setDelFlag("N");
                    documentAttachment = documentAttachmentRepository.save(documentAttachment);
                    uploadedList.add(documentAttachment);
                    log.debug("Uploaded successfully: {}", fullRemoteFilePath);
                } catch (Exception ex) {
                    log.debug("Failed to upload file : [" + fullRemoteFilePath + "]");
                    log.error("Upload Failed :", ex);
                    replyMessage.setCode(ResponseCode.EXCEP_EX);
                    replyMessage.setMessage(String.format("Failed to upload file :%s [%s]", orgFileName, ex.getMessage()));
                    // Removing previous uploaded files from FTP Server if failed to upload one file
                    if (uploadedFileList != null && uploadedFileList.size() > 0) removePreviousFiles(uploadedFileList, ftpSession);
                    return uploadedList;
                }
            }
        } catch (Exception ex) {
            log.error("Exception: ", ex);
            replyMessage.setCode(ResponseCode.EXCEP_EX);
            replyMessage.setMessage(ex.getMessage());
            return uploadedList;
        }

        return uploadedList;
    }

    private void removePreviousFiles(List<String> uploadedFileList, FtpSession ftpSession) {
        log.debug("Removing previous uploaded files because of failure of uploading one file");
        for (String filePath : uploadedFileList) {
            try {
                ftpSession.remove(filePath);
                log.debug("Removed successfully : {}", filePath);
            } catch (Exception ex) {
                log.debug("Failed to remove : {}", filePath);
                log.error("Exception :", ex);
            }
        }
    }

    @Override
    public Page<DocumentDeliveryDTO> getReceivedDeliveryList(SearchCriteriaMessage criteria, Pageable pageable) {
        Page<DocumentDelivery> page = null;
        ZoneId zoneId = ZoneId.systemDefault();
        String zoneCode = zoneId.getId();
        if (criteria.getRequestFrom() == RequestFrom.DASHBOARD.value) {
            page =
                documentDeliveryRepository.findDocumentsReceived(
                    criteria.getReceiverId(),
                    criteria.getStatus(),
                    criteria.getDateOn(),
                    zoneCode,
                    pageable
                );
        } else {
            page = documentDeliveryRepository.findDocumentsReceived(criteria, pageable);
        }
        return page.map(documentDeliveryMapper::toDto);
    }

    @Override
    public Page<DocumentDeliveryDTO> getSentDeliveryList(SearchCriteriaMessage criteria, Pageable pageable) {
        Page<DocumentDelivery> page = null;
        ZoneId zoneId = ZoneId.systemDefault();
        String zoneCode = zoneId.getId();
        if (criteria.getRequestFrom() == RequestFrom.DASHBOARD.value) {
            page = documentDeliveryRepository.findDocumentsSent(criteria.getSenderId(), criteria.getDateOn(), zoneCode, pageable);
        } else {
            String subject = criteria.getSubject() == null ? "" : criteria.getSubject();
            String referenceNo = criteria.getReferenceNo() == null ? "" : criteria.getReferenceNo();
            page =
                documentDeliveryRepository.findDocumentsSent(
                    criteria.getSenderId(),
                    criteria.getDateFrom(),
                    criteria.getDateTo(),
                    subject,
                    referenceNo,
                    zoneCode,
                    pageable
                );
        }
        return page.map(documentDeliveryMapper::toDto);
    }

    @Override
    public Page<DocumentDeliveryDTO> getDeliveryDraftList(SearchCriteriaMessage criteria, Pageable pageable) {
        ZoneId zoneId = ZoneId.systemDefault();
        String zoneCode = zoneId.getId();

        String subject = criteria.getSubject() == null ? "" : criteria.getSubject();
        String referenceNo = criteria.getReferenceNo() == null ? "" : criteria.getReferenceNo();
        Page<DocumentDelivery> page = documentDeliveryRepository.findDeliveryDraftList(
            criteria.getSenderId(),
            criteria.getDateFrom(),
            criteria.getDateTo(),
            subject,
            referenceNo,
            zoneCode,
            pageable
        );
        return page.map(documentDeliveryMapper::toDto);
    }

    @Override
    public ReplyMessage<String> markAsRead(Long deliveryId, Long loginDepId) {
        ReplyMessage<String> replyMessage = new ReplyMessage<String>();

        try {
            Optional<DocumentDelivery> data = documentDeliveryRepository.findById(deliveryId);
            if (data.isPresent()) {
                DocumentDelivery delivery = data.get();
                if (loginDepId == delivery.getSender().getId()) {
                    delivery.setStatus(ViewStatus.READ.value);
                    documentDeliveryRepository.save(delivery);
                } else {
                    List<DocumentReceiver> list = documentReceiverRepository.findByHeaderIdAndReceiverId(deliveryId, loginDepId);
                    for (DocumentReceiver receiver : list) {
                        receiver.setStatus(ViewStatus.READ.value);
                        documentReceiverRepository.save(receiver);
                    }
                }

                replyMessage.setCode(ResponseCode.SUCCESS);
                replyMessage.setMessage("Letter has been marked as read");
            } else {
                replyMessage.setCode(ResponseCode.ERROR_E00);
                replyMessage.setMessage("Invalid document delivery : " + deliveryId);
            }
        } catch (Exception ex) {
            replyMessage.setCode(ResponseCode.EXCEP_EX);
            replyMessage.setMessage(ex.getMessage());
            log.error("Error while marking document as read :", ex);
        }

        return replyMessage;
    }

    @Override
    public ReplyMessage<String> markAsUnRead(Long deliveryId, Long loginDepId) {
        ReplyMessage<String> replyMessage = new ReplyMessage<String>();

        try {
            Optional<DocumentDelivery> data = documentDeliveryRepository.findById(deliveryId);
            if (data.isPresent()) {
                DocumentDelivery delivery = data.get();
                if (loginDepId == delivery.getSender().getId()) {
                    delivery.setStatus(ViewStatus.UNREAD.value);
                    documentDeliveryRepository.save(delivery);
                } else {
                    List<DocumentReceiver> list = documentReceiverRepository.findByHeaderIdAndReceiverId(deliveryId, loginDepId);
                    for (DocumentReceiver receiver : list) {
                        receiver.setStatus(ViewStatus.UNREAD.value);
                        documentReceiverRepository.save(receiver);
                    }
                }

                replyMessage.setCode(ResponseCode.SUCCESS);
                replyMessage.setMessage("Letter has been marked as unread");
            } else {
                replyMessage.setCode(ResponseCode.ERROR_E00);
                replyMessage.setMessage("Invalid document delivery : " + deliveryId);
            }
        } catch (Exception ex) {
            replyMessage.setCode(ResponseCode.EXCEP_EX);
            replyMessage.setMessage(ex.getMessage());
            log.error("Error while marking document as unread :", ex);
        }

        return replyMessage;
    }

    @Override
    public BaseMessage deleteAttachment(Long id) {
        BaseMessage replyMessage = new BaseMessage();
        try {
            documentAttachmentRepository.updateDelFlagById(id);
            replyMessage.setCode(ResponseCode.SUCCESS);
            replyMessage.setMessage("Document has been removed successfully");
        } catch (Exception ex) {
            log.error("Cannot remove document ", ex);
            replyMessage.setCode(ResponseCode.EXCEP_EX);
            replyMessage.setMessage("Cannot remove document");
        }

        return replyMessage;
    }
}
