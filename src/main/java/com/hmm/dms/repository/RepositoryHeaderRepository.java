package com.hmm.dms.repository;

import com.hmm.dms.domain.RepositoryHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MetaDataHeader entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RepositoryHeaderRepository extends JpaRepository<RepositoryHeader, Long> {}
