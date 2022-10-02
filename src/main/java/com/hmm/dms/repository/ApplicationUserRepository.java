package com.hmm.dms.repository;

import com.hmm.dms.domain.ApplicationUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ApplicationUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    @Query(value = "SELECT ap.* FROM application_user ap WHERE ap.user_id=?1", nativeQuery = true)
    ApplicationUser findOneByUserID(Long userID);
}
