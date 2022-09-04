package com.hmm.dms.repository;

import com.hmm.dms.domain.RepositoryDomain;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MetaDataHeader entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RepositoryRepo extends JpaRepository<RepositoryDomain, Long> {
    void deleteByHeaderId(Long id);

    Stream<RepositoryDomain> findByHeaderId(Long id);
}
