package creatip.oms.service;

import creatip.oms.service.dto.DocumentDeliveryDTO;
import creatip.oms.service.message.BaseMessage;
import creatip.oms.service.message.DeliveryMessage;
import creatip.oms.service.message.NotificationMessage;
import creatip.oms.service.message.ReplyMessage;
import creatip.oms.service.message.SearchCriteriaMessage;
import creatip.oms.service.message.UploadFailedException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentDeliveryService {
    ReplyMessage<DeliveryMessage> save(DeliveryMessage deliveryMessage, List<MultipartFile> attachedFiles) throws UploadFailedException;

    Page<DocumentDeliveryDTO> getReceivedDeliveryList(SearchCriteriaMessage criteria, Pageable pageable);

    Page<DocumentDeliveryDTO> getSentDeliveryList(SearchCriteriaMessage criteria, Pageable pageable);

    Page<DocumentDeliveryDTO> getDeliveryDraftList(SearchCriteriaMessage criteria, Pageable pageable);

    Optional<DeliveryMessage> findOne(Long id);

    List<NotificationMessage> getNotification(Long departmentId);

    ReplyMessage<String> markAsRead(Long deliveryId, Long loginDepId);

    ReplyMessage<String> markAsUnRead(Long deliveryId, Long loginDepId);

    BaseMessage deleteAttachment(Long id);
}
