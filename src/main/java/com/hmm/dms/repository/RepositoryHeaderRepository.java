package com.hmm.dms.repository;

import com.hmm.dms.domain.RepositoryDomain;
import com.hmm.dms.domain.RepositoryHeader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MetaDataHeader entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RepositoryHeaderRepository extends JpaRepository<RepositoryHeader, Long> {
    @Query(
        value = "SELECT dh.id,dh.repository_name,dh.del_flag," +
        "dh.created_by, date(dh.created_date) as created_date," +
        "dh.last_modified_by, date(dh.last_modified_date) as last_modified_date " +
        "FROM repository_header dh " +
        "WHERE dh.del_flag='N' AND dh.repository_name LIKE %?1%",
        nativeQuery = true
    )
    Page<RepositoryHeader> findAllByRepositoryName(String repositoryName, Pageable pageable);

    @Query(
        value = "SELECT dh.id,dh.repository_name,dh.del_flag," +
        "dh.created_by, date(dh.created_date) as created_date," +
        "dh.last_modified_by, date(dh.last_modified_date) as last_modified_date " +
        "FROM repository_header dh " +
        "WHERE dh.del_flag='N' AND dh.repository_name LIKE %?1% AND date(dh.created_date) = str_to_date(?2,'%d-%m-%Y')",
        nativeQuery = true
    )
    Page<RepositoryHeader> findAllByRepositoryNameAndDate(String repositoryName, String createdDate, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE repository_header SET del_flag='Y' " + "WHERE id = ?", nativeQuery = true)
    void updateById(Long id);
}
