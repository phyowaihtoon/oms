package com.hmm.dms.service;

import com.hmm.dms.service.dto.RepositoryDocDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.hmm.dms.domain.RepositoryDoc}.
 */
public interface RepositoryDocService {
    /**
     * Save a repository.
     *
     * @param repositoryDTO the entity to save.
     * @return the persisted entity.
     */
    RepositoryDocDTO save(RepositoryDocDTO repositoryDTO);

    /**
     * Partially updates a repository.
     *
     * @param repositoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RepositoryDocDTO> partialUpdate(RepositoryDocDTO repositoryDTO);

    /**
     * Get all the repositories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RepositoryDocDTO> findAll(Pageable pageable);

    /**
     * Get the "id" repository.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RepositoryDocDTO> findOne(Long id);

    /**
     * Delete the "id" repository.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
