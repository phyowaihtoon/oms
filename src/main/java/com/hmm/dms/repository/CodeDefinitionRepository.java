package com.hmm.dms.repository;

import com.hmm.dms.domain.CodeDefinition;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CodeDefinition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CodeDefinitionRepository extends JpaRepository<CodeDefinition, Long> {}
