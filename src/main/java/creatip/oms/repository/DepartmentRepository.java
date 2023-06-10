package creatip.oms.repository;

import creatip.oms.domain.Department;
import creatip.oms.domain.DocumentHeader;
import creatip.oms.domain.RepositoryHeader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Department entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Modifying(clearAutomatically = true)
    @Query(value = "update Department dept set dept.delFlag=?1 where dept.id=?2")
    void updateDelFlag(String delFlag, Long id);

    Page<Department> findAllByDelFlag(String delflag, Pageable pageable);
}
