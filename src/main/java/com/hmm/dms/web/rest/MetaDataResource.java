package com.hmm.dms.web.rest;

import com.hmm.dms.repository.MetaDataHeaderRepository;
import com.hmm.dms.service.MetaDataService;
import com.hmm.dms.service.dto.MetaDataDTO;
import com.hmm.dms.service.dto.MetaDataHeaderDTO;
import com.hmm.dms.service.message.BaseMessage;
import com.hmm.dms.service.message.MetaDataInquiryMessage;
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
 * REST controller for managing {@link com.hmm.dms.domain.MetaData}.
 */
@RestController
@RequestMapping("/api")
public class MetaDataResource {

    private final Logger log = LoggerFactory.getLogger(MetaDataResource.class);

    private static final String ENTITY_NAME = "metaData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MetaDataService metaDataService;

    private final MetaDataHeaderRepository metaDataHeaderRepository;

    public MetaDataResource(MetaDataService metaDataService, MetaDataHeaderRepository metaDataHeaderRepository) {
        this.metaDataService = metaDataService;
        this.metaDataHeaderRepository = metaDataHeaderRepository;
    }

    /**
     * {@code POST  /meta-data} : Create a new metaData.
     *
     * @param metaDataDTO the metaDataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new metaDataDTO, or with status {@code 400 (Bad Request)} if the metaData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/meta-data/save")
    public ResponseEntity<MetaDataHeaderDTO> createMetaData(@Valid @RequestBody MetaDataHeaderDTO metaDataDTO) throws URISyntaxException {
        log.debug("REST request to save MetaData : {}", metaDataDTO);
        if (metaDataDTO.getId() != null) {
            throw new BadRequestAlertException("A new metaData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MetaDataHeaderDTO result = metaDataService.save(metaDataDTO);
        return ResponseEntity
            .created(new URI("/api/meta-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /meta-data/:id} : Updates an existing metaData.
     *
     * @param id the id of the metaDataDTO to save.
     * @param metaDataDTO the metaDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metaDataDTO,
     * or with status {@code 400 (Bad Request)} if the metaDataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the metaDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/meta-data/{id}")
    public ResponseEntity<MetaDataHeaderDTO> updateMetaData(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MetaDataHeaderDTO metaDataDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MetaData : {}, {}", id, metaDataDTO);

        if (metaDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metaDataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!metaDataHeaderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MetaDataHeaderDTO result = metaDataService.save(metaDataDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metaDataDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /meta-data/:id} : Partial updates given fields of an existing metaData, field will ignore if it is null
     *
     * @param id the id of the metaDataDTO to save.
     * @param metaDataDTO the metaDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metaDataDTO,
     * or with status {@code 400 (Bad Request)} if the metaDataDTO is not valid,
     * or with status {@code 404 (Not Found)} if the metaDataDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the metaDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/meta-data/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MetaDataDTO> partialUpdateMetaData(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MetaDataDTO metaDataDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MetaData partially : {}, {}", id, metaDataDTO);
        if (metaDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metaDataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!metaDataHeaderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MetaDataDTO> result = metaDataService.partialUpdate(metaDataDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metaDataDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /meta-data} : get all the metaData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of metaData in body.
     */
    /*
     * @GetMapping("/meta-data") public List<MetaDataHeaderDTO> getAllMetaData() {
     * log.debug("REST request to get all MetaData"); return
     * metaDataService.findAll(); }
     */
    /**
     * {@code GET  /meta-data} : get all the metaData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of metaData in body.
     */
    @GetMapping("/meta-data")
    public ResponseEntity<List<MetaDataHeaderDTO>> getAllMetaDatas(Pageable pageable) {
        log.debug("REST request to get a page of Categories");
        Page<MetaDataHeaderDTO> page = metaDataService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /meta-data/:id} : get the "id" metaData.
     *
     * @param id the id of the metaDataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the metaDataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/meta-data/{id}")
    public ResponseEntity<MetaDataHeaderDTO> getMetaData(@PathVariable Long id) {
        log.debug("REST request to get MetaData : {}", id);
        Optional<MetaDataHeaderDTO> metaDataDTO = metaDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(metaDataDTO);
    }

    /**
     * {@code DELETE  /meta-data/:id} : delete the "id" metaData.
     *
     * @param id the id of the metaDataDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/meta-data/{id}")
    public ResponseEntity<Void> deleteMetaData(@PathVariable Long id) {
        log.debug("REST request to delete MetaData : {}", id);
        metaDataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/meta-data/search")
    public ResponseEntity<List<MetaDataHeaderDTO>> getAllMetaData(@RequestBody MetaDataInquiryMessage message, Pageable pageable) {
        log.debug("REST request to get all metadata");
        Page<MetaDataHeaderDTO> page = metaDataService.getAllMetaData(message, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/meta-data/trashbin")
    public ResponseEntity<List<MetaDataHeaderDTO>> getAllMetaDataInTrashBin(
        @RequestBody MetaDataInquiryMessage message,
        Pageable pageable
    ) {
        log.debug("REST request to get all metadata");
        Page<MetaDataHeaderDTO> page = metaDataService.getAllMetaDataInTrashBin(message, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PutMapping("/meta-data/restore/{id}")
    public ResponseEntity<BaseMessage> restoreMetaData(@PathVariable(value = "id", required = false) final Long id)
        throws URISyntaxException {
        BaseMessage result = metaDataService.restoreMetaData(id);
        String docHeaderId = id.toString();
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, docHeaderId))
            .body(result);
    }
}
