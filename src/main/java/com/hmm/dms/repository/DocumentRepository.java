package com.hmm.dms.repository;

import com.hmm.dms.domain.Document;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Document entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findAllByHeaderId(Long id);
    List<Document> findAllByHeaderIdAndDelFlag(Long id, String delFlag);
    List<Document> findByFileName(String filename);

    void deleteByHeaderId(Long id);
    List<Document> findByHeaderIdAndFilePathAndFileName(Long id, String filepath, String filename);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE document_header SET status=?1, approved_by=?2, approved_date=?3  " + "WHERE id = ?4", nativeQuery = true)
    void updateStatusById(int status, String approvedBy, Instant currentTime, Long id);

    @Query(value = "SELECT count(*) FROM document where file_path = ?1 and file_name = ?2", nativeQuery = true)
    long checkFileVersion(String filepath, String filename);

    @Query(value = "SELECT count(*) FROM document where version = 0 and id =  ?1", nativeQuery = true)
    long checkVersion(Long id);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE document SET del_flag = 'Y'  " + "WHERE id = ?1", nativeQuery = true)
    void updateFlagById(Long id);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE document SET del_flag = 'N'  " + "WHERE id = ?1 and header_id=?2", nativeQuery = true)
    void restoreDocument(Long id, Long headerId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE document SET file_name_version = ?2, version = ?3 where id=?1", nativeQuery = true)
    void update_Version(Long id, String fileNameWithVersion, float versionNo);
}
