package com.hmm.dms.repository;

import com.hmm.dms.domain.MetaData;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MetaData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MetaDataRepository extends JpaRepository<MetaData, Long> {
    void deleteByHeaderId(Long id);
}
