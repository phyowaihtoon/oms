package com.hmm.dms.repository;

import com.hmm.dms.domain.MetaData;
import com.hmm.dms.service.dto.MetaDataDTO;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MetaData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MetaDataRepository extends JpaRepository<MetaData, Long> {
    void deleteByHeaderId(Long id);

    Stream<MetaData> findByHeaderId(Long id);
}
