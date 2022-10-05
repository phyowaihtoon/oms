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
    @Query(
        value = "SELECT dh.id,dh.meta_data_header_id,dh.field_names,dh.field_values,dh.message,dh.del_flag," +
        "dh.created_by, date(dh.created_date) as created_date," +
        "dh.last_modified_by, date(dh.last_modified_date) as last_modified_date " +
        "FROM document_header dh WHERE dh.meta_data_header_id=?1 AND dh.field_values LIKE %?2%",
        countQuery = "SELECT count(*) FROM document_header dh WHERE dh.meta_data_header_id=?1 AND dh.field_values LIKE %?2%",
        nativeQuery = true
    )
    Page<DocumentHeader> findAll(Long id, String fieldValues, String repURL, Pageable pageable);

    @Query(
        value = "SELECT dh.id,dh.meta_data_header_id,dh.field_names,dh.field_values,dh.message,dh.del_flag," +
        "dh.created_by, date(dh.created_date) as created_date," +
        "dh.last_modified_by, date(dh.last_modified_date) as last_modified_date " +
        "FROM document_header dh WHERE dh.meta_data_header_id=?1 AND dh.field_values LIKE %?2% AND date(dh.created_date) = str_to_date(?3,'%d-%m-%Y')",
        countQuery = "SELECT count(*) FROM document_header dh WHERE dh.meta_data_header_id=?1 AND dh.field_values LIKE %?2% AND date(dh.created_date) = str_to_date(?3,'%d-%m-%Y')",
        nativeQuery = true
    )
    Page<DocumentHeader> findAllByDate(Long id, String fieldValues, String createdDate, String repURL, Pageable pageable);

    @Query(value = "SELECT dh FROM DocumentHeader dh where dh.id=?1")
    DocumentHeader findDocumentHeaderById(Long id);
}
