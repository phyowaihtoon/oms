package com.hmm.dms.repository;

import com.hmm.dms.domain.DocumentHeader;
import java.time.Instant;
import java.util.Collection;
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
public interface DocumentHeaderRepository extends JpaRepository<DocumentHeader, Long>, CustomDocumentInquiryRepository {
    @Query(
        value = "SELECT dh.id,dh.meta_data_header_id,dh.field_names,dh.field_values,dh.message,dh.del_flag," +
        "dh.created_by, date(dh.created_date) as created_date,dh.approved_by, dh.priority, dh.status, dh.reason_for_amend," +
        "dh.reason_for_reject, dh.approved_date, dh.last_modified_by, date(dh.last_modified_date) as last_modified_date " +
        "FROM document_header dh WHERE dh.meta_data_header_id=?1 " +
        "AND SUBSTRING_INDEX(SUBSTRING_INDEX(dh.field_values,'|',?2),'|',-1) =?3 " +
        "AND SUBSTRING_INDEX(SUBSTRING_INDEX(dh.field_values,'|',?4),'|',-1) =?5 " +
        "AND SUBSTRING_INDEX(SUBSTRING_INDEX(dh.field_values,'|',?6),'|',-1) =?7 " +
        "AND dh.field_values LIKE %?8% AND dh.status IN ?9 ",
        countQuery = "SELECT count(*) FROM document_header dh WHERE dh.meta_data_header_id=?1 " +
        "AND SUBSTRING_INDEX(SUBSTRING_INDEX(dh.field_values,'|',?2),'|',-1) =?3 " +
        "AND SUBSTRING_INDEX(SUBSTRING_INDEX(dh.field_values,'|',?4),'|',-1) =?5 " +
        "AND SUBSTRING_INDEX(SUBSTRING_INDEX(dh.field_values,'|',?6),'|',-1) =?7 " +
        "AND dh.field_values LIKE %?8% AND dh.status IN ?9 ",
        nativeQuery = true
    )
    Page<DocumentHeader> findAll(
        Long headerId,
        int fieldIndex1,
        String fieldValue1,
        int fieldIndex2,
        String fieldValue2,
        int fieldIndex3,
        String fieldValue3,
        String generalValue,
        Collection<Integer> status,
        Pageable pageable
    );

    @Query(
        value = "SELECT dh.id,dh.meta_data_header_id,dh.field_names,dh.field_values,dh.message,dh.del_flag," +
        "dh.created_by, date(dh.created_date) as created_date,dh.approved_by, dh.priority, dh.status, dh.reason_for_amend," +
        "dh.reason_for_reject, dh.approved_date, dh.last_modified_by, date(dh.last_modified_date) as last_modified_date " +
        "FROM document_header dh WHERE dh.meta_data_header_id=?1 " +
        "AND SUBSTRING_INDEX(SUBSTRING_INDEX(dh.field_values,'|',?2),'|',-1) =?3 " +
        "AND SUBSTRING_INDEX(SUBSTRING_INDEX(dh.field_values,'|',?4),'|',-1) =?5 " +
        "AND SUBSTRING_INDEX(SUBSTRING_INDEX(dh.field_values,'|',?6),'|',-1) =?7 " +
        "AND dh.field_values LIKE %?8% AND date(dh.created_date) = str_to_date(?9,'%d-%m-%Y') " +
        "AND dh.status IN ?10 ",
        countQuery = "SELECT count(*) FROM document_header dh WHERE dh.meta_data_header_id=?1 " +
        "AND SUBSTRING_INDEX(SUBSTRING_INDEX(dh.field_values,'|',?2),'|',-1) =?3 " +
        "AND SUBSTRING_INDEX(SUBSTRING_INDEX(dh.field_values,'|',?4),'|',-1) =?5 " +
        "AND SUBSTRING_INDEX(SUBSTRING_INDEX(dh.field_values,'|',?6),'|',-1) =?7 " +
        "AND dh.field_values LIKE %?8% AND date(dh.created_date) = str_to_date(?9,'%d-%m-%Y') " +
        "AND dh.status IN ?10 ",
        nativeQuery = true
    )
    Page<DocumentHeader> findAllByDate(
        Long headerId,
        int fieldIndex1,
        String fieldValue1,
        int fieldIndex2,
        String fieldValue2,
        int fieldIndex3,
        String fieldValue3,
        String generalValue,
        String createdDate,
        Collection<Integer> status,
        Pageable pageable
    );

