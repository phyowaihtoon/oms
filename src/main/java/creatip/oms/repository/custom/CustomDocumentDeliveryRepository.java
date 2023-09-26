package creatip.oms.repository.custom;

import creatip.oms.domain.DocumentDelivery;
import creatip.oms.service.message.SearchCriteriaMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomDocumentDeliveryRepository {
    Page<DocumentDelivery> findDocumentsReceived(SearchCriteriaMessage criteria, Pageable pageable);
}
