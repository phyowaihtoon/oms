package creatip.oms.service.impl;

import creatip.oms.domain.RoleDashboardAccess;
import creatip.oms.repository.RoleDashboardAccessRepository;
import creatip.oms.service.RoleDashboardAccessService;
import creatip.oms.service.dto.DashboardTemplateDto;
import creatip.oms.service.dto.RoleDashboardAccessDTO;
import creatip.oms.service.mapper.RoleDashboardAccessMapper;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleDashboardAccessServiceImpl implements RoleDashboardAccessService {

    private final RoleDashboardAccessRepository roleDashboardAccessRepository;
    private final RoleDashboardAccessMapper roleDashboardAccessMapper;

    public RoleDashboardAccessServiceImpl(
        RoleDashboardAccessRepository roleDashboardAccessRepository,
        RoleDashboardAccessMapper roleDashboardAccessMapper
    ) {
        this.roleDashboardAccessRepository = roleDashboardAccessRepository;
        this.roleDashboardAccessMapper = roleDashboardAccessMapper;
    }

    @Override
    public List<RoleDashboardAccessDTO> getAllDashboardAccessByRole(Long roleId) {
        List<RoleDashboardAccess> entityList = this.roleDashboardAccessRepository.findAllByUserRoleId(roleId);
        List<RoleDashboardAccessDTO> dtoList = this.roleDashboardAccessMapper.toDto(entityList);
        return dtoList;
    }

    @Override
    public List<DashboardTemplateDto> getAllDashboardTemplateByRole(Long roleId) {
        List<DashboardTemplateDto> dashboardTemplateList = null;
        List<RoleDashboardAccess> entityList = this.roleDashboardAccessRepository.findAllByUserRoleId(roleId);
        List<RoleDashboardAccessDTO> dtoList = this.roleDashboardAccessMapper.toDto(entityList);
        if (dtoList != null && dtoList.size() > 0) {
            dashboardTemplateList = new ArrayList<DashboardTemplateDto>();
            for (RoleDashboardAccessDTO data : dtoList) {
                DashboardTemplateDto dashboardDto = data.getDashboardTemplate();
                dashboardTemplateList.add(dashboardDto);
            }
        }
        return dashboardTemplateList;
    }
}
