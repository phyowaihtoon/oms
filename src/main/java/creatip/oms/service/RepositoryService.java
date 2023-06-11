package creatip.oms.service;

import creatip.oms.service.dto.RepositoryDTO;
import creatip.oms.service.dto.RepositoryHeaderDTO;
import creatip.oms.service.message.BaseMessage;
import creatip.oms.service.message.RepositoryInquiryMessage;
import java.util.List;
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
    RepositoryHeaderDTO save(RepositoryHeaderDTO data);

    /**
     * Partially updates a Repository.
     *
     * @param RepositoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RepositoryDTO> partialUpdate(RepositoryDTO data);

    /**
     * Get all the repository.
     *
     * @return the list of entities.
     */
    List<RepositoryHeaderDTO> findAll();

    /**
     * Get the "id" repository.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RepositoryHeaderDTO> findOne(Long id);

    /**
     * Delete the "id" repository.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<RepositoryHeaderDTO> findAll(Pageable pageable);

    Page<RepositoryHeaderDTO> getAllRepositoryData(RepositoryInquiryMessage message, Pageable pageable);

    Page<RepositoryHeaderDTO> getAllRepositoryDataInTrashBin(RepositoryInquiryMessage message, Pageable pageable);

    BaseMessage restoreRepository(Long id);

    List<RepositoryHeaderDTO> findAllRepository();
}