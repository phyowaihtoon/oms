package creatip.oms.repository;

import creatip.oms.domain.MenuItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    @Query(
        value = "SELECT mi FROM MenuItem mi " +
        "WHERE mi.id NOT IN (" +
        "SELECT rma.menuItem.id " +
        "FROM RoleMenuAccess rma " +
        "WHERE rma.userRole.id=?1) " +
        "ORDER BY mi.menuGroup.orderNo"
    )
    List<MenuItem> findAllNotDefinedByRoleId(Long roleId);

    @Query(value = "select mi from MenuItem mi order by mi.menuGroup.orderNo")
    List<MenuItem> findAllOrderByMenuGroup();
}
