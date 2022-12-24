package com.hmm.dms.service;

import com.hmm.dms.service.dto.DashboardTemplateDto;
import com.hmm.dms.service.dto.PieHeaderDataDto;
import java.util.List;
import java.util.Optional;

public interface DashboardService {
    List<DashboardTemplateDto> findAll();

    Optional<PieHeaderDataDto> getAllSummary();

    Optional<PieHeaderDataDto> getTodaySummary();
}
