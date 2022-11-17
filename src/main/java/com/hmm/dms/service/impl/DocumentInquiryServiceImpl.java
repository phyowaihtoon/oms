package com.hmm.dms.service.impl;

import com.hmm.dms.domain.Document;
import com.hmm.dms.domain.DocumentHeader;
import com.hmm.dms.repository.DocumentHeaderRepository;
import com.hmm.dms.repository.DocumentRepository;
import com.hmm.dms.service.DocumentInquiryService;
import com.hmm.dms.service.dto.DocumentDTO;
import com.hmm.dms.service.dto.DocumentHeaderDTO;
import com.hmm.dms.service.mapper.DocumentHeaderMapper;
import com.hmm.dms.service.mapper.DocumentMapper;
import com.hmm.dms.service.message.DocumentInquiryMessage;
import com.hmm.dms.service.message.ReplyMessage;
import com.hmm.dms.util.FTPSessionFactory;
import com.hmm.dms.util.ResponseCode;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.ftp.session.FtpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DocumentInquiryServiceImpl implements DocumentInquiryService {

    private final Logger log = LoggerFactory.getLogger(DocumentInquiryServiceImpl.class);

    private final DocumentHeaderRepository documentHeaderRepository;
    private final DocumentRepository documentRepository;

    private final DocumentHeaderMapper documentHeaderMapper;
    private final DocumentMapper documentMapper;

    @Autowired
    private FTPSessionFactory ftpSessionFactory;

    public DocumentInquiryServiceImpl(
        DocumentHeaderRepository documentHeaderRepository,
        DocumentRepository documentRepository,
        DocumentHeaderMapper documentHeaderMapper,
        DocumentMapper documentMapper
    ) {
        this.documentHeaderRepository = documentHeaderRepository;
        this.documentRepository = documentRepository;
        this.documentHeaderMapper = documentHeaderMapper;
        this.documentMapper = documentMapper;
    }

    @SuppressWarnings("unused")
    @Override
    public Page<DocumentHeaderDTO> searchDocumentHeaderByMetaData(DocumentInquiryMessage dto, Pageable pageable) {
        String repURL = dto.getRepositoryURL();
        String fValues = dto.getFieldValues();
        if (repURL == null || repURL.equals("null") || repURL.isEmpty()) repURL = ""; else repURL = repURL.trim();
        if (fValues == null || fValues.equals("null") || fValues.isEmpty()) fValues = ""; else fValues = fValues.trim();

        if (dto.getMetaDataHeaderId() == null) {
            Page<DocumentHeader> pageWithEntity = this.documentHeaderRepository.findByStatus(2, fValues, repURL, pageable);
            return pageWithEntity.map(documentHeaderMapper::toDto);
        } else if (dto.getCreatedDate() != null && dto.getCreatedDate().trim().length() > 0) {
            String createdDate = dto.getCreatedDate();
            Page<DocumentHeader> pageWithEntity =
                this.documentHeaderRepository.findAllByDate(dto.getMetaDataHeaderId(), fValues, createdDate, repURL, pageable);
            return pageWithEntity.map(documentHeaderMapper::toDto);
        }

        Page<DocumentHeader> pageWithEntity = this.documentHeaderRepository.findAll(dto.getMetaDataHeaderId(), fValues, repURL, pageable);

        return pageWithEntity.map(documentHeaderMapper::toDto);
    }

    @Override
    public DocumentHeaderDTO findAllDocumentsByHeaderId(Long id) {
        DocumentHeader docHeaderEntity = this.documentHeaderRepository.findDocumentHeaderById(id);
        DocumentHeaderDTO docHeaderDTO = this.documentHeaderMapper.toDto(docHeaderEntity);
        List<Document> docList = this.documentRepository.findAllByHeaderId(docHeaderDTO.getId());
        List<DocumentDTO> docDTOList = this.documentMapper.toDto(docList);
        docHeaderDTO.setDocList(docDTOList);
        return docHeaderDTO;
    }

    @Override
    public DocumentDTO getDocumentById(Long id) {
        Optional<Document> document = this.documentRepository.findById(id);
        DocumentDTO docDTO = this.documentMapper.toDto(document.get());
        return docDTO;
    }

    @Override
    public ReplyMessage<ByteArrayResource> downloadFileFromFTPServer(String filePath) throws IOException, Exception {
        ReplyMessage<ByteArrayResource> replyMessage = new ReplyMessage<ByteArrayResource>();
        FtpSession ftpSession = this.ftpSessionFactory.getSession();
        System.out.println("Connected successfully to FTP Server");
        System.out.println("Start downloading file: [" + filePath + "]");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ftpSession.read(filePath, out);
        ByteArrayResource resource = new ByteArrayResource(out.toByteArray());
        replyMessage.setCode(ResponseCode.SUCCESS);
        replyMessage.setData(resource);
        ftpSession.close();

        System.out.println("Downloaded Successfully: [" + filePath + "]");

        return replyMessage;
    }
}
