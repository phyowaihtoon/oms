package creatip.oms.service.impl;

import creatip.oms.domain.ApplicationUser;
import creatip.oms.domain.RoleDashboardAccess;
import creatip.oms.domain.RoleMenuAccess;
import creatip.oms.domain.RoleTemplateAccess;
import creatip.oms.domain.UserRole;
import creatip.oms.repository.ApplicationUserRepository;
import creatip.oms.repository.RoleDashboardAccessRepository;
import creatip.oms.repository.RoleMenuAccessRepository;
import creatip.oms.repository.RoleTemplateAccessRepository;
import creatip.oms.repository.UserRoleRepository;
import creatip.oms.service.UserRoleService;
import creatip.oms.service.dto.RoleDashboardAccessDTO;
import creatip.oms.service.dto.RoleMenuAccessDTO;
import creatip.oms.service.dto.UserRoleDTO;
import creatip.oms.service.mapper.RoleDashboardAccessMapper;
import creatip.oms.service.mapper.RoleMenuAccessMapper;
import creatip.oms.service.mapper.RoleTemplateAccessMapper;
import creatip.oms.service.mapper.UserRoleMapper;
import creatip.oms.service.message.BaseMessage;
import creatip.oms.service.message.HeaderDetailsMessage;
import creatip.oms.service.message.RoleTemplateAccessDTO;
import creatip.oms.util.ResponseCode;
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
    private final ApplicationUserRepository applicationUserRepository;

    private final RoleMenuAccessRepository roleMenuAccessRepository;
    private final RoleMenuAccessMapper roleMenuAccessMapper;
    private final RoleTemplateAccessMapper roleTemplateAccessMapper;
    private final RoleTemplateAccessRepository roleTemplateAccessRepository;

    private final RoleDashboardAccessMapper roleDashboardAccessMapper;
    private final RoleDashboardAccessRepository roleDashboardAccessRepository;

    public UserRoleServiceImpl(
        UserRoleRepository userRoleRepository,
        UserRoleMapper userRoleMapper,
        RoleMenuAccessRepository roleMenuAccessRepository,
        RoleMenuAccessMapper roleMenuAccessMapper,
        RoleTemplateAccessRepository roleTemplateAccessRepository,
        RoleTemplateAccessMapper roleTemplateAccessMapper,
        ApplicationUserRepository applicationUserRepository,
        RoleDashboardAccessMapper roleDashboardAccessMapper,
        RoleDashboardAccessRepository roleDashboardAccessRepository
    ) {
        this.userRoleRepository = userRoleRepository;
        this.userRoleMapper = userRoleMapper;
        this.roleMenuAccessRepository = roleMenuAccessRepository;
        this.roleMenuAccessMapper = roleMenuAccessMapper;
        this.roleTemplateAccessRepository = roleTemplateAccessRepository;
        this.roleTemplateAccessMapper = roleTemplateAccessMapper;
        this.applicationUserRepository = applicationUserRepository;
        this.roleDashboardAccessMapper = roleDashboardAccessMapper;
        this.roleDashboardAccessRepository = roleDashboardAccessRepository;
    }

    @Override
    public HeaderDetailsMessage<UserRoleDTO, RoleMenuAccessDTO, RoleTemplateAccessDTO, RoleDashboardAccessDTO> save(
        HeaderDetailsMessage<UserRoleDTO, RoleMenuAccessDTO, RoleTemplateAccessDTO, RoleDashboardAccessDTO> message
    ) {
        log.debug("Request to save UserRole : {}", message.getHeader());
        HeaderDetailsMessage<UserRoleDTO, RoleMenuAccessDTO, RoleTemplateAccessDTO, RoleDashboardAccessDTO> savedMessage = new HeaderDetailsMessage<UserRoleDTO, RoleMenuAccessDTO, RoleTemplateAccessDTO, RoleDashboardAccessDTO>();
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

        List<RoleDashboardAccessDTO> dashboardDTOList = message.getDetails3();
        List<RoleDashboardAccess> dashboardEntityList = this.roleDashboardAccessMapper.toEntity(dashboardDTOList);

        if (dashboardEntityList != null && dashboardEntityList.size() > 0) {
            for (RoleDashboardAccess entity : dashboardEntityList) {
                entity.setUserRole(userRole);
            }
        }

        /* Deleting all dashboard template access before saving */
        this.roleDashboardAccessRepository.deleteByUserRoleId(userRole.getId());
        /* Saving dashboard template access */
        List<RoleDashboardAccess> savedDashboardList = this.roleDashboardAccessRepository.saveAll(dashboardEntityList);
        List<RoleDashboardAccessDTO> savedDashboardDTOList = this.roleDashboardAccessMapper.toDto(savedDashboardList);

        savedMessage.setHeader(savedUserDTO);
        savedMessage.setDetails1(savedDTOList);
        savedMessage.setDetails2(savedTemplateDTOList);
        savedMessage.setDetails3(savedDashboardDTOList);
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
        roleMenuAccessRepository.deleteByUserRoleId(id);
        roleTemplateAccessRepository.deleteByUserRoleId(id);
        userRoleRepository.deleteById(id);
    }

    @Override
    public BaseMessage checkDependency(Long roleId) {
        BaseMessage replyMessage = new BaseMessage();
        replyMessage.setCode(ResponseCode.SUCCESS);
        List<ApplicationUser> userList = this.applicationUserRepository.findAllByRoleID(roleId);
        if (userList != null && userList.size() > 0) {
            replyMessage.setCode(ResponseCode.WARNING);
            replyMessage.setMessage(
                "This role is currently being used in User Authority. It can be deleted only after removing first in User Authority."
            );
        }
        return replyMessage;
    }
}
