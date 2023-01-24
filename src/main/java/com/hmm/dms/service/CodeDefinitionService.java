package com.hmm.dms.service;

import com.hmm.dms.service.dto.CodeDefinitionDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.hmm.dms.domain.CodeDefinition}.
 */
public interface CodeDefinitionService {
    /**
     * Save a codeDefinition.
     *
     * @param codeDefinitionDTO the entity to save.
     * @return the persisted entity.
     */
    CodeDefinitionDTO save(CodeDefinitionDTO codeDefinitionDTO);

    /**
     * Updates a codeDefinition.
     *
     * @param codeDefinitionDTO the entity to update.
     * @return the persisted entity.
     */
    CodeDefinitionDTO update(CodeDefinitionDTO codeDefinitionDTO);

    /**
     * Partially updates a codeDefinition.
     *
     * @param codeDefinitionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CodeDefinitionDTO> partialUpdate(CodeDefinitionDTO codeDefinitionDTO);

    /**
     * Get all the codeDefinitions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CodeDefinitionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" codeDefinition.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CodeDefinitionDTO> findOne(Long id);

    /**
     * Delete the "id" codeDefinition.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<CodeDefinitionDTO> findCodesByRole(Long roleID);
}
