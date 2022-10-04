package com.hmm.dms.service.impl;

import com.hmm.dms.domain.Document;
import com.hmm.dms.domain.DocumentHeader;
import com.hmm.dms.repository.DocumentHeaderRepository;
import com.hmm.dms.repository.DocumentRepository;
import com.hmm.dms.service.DocumentHeaderService;
import com.hmm.dms.service.dto.DocumentDTO;
import com.hmm.dms.service.dto.DocumentHeaderDTO;
import com.hmm.dms.service.dto.ReplyMessage;
import com.hmm.dms.service.mapper.DocumentHeaderMapper;
import com.hmm.dms.service.mapper.DocumentMapper;
import com.hmm.dms.util.FTPSessionFactory;
import com.hmm.dms.util.ResponseCode;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.net.ftp.FTPClient;
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
    private FTPClient ftpClient;
    private ReplyMessage<DocumentHeaderDTO> replyMessage;

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
    }

    @Override
    public ReplyMessage<DocumentHeaderDTO> saveAndUploadDocuments(List<MultipartFile> multipartFiles, DocumentHeaderDTO documentHeaderDTO) {
        if (multipartFiles != null && multipartFiles.size() > 0) {
            if (!uploadFiles(multipartFiles)) {
                return replyMessage;
            }
        }

        if (documentHeaderDTO != null && documentHeaderDTO.getDocList() != null && documentHeaderDTO.getDocList().size() == 0) {
            replyMessage.setCode(ResponseCode.WARNING);
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
            replyMessage.setCode(ResponseCode.ERROR);
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
            ftpClient = ftpSession.getClientInstance();

            for (MultipartFile file : multipartFiles) {
                String[] filenameNdir = StringUtils.cleanPath(file.getOriginalFilename()).split("@");

                String filename = filenameNdir[0];
                String[] fullDirectory = filenameNdir[1].split("//");
                String directory = "";

                for (int i = 0; i < fullDirectory.length; i++) {
                    directory += "/" + fullDirectory[i];

                    boolean isDirExists = checkDirectoryExists(directory);
                    if (!isDirExists) {
                        ftpClient.makeDirectory(directory);
                    }
                }
                String firstRemoteFile = directory + "/" + filename;

                InputStream inputStream = file.getInputStream();
                System.out.println("Start uploading file:" + firstRemoteFile);

                boolean isUploaded = ftpClient.storeFile(firstRemoteFile, inputStream);
                inputStream.close();
                if (isUploaded) {
                    System.out.println(firstRemoteFile + " is successfully uploaded.");
                    uploadedFileList.add(firstRemoteFile);
                }
                if (!isUploaded) {
                    System.out.println("Failed to upload file :" + firstRemoteFile);
                    replyMessage.setCode(ResponseCode.ERROR);
                    replyMessage.setMessage("Failed to upload file :" + filename);
                    if (uploadedFileList != null && uploadedFileList.size() > 0) {
                        //Removing previous uploaded files from FTP Server if failed to upload one file
                        removePreviousFiles(uploadedFileList);
                    }
                    return false;
                }
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
            replyMessage.setCode(ResponseCode.ERROR);
            replyMessage.setMessage(ex.getMessage());
            return false;
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
            replyMessage.setCode(ResponseCode.ERROR);
            replyMessage.setMessage(ex.getMessage());
            return false;
        } finally {
            try {
                if (ftpClient != null && ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return true;
    }

    private void removePreviousFiles(List<String> uploadedFileList) {
        for (String filePath : uploadedFileList) {
            System.out.println("Removing previous uploaded files if failed to upload one file");
            try {
                ftpClient.deleteFile(filePath);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean checkDirectoryExists(String dirPath) throws IOException {
        ftpClient.changeWorkingDirectory(dirPath);
        int returnCode = ftpClient.getReplyCode();
        if (returnCode == 550) {
            return false;
        }
        return true;
    }
}
