package creatip.oms.repository;

import creatip.oms.domain.Announcement;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Announcement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
	
    @Modifying(clearAutomatically = true)
    @Query(value = "update Announcement ann set ann.delFlag=?1 where ann.id=?2")
    void updateDelFlag(String delFlag, Long id);

    Page<Announcement> findAllByDelFlag(String delflag, Pageable pageable);

    List<Announcement> findByActiveFlagOrderById(String activeFlag);
    
    List<Announcement> findByActiveFlagAndDelFlagOrderById(String activeFlag, String delFlag);
}
