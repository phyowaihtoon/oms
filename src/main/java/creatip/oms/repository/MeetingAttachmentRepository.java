package creatip.oms.repository;

import creatip.oms.domain.MeetingAttachment;
import creatip.oms.domain.MeetingReceiver;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Department entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MeetingAttachmentRepository extends JpaRepository<MeetingAttachment, Long> {
    List<MeetingAttachment> findByHeaderId(Long headerId);
}
