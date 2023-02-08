package com.hmm.dms.web.rest;

import com.hmm.dms.service.DashboardService;
import com.hmm.dms.service.dto.DashboardTemplateDto;
import com.hmm.dms.service.dto.InputParamDto;
import com.hmm.dms.service.dto.PieHeaderDataDto;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mab.dashboard.domain.Template}.
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardResource {

    private final Logger log = LoggerFactory.getLogger(DashboardResource.class);

    // private static final String ENTITY_NAME = "dashboard";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DashboardService dashboardService;

    public DashboardResource(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/allTemplate")
    public ResponseEntity<List<DashboardTemplateDto>> getAllDashboardTemplate() {
        log.debug("REST request to get Dashboard Template []");
        List<DashboardTemplateDto> data = dashboardService.findAll();
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/getAllSummary")
    public ResponseEntity<PieHeaderDataDto> getAllSummary() {
        log.debug("REST request to get Summary Data");
        Optional<PieHeaderDataDto> pieHeaderDataDto = dashboardService.getAllSummary();
        return ResponseUtil.wrapOrNotFound(pieHeaderDataDto);
    }

    @GetMapping("/getTodaySummary")
    public ResponseEntity<PieHeaderDataDto> getTodaySummary() {
        log.debug("REST request to get Summary Data");
        Optional<PieHeaderDataDto> pieHeaderDataDto = dashboardService.getTodaySummary();
        return ResponseUtil.wrapOrNotFound(pieHeaderDataDto);
    }

    @PostMapping("/getDataByTemplate")
    public ResponseEntity<List<HashMap<String, Object>>> getDataByTemplate(@Valid @RequestBody InputParamDto param)
        throws URISyntaxException {
        log.debug("REST request to get getDataByTemplate ");
        List<HashMap<String, Object>> data = dashboardService.getDataByTemplate(param);
        return ResponseEntity.ok().body(data);
    }

    @PostMapping("/getTodaySummaryByTemplate")
    public ResponseEntity<PieHeaderDataDto> getTodaySummaryByTemplate(@Valid @RequestBody InputParamDto param) throws URISyntaxException {
        log.debug("REST request to get getTodaySummaryByTemplate ");
        Optional<PieHeaderDataDto> pieHeaderDataDto = dashboardService.getTodaySummaryByTemplate(param);
        return ResponseUtil.wrapOrNotFound(pieHeaderDataDto);
    }

    @PostMapping("/getDataByTemplateType")
    public ResponseEntity<List<HashMap<String, Object>>> getDataByTemplateType(@Valid @RequestBody InputParamDto param)
        throws URISyntaxException {
        log.debug("REST request to get getDataByTemplateType ");
        List<HashMap<String, Object>> data = dashboardService.getDataByTemplateType(param);
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/getOverallSummaryByTemplate")
    public ResponseEntity<PieHeaderDataDto> getOverallSummaryByTemplate() {
        log.debug("REST request to get getOverallSummaryByTemplate Data");
        Optional<PieHeaderDataDto> pieHeaderDataDto = dashboardService.getOverallSummaryByTemplate();
        return ResponseUtil.wrapOrNotFound(pieHeaderDataDto);
    }
}
