package creatip.oms.service;

import creatip.oms.service.dto.DocumentDTO;
import creatip.oms.service.dto.DocumentHeaderDTO;
import creatip.oms.service.message.DocumentInquiryMessage;
import creatip.oms.service.message.ReplyMessage;
import java.io.IOException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DocumentInquiryService {
    public Page<DocumentHeaderDTO> findAllDocumentHeaderByMetaData(DocumentInquiryMessage dto, Pageable pageable);

    public Page<DocumentHeaderDTO> searchDocumentHeaderByMetaData(DocumentInquiryMessage dto, Pageable pageable);

    public Page<DocumentHeaderDTO> searchDocumentHeaderInTrashBin(DocumentInquiryMessage dto, Pageable pageable);

    public Page<DocumentHeaderDTO> searchDocumentHeaderForServiceQueue(DocumentInquiryMessage dto, Pageable pageable);

    public DocumentHeaderDTO findAllDocumentsByHeaderId(Long id);

    public DocumentHeaderDTO findDeletedDocumentsByHeaderId(Long id);

    DocumentDTO getDocumentById(Long id);

    public ReplyMessage<ByteArrayResource> downloadFileFromFTPServer(String filePath) throws IOException, Exception;

    public ReplyMessage<ByteArrayResource> downloadPreviewFile(String filePath) throws IOException, Exception;
}
