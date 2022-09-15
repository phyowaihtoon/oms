package com.hmm.dms.repository;

import com.hmm.dms.domain.MetaData;
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
    @Query(value = "UPDATE meta_data SET del_flag='Y' " + "WHERE header_id = ?", nativeQuery = true)
    void updateByHeaderId(Long id);
}
