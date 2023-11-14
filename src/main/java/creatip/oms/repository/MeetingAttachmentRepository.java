package creatip.oms.repository;

import creatip.oms.domain.MeetingAttachment;
import creatip.oms.domain.MeetingReceiver;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Department entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MeetingAttachmentRepository extends JpaRepository<MeetingAttachment, Long> {
    List<MeetingAttachment> findByHeaderIdAndDelFlag(Long headerId, String delFlag);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE meeting_attachment SET del_flag = 'Y' WHERE id = ?1", nativeQuery = true)
    void updateDelFlagById(Long id);
}
