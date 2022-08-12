package com.hmm.dms.repository;

import com.hmm.dms.domain.RoleMenuMap;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RoleMenuMap entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoleMenuMapRepository extends JpaRepository<RoleMenuMap, Long> {}
