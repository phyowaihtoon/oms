package com.hmm.dms.web.rest;

import com.hmm.dms.service.DashboardService;
import com.hmm.dms.service.dto.DashboardTemplateDto;
import com.hmm.dms.service.dto.PieHeaderDataDto;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
