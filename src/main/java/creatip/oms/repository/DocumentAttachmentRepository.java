package creatip.oms.repository;

import creatip.oms.domain.DocumentAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Department entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentAttachmentRepository extends JpaRepository<DocumentAttachment, Long> {}
