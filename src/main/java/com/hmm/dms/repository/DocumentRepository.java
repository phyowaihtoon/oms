package com.hmm.dms.repository;

import com.hmm.dms.domain.Document;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Document entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findAllByHeaderId(Long id);
}
