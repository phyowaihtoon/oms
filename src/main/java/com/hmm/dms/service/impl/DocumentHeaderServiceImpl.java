package com.hmm.dms.service.impl;

import com.hmm.dms.domain.Document;
import com.hmm.dms.domain.DocumentHeader;
import com.hmm.dms.repository.DocumentHeaderRepository;
import com.hmm.dms.repository.DocumentRepository;
import com.hmm.dms.service.DocumentHeaderService;
import com.hmm.dms.service.dto.DocumentDTO;
import com.hmm.dms.service.dto.DocumentHeaderDTO;
import com.hmm.dms.service.mapper.DocumentHeaderMapper;
import com.hmm.dms.service.mapper.DocumentMapper;
import com.hmm.dms.service.message.DocumentInquiryMessage;
import com.hmm.dms.service.message.ReplyMessage;
import com.hmm.dms.util.FTPSessionFactory;
import com.hmm.dms.util.ResponseCode;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.ftp.session.FtpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link Document}.
 */
@Service
@Transactional
public class DocumentHeaderServiceImpl implements DocumentHeaderService {

    private final Logger log = LoggerFactory.getLogger(DocumentHeaderServiceImpl.class);
    private final FTPSessionFactory ftpSessionFactory;
    private final DocumentHeaderRepository documentHeaderRepository;
    private final DocumentHeaderMapper documentHeaderMapper;
    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    private ReplyMessage<DocumentHeaderDTO> replyMessage;
    private ReplyMessage<DocumentInquiryMessage> replyMessage_BM;

    public DocumentHeaderServiceImpl(
        DocumentHeaderRepository documentHeaderRepository,
        DocumentHeaderMapper documentHeaderMapper,
        DocumentRepository documentRepository,
        DocumentMapper documentMapper,
        FTPSessionFactory ftpSessionFactory
    ) {
        this.documentHeaderRepository = documentHeaderRepository;
        this.documentHeaderMapper = documentHeaderMapper;
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
        this.ftpSessionFactory = ftpSessionFactory;
        this.replyMessage = new ReplyMessage<DocumentHeaderDTO>();
        this.replyMessage_BM = new ReplyMessage<DocumentInquiryMessage>();
    }

    @Override
    public ReplyMessage<DocumentHeaderDTO> saveAndUploadDocuments(List<MultipartFile> multipartFiles, DocumentHeaderDTO documentHeaderDTO) {
        if (multipartFiles != null && multipartFiles.size() > 0) {
            if (!uploadFiles(multipartFiles)) {
                return replyMessage;
            }
        }

        if (documentHeaderDTO != null && documentHeaderDTO.getDocList() != null && documentHeaderDTO.getDocList().size() == 0) {
            replyMessage.setCode(ResponseCode.ERROR_E00);
            replyMessage.setMessage("There is no attached documents.");
            return replyMessage;
        }

        try {
            log.debug("Saving Document Header: {}", documentHeaderDTO);
            DocumentHeader documentHeader = documentHeaderMapper.toEntity(documentHeaderDTO);
            List<Document> documentList = documentHeaderDTO
                .getDocList()
                .stream()
                .map(documentMapper::toEntity)
                .collect(Collectors.toList());

            documentHeader = documentHeaderRepository.save(documentHeader);

            for (Document document : documentList) {
                document.setHeaderId(documentHeader.getId());
            }

            log.debug("Deleting Document Details by header ID before saving: {}");
            documentRepository.deleteByHeaderId(documentHeader.getId());

            log.debug("Saving Document Details");
            documentList = documentRepository.saveAll(documentList);

            List<DocumentDTO> documentDTOList = documentMapper.toDto(documentList);
            DocumentHeaderDTO docHeaderDTO = documentHeaderMapper.toDto(documentHeader);
            docHeaderDTO.setDocList(documentDTOList);

            replyMessage.setCode(ResponseCode.SUCCESS);
            replyMessage.setMessage("Document Mapping is successfully saved");
            replyMessage.setData(docHeaderDTO);
        } catch (Exception ex) {
            ex.printStackTrace();
            replyMessage.setCode(ResponseCode.ERROR_E01);
            replyMessage.setMessage(ex.getMessage());
        }

        return replyMessage;
    }

