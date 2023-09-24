package creatip.oms.repository;

import creatip.oms.domain.MeetingDelivery;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Department entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MeetingDeliveryRepository extends JpaRepository<MeetingDelivery, Long> {
    @Query(
        value = "SELECT md FROM MeetingDelivery md " + "WHERE md.delFlag='N' and md.deliveryStatus=1 " + "and md.meetingStatus in (1,3) "
    )
    List<MeetingDelivery> findScheduledMeetingList();
}
