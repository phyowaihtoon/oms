package creatip.oms.repository.custom;

import creatip.oms.domain.MeetingDelivery;
import creatip.oms.service.message.SearchCriteriaMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomMeetingDeliveryRepository {
    Page<MeetingDelivery> findReceivedMeetingList(SearchCriteriaMessage criteria, Pageable pageable);
}
