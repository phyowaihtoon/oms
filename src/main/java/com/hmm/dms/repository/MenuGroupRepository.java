package com.hmm.dms.repository;

import com.hmm.dms.domain.MenuGroup;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {
    @Query(
        value = "select distinct mg.* from menu_group mg,menu_item mi,role_menu_access rma " +
        "where mg.id=mi.menu_group_id and " +
        "mi.id=rma.menu_item_id and " +
        "rma.user_role_id=?1",
        nativeQuery = true
    )
    List<MenuGroup> findAllByRole(Long roleId);
}
