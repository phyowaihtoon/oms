package com.hmm.dms.repository;

import com.hmm.dms.domain.CodeDefinition;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CodeDefinition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CodeDefinitionRepository extends JpaRepository<CodeDefinition, Long> {
    @Query(value = "select cd from CodeDefinition cd")
    List<CodeDefinition> findAllTemplates();
}
