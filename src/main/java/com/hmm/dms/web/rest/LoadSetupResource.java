package com.hmm.dms.web.rest;

import com.hmm.dms.service.LoadSetupService;
import com.hmm.dms.service.dto.DocumentDTO;
import com.hmm.dms.service.dto.MetaDataDTO;
import com.hmm.dms.service.dto.MetaDataHeaderDTO;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.ResponseUtil;

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
}
