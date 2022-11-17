package com.hmm.dms.repository;

import com.hmm.dms.domain.DocumentHeader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Data SQL repository for the DocumentHeader entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentHeaderRepository extends JpaRepository<DocumentHeader, Long> {
    @Query(
        value = "SELECT dh.id,dh.meta_data_header_id,dh.field_names,dh.field_values,dh.message,dh.del_flag," +
        "dh.created_by, date(dh.created_date) as created_date,dh.approved_by, dh.priority, dh.status, dh.reason_for_amend," +
        "dh.reason_for_reject, dh.last_modified_by, date(dh.last_modified_date) as last_modified_date " +
        "FROM document_header dh WHERE dh.meta_data_header_id=?1 AND dh.field_values LIKE %?2%",
        countQuery = "SELECT count(*) FROM document_header dh WHERE dh.meta_data_header_id=?1 AND dh.field_values LIKE %?2%",
        nativeQuery = true
    )
    Page<DocumentHeader> findAll(Long id, String fieldValues, String repURL, Pageable pageable);

    @Query(
        value = "SELECT dh.id,dh.meta_data_header_id,dh.field_names,dh.field_values,dh.message,dh.del_flag," +
        "dh.created_by, date(dh.created_date) as created_date,dh.approved_by, dh.priority, dh.status, dh.reason_for_amend," +
        "dh.reason_for_reject, dh.last_modified_by, date(dh.last_modified_date) as last_modified_date " +
        "FROM document_header dh WHERE dh.meta_data_header_id=?1 AND dh.field_values LIKE %?2% AND date(dh.created_date) = str_to_date(?3,'%d-%m-%Y')",
        countQuery = "SELECT count(*) FROM document_header dh WHERE dh.meta_data_header_id=?1 AND dh.field_values LIKE %?2% AND date(dh.created_date) = str_to_date(?3,'%d-%m-%Y')",
        nativeQuery = true
    )
    Page<DocumentHeader> findAllByDate(Long id, String fieldValues, String createdDate, String repURL, Pageable pageable);

    @Query(value = "SELECT dh FROM DocumentHeader dh where dh.id=?1")
    DocumentHeader findDocumentHeaderById(Long id);

    @Query(value = "SELECT dh FROM DocumentHeader dh where dh.status=?1")
    Page<DocumentHeader> findByStatus(int id, String fieldValues, String repURL, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE document_header SET status=?1, reason_for_amend=?2 " + "WHERE id = ?3", nativeQuery = true)
    void updateAmmendById(int status, String reason, Long id);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE document_header SET status=?1, reason_for_reject=?2 " + "WHERE id = ?3", nativeQuery = true)
    void updateRejectById(int status, String reason, Long id);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE document_header SET status=?1 " + "WHERE id = ?2", nativeQuery = true)
    void updateStatusById(int status, Long id);
}
