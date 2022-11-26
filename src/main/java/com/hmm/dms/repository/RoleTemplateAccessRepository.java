package com.hmm.dms.repository;

import com.hmm.dms.domain.RoleTemplateAccess;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleTemplateAccessRepository extends JpaRepository<RoleTemplateAccess, Long> {
    List<RoleTemplateAccess> findAllByUserRoleId(Long id);

    void deleteByUserRoleId(Long id);
}
