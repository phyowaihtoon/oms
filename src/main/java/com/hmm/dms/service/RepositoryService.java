package com.hmm.dms.service;

import com.hmm.dms.service.dto.RepositoryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.hmm.dms.domain.Repository}.
 */
public interface RepositoryService {
    /**
     * Save a repository.
     *
     * @param repositoryDTO the entity to save.
     * @return the persisted entity.
     */
    RepositoryDTO save(RepositoryDTO repositoryDTO);

    /**
     * Partially updates a repository.
     *
     * @param repositoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RepositoryDTO> partialUpdate(RepositoryDTO repositoryDTO);

    /**
     * Get all the repositories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RepositoryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" repository.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RepositoryDTO> findOne(Long id);

    /**
     * Delete the "id" repository.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
