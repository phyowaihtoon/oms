package creatip.oms.repository;

import creatip.oms.domain.Department;
import creatip.oms.domain.DocumentDelivery;
import creatip.oms.domain.MeetingDelivery;
import creatip.oms.repository.custom.CustomMeetingDeliveryRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Department entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MeetingDeliveryRepository extends JpaRepository<MeetingDelivery, Long>, CustomMeetingDeliveryRepository {
    @Query(value = "SELECT md FROM MeetingDelivery md WHERE md.delFlag='N' and md.deliveryStatus=1 " + "and md.meetingStatus in (1,3) ")
    List<MeetingDelivery> findScheduledMeetingList();

    @Query(
        value = "select DISTINCT md from MeetingDelivery md, MeetingReceiver mr " +
        "where md.id=mr.header.id and md.delFlag='N' and md.deliveryStatus=1 " +
        "and mr.receiver.id=?1 and date(md.sentDate) = str_to_date(?2,'%d-%m-%Y') " +
        "and mr.delFlag='N'"
    )
    Page<MeetingDelivery> findReceivedMeetingList(Long receiverId, String sentDate, Pageable pageable);

    @Query(
        value = "select md from MeetingDelivery md " +
        "where md.delFlag='N' and md.deliveryStatus=1 " +
        "and md.sender.id=?1 and date(md.sentDate) = str_to_date(?2,'%d-%m-%Y') "
    )
    Page<MeetingDelivery> findSentMeetingList(Long senderId, String sentDate, Pageable pageable);

    @Query(
        value = "select md from MeetingDelivery md " +
        "where md.delFlag='N' and md.deliveryStatus=1 and md.sender.id=?1 " +
        "and date(md.sentDate) >= str_to_date(?2,'%d-%m-%Y') and date(md.sentDate) <= str_to_date(?3,'%d-%m-%Y')"
    )
    Page<MeetingDelivery> findSentMeetingList(Long senderId, String dateFrom, String dateTo, Pageable pageable);

    @Query(value = "select md from MeetingDelivery md " + "where md.delFlag='N' and md.deliveryStatus=0 and md.sender.id=?1 ")
    Page<MeetingDelivery> findMeetingDraftList(Long senderId, Pageable pageable);

    Long countBySenderAndDeliveryStatusAndDelFlag(Department sender, short deliveryStatus, String delFlag);
}
