package com.hmm.dms.repository;

import com.hmm.dms.domain.RoleMenuAccess;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleMenuAccessRepository extends JpaRepository<RoleMenuAccess, Long> {
    List<RoleMenuAccess> findAllByUserRoleId(Long id);

    @Query(
        value = "select rma.* from menu_group mg,menu_item mi,role_menu_access rma " +
        "where mg.id=mi.menu_group_id and " +
        "mi.id=rma.menu_item_id and " +
        "rma.user_role_id=?1 and " +
        "mg.id=?2;",
        nativeQuery = true
    )
    List<RoleMenuAccess> findAllByUserRoleAndMenuGroup(Long userRoleId, Long menuGroupId);
}
