package com.hmm.dms.repository;

import com.hmm.dms.domain.MetaDataHeader;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MetaDataHeader entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MetaDataHeaderRepository extends JpaRepository<MetaDataHeader, Long> {}
