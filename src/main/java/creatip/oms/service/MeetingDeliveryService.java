package creatip.oms.service;

import creatip.oms.service.dto.MeetingDeliveryDTO;
import creatip.oms.service.message.MeetingMessage;
import creatip.oms.service.message.ReplyMessage;
import creatip.oms.service.message.SearchCriteriaMessage;
import creatip.oms.service.message.UploadFailedException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface MeetingDeliveryService {
    ReplyMessage<MeetingMessage> save(MeetingMessage meetingMessage, List<MultipartFile> attachedFiles) throws UploadFailedException;

    Optional<MeetingMessage> findOne(Long id);

    Page<MeetingDeliveryDTO> getReceivedMeetingList(SearchCriteriaMessage criteria, Pageable pageable);

    Page<MeetingDeliveryDTO> getSentMeetingList(SearchCriteriaMessage criteria, Pageable pageable);

    Page<MeetingDeliveryDTO> getMeetingDraftList(SearchCriteriaMessage criteria, Pageable pageable);

    List<MeetingDeliveryDTO> getScheduledMeetingList();

    ReplyMessage<String> markAsRead(Long deliveryId, Long loginDepId);

    ReplyMessage<String> markAsUnRead(Long deliveryId, Long loginDepId);
}
