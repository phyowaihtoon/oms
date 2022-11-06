package com.hmm.dms.repository;

import com.hmm.dms.domain.MetaDataHeader;
import java.util.List;
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
public interface MetaDataHeaderRepository extends JpaRepository<MetaDataHeader, Long> {
    @Query(
        value = "SELECT mdh.id,mdh.doc_title,mdh.del_flag," +
        "mdh.created_by, date(mdh.created_date) as created_date," +
        "mdh.last_modified_by, date(mdh.last_modified_date) as last_modified_date " +
        "FROM meta_data_header mdh " +
        "WHERE mdh.del_flag='N' AND mdh.doc_title LIKE %?1%",
        countQuery = "SELECT count(*) FROM meta_data_header mdh WHERE mdh.del_flag='N' AND mdh.doc_title LIKE %?1%",
        nativeQuery = true
    )
    Page<MetaDataHeader> findAllByDocTitle(String docTitle, Pageable pageable);

    @Query(
        value = "SELECT mdh.id,mdh.doc_title,mdh.del_flag," +
        "mdh.created_by, date(mdh.created_date) as created_date," +
        "mdh.last_modified_by, date(mdh.last_modified_date) as last_modified_date " +
        "FROM meta_data_header mdh " +
        "WHERE mdh.del_flag='N' AND mdh.doc_title LIKE %?1% AND date(mdh.created_date) = str_to_date(?2,'%d-%m-%Y')",
        countQuery = "SELECT count(*) FROM meta_data_header mdh WHERE mdh.del_flag='N' AND mdh.doc_title LIKE %?1% AND date(mdh.created_date) = str_to_date(?2,'%d-%m-%Y')",
        nativeQuery = true
    )
    Page<MetaDataHeader> findAllByDocTitleAndDate(String docTitle, String createdDate, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE meta_data_header SET del_flag='Y' " + "WHERE id = ?", nativeQuery = true)
    void updateById(Long id);

    List<MetaDataHeader> findByDelFlagEquals(String delFlag);
}
