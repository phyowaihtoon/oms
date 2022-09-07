package com.hmm.dms.service;

import com.hmm.dms.service.dto.DocumentDTO;
import com.hmm.dms.service.dto.DocumentHeaderDTO;
import com.hmm.dms.service.dto.ReplyMessage;
import java.io.IOException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DocumentInquiryService {
    public Page<DocumentHeaderDTO> searchDocumentHeaderByMetaData(DocumentHeaderDTO dto, Pageable pageable);

    public DocumentHeaderDTO findAllDocumentsByHeaderId(Long id);

    DocumentDTO getDocumentById(Long id);

    public ReplyMessage<ByteArrayResource> downloadFileFromFTPServer(String filePath) throws IOException, Exception;
}
