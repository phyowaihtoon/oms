package com.hmm.dms.web.rest;

import com.hmm.dms.service.LoadSetupService;
import com.hmm.dms.service.dto.MetaDataHeaderDTO;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/setup")
public class LoadSetupResource {

    private final LoadSetupService loadSetupService;

    public LoadSetupResource(LoadSetupService loadSetupService) {
        this.loadSetupService = loadSetupService;
    }

    @GetMapping("/metadataheader")
    public List<MetaDataHeaderDTO> loadAllMetaDataHeader() {
        return this.loadSetupService.getMetaDataHeader();
    }
}
