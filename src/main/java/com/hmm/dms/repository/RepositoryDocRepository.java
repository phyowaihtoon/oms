package com.hmm.dms.repository;

import com.hmm.dms.domain.RepositoryDoc;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Repository entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RepositoryDocRepository extends JpaRepository<RepositoryDoc, Long> {}