    @Query(
        value = "SELECT distinct dh.id,dh.meta_data_header_id,dh.field_names,dh.field_values,dh.message,dh.del_flag," +
        "dh.created_by, date(dh.created_date) as created_date,dh.approved_by, dh.priority, dh.status, dh.reason_for_amend," +
        "dh.reason_for_reject, dh.approved_date, dh.last_modified_by, date(dh.last_modified_date) as last_modified_date " +
        "FROM document_header dh,document dd " +
        "WHERE dh.id=dd.header_id AND dd.del_flag='Y' " +
        "AND dh.meta_data_header_id=?1 " +
        "AND SUBSTRING_INDEX(SUBSTRING_INDEX(dh.field_values,'|',?2),'|',-1) =?3 " +
        "AND SUBSTRING_INDEX(SUBSTRING_INDEX(dh.field_values,'|',?4),'|',-1) =?5 " +
        "AND SUBSTRING_INDEX(SUBSTRING_INDEX(dh.field_values,'|',?6),'|',-1) =?7 " +
        "AND dh.field_values LIKE %?8% AND dh.status IN ?9 ",
        countQuery = "SELECT count(*) FROM document_header dh,document dd " +
        "WHERE dh.id=dd.header_id AND dd.del_flag='Y' AND dh.meta_data_header_id=?1 " +
        "AND SUBSTRING_INDEX(SUBSTRING_INDEX(dh.field_values,'|',?2),'|',-1) =?3 " +
        "AND SUBSTRING_INDEX(SUBSTRING_INDEX(dh.field_values,'|',?4),'|',-1) =?5 " +
        "AND SUBSTRING_INDEX(SUBSTRING_INDEX(dh.field_values,'|',?6),'|',-1) =?7 " +
        "AND dh.field_values LIKE %?8% AND dh.status IN ?9 ",
        nativeQuery = true
    )
    Page<DocumentHeader> findTrashItems(
        Long headerId,
        int fieldIndex1,
        String fieldValue1,
        int fieldIndex2,
        String fieldValue2,
        int fieldIndex3,
        String fieldValue3,
        String generalValue,
        Collection<Integer> status,
        Pageable pageable
    );

    @Query(
        value = "SELECT distinct dh.id,dh.meta_data_header_id,dh.field_names,dh.field_values,dh.message,dh.del_flag," +
        "dh.created_by, date(dh.created_date) as created_date,dh.approved_by, dh.priority, dh.status, dh.reason_for_amend," +
        "dh.reason_for_reject, dh.approved_date, dh.last_modified_by, date(dh.last_modified_date) as last_modified_date " +
        "FROM document_header dh,document dd " +
        "WHERE dh.id=dd.header_id AND dd.del_flag='Y' AND dh.meta_data_header_id=?1 " +
        "AND SUBSTRING_INDEX(SUBSTRING_INDEX(dh.field_values,'|',?2),'|',-1) =?3 " +
        "AND SUBSTRING_INDEX(SUBSTRING_INDEX(dh.field_values,'|',?4),'|',-1) =?5 " +
        "AND SUBSTRING_INDEX(SUBSTRING_INDEX(dh.field_values,'|',?6),'|',-1) =?7 " +
        "AND dh.field_values LIKE %?8% AND date(dh.created_date) = str_to_date(?9,'%d-%m-%Y') " +
        "AND dh.status IN ?10 ",
        countQuery = "SELECT count(*) FROM document_header dh,document dd " +
        "WHERE dh.id=dd.header_id AND dd.del_flag='Y' AND dh.meta_data_header_id=?1 " +
        "AND SUBSTRING_INDEX(SUBSTRING_INDEX(dh.field_values,'|',?2),'|',-1) =?3 " +
        "AND SUBSTRING_INDEX(SUBSTRING_INDEX(dh.field_values,'|',?4),'|',-1) =?5 " +
        "AND SUBSTRING_INDEX(SUBSTRING_INDEX(dh.field_values,'|',?6),'|',-1) =?7 " +
        "AND dh.field_values LIKE %?8% AND date(dh.created_date) = str_to_date(?9,'%d-%m-%Y') " +
        "AND dh.status IN ?10 ",
        nativeQuery = true
    )
    Page<DocumentHeader> findTrashItemsByDate(
        Long headerId,
        int fieldIndex1,
        String fieldValue1,
        int fieldIndex2,
        String fieldValue2,
        int fieldIndex3,
        String fieldValue3,
        String generalValue,
        String createdDate,
        Collection<Integer> status,
        Pageable pageable
    );

    @Query(value = "SELECT dh FROM DocumentHeader dh where dh.id=?1")
    DocumentHeader findDocumentHeaderById(Long id);

    @Query(value = "SELECT dh FROM DocumentHeader dh where dh.status=?1")
    Page<DocumentHeader> findByStatus(int id, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE document_header SET status=?1, reason_for_amend=?2 " + "WHERE id = ?3", nativeQuery = true)
    void updateAmmendById(int status, String reason, Long id);

    @Modifying(clearAutomatically = true)
    @Query(
        value = "UPDATE document_header SET status=?1, reason_for_reject=?2, approved_by=?3, approved_date=?4 " + "WHERE id = ?5",
        nativeQuery = true
    )
    void updateRejectById(int status, String reason, String approvedBy, Instant currentTime, Long id);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE document_header SET status=?1, approved_by=?2, approved_date=?3  " + "WHERE id = ?4", nativeQuery = true)
    void updateStatusById(int status, String approvedBy, Instant currentTime, Long id);
    
    @Query(value = "Select count(*) from document_header where meta_data_header_id=?1 and field_values=?2 ", nativeQuery = true)
    long checkDuplication(long meta_data_header_id, String field_value);
}
