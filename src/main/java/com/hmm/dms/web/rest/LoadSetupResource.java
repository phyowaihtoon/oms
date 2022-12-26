package com.hmm.dms.web.rest;

import com.hmm.dms.enumeration.CommonEnum.DocumentStatusEnum;
import com.hmm.dms.enumeration.CommonEnum.PriorityEnum;
import com.hmm.dms.enumeration.CommonEnum.WorkflowAuthorityEnum;
import com.hmm.dms.service.LoadSetupService;
import com.hmm.dms.service.dto.DashboardTemplateDto;
import com.hmm.dms.service.dto.MetaDataDTO;
import com.hmm.dms.service.dto.MetaDataHeaderDTO;
import com.hmm.dms.service.dto.RepositoryHeaderDTO;
import com.hmm.dms.service.message.RepositoryInquiryMessage;
import com.hmm.dms.service.message.SetupEnumMessage;
import java.util.ArrayList;
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

    @GetMapping("/workflow")
    public List<SetupEnumMessage<Integer, String>> loadWorkflowAuthority() {
        List<SetupEnumMessage<Integer, String>> workflowAuthorityList = new ArrayList<SetupEnumMessage<Integer, String>>();
        for (WorkflowAuthorityEnum enumData : WorkflowAuthorityEnum.values()) {
            SetupEnumMessage<Integer, String> setupDTO = new SetupEnumMessage<Integer, String>();
            setupDTO.setValue(enumData.value);
            setupDTO.setDescription(enumData.description);
            workflowAuthorityList.add(setupDTO);
        }
        return workflowAuthorityList;
    }

    @GetMapping("/docstatus")
    public List<SetupEnumMessage<Integer, String>> loadDocumentStatus() {
        List<SetupEnumMessage<Integer, String>> docStatusList = new ArrayList<SetupEnumMessage<Integer, String>>();
        for (DocumentStatusEnum enumData : DocumentStatusEnum.values()) {
            SetupEnumMessage<Integer, String> setupDTO = new SetupEnumMessage<Integer, String>();
            setupDTO.setValue(enumData.value);
            setupDTO.setDescription(enumData.description);
            docStatusList.add(setupDTO);
        }
        return docStatusList;
    }

    @GetMapping("/priority")
    public List<SetupEnumMessage<Integer, String>> loadPriority() {
        List<SetupEnumMessage<Integer, String>> priorityList = new ArrayList<SetupEnumMessage<Integer, String>>();
        for (PriorityEnum enumData : PriorityEnum.values()) {
            SetupEnumMessage<Integer, String> setupDTO = new SetupEnumMessage<Integer, String>();
            setupDTO.setValue(enumData.value);
            setupDTO.setDescription(enumData.description);
            priorityList.add(setupDTO);
        }
        return priorityList;
    }

    @GetMapping("/metadataheader")
    public List<MetaDataHeaderDTO> loadAllMetaDataHeader() {
        return this.loadSetupService.getAllMetaDataHeader();
    }

    @GetMapping("/metadataheader/{id}")
    public List<MetaDataHeaderDTO> getAllMetaDataHeaderAccessByRole(@PathVariable Long id) {
        return this.loadSetupService.getAllMetaDataHeaderAccessByRole(id);
    }

    @GetMapping("/metadata/{id}")
    public List<MetaDataDTO> loadAllMetaDatabyHeaderId(@PathVariable Long id) {
        log.debug("REST request to get meta : {}", id);
        return this.loadSetupService.getMetaDatabyHeaderId(id);
    }

    @PostMapping("/repository")
    public ResponseEntity<List<RepositoryHeaderDTO>> getAllRepository(@RequestBody RepositoryInquiryMessage dto, Pageable pageable) {
        log.debug("REST request to get all Documents");
        Page<RepositoryHeaderDTO> page = loadSetupService.getAllRepositoryData(dto, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/dashboard")
    public List<DashboardTemplateDto> loadAllDashboardTemplate() {
        return this.loadSetupService.loadAllDashboardTemplate();
    }
}
