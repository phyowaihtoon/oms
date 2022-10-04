package com.hmm.dms.service.impl;

import com.hmm.dms.domain.ApplicationUser;
import com.hmm.dms.repository.ApplicationUserRepository;
import com.hmm.dms.service.ApplicationUserService;
import com.hmm.dms.service.dto.ApplicationUserDTO;
import com.hmm.dms.service.mapper.ApplicationUserMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ApplicationUser}.
 */
@Service
@Transactional
public class ApplicationUserServiceImpl implements ApplicationUserService {

    private final Logger log = LoggerFactory.getLogger(ApplicationUserServiceImpl.class);

    private final ApplicationUserRepository applicationUserRepository;

    private final ApplicationUserMapper applicationUserMapper;

    public ApplicationUserServiceImpl(ApplicationUserRepository applicationUserRepository, ApplicationUserMapper applicationUserMapper) {
        this.applicationUserRepository = applicationUserRepository;
        this.applicationUserMapper = applicationUserMapper;
    }

    @Override
    public ApplicationUserDTO save(ApplicationUserDTO applicationUserDTO) {
        log.debug("Request to save ApplicationUser : {}", applicationUserDTO);
        ApplicationUser applicationUser = applicationUserMapper.toEntity(applicationUserDTO);
        applicationUser = applicationUserRepository.save(applicationUser);
        return applicationUserMapper.toDto(applicationUser);
    }

    @Override
    public Optional<ApplicationUserDTO> partialUpdate(ApplicationUserDTO applicationUserDTO) {
        log.debug("Request to partially update ApplicationUser : {}", applicationUserDTO);

        return applicationUserRepository
            .findById(applicationUserDTO.getId())
            .map(
                existingApplicationUser -> {
                    applicationUserMapper.partialUpdate(existingApplicationUser, applicationUserDTO);
                    return existingApplicationUser;
                }
            )
            .map(applicationUserRepository::save)
            .map(applicationUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ApplicationUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ApplicationUsers");
        return applicationUserRepository.findAll(pageable).map(applicationUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ApplicationUserDTO> findOne(Long id) {
        log.debug("Request to get ApplicationUser : {}", id);
        return applicationUserRepository.findById(id).map(applicationUserMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ApplicationUser : {}", id);
        applicationUserRepository.deleteById(id);
    }

    @Override
    public ApplicationUserDTO findOneByUserID(Long id) {
        ApplicationUser entity = applicationUserRepository.findOneByUserID(id);
        return applicationUserMapper.toDto(entity);
    }
}
