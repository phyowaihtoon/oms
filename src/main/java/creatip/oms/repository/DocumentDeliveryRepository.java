package creatip.oms.repository;

import creatip.oms.domain.Department;
import creatip.oms.domain.DocumentDelivery;
import creatip.oms.repository.custom.CustomDocumentDeliveryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface DocumentDeliveryRepository extends JpaRepository<DocumentDelivery, Long>, CustomDocumentDeliveryRepository {
    @Query(
        value = "select DISTINCT dd from DocumentDelivery dd, DocumentReceiver dc " +
        "where dd.id=dc.header.id and dd.delFlag='N' and dd.deliveryStatus=1 " +
        "and dc.receiver.id=?1 and dc.status=?2 and date(dd.sentDate) = str_to_date(?3,'%d-%m-%Y') " +
        "and dc.delFlag='N' "
    )
    Page<DocumentDelivery> findDocumentsReceived(Long receiverId, short status, String sentDate, Pageable pageable);

    @Query(
        value = "select dd from DocumentDelivery dd " +
        "where dd.delFlag='N' and dd.deliveryStatus=1 " +
        "and dd.sender.id=?1 and date(dd.sentDate) = str_to_date(?2,'%d-%m-%Y') "
    )
    Page<DocumentDelivery> findDocumentsSent(Long senderId, String sentDate, Pageable pageable);

    @Query(
        value = "select dd from DocumentDelivery dd " +
        "where dd.delFlag='N' and dd.deliveryStatus=1 and dd.sender.id=?1 " +
        "and date(dd.sentDate) >= str_to_date(?2,'%d-%m-%Y') and date(dd.sentDate) <= str_to_date(?3,'%d-%m-%Y')"
    )
    Page<DocumentDelivery> findDocumentsSent(Long senderId, String dateFrom, String dateTo, Pageable pageable);

    @Query(value = "select dd from DocumentDelivery dd " + "where dd.delFlag='N' and dd.deliveryStatus=0 " + "and dd.sender.id=?1")
    Page<DocumentDelivery> findDeliveryDraftList(Long senderId, Pageable pageable);

    Long countBySenderAndDeliveryStatusAndDelFlag(Department sender, short deliveryStatus, String delFlag);
}