    @Override
    public Optional<DocumentHeaderDTO> partialUpdate(DocumentHeaderDTO documentHeaderDTO) {
        log.debug("Request to partially update Document : {}", documentHeaderDTO);

        return documentHeaderRepository
            .findById(documentHeaderDTO.getId())
            .map(
                existingDocument -> {
                    documentHeaderMapper.partialUpdate(existingDocument, documentHeaderDTO);
                    return existingDocument;
                }
            )
            .map(documentHeaderRepository::save)
            .map(documentHeaderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentHeaderDTO> findAll() {
        log.debug("Request to get all Documents");
        return documentHeaderRepository
            .findAll()
            .stream()
            .map(documentHeaderMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentHeaderDTO> findOne(Long id) {
        log.debug("Request to get Document : {}", id);
        return documentHeaderRepository.findById(id).map(documentHeaderMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Document : {}", id);
        documentHeaderRepository.deleteById(id);
    }

    private boolean uploadFiles(List<MultipartFile> multipartFiles) {
        List<String> uploadedFileList = new ArrayList<>();
        try {
            FtpSession ftpSession = this.ftpSessionFactory.getSession();
            System.out.println("Connected successfully to FTP Server");

            for (MultipartFile file : multipartFiles) {
                String[] filenameNdir = StringUtils.cleanPath(file.getOriginalFilename()).split("@");

                String filename = filenameNdir[0];
                String[] fullDirectory = filenameNdir[1].split("//");
                String directory = "";

                for (int i = 0; i < fullDirectory.length; i++) {
                    directory += "/" + fullDirectory[i];

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
                            return false;
                        }
                    }
                }
                String firstRemoteFile = directory + "/" + filename;

                InputStream inputStream = file.getInputStream();
                System.out.println("Start uploading file: [" + firstRemoteFile + "]");

                try {
                    ftpSession.write(inputStream, firstRemoteFile);
                    inputStream.close();
                    System.out.println("Uploaded successfully: [" + firstRemoteFile + "]");
                    uploadedFileList.add(firstRemoteFile);
                } catch (IOException ex) {
                    System.out.println("Failed to upload file : [" + firstRemoteFile + "]");
                    ex.printStackTrace();
                    replyMessage.setCode(ResponseCode.ERROR_E01);
                    replyMessage.setMessage("Failed to upload file :" + filename + " " + ex.getMessage());
                    //Removing previous uploaded files from FTP Server if failed to upload one file
                    if (uploadedFileList != null && uploadedFileList.size() > 0) removePreviousFiles(uploadedFileList, ftpSession);
                    return false;
                }
            }
        } catch (IllegalStateException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
            replyMessage.setCode(ResponseCode.ERROR_E01);
            replyMessage.setMessage("Cannot connect to FTP Server. [" + ex.getMessage() + "]");
            return false;
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
            replyMessage.setCode(ResponseCode.ERROR_E01);
            replyMessage.setMessage(ex.getMessage());
            return false;
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
            replyMessage.setCode(ResponseCode.ERROR_E01);
            replyMessage.setMessage(ex.getMessage());
            return false;
        }

        return true;
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

    @Override
    public ReplyMessage<DocumentInquiryMessage> partialUpdate(DocumentInquiryMessage approvalInfo, Long id) {
        try {
            if (id != null) {
                if (approvalInfo.getStatus() == 4) {
                    documentHeaderRepository.updateAmmendById(approvalInfo.getStatus(), approvalInfo.getReason(), id);
                    replyMessage_BM.setMessage("Documnet is successfully sent to amend.");
                } else if (approvalInfo.getStatus() == 6) {
                    documentHeaderRepository.updateRejectById(
                        approvalInfo.getStatus(),
                        approvalInfo.getReason(),
                        approvalInfo.getApprovedBy(),
                        Instant.now(),
                        id
                    );
                    replyMessage_BM.setMessage("Documnet is successfully rejected.");
                } else {
                    documentHeaderRepository.updateStatusById(approvalInfo.getStatus(), approvalInfo.getApprovedBy(), Instant.now(), id);
                    replyMessage_BM.setMessage("Documnet is successfully aproved");
                }
                replyMessage_BM.setCode(ResponseCode.SUCCESS);
            }
        } catch (IllegalStateException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
            replyMessage.setCode(ResponseCode.ERROR_E01);
            replyMessage.setMessage("Cannot connect to FTP Server. [" + ex.getMessage() + "]");
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
            replyMessage.setCode(ResponseCode.ERROR_E01);
            replyMessage.setMessage(ex.getMessage());
        }

        return replyMessage_BM;
    }
}
