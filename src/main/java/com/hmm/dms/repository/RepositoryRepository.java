package com.hmm.dms.repository;

import com.hmm.dms.domain.Repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Repository entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RepositoryRepository extends JpaRepository<Repository, Long> {}
