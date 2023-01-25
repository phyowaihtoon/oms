package com.hmm.dms.repository;

import com.hmm.dms.domain.DashboardTemplate;
import com.hmm.dms.domain.MetaData;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MetaData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MetaDataRepository extends JpaRepository<MetaData, Long> {
    void deleteByHeaderId(Long id);

    Stream<MetaData> findByHeaderId(Long id);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE meta_data SET del_flag='Y' " + "WHERE header_id = ?1", nativeQuery = true)
    void updateByHeaderId(Long id);

    @Query(value = "select md from MetaData md where md.headerId=?1")
    List<MetaData> findAllByMetaDataHeaderId(Long id);

    @Query(value = "select header_id from meta_data where del_flag = 'N' and id = ?1 ", nativeQuery = true)
    Long getHeaderIdById(Long id);

    @Query(value = "select count(*) from document_header where del_flag = 'N' and meta_data_header_id = ?1", nativeQuery = true)
    long checkHeaderId(Long id);

    @Query(
        value = "select * from meta_data where header_id = ?1 and " + "field_type = ?2 and del_flag = 'N' and show_dashboard = 'Y'",
        nativeQuery = true
    )
    Stream<MetaData> findByHeaderIdAndFieldType(Long headerId, String fieldType);

    Optional<MetaData> findByHeaderIdAndFieldTypeAndShowDashboard(Long templateId, String string, String string2);
}
