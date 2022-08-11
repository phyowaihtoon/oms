package com.hmm.dms.web.rest;

import com.hmm.dms.repository.RoleMenuMapRepository;
import com.hmm.dms.service.RoleMenuMapService;
import com.hmm.dms.service.dto.RoleMenuMapDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hmm.dms.domain.RoleMenuMap}.
 */
@RestController
@RequestMapping("/api")
public class RoleMenuMapResource {

    private final Logger log = LoggerFactory.getLogger(RoleMenuMapResource.class);

    private static final String ENTITY_NAME = "roleMenuMap";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoleMenuMapService roleMenuMapService;

    private final RoleMenuMapRepository roleMenuMapRepository;

    public RoleMenuMapResource(RoleMenuMapService roleMenuMapService, RoleMenuMapRepository roleMenuMapRepository) {
        this.roleMenuMapService = roleMenuMapService;
        this.roleMenuMapRepository = roleMenuMapRepository;
    }

    /**
     * {@code POST  /role-menu-maps} : Create a new roleMenuMap.
     *
     * @param roleMenuMapDTO the roleMenuMapDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roleMenuMapDTO, or with status {@code 400 (Bad Request)} if the roleMenuMap has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/role-menu-maps")
    public ResponseEntity<RoleMenuMapDTO> createRoleMenuMap(@Valid @RequestBody RoleMenuMapDTO roleMenuMapDTO) throws URISyntaxException {
        log.debug("REST request to save RoleMenuMap : {}", roleMenuMapDTO);
        if (roleMenuMapDTO.getId() != null) {
            throw new BadRequestAlertException("A new roleMenuMap cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RoleMenuMapDTO result = roleMenuMapService.save(roleMenuMapDTO);
        return ResponseEntity
            .created(new URI("/api/role-menu-maps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /role-menu-maps/:id} : Updates an existing roleMenuMap.
     *
     * @param id the id of the roleMenuMapDTO to save.
     * @param roleMenuMapDTO the roleMenuMapDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roleMenuMapDTO,
     * or with status {@code 400 (Bad Request)} if the roleMenuMapDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roleMenuMapDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/role-menu-maps/{id}")
    public ResponseEntity<RoleMenuMapDTO> updateRoleMenuMap(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RoleMenuMapDTO roleMenuMapDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RoleMenuMap : {}, {}", id, roleMenuMapDTO);
        if (roleMenuMapDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roleMenuMapDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roleMenuMapRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RoleMenuMapDTO result = roleMenuMapService.save(roleMenuMapDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roleMenuMapDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /role-menu-maps/:id} : Partial updates given fields of an existing roleMenuMap, field will ignore if it is null
     *
     * @param id the id of the roleMenuMapDTO to save.
     * @param roleMenuMapDTO the roleMenuMapDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roleMenuMapDTO,
     * or with status {@code 400 (Bad Request)} if the roleMenuMapDTO is not valid,
     * or with status {@code 404 (Not Found)} if the roleMenuMapDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the roleMenuMapDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/role-menu-maps/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<RoleMenuMapDTO> partialUpdateRoleMenuMap(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RoleMenuMapDTO roleMenuMapDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RoleMenuMap partially : {}, {}", id, roleMenuMapDTO);
        if (roleMenuMapDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roleMenuMapDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roleMenuMapRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RoleMenuMapDTO> result = roleMenuMapService.partialUpdate(roleMenuMapDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roleMenuMapDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /role-menu-maps} : get all the roleMenuMaps.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roleMenuMaps in body.
     */
    @GetMapping("/role-menu-maps")
    public List<RoleMenuMapDTO> getAllRoleMenuMaps() {
        log.debug("REST request to get all RoleMenuMaps");
        return roleMenuMapService.findAll();
    }

    /**
     * {@code GET  /role-menu-maps/:id} : get the "id" roleMenuMap.
     *
     * @param id the id of the roleMenuMapDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roleMenuMapDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/role-menu-maps/{id}")
    public ResponseEntity<RoleMenuMapDTO> getRoleMenuMap(@PathVariable Long id) {
        log.debug("REST request to get RoleMenuMap : {}", id);
        Optional<RoleMenuMapDTO> roleMenuMapDTO = roleMenuMapService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roleMenuMapDTO);
    }

    /**
     * {@code DELETE  /role-menu-maps/:id} : delete the "id" roleMenuMap.
     *
     * @param id the id of the roleMenuMapDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/role-menu-maps/{id}")
    public ResponseEntity<Void> deleteRoleMenuMap(@PathVariable Long id) {
        log.debug("REST request to delete RoleMenuMap : {}", id);
        roleMenuMapService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
