package com.hmm.dms.repository;

import com.hmm.dms.domain.ApplicationUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ApplicationUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    @Query(value = "SELECT ap.* FROM application_user ap WHERE ap.user_id=?1", nativeQuery = true)
    ApplicationUser findOneByUserID(Long userID);

    @Query(value = "SELECT ap.* FROM application_user ap WHERE ap.user_role_id=?1", nativeQuery = true)
    List<ApplicationUser> findAllByRoleID(Long roleID);

    @Modifying
    @Query(value = "delete from ApplicationUser ap WHERE ap.user.id=?1")
    void deleteByUserId(Long userId);
}
