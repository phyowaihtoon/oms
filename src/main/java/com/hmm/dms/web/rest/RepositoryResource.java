package com.hmm.dms.web.rest;

import com.hmm.dms.repository.RepositoryHeaderRepository;
import com.hmm.dms.service.RepositoryService;
import com.hmm.dms.service.dto.RepositoryDTO;
import com.hmm.dms.service.dto.RepositoryHeaderDTO;
import com.hmm.dms.service.dto.RepositoryInquiryDTO;
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
 * REST controller for managing {@link com.hmm.dms.domain.Repository}.
 */
@RestController
@RequestMapping("/api")
public class RepositoryResource {

    private final Logger log = LoggerFactory.getLogger(RepositoryResource.class);

    private static final String ENTITY_NAME = "repository";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RepositoryService repositoryService;

    private final RepositoryHeaderRepository repositoryHeaderRepository;

    public RepositoryResource(RepositoryService repositoryService, RepositoryHeaderRepository repositoryHeaderRepository) {
        this.repositoryService = repositoryService;
        this.repositoryHeaderRepository = repositoryHeaderRepository;
    }

    /**
     * {@code POST  /repository} : Create a new repository.
     *
     * @param repositoryDTO the repositoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new repositoryDTO, or with status {@code 400 (Bad Request)} if the repository has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/repository/save")
    public ResponseEntity<RepositoryHeaderDTO> createRepository(@Valid @RequestBody RepositoryHeaderDTO repositoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save Repository : {}", repositoryDTO);
        if (repositoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new repository cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RepositoryHeaderDTO result = repositoryService.save(repositoryDTO);
        return ResponseEntity
            .created(new URI("/api/repository/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /repository/:id} : Updates an existing repository.
     *
     * @param id the id of the repositoryDTO to save.
     * @param RepositoryDTO the repositoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated repositoryDTO,
     * or with status {@code 400 (Bad Request)} if the repositoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the repositoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/repository/{id}")
    public ResponseEntity<RepositoryHeaderDTO> updateRepository(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RepositoryHeaderDTO repositoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Repository : {}, {}", id, repositoryDTO);
        if (repositoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, repositoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!repositoryHeaderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RepositoryHeaderDTO result = repositoryService.save(repositoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, repositoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /repository/:id} : Partial updates given fields of an existing repository, field will ignore if it is null
     *
     * @param id the id of the repositoryDTO to save.
     * @param RepositoryDTO the repositoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated repositoryDTO,
     * or with status {@code 400 (Bad Request)} if the repositoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the repositoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the repositoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/repository/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<RepositoryDTO> partialUpdateRepository(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RepositoryDTO repositoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Repository partially : {}, {}", id, repositoryDTO);
        if (repositoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, repositoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!repositoryHeaderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RepositoryDTO> result = repositoryService.partialUpdate(repositoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, repositoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /repository} : get all the Repository.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Repository in body.
     */
    /*
     * @GetMapping("/repository") public List<RepositoryHeaderDTO> getAllRepository() {
     * log.debug("REST request to get all Repository"); return
     * repositoryService.findAll(); }
     */
    /**
     * {@code GET  /repository} : get all the repository.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of repository in body.
     */
    @GetMapping("/repository")
    public ResponseEntity<List<RepositoryHeaderDTO>> getAllRepositories(Pageable pageable) {
        log.debug("REST request to get a page of Repositories");
        Page<RepositoryHeaderDTO> page = repositoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /repository/:id} : get the "id" Repository.
     *
     * @param id the id of the RepositoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the repositoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/repository/{id}")
    public ResponseEntity<RepositoryHeaderDTO> getRepository(@PathVariable Long id) {
        log.debug("REST request to get Repository : {}", id);
        Optional<RepositoryHeaderDTO> repositoryDTO = repositoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(repositoryDTO);
    }

    /**
     * {@code DELETE  /repository/:id} : delete the "id" Repository.
     *
     * @param id the id of the RepositoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/repository/{id}")
    public ResponseEntity<Void> deleteRepository(@PathVariable Long id) {
        log.debug("REST request to delete Repository : {}", id);
        repositoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/repository/search")
    public ResponseEntity<List<RepositoryHeaderDTO>> getAllRepositoryData(@RequestBody RepositoryInquiryDTO dto, Pageable pageable) {
        log.debug("REST request to get all Documents");
        Page<RepositoryHeaderDTO> page = repositoryService.getAllRepositoryData(dto, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
