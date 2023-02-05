package com.hmm.dms.repository;

import com.hmm.dms.domain.Department;
import com.hmm.dms.domain.DocumentHeader;
import com.hmm.dms.domain.RepositoryHeader;
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
