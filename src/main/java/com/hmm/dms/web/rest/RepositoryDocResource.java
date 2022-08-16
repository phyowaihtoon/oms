package com.hmm.dms.web.rest;

import com.hmm.dms.repository.RepositoryDocRepository;
import com.hmm.dms.service.RepositoryDocService;
import com.hmm.dms.service.dto.RepositoryDocDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hmm.dms.domain.RepositoryDoc}.
 */
@RestController
@RequestMapping("/api")
public class RepositoryDocResource {

    private final Logger log = LoggerFactory.getLogger(RepositoryDocResource.class);

    private static final String ENTITY_NAME = "repository";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RepositoryDocService repositoryService;

    private final RepositoryDocRepository repositoryRepository;

    public RepositoryDocResource(RepositoryDocService repositoryService, RepositoryDocRepository repositoryRepository) {
        this.repositoryService = repositoryService;
        this.repositoryRepository = repositoryRepository;
    }

    /**
     * {@code POST  /repositories} : Create a new repository.
     *
     * @param repositoryDTO the repositoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new repositoryDTO, or with status {@code 400 (Bad Request)} if the repository has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/repositories")
    public ResponseEntity<RepositoryDocDTO> createRepository(@Valid @RequestBody RepositoryDocDTO repositoryDTO) throws URISyntaxException {
        log.debug("REST request to save Repository : {}", repositoryDTO);
        if (repositoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new repository cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RepositoryDocDTO result = repositoryService.save(repositoryDTO);
        return ResponseEntity
            .created(new URI("/api/repositories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /repositories/:id} : Updates an existing repository.
     *
     * @param id the id of the repositoryDTO to save.
     * @param repositoryDTO the repositoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated repositoryDTO,
     * or with status {@code 400 (Bad Request)} if the repositoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the repositoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/repositories/{id}")
    public ResponseEntity<RepositoryDocDTO> updateRepository(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RepositoryDocDTO repositoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Repository : {}, {}", id, repositoryDTO);
        if (repositoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, repositoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!repositoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RepositoryDocDTO result = repositoryService.save(repositoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, repositoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /repositories/:id} : Partial updates given fields of an existing repository, field will ignore if it is null
     *
     * @param id the id of the repositoryDTO to save.
     * @param repositoryDTO the repositoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated repositoryDTO,
     * or with status {@code 400 (Bad Request)} if the repositoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the repositoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the repositoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/repositories/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<RepositoryDocDTO> partialUpdateRepository(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RepositoryDocDTO repositoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Repository partially : {}, {}", id, repositoryDTO);
        if (repositoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, repositoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!repositoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RepositoryDocDTO> result = repositoryService.partialUpdate(repositoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, repositoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /repositories} : get all the repositories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of repositories in body.
     */
    @GetMapping("/repositories")
    public ResponseEntity<List<RepositoryDocDTO>> getAllRepositories(Pageable pageable) {
        log.debug("REST request to get a page of Repositories");
        Page<RepositoryDocDTO> page = repositoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /repositories/:id} : get the "id" repository.
     *
     * @param id the id of the repositoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the repositoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/repositories/{id}")
    public ResponseEntity<RepositoryDocDTO> getRepository(@PathVariable Long id) {
        log.debug("REST request to get Repository : {}", id);
        Optional<RepositoryDocDTO> repositoryDTO = repositoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(repositoryDTO);
    }

    /**
     * {@code DELETE  /repositories/:id} : delete the "id" repository.
     *
     * @param id the id of the repositoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/repositories/{id}")
    public ResponseEntity<Void> deleteRepository(@PathVariable Long id) {
        log.debug("REST request to delete Repository : {}", id);
        repositoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
