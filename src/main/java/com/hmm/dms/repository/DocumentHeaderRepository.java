package com.hmm.dms.repository;

import com.hmm.dms.domain.DocumentHeader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DocumentHeader entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentHeaderRepository extends JpaRepository<DocumentHeader, Long> {
    @Query("SELECT dh FROM DocumentHeader dh WHERE dh.metaDataHeaderId=?1 AND dh.repositoryURL LIKE ?2% AND dh.fieldValues LIKE %?3%")
    Page<DocumentHeader> findAll(Long id, String repURL, String fieldValues, Pageable pageable);
}
