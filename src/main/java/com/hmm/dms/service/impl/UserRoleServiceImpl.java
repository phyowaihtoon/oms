package com.hmm.dms.service.impl;

import com.hmm.dms.domain.RoleMenuAccess;
import com.hmm.dms.domain.RoleTemplateAccess;
import com.hmm.dms.domain.UserRole;
import com.hmm.dms.repository.RoleMenuAccessRepository;
import com.hmm.dms.repository.RoleTemplateAccessRepository;
import com.hmm.dms.repository.UserRoleRepository;
import com.hmm.dms.service.UserRoleService;
import com.hmm.dms.service.dto.RoleMenuAccessDTO;
import com.hmm.dms.service.dto.UserRoleDTO;
import com.hmm.dms.service.mapper.RoleMenuAccessMapper;
import com.hmm.dms.service.mapper.RoleTemplateAccessMapper;
import com.hmm.dms.service.mapper.UserRoleMapper;
import com.hmm.dms.service.message.HeaderDetailsMessage;
import com.hmm.dms.service.message.RoleTemplateAccessDTO;
import com.hmm.dms.util.ResponseCode;
import java.util.List;
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

    private final RoleMenuAccessRepository roleMenuAccessRepository;
    private final RoleMenuAccessMapper roleMenuAccessMapper;
    private final RoleTemplateAccessMapper roleTemplateAccessMapper;
    private final RoleTemplateAccessRepository roleTemplateAccessRepository;

    public UserRoleServiceImpl(
        UserRoleRepository userRoleRepository,
        UserRoleMapper userRoleMapper,
        RoleMenuAccessRepository roleMenuAccessRepository,
        RoleMenuAccessMapper roleMenuAccessMapper,
        RoleTemplateAccessRepository roleTemplateAccessRepository,
        RoleTemplateAccessMapper roleTemplateAccessMapper
    ) {
        this.userRoleRepository = userRoleRepository;
        this.userRoleMapper = userRoleMapper;
        this.roleMenuAccessRepository = roleMenuAccessRepository;
        this.roleMenuAccessMapper = roleMenuAccessMapper;
        this.roleTemplateAccessRepository = roleTemplateAccessRepository;
        this.roleTemplateAccessMapper = roleTemplateAccessMapper;
    }

    @Override
    public HeaderDetailsMessage<UserRoleDTO, RoleMenuAccessDTO, RoleTemplateAccessDTO> save(
        HeaderDetailsMessage<UserRoleDTO, RoleMenuAccessDTO, RoleTemplateAccessDTO> message
    ) {
        log.debug("Request to save UserRole : {}", message.getHeader());
        HeaderDetailsMessage<UserRoleDTO, RoleMenuAccessDTO, RoleTemplateAccessDTO> savedMessage = new HeaderDetailsMessage<UserRoleDTO, RoleMenuAccessDTO, RoleTemplateAccessDTO>();
        UserRole userRole = userRoleMapper.toEntity(message.getHeader());
        userRole = userRoleRepository.save(userRole);
        UserRoleDTO savedUserDTO = userRoleMapper.toDto(userRole);

        List<RoleMenuAccessDTO> dtoList = message.getDetails1();
        List<RoleMenuAccess> entityList = this.roleMenuAccessMapper.toEntity(dtoList);

        if (entityList != null && entityList.size() > 0) {
            for (RoleMenuAccess entity : entityList) {
                entity.setUserRole(userRole);
            }
        }

        List<RoleMenuAccess> savedEntityList = this.roleMenuAccessRepository.saveAll(entityList);
        List<RoleMenuAccessDTO> savedDTOList = this.roleMenuAccessMapper.toDto(savedEntityList);

        List<RoleTemplateAccessDTO> templateDTOList = message.getDetails2();
        List<RoleTemplateAccess> templateEntityList = this.roleTemplateAccessMapper.toEntity(templateDTOList);

        if (templateEntityList != null && templateEntityList.size() > 0) {
            for (RoleTemplateAccess entity : templateEntityList) {
                entity.setUserRole(userRole);
            }
        }

        /* Deleting all template access before saving */
        this.roleTemplateAccessRepository.deleteByUserRoleId(userRole.getId());
        /* Saving template access */
        List<RoleTemplateAccess> savedTemplateList = this.roleTemplateAccessRepository.saveAll(templateEntityList);
        List<RoleTemplateAccessDTO> savedTemplateDTOList = this.roleTemplateAccessMapper.toDto(savedTemplateList);

        savedMessage.setHeader(savedUserDTO);
        savedMessage.setDetails1(savedDTOList);
        savedMessage.setDetails2(savedTemplateDTOList);
        savedMessage.setCode(ResponseCode.SUCCESS);
        savedMessage.setMessage("User Role and Access are successfully saved");
        return savedMessage;
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
