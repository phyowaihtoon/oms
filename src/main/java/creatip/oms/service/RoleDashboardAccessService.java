package creatip.oms.service;

import creatip.oms.service.dto.DashboardTemplateDto;
import creatip.oms.service.dto.RoleDashboardAccessDTO;
import java.util.List;

public interface RoleDashboardAccessService {
    public List<RoleDashboardAccessDTO> getAllDashboardAccessByRole(Long roleId);

    public List<DashboardTemplateDto> getAllDashboardTemplateByRole(Long roleId);
}
