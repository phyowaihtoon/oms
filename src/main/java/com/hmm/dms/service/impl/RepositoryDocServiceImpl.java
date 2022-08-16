package com.hmm.dms.service.impl;

import com.hmm.dms.domain.RepositoryDoc;
import com.hmm.dms.repository.RepositoryDocRepository;
import com.hmm.dms.service.RepositoryDocService;
import com.hmm.dms.service.dto.RepositoryDocDTO;
import com.hmm.dms.service.mapper.RepositoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RepositoryDoc}.
 */
@Service
@Transactional
public class RepositoryDocServiceImpl implements RepositoryDocService {

    private final Logger log = LoggerFactory.getLogger(RepositoryDocServiceImpl.class);

    private final RepositoryDocRepository repositoryDocRepository;

    private final RepositoryMapper repositoryMapper;

    public RepositoryDocServiceImpl(RepositoryDocRepository repositoryRepository, RepositoryMapper repositoryMapper) {
        this.repositoryDocRepository = repositoryRepository;
        this.repositoryMapper = repositoryMapper;
    }

    @Override
    public RepositoryDocDTO save(RepositoryDocDTO repositoryDTO) {
        log.debug("Request to save Repository : {}", repositoryDTO);
        RepositoryDoc repository = repositoryMapper.toEntity(repositoryDTO);
        repository = repositoryDocRepository.save(repository);
        return repositoryMapper.toDto(repository);
    }

    @Override
    public Optional<RepositoryDocDTO> partialUpdate(RepositoryDocDTO repositoryDTO) {
        log.debug("Request to partially update Repository : {}", repositoryDTO);

        return repositoryDocRepository
            .findById(repositoryDTO.getId())
            .map(
                existingRepository -> {
                    repositoryMapper.partialUpdate(existingRepository, repositoryDTO);
                    return existingRepository;
                }
            )
            .map(repositoryDocRepository::save)
            .map(repositoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RepositoryDocDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Repositories");
        return repositoryDocRepository.findAll(pageable).map(repositoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RepositoryDocDTO> findOne(Long id) {
        log.debug("Request to get Repository : {}", id);
        return repositoryDocRepository.findById(id).map(repositoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Repository : {}", id);
        repositoryDocRepository.deleteById(id);
    }
}
