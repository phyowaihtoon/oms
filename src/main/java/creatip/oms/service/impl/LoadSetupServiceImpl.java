package creatip.oms.service.impl;

import creatip.oms.domain.DashboardTemplate;
import creatip.oms.domain.Department;
import creatip.oms.domain.HeadDepartment;
import creatip.oms.repository.DashboardTemplateRepository;
import creatip.oms.repository.DepartmentRepository;
import creatip.oms.repository.HeadDepartmentRepository;
import creatip.oms.service.LoadSetupService;
import creatip.oms.service.dto.DashboardTemplateDto;
import creatip.oms.service.dto.DepartmentDTO;
import creatip.oms.service.dto.HeadDepartmentDTO;
import creatip.oms.service.mapper.DashboardTemplateMapper;
import creatip.oms.service.mapper.DepartmentMapper;
import creatip.oms.service.mapper.HeadDepartmentMapper;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoadSetupServiceImpl implements LoadSetupService {

    private final DashboardTemplateRepository dashboardTemplateRepository;
    private final DashboardTemplateMapper dashboardTemplateMapper;

    private final HeadDepartmentRepository headDepartmentRepository;
    private final HeadDepartmentMapper headDepartmentMapper;

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public LoadSetupServiceImpl(
        DashboardTemplateRepository dashboardTemplateRepository,
        DashboardTemplateMapper dashboardTemplateMapper,
        HeadDepartmentRepository headDepartmentRepository,
        HeadDepartmentMapper headDepartmentMapper,
        DepartmentRepository departmentRepository,
        DepartmentMapper departmentMapper
    ) {
        this.dashboardTemplateRepository = dashboardTemplateRepository;
        this.dashboardTemplateMapper = dashboardTemplateMapper;
        this.headDepartmentRepository = headDepartmentRepository;
        this.headDepartmentMapper = headDepartmentMapper;
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    @Override
    public List<DashboardTemplateDto> loadAllDashboardTemplate() {
        List<DashboardTemplate> entityList = this.dashboardTemplateRepository.findAll();
        return this.dashboardTemplateMapper.toDto(entityList);
    }

    @Override
    public List<HeadDepartmentDTO> getAllHeadDepartments() {
        List<HeadDepartment> entityList = this.headDepartmentRepository.findAll();
        return this.headDepartmentMapper.toDto(entityList);
    }

    @Override
    public List<DepartmentDTO> getAllSubDepartments() {
        List<Department> entityList = this.departmentRepository.findAll();
        return this.departmentMapper.toDto(entityList);
    }
}
