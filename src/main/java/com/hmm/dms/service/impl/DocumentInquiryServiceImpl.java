package com.hmm.dms.service.impl;

import com.hmm.dms.domain.Document;
import com.hmm.dms.domain.DocumentHeader;
import com.hmm.dms.repository.DocumentHeaderRepository;
import com.hmm.dms.repository.DocumentRepository;
import com.hmm.dms.service.DocumentInquiryService;
import com.hmm.dms.service.dto.DocumentDTO;
import com.hmm.dms.service.dto.DocumentHeaderDTO;
import com.hmm.dms.service.dto.ReplyMessage;
import com.hmm.dms.service.mapper.DocumentHeaderMapper;
import com.hmm.dms.service.mapper.DocumentMapper;
import com.hmm.dms.util.FTPSessionFactory;
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

    @Override
    public Page<DocumentHeaderDTO> searchDocumentHeaderByMetaData(DocumentHeaderDTO dto, Pageable pageable) {
        String repURL = dto.getRepositoryURL();
        String fValues = dto.getFieldValues();
        if (repURL == null || repURL.equals("null") || repURL.isEmpty()) repURL = ""; else repURL = repURL.trim();
        if (fValues == null || fValues.equals("null") || fValues.isEmpty()) fValues = ""; else fValues = fValues.trim();
        Page<DocumentHeader> pageWithEntity = this.documentHeaderRepository.findAll(dto.getMetaDataHeaderId(), repURL, fValues, pageable);
        return pageWithEntity.map(documentHeaderMapper::toDto);
    }

    @Override
    public DocumentHeaderDTO findAllDocumentsByHeaderId(Long id) {
        Optional<DocumentHeader> docHeader = this.documentHeaderRepository.findById(id);
        DocumentHeaderDTO docHeaderDTO = this.documentHeaderMapper.toDto(docHeader.get());
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
    public ReplyMessage<ByteArrayResource> downloadFileFromFTPServer(String filePath) {
        ReplyMessage<ByteArrayResource> replyMessage = new ReplyMessage<ByteArrayResource>();
        try {
            FtpSession ftpSession = this.ftpSessionFactory.getSession();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ftpSession.read(filePath, out);
            ByteArrayResource byteResource = new ByteArrayResource(out.toByteArray());
            replyMessage.setCode("000");
            replyMessage.setData(byteResource);
            ftpSession.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return replyMessage;
    }
}
