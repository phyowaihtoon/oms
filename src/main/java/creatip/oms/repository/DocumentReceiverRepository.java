package creatip.oms.repository;

import creatip.oms.domain.DocumentReceiver;
import creatip.oms.domain.RoleMenuAccess;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Department entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentReceiverRepository extends JpaRepository<DocumentReceiver, Long> {
    @Query(value = "select dc from DocumentReceiver dc where dc.receiver.id=?1")
    List<DocumentReceiver> findAllByRecieverId(Long receiverId);

    @Query(value = "select dc from DocumentReceiver dc where dc.receiver.id=?1 and dc.status=0 and dc.delFlag='N'")
    List<DocumentReceiver> findUnReadMailByRecieverId(Long receiverId);
}
