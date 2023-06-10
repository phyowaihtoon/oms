package creatip.oms.web.rest;

import creatip.oms.domain.MetaDataHeader;
import creatip.oms.repository.MetaDataHeaderRepository;
import creatip.oms.web.rest.errors.BadRequestAlertException;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link creatip.oms.domain.MetaDataHeader}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MetaDataHeaderResource {

    private final Logger log = LoggerFactory.getLogger(MetaDataHeaderResource.class);

    private static final String ENTITY_NAME = "metaDataHeader";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MetaDataHeaderRepository metaDataHeaderRepository;

    public MetaDataHeaderResource(MetaDataHeaderRepository metaDataHeaderRepository) {
        this.metaDataHeaderRepository = metaDataHeaderRepository;
    }

    /**
     * {@code POST  /meta-data-headers} : Create a new metaDataHeader.
     *
     * @param metaDataHeader the metaDataHeader to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new metaDataHeader, or with status {@code 400 (Bad Request)} if the metaDataHeader has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/meta-data-headers")
    public ResponseEntity<MetaDataHeader> createMetaDataHeader(@Valid @RequestBody MetaDataHeader metaDataHeader)
        throws URISyntaxException {
        log.debug("REST request to save MetaDataHeader : {}", metaDataHeader);
        if (metaDataHeader.getId() != null) {
            throw new BadRequestAlertException("A new metaDataHeader cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MetaDataHeader result = metaDataHeaderRepository.save(metaDataHeader);
        return ResponseEntity
            .created(new URI("/api/meta-data-headers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /meta-data-headers/:id} : Updates an existing metaDataHeader.
     *
     * @param id the id of the metaDataHeader to save.
     * @param metaDataHeader the metaDataHeader to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metaDataHeader,
     * or with status {@code 400 (Bad Request)} if the metaDataHeader is not valid,
     * or with status {@code 500 (Internal Server Error)} if the metaDataHeader couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/meta-data-headers/{id}")
    public ResponseEntity<MetaDataHeader> updateMetaDataHeader(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MetaDataHeader metaDataHeader
    ) throws URISyntaxException {
        log.debug("REST request to update MetaDataHeader : {}, {}", id, metaDataHeader);
        if (metaDataHeader.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metaDataHeader.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!metaDataHeaderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MetaDataHeader result = metaDataHeaderRepository.save(metaDataHeader);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metaDataHeader.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /meta-data-headers/:id} : Partial updates given fields of an existing metaDataHeader, field will ignore if it is null
     *
     * @param id the id of the metaDataHeader to save.
     * @param metaDataHeader the metaDataHeader to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metaDataHeader,
     * or with status {@code 400 (Bad Request)} if the metaDataHeader is not valid,
     * or with status {@code 404 (Not Found)} if the metaDataHeader is not found,
     * or with status {@code 500 (Internal Server Error)} if the metaDataHeader couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/meta-data-headers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MetaDataHeader> partialUpdateMetaDataHeader(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MetaDataHeader metaDataHeader
    ) throws URISyntaxException {
        log.debug("REST request to partial update MetaDataHeader partially : {}, {}", id, metaDataHeader);
        if (metaDataHeader.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metaDataHeader.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!metaDataHeaderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MetaDataHeader> result = metaDataHeaderRepository
            .findById(metaDataHeader.getId())
            .map(
                existingMetaDataHeader -> {
                    if (metaDataHeader.getDocTitle() != null) {
                        existingMetaDataHeader.setDocTitle(metaDataHeader.getDocTitle());
                    }

                    return existingMetaDataHeader;
                }
            )
            .map(metaDataHeaderRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metaDataHeader.getId().toString())
        );
    }

    /**
     * {@code GET  /meta-data-headers} : get all the metaDataHeaders.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of metaDataHeaders in body.
     */
    @GetMapping("/meta-data-headers")
    public List<MetaDataHeader> getAllMetaDataHeaders() {
        log.debug("REST request to get all MetaDataHeaders");
        return metaDataHeaderRepository.findAll();
    }

    /**
     * {@code GET  /meta-data-headers/:id} : get the "id" metaDataHeader.
     *
     * @param id the id of the metaDataHeader to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the metaDataHeader, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/meta-data-headers/{id}")
    public ResponseEntity<MetaDataHeader> getMetaDataHeader(@PathVariable Long id) {
        log.debug("REST request to get MetaDataHeader : {}", id);
        Optional<MetaDataHeader> metaDataHeader = metaDataHeaderRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(metaDataHeader);
    }

    /**
     * {@code DELETE  /meta-data-headers/:id} : delete the "id" metaDataHeader.
     *
     * @param id the id of the metaDataHeader to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/meta-data-headers/{id}")
    public ResponseEntity<Void> deleteMetaDataHeader(@PathVariable Long id) {
        log.debug("REST request to delete MetaDataHeader : {}", id);
        metaDataHeaderRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
