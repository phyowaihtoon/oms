package com.hmm.dms.repository;

import com.hmm.dms.domain.RepositoryHeader;
import com.hmm.dms.domain.RoleDashboardAccess;
import com.hmm.dms.domain.RoleTemplateAccess;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface RoleDashboardAccessRepository extends JpaRepository<RoleDashboardAccess, Long> {
    List<RoleDashboardAccess> findAllByUserRoleId(Long id);

    void deleteByUserRoleId(Long id);
}
