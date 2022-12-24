package com.hmm.dms.service;

import com.hmm.dms.service.dto.DashboardTemplateDto;
import com.hmm.dms.service.dto.RoleDashboardAccessDTO;
import java.util.List;

public interface RoleDashboardAccessService {
    public List<RoleDashboardAccessDTO> getAllDashboardAccessByRole(Long roleId);

    public List<DashboardTemplateDto> getAllDashboardTemplateByRole(Long roleId);
}
