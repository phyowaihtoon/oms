package com.hmm.dms.service.impl;

import com.hmm.dms.domain.RoleDashboardAccess;
import com.hmm.dms.repository.RoleDashboardAccessRepository;
import com.hmm.dms.service.RoleDashboardAccessService;
import com.hmm.dms.service.dto.DashboardTemplateDto;
import com.hmm.dms.service.dto.RoleDashboardAccessDTO;
import com.hmm.dms.service.mapper.RoleDashboardAccessMapper;
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
