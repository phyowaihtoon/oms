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
import com.hmm.dms.service.message.BaseMessage;
import com.hmm.dms.service.message.DocumentInquiryMessage;
import com.hmm.dms.service.message.ReplyMessage;
import com.hmm.dms.service.message.UploadFailedException;
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
    @Transactional(rollbackFor = UploadFailedException.class)
    public ReplyMessage<DocumentHeaderDTO> saveAndUploadDocuments(List<MultipartFile> multipartFiles, DocumentHeaderDTO documentHeaderDTO)
        throws UploadFailedException {
        if (documentHeaderDTO != null && documentHeaderDTO.getDocList() != null && documentHeaderDTO.getDocList().size() == 0) {
            replyMessage.setCode(ResponseCode.ERROR_E00);
            replyMessage.setMessage("There is no attached document.");
            return replyMessage;
        }
        
        if(documentHeaderDTO.getId()==null) {
        	 if(checkDuplication(documentHeaderDTO)) {
             	replyMessage.setCode(ResponseCode.ERROR_E00);
                 replyMessage.setMessage("Record already existed.");
                 return replyMessage;
             }
        }
       
        
        try {
        	
            log.debug("Saving Document Header: {}", documentHeaderDTO);
            DocumentHeader documentHeader = documentHeaderMapper.toEntity(documentHeaderDTO);
            List<Document> documentList = documentHeaderDTO
                .getDocList()
                .stream()
                .map(documentMapper::toEntity)
                .collect(Collectors.toList());

            List<Document> doc_List = new ArrayList<Document>();

            documentHeader = documentHeaderRepository.save(documentHeader);

            for (Document document : documentList) {
                if (document.getId() != null) {
                    document.setHeaderId(documentHeader.getId());
                    document = documentRepository.save(document);
                    doc_List.add(document);
                }
            }

            log.debug("Saving Document Details");

            List<DocumentDTO> documentDTOList = documentMapper.toDto(doc_List);

            /*
             * New uploaded document information will be saved and uploaded
             * separately in this method
             */
            if (multipartFiles != null && multipartFiles.size() > 0) {
                if (!saveAndUploadFiles(multipartFiles, documentHeader.getId(), documentDTOList)) {
                    throw new UploadFailedException("Upload failed", replyMessage.getCode(), replyMessage.getMessage());
                }
            }

            DocumentHeaderDTO docHeaderDTO = documentHeaderMapper.toDto(documentHeader);
            docHeaderDTO.setDocList(documentDTOList);

            replyMessage.setCode(ResponseCode.SUCCESS);
            replyMessage.setMessage("Document Mapping is successfully saved");
            replyMessage.setData(docHeaderDTO);
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

    private int checkFileVersion(Long id, String directory, String filename) {
        int filecount = 0;
        List<Document> docList = documentRepository.findByHeaderIdAndFilePathAndFileName(id, directory, filename);
        filecount = docList.size();
        return filecount;
    }

    private String updateFileNameWithVersion(String filename, Long headerId, int versionNo) {
        int indexOfDot = filename.lastIndexOf(".");
        String fileNameWithVersion =
            filename.substring(0, indexOfDot) +
            "_HID" +
            Long.toString(headerId) +
            "_V" +
            Integer.toString(versionNo) +
            filename.substring(indexOfDot);
        return fileNameWithVersion;
    }

    private boolean saveAndUploadFiles(List<MultipartFile> multipartFiles, Long headerID, List<DocumentDTO> documentDTOList) {
        List<String> uploadedFileList = new ArrayList<>();
        try {
            FtpSession ftpSession = this.ftpSessionFactory.getSession();
            System.out.println("Connected successfully to FTP Server");

            for (MultipartFile file : multipartFiles) {
                String[] docDetailInfo = StringUtils.cleanPath(file.getOriginalFilename()).split("@");
                System.out.println("Document Information :" + file.getOriginalFilename() + ", Length :" + docDetailInfo.length);
                String orgFileName = docDetailInfo[0];
                String fileName = docDetailInfo[0];
                String orgFilePath = docDetailInfo[1];
                String[] fullDirectory = orgFilePath.split("//");
                String remark = "";
                if (docDetailInfo.length > 2) remark = docDetailInfo[2];
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

                int versionNo = checkFileVersion(headerID, orgFilePath, fileName) + 1;

                String fileNameWithVersion = updateFileNameWithVersion(fileName, headerID, versionNo);

                String fullRemoteFilePath = directory + "/" + fileNameWithVersion + "";

                InputStream inputStream = file.getInputStream();
                System.out.println("Start uploading file: [" + fullRemoteFilePath + "]");

                try {
                    ftpSession.write(inputStream, fullRemoteFilePath);
                    inputStream.close();
                    System.out.println("Uploaded successfully: [" + fullRemoteFilePath + "]");
                    uploadedFileList.add(fullRemoteFilePath);

                    Document documentDetail = new Document();
                    documentDetail.setHeaderId(headerID);
                    documentDetail.setFilePath(orgFilePath);
                    documentDetail.setFileName(orgFileName);
                    documentDetail.setFileNameVersion(fileNameWithVersion);
                    documentDetail.setVersion(versionNo);
                    documentDetail.setFileSize(file.getSize() / 1024); // Convert Bytes to Kilobytes
                    documentDetail.setRemark(remark);
                    documentDetail.setDelFlag("N");
                    documentDetail = documentRepository.save(documentDetail);

                    DocumentDTO docDTO = documentMapper.toDto(documentDetail);
                    documentDTOList.add(docDTO);
                } catch (IOException ex) {
                    System.out.println("Failed to upload file : [" + fullRemoteFilePath + "]");
                    ex.printStackTrace();
                    replyMessage.setCode(ResponseCode.ERROR_E01);
                    replyMessage.setMessage("Failed to upload file :" + fileNameWithVersion + " " + ex.getMessage());
                    // Removing previous uploaded files from FTP Server if failed to upload one file
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
                    replyMessage_BM.setMessage("Document is successfully sent to amend.");
                } else if (approvalInfo.getStatus() == 6) {
                    documentHeaderRepository.updateRejectById(
                        approvalInfo.getStatus(),
                        approvalInfo.getReason(),
                        approvalInfo.getApprovedBy(),
                        Instant.now(),
                        id
                    );
                    replyMessage_BM.setMessage("Document has been rejected.");
                } else if (approvalInfo.getStatus() == 5) {
                    documentHeaderRepository.updateStatusById(approvalInfo.getStatus(), approvalInfo.getApprovedBy(), Instant.now(), id);
                    replyMessage_BM.setMessage("Document is successfully approved.");
                } else if (approvalInfo.getStatus() == 2) {
                    documentHeaderRepository.updateStatusById(approvalInfo.getStatus(), approvalInfo.getApprovedBy(), Instant.now(), id);
                    replyMessage_BM.setMessage("Document is successfully sent to approve.");
                } else {
                    documentHeaderRepository.updateStatusById(approvalInfo.getStatus(), approvalInfo.getApprovedBy(), Instant.now(), id);
                    replyMessage_BM.setMessage("Document has been canceled.");
                }
                replyMessage_BM.setCode(ResponseCode.SUCCESS);
            } else {
                replyMessage_BM.setCode(ResponseCode.WARNING);
                replyMessage_BM.setMessage("Invalid Document.");
            }
        } catch (Exception ex) {
            System.out.println("Error while updating document status: " + ex.getMessage());
            ex.printStackTrace();
            replyMessage_BM.setCode(ResponseCode.ERROR_E01);
            replyMessage_BM.setMessage(ex.getMessage());
        }
        return replyMessage_BM;
    }

    @Override
    public BaseMessage restoreDocument(DocumentHeaderDTO documentHeaderDTO) {
        BaseMessage replyMessage = new BaseMessage();
        try {
            List<DocumentDTO> docList = documentHeaderDTO.getDocList();
            if (docList == null || docList.size() <= 0) {
                replyMessage.setCode(ResponseCode.ERROR_E00);
                replyMessage.setMessage("Please, select document to restore.");
                return replyMessage;
            }
            for (DocumentDTO docDetail : docList) {
                this.documentRepository.restoreDocument(docDetail.getId(), documentHeaderDTO.getId());
            }
            replyMessage.setCode(ResponseCode.SUCCESS);
            replyMessage.setMessage("Document has been restored.");
        } catch (Exception ex) {
            System.out.println("Error while restoring document :" + ex.getMessage());
            ex.printStackTrace();
            replyMessage.setCode(ResponseCode.ERROR_E01);
            replyMessage.setMessage("Document Restore failed : " + ex.getMessage());
        }
        return replyMessage;
    }
    
    public boolean checkDuplication(DocumentHeaderDTO documentHeaderDTO) {
    	boolean isDuplicate = false;
    	
    	long duplicateCount = this.documentHeaderRepository.checkDuplication(documentHeaderDTO.getMetaDataHeaderId(), 
    			documentHeaderDTO.getFieldNames(), documentHeaderDTO.getFieldValues(), documentHeaderDTO.getPriority());
    	
    	if (duplicateCount > 0)
    		isDuplicate = true;
    	
    	return isDuplicate;
    }
    
    
}
