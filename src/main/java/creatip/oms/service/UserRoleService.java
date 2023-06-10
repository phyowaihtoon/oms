package creatip.oms.service;

import creatip.oms.service.dto.RoleDashboardAccessDTO;
import creatip.oms.service.dto.RoleMenuAccessDTO;
import creatip.oms.service.dto.UserRoleDTO;
import creatip.oms.service.message.BaseMessage;
import creatip.oms.service.message.HeaderDetailsMessage;
import creatip.oms.service.message.RoleTemplateAccessDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link creatip.oms.domain.UserRole}.
 */
public interface UserRoleService {
    /**
     * Save a userRole.
     *
     * @param userRoleDTO the entity to save.
     * @return the persisted entity.
     */
    HeaderDetailsMessage<UserRoleDTO, RoleMenuAccessDTO, RoleTemplateAccessDTO, RoleDashboardAccessDTO> save(
        HeaderDetailsMessage<UserRoleDTO, RoleMenuAccessDTO, RoleTemplateAccessDTO, RoleDashboardAccessDTO> message
    );

    /**
     * Partially updates a userRole.
     *
     * @param userRoleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserRoleDTO> partialUpdate(UserRoleDTO userRoleDTO);

    /**
     * Get all the userRoles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserRoleDTO> findAll(Pageable pageable);

    /**
     * Get the "id" userRole.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserRoleDTO> findOne(Long id);

    /**
     * Delete the "id" userRole.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    BaseMessage checkDependency(Long roleId);
}
