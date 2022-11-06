package com.hmm.dms.repository;

import com.hmm.dms.domain.MenuGroup;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {
    @Query(
        value = "select distinct mg from MenuGroup mg,MenuItem mi,RoleMenuAccess rma " +
        "where mg.id=mi.menuGroup.id and " +
        "mi.id=rma.menuItem.id and rma.isAllow=1 and " +
        "rma.userRole.id=?1 order by mg.orderNo asc"
    )
    List<MenuGroup> findAllMenuGroupByRoleId(Long roleId);
}
