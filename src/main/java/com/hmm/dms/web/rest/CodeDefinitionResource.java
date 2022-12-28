package com.hmm.dms.web.rest;

import com.hmm.dms.repository.CodeDefinitionRepository;
import com.hmm.dms.service.CodeDefinitionService;
import com.hmm.dms.service.dto.CodeDefinitionDTO;
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
 * REST controller for managing {@link com.hmm.dms.domain.CodeDefinition}.
 */
@RestController
@RequestMapping("/api")
public class CodeDefinitionResource {

    private final Logger log = LoggerFactory.getLogger(CodeDefinitionResource.class);

    private static final String ENTITY_NAME = "codeDefinition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CodeDefinitionService codeDefinitionService;

    private final CodeDefinitionRepository codeDefinitionRepository;

    public CodeDefinitionResource(CodeDefinitionService codeDefinitionService, CodeDefinitionRepository codeDefinitionRepository) {
        this.codeDefinitionService = codeDefinitionService;
        this.codeDefinitionRepository = codeDefinitionRepository;
    }

    /**
     * {@code POST  /code-definitions} : Create a new codeDefinition.
     *
     * @param codeDefinitionDTO the codeDefinitionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new codeDefinitionDTO, or with status {@code 400 (Bad Request)} if the codeDefinition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/code-definitions")
    public ResponseEntity<CodeDefinitionDTO> createCodeDefinition(@Valid @RequestBody CodeDefinitionDTO codeDefinitionDTO)
        throws URISyntaxException {
        log.debug("REST request to save CodeDefinition : {}", codeDefinitionDTO);
        if (codeDefinitionDTO.getId() != null) {
            throw new BadRequestAlertException("A new codeDefinition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CodeDefinitionDTO result = codeDefinitionService.save(codeDefinitionDTO);
        return ResponseEntity
            .created(new URI("/api/code-definitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /code-definitions/:id} : Updates an existing codeDefinition.
     *
     * @param id the id of the codeDefinitionDTO to save.
     * @param codeDefinitionDTO the codeDefinitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated codeDefinitionDTO,
     * or with status {@code 400 (Bad Request)} if the codeDefinitionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the codeDefinitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/code-definitions/{id}")
    public ResponseEntity<CodeDefinitionDTO> updateCodeDefinition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CodeDefinitionDTO codeDefinitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CodeDefinition : {}, {}", id, codeDefinitionDTO);
        if (codeDefinitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, codeDefinitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!codeDefinitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CodeDefinitionDTO result = codeDefinitionService.update(codeDefinitionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, codeDefinitionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /code-definitions/:id} : Partial updates given fields of an existing codeDefinition, field will ignore if it is null
     *
     * @param id the id of the codeDefinitionDTO to save.
     * @param codeDefinitionDTO the codeDefinitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated codeDefinitionDTO,
     * or with status {@code 400 (Bad Request)} if the codeDefinitionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the codeDefinitionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the codeDefinitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/code-definitions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CodeDefinitionDTO> partialUpdateCodeDefinition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CodeDefinitionDTO codeDefinitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CodeDefinition partially : {}, {}", id, codeDefinitionDTO);
        if (codeDefinitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, codeDefinitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!codeDefinitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CodeDefinitionDTO> result = codeDefinitionService.partialUpdate(codeDefinitionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, codeDefinitionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /code-definitions} : get all the codeDefinitions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of codeDefinitions in body.
     */
    @GetMapping("/code-definitions")
    public ResponseEntity<List<CodeDefinitionDTO>> getAllCodeDefinitions(Pageable pageable) {
        log.debug("REST request to get a page of CodeDefinitions");
        Page<CodeDefinitionDTO> page = codeDefinitionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /code-definitions/:id} : get the "id" codeDefinition.
     *
     * @param id the id of the codeDefinitionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the codeDefinitionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/code-definitions/{id}")
    public ResponseEntity<CodeDefinitionDTO> getCodeDefinition(@PathVariable Long id) {
        log.debug("REST request to get CodeDefinition : {}", id);
        Optional<CodeDefinitionDTO> codeDefinitionDTO = codeDefinitionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(codeDefinitionDTO);
    }

    /**
     * {@code DELETE  /code-definitions/:id} : delete the "id" codeDefinition.
     *
     * @param id the id of the codeDefinitionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/code-definitions/{id}")
    public ResponseEntity<Void> deleteCodeDefinition(@PathVariable Long id) {
        log.debug("REST request to delete CodeDefinition : {}", id);
        codeDefinitionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /code-definitions} : get all the codeDefinitions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of codeDefinitions in body.
     */
    @GetMapping("/code-definitions/template")
    public ResponseEntity<List<CodeDefinitionDTO>> getTemplateCodeDefinitions() {
        log.debug("REST request to get a page of CodeDefinitions");
        List<CodeDefinitionDTO> dtoList = codeDefinitionService.findAllTemplateCodeDefinitions();
        return ResponseEntity.ok().body(dtoList);
    }
}
