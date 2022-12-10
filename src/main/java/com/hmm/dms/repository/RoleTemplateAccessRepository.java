package com.hmm.dms.repository;

import com.hmm.dms.domain.RoleTemplateAccess;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleTemplateAccessRepository extends JpaRepository<RoleTemplateAccess, Long> {
    List<RoleTemplateAccess> findAllByUserRoleId(Long id);

    List<RoleTemplateAccess> findAllByUserRoleIdAndMetaDataHeaderDelFlagEquals(Long id, String delFlag);

    void deleteByUserRoleId(Long id);
}
