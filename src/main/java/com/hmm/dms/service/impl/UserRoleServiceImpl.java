package com.hmm.dms.service.impl;

import com.hmm.dms.domain.UserRole;
import com.hmm.dms.repository.UserRoleRepository;
import com.hmm.dms.service.UserRoleService;
import com.hmm.dms.service.dto.UserRoleDTO;
import com.hmm.dms.service.mapper.UserRoleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserRole}.
 */
@Service
@Transactional
public class UserRoleServiceImpl implements UserRoleService {

    private final Logger log = LoggerFactory.getLogger(UserRoleServiceImpl.class);

    private final UserRoleRepository userRoleRepository;

    private final UserRoleMapper userRoleMapper;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository, UserRoleMapper userRoleMapper) {
        this.userRoleRepository = userRoleRepository;
        this.userRoleMapper = userRoleMapper;
    }

    @Override
    public UserRoleDTO save(UserRoleDTO userRoleDTO) {
        log.debug("Request to save UserRole : {}", userRoleDTO);
        UserRole userRole = userRoleMapper.toEntity(userRoleDTO);
        userRole = userRoleRepository.save(userRole);
        return userRoleMapper.toDto(userRole);
    }

    @Override
    public Optional<UserRoleDTO> partialUpdate(UserRoleDTO userRoleDTO) {
        log.debug("Request to partially update UserRole : {}", userRoleDTO);

        return userRoleRepository
            .findById(userRoleDTO.getId())
            .map(
                existingUserRole -> {
                    userRoleMapper.partialUpdate(existingUserRole, userRoleDTO);
                    return existingUserRole;
                }
            )
            .map(userRoleRepository::save)
            .map(userRoleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserRoleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserRoles");
        return userRoleRepository.findAll(pageable).map(userRoleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserRoleDTO> findOne(Long id) {
        log.debug("Request to get UserRole : {}", id);
        return userRoleRepository.findById(id).map(userRoleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserRole : {}", id);
        userRoleRepository.deleteById(id);
    }
}
