package com.hmm.dms.repository;

import com.hmm.dms.domain.MenuItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    @Query(
        value = "SELECT mi.* FROM menu_item mi " +
        "WHERE mi.id NOT IN (" +
        "SELECT rma.menu_item_id " +
        "FROM role_menu_access rma " +
        "WHERE rma.user_role_id=?1)",
        nativeQuery = true
    )
    List<MenuItem> findAllNotDefinedByRoleId(Long roleId);
}
