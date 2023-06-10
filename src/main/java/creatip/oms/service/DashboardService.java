package creatip.oms.service;

import creatip.oms.service.dto.DashboardTemplateDto;
import creatip.oms.service.dto.InputParamDto;
import creatip.oms.service.dto.PieHeaderDataDto;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

public interface DashboardService {
    List<DashboardTemplateDto> findAll();

    Optional<PieHeaderDataDto> getAllSummary();

    Optional<PieHeaderDataDto> getTodaySummary();

    List<HashMap<String, Object>> getDataByTemplate(@Valid InputParamDto param);

    Optional<PieHeaderDataDto> getTodaySummaryByTemplate(@Valid InputParamDto param);

    List<HashMap<String, Object>> getDataByTemplateType(@Valid InputParamDto param);

    Optional<PieHeaderDataDto> getOverallSummaryByTemplate();
}
