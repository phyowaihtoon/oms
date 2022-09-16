package com.hmm.dms.web.rest;

import com.hmm.dms.service.LoadSetupService;
import com.hmm.dms.service.dto.MetaDataDTO;
import com.hmm.dms.service.dto.MetaDataHeaderDTO;
import com.hmm.dms.service.dto.RepositoryHeaderDTO;
import com.hmm.dms.service.dto.RepositoryInquiryDTO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/setup")
public class LoadSetupResource {

    private final Logger log = LoggerFactory.getLogger(DocumentResource.class);
    private final LoadSetupService loadSetupService;

    public LoadSetupResource(LoadSetupService loadSetupService) {
        this.loadSetupService = loadSetupService;
    }

    @GetMapping("/metadataheader")
    public List<MetaDataHeaderDTO> loadAllMetaDataHeader() {
        return this.loadSetupService.getMetaDataHeader();
    }

    @GetMapping("/metadata/{id}")
    public List<MetaDataDTO> loadAllMetaDatabyHeaderId(@PathVariable Long id) {
        log.debug("REST request to get meta : {}", id);
        return this.loadSetupService.getMetaDatabyHeaderId(id);
    }

    @PostMapping("/repository")
    public ResponseEntity<List<RepositoryHeaderDTO>> getAllMetaData(@RequestBody RepositoryInquiryDTO dto, Pageable pageable) {
        log.debug("REST request to get all Documents");
        Page<RepositoryHeaderDTO> page = loadSetupService.getAllRepositoryData(dto, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
