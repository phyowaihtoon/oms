package creatip.oms.repository;

import creatip.oms.domain.RepositoryHeader;
import creatip.oms.domain.RoleDashboardAccess;
import creatip.oms.domain.RoleTemplateAccess;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface RoleDashboardAccessRepository extends JpaRepository<RoleDashboardAccess, Long> {
    List<RoleDashboardAccess> findAllByUserRoleId(Long id);

    void deleteByUserRoleId(Long id);
}
