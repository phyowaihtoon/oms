package com.hmm.dms.repository;

import com.hmm.dms.domain.DocumentHeader;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DocumentHeader entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentHeaderRepository extends JpaRepository<DocumentHeader, Long> {}
