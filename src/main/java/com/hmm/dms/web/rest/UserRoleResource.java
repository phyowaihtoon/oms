package com.hmm.dms.web.rest;

import com.hmm.dms.repository.UserRoleRepository;
import com.hmm.dms.service.RoleMenuAccessService;
import com.hmm.dms.service.RoleTemplateAccessService;
import com.hmm.dms.service.UserRoleService;
import com.hmm.dms.service.dto.RoleMenuAccessDTO;
import com.hmm.dms.service.dto.UserRoleDTO;
import com.hmm.dms.service.message.HeaderDetailsMessage;
import com.hmm.dms.service.message.RoleTemplateAccessDTO;
import com.hmm.dms.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hmm.dms.domain.UserRole}.
 */
@RestController
@RequestMapping("/api")
public class UserRoleResource {

    private final Logger log = LoggerFactory.getLogger(UserRoleResource.class);

    private static final String ENTITY_NAME = "userRole";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserRoleService userRoleService;

    private final RoleMenuAccessService roleMenuAccessService;

    private final RoleTemplateAccessService roleTemplateAccessService;

    private final UserRoleRepository userRoleRepository;

    public UserRoleResource(
        UserRoleService userRoleService,
        UserRoleRepository userRoleRepository,
        RoleMenuAccessService roleMenuAccessService,
        RoleTemplateAccessService roleTemplateAccessService
    ) {
        this.userRoleService = userRoleService;
        this.userRoleRepository = userRoleRepository;
        this.roleMenuAccessService = roleMenuAccessService;
        this.roleTemplateAccessService = roleTemplateAccessService;
    }

    /**
     * {@code POST  /user-roles} : Create a new userRole.
     *
     * @param userRoleDTO the userRoleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userRoleDTO, or with status {@code 400 (Bad Request)} if the userRole has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-roles")
    public ResponseEntity<HeaderDetailsMessage<UserRoleDTO, RoleMenuAccessDTO, RoleTemplateAccessDTO>> createUserRole(
        @Valid @RequestBody HeaderDetailsMessage<UserRoleDTO, RoleMenuAccessDTO, RoleTemplateAccessDTO> message
    ) throws URISyntaxException {
        log.debug("REST request to save UserRole : {}", message.getHeader());
        if (message.getHeader().getId() != null) {
            throw new BadRequestAlertException("A new userRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HeaderDetailsMessage<UserRoleDTO, RoleMenuAccessDTO, RoleTemplateAccessDTO> result = userRoleService.save(message);
        return ResponseEntity
            .created(new URI("/api/user-roles/" + result.getHeader().getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getHeader().getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-roles/:id} : Updates an existing userRole.
     *
     * @param id the id of the userRoleDTO to save.
     * @param userRoleDTO the userRoleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userRoleDTO,
     * or with status {@code 400 (Bad Request)} if the userRoleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userRoleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-roles/{id}")
    public ResponseEntity<HeaderDetailsMessage<UserRoleDTO, RoleMenuAccessDTO, RoleTemplateAccessDTO>> updateUserRole(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HeaderDetailsMessage<UserRoleDTO, RoleMenuAccessDTO, RoleTemplateAccessDTO> message
    ) throws URISyntaxException {
        log.debug("REST request to update UserRole : {}, {}", id, message);
        if (message.getHeader().getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, message.getHeader().getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HeaderDetailsMessage<UserRoleDTO, RoleMenuAccessDTO, RoleTemplateAccessDTO> result = userRoleService.save(message);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, message.getHeader().getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-roles/:id} : Partial updates given fields of an existing userRole, field will ignore if it is null
     *
     * @param id the id of the userRoleDTO to save.
     * @param userRoleDTO the userRoleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userRoleDTO,
     * or with status {@code 400 (Bad Request)} if the userRoleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userRoleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userRoleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-roles/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<UserRoleDTO> partialUpdateUserRole(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserRoleDTO userRoleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserRole partially : {}, {}", id, userRoleDTO);
        if (userRoleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userRoleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserRoleDTO> result = userRoleService.partialUpdate(userRoleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userRoleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-roles} : get all the userRoles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userRoles in body.
     */
    @GetMapping("/user-roles")
    public ResponseEntity<List<UserRoleDTO>> getAllUserRoles(Pageable pageable) {
        log.debug("REST request to get a page of UserRoles");
        Page<UserRoleDTO> page = userRoleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-roles/:id} : get the "id" userRole.
     *
     * @param id the id of the userRoleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userRoleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-roles/{id}")
    public ResponseEntity<HeaderDetailsMessage<UserRoleDTO, RoleMenuAccessDTO, RoleTemplateAccessDTO>> getUserRole(@PathVariable Long id) {
        log.debug("REST request to get UserRole : {}", id);
        Optional<UserRoleDTO> userRoleDTO = userRoleService.findOne(id);
        List<RoleMenuAccessDTO> menuAccessList = roleMenuAccessService.getAllMenuAccessByRole(id);
        List<RoleTemplateAccessDTO> templateAccessList = roleTemplateAccessService.getAllTemplateAccessByRole(id);

        HeaderDetailsMessage<UserRoleDTO, RoleMenuAccessDTO, RoleTemplateAccessDTO> replyMessage = new HeaderDetailsMessage<UserRoleDTO, RoleMenuAccessDTO, RoleTemplateAccessDTO>();
        replyMessage.setHeader(userRoleDTO.get());
        replyMessage.setDetails1(menuAccessList);
        replyMessage.setDetails2(templateAccessList);
        return ResponseEntity.ok().body(replyMessage);
    }

    /**
     * {@code DELETE  /user-roles/:id} : delete the "id" userRole.
     *
     * @param id the id of the userRoleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-roles/{id}")
    public ResponseEntity<Void> deleteUserRole(@PathVariable Long id) {
        log.debug("REST request to delete UserRole : {}", id);
        userRoleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
