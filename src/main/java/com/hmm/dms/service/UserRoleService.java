package com.hmm.dms.service;

import com.hmm.dms.service.dto.RoleMenuAccessDTO;
import com.hmm.dms.service.dto.UserRoleDTO;
import com.hmm.dms.service.message.BaseMessage;
import com.hmm.dms.service.message.HeaderDetailsMessage;
import com.hmm.dms.service.message.RoleTemplateAccessDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.hmm.dms.domain.UserRole}.
 */
public interface UserRoleService {
    /**
     * Save a userRole.
     *
     * @param userRoleDTO the entity to save.
     * @return the persisted entity.
     */
    HeaderDetailsMessage<UserRoleDTO, RoleMenuAccessDTO, RoleTemplateAccessDTO> save(
        HeaderDetailsMessage<UserRoleDTO, RoleMenuAccessDTO, RoleTemplateAccessDTO> message
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
