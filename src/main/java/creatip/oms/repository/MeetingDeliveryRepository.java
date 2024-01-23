package creatip.oms.repository;

import creatip.oms.domain.Department;
import creatip.oms.domain.DocumentDelivery;
import creatip.oms.domain.MeetingDelivery;
import creatip.oms.repository.custom.CustomMeetingDeliveryRepository;
import java.util.Collection;
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
        "and mr.receiver.id=?1 and DATE(CONVERT_TZ(md.sentDate, 'UTC', ?3)) = str_to_date(?2,'%d-%m-%Y') " +
        "and mr.delFlag='N'"
    )
    Page<MeetingDelivery> findReceivedMeetingList(Long receiverId, String sentDate, String zoneCode, Pageable pageable);

    @Query(
        value = "select md from MeetingDelivery md " +
        "where md.delFlag='N' and md.deliveryStatus=1 " +
        "and md.sender.id=?1 and DATE(CONVERT_TZ(md.sentDate, 'UTC', ?3)) = str_to_date(?2,'%d-%m-%Y') "
    )
    Page<MeetingDelivery> findSentMeetingList(Long senderId, String sentDate, String zoneCode, Pageable pageable);

    @Query(
        value = "select md from MeetingDelivery md " +
        "where md.delFlag='N' and md.deliveryStatus=1 and md.sender.id=?1 " +
        "and DATE(CONVERT_TZ(md.sentDate, 'UTC', ?6)) >= str_to_date(?2,'%d-%m-%Y') and DATE(CONVERT_TZ(md.sentDate, 'UTC', ?6)) <= str_to_date(?3,'%d-%m-%Y') " +
        "and md.subject LIKE CONCAT('%', ?4, '%') " +
        "and md.referenceNo LIKE CONCAT('%', ?5, '%') " +
        "and md.status IN ?7 "
    )
    Page<MeetingDelivery> findSentMeetingList(
        Long senderId,
        String dateFrom,
        String dateTo,
        String subject,
        String referenceNo,
        String zoneCode,
        Collection<Short> status,
        Pageable pageable
    );

    @Query(
        value = "select md from MeetingDelivery md " +
        "where md.delFlag='N' and md.deliveryStatus=1 and md.sender.id=?1 " +
        "and md.subject LIKE CONCAT('%', ?2, '%') " +
        "and md.referenceNo LIKE CONCAT('%', ?3, '%') " +
        "and md.status IN ?4 "
    )
    Page<MeetingDelivery> findSentMeetingList(
        Long senderId,
        String subject,
        String referenceNo,
        Collection<Short> status,
        Pageable pageable
    );

    @Query(
        value = "select md from MeetingDelivery md " +
        "where md.delFlag='N' and md.deliveryStatus=0 and md.sender.id=?1 " +
        "and DATE(CONVERT_TZ(md.createdDate, 'UTC', ?6)) >= str_to_date(?2,'%d-%m-%Y') and DATE(CONVERT_TZ(md.createdDate, 'UTC', ?6)) <= str_to_date(?3,'%d-%m-%Y') " +
        "and md.subject LIKE CONCAT('%', ?4, '%') " +
        "and md.referenceNo LIKE CONCAT('%', ?5, '%') " +
        "and md.status IN ?7 "
    )
    Page<MeetingDelivery> findMeetingDraftList(
        Long senderId,
        String dateFrom,
        String dateTo,
        String subject,
        String referenceNo,
        String zoneCode,
        Collection<Short> status,
        Pageable pageable
    );

    @Query(
        value = "select md from MeetingDelivery md " +
        "where md.delFlag='N' and md.deliveryStatus=0 and md.sender.id=?1 " +
        "and md.subject LIKE CONCAT('%', ?2, '%') " +
        "and md.referenceNo LIKE CONCAT('%', ?3, '%') " +
        "and md.status IN ?4 "
    )
    Page<MeetingDelivery> findMeetingDraftList(
        Long senderId,
        String subject,
        String referenceNo,
        Collection<Short> status,
        Pageable pageable
    );

    Long countBySenderAndDeliveryStatusAndDelFlag(Department sender, short deliveryStatus, String delFlag);
}
