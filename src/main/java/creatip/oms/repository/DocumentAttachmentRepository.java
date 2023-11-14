package creatip.oms.repository;

import creatip.oms.domain.DocumentAttachment;
import creatip.oms.domain.DocumentReceiver;
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
public interface DocumentAttachmentRepository extends JpaRepository<DocumentAttachment, Long> {
    List<DocumentAttachment> findByHeaderIdAndDelFlag(Long headerId, String delFlag);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE document_attachment SET del_flag = 'Y' WHERE id = ?1", nativeQuery = true)
    void updateDelFlagById(Long id);
}
