package creatip.oms.repository;

import creatip.oms.domain.DocumentHeader;
import creatip.oms.service.message.DocumentInquiryMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomDocumentInquiryRepository {
    Page<DocumentHeader> findDocumentHeaderByMetaData(DocumentInquiryMessage dto, Pageable pageable);
}
