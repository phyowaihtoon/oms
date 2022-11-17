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
        "FROM document_header dh WHERE dh.meta_data_header_id=?1 AND SUBSTRING_INDEX(SUBSTRING_INDEX(dh.field_values,'|',?2),'|',-1) LIKE %?3% AND dh.field_values LIKE %?4% ",
        countQuery = "SELECT count(*) FROM document_header dh WHERE dh.meta_data_header_id=?1 AND SUBSTRING_INDEX(SUBSTRING_INDEX(dh.field_values,'|',?2),'|',-1) LIKE %?3% AND dh.field_values LIKE %?4% ",
        nativeQuery = true
    )
    Page<DocumentHeader> findAll(Long headerId, int fieldIndex, String fieldValue, String generalValue, Pageable pageable);

    @Query(
        value = "SELECT dh.id,dh.meta_data_header_id,dh.field_names,dh.field_values,dh.message,dh.del_flag," +
        "dh.created_by, date(dh.created_date) as created_date," +
        "dh.last_modified_by, date(dh.last_modified_date) as last_modified_date " +
        "FROM document_header dh WHERE dh.meta_data_header_id=?1 AND SUBSTRING_INDEX(SUBSTRING_INDEX(dh.field_values,'|',?2),'|',-1) LIKE %?3% AND dh.field_values LIKE %?4% AND date(dh.created_date) = str_to_date(?5,'%d-%m-%Y') ",
        countQuery = "SELECT count(*) FROM document_header dh WHERE dh.meta_data_header_id=?1 AND SUBSTRING_INDEX(SUBSTRING_INDEX(dh.field_values,'|',?2),'|',-1) LIKE %?3% AND dh.field_values LIKE %?4% AND date(dh.created_date) = str_to_date(?5,'%d-%m-%Y') ",
        nativeQuery = true
    )
    Page<DocumentHeader> findAllByDate(
        Long headerId,
        int fieldIndex,
        String fieldValue,
        String generalValue,
        String createdDate,
        Pageable pageable
    );

    @Query(value = "SELECT dh FROM DocumentHeader dh where dh.id=?1")
    DocumentHeader findDocumentHeaderById(Long id);
}
