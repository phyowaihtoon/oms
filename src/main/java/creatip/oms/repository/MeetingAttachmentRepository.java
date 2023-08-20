package creatip.oms.repository;

import creatip.oms.domain.MeetingAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Department entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MeetingAttachmentRepository extends JpaRepository<MeetingAttachment, Long> {}
