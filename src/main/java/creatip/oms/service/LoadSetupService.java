package creatip.oms.service;

import creatip.oms.service.dto.DashboardTemplateDto;
import creatip.oms.service.dto.DepartmentDTO;
import creatip.oms.service.dto.HeadDepartmentDTO;
import java.util.List;

public interface LoadSetupService {
    public List<DashboardTemplateDto> loadAllDashboardTemplate();

    public List<HeadDepartmentDTO> getAllHeadDepartments();

    public List<DepartmentDTO> getAllSubDepartments();
}
