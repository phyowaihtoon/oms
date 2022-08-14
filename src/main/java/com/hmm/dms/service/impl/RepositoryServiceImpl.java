package com.hmm.dms.service.impl;

import com.hmm.dms.domain.Repository;
import com.hmm.dms.repository.RepositoryRepository;
import com.hmm.dms.service.RepositoryService;
import com.hmm.dms.service.dto.RepositoryDTO;
import com.hmm.dms.service.mapper.RepositoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Repository}.
 */
@Service
@Transactional
public class RepositoryServiceImpl implements RepositoryService {

    private final Logger log = LoggerFactory.getLogger(RepositoryServiceImpl.class);

    private final RepositoryRepository repositoryRepository;

    private final RepositoryMapper repositoryMapper;

    public RepositoryServiceImpl(RepositoryRepository repositoryRepository, RepositoryMapper repositoryMapper) {
        this.repositoryRepository = repositoryRepository;
        this.repositoryMapper = repositoryMapper;
    }

    @Override
    public RepositoryDTO save(RepositoryDTO repositoryDTO) {
        log.debug("Request to save Repository : {}", repositoryDTO);
        Repository repository = repositoryMapper.toEntity(repositoryDTO);
        repository = repositoryRepository.save(repository);
        return repositoryMapper.toDto(repository);
    }

    @Override
    public Optional<RepositoryDTO> partialUpdate(RepositoryDTO repositoryDTO) {
        log.debug("Request to partially update Repository : {}", repositoryDTO);

        return repositoryRepository
            .findById(repositoryDTO.getId())
            .map(
                existingRepository -> {
                    repositoryMapper.partialUpdate(existingRepository, repositoryDTO);
                    return existingRepository;
                }
            )
            .map(repositoryRepository::save)
            .map(repositoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RepositoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Repositories");
        return repositoryRepository.findAll(pageable).map(repositoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RepositoryDTO> findOne(Long id) {
        log.debug("Request to get Repository : {}", id);
        return repositoryRepository.findById(id).map(repositoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Repository : {}", id);
        repositoryRepository.deleteById(id);
    }
}
