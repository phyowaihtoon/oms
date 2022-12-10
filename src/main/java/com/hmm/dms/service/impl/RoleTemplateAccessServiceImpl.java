package com.hmm.dms.service.impl;

import com.hmm.dms.domain.MetaData;
import com.hmm.dms.domain.RoleTemplateAccess;
import com.hmm.dms.repository.MetaDataRepository;
import com.hmm.dms.repository.RoleTemplateAccessRepository;
import com.hmm.dms.service.RoleTemplateAccessService;
import com.hmm.dms.service.dto.MetaDataDTO;
import com.hmm.dms.service.dto.MetaDataHeaderDTO;
import com.hmm.dms.service.mapper.MetaDataHeaderMapper;
import com.hmm.dms.service.mapper.MetaDataMapper;
import com.hmm.dms.service.mapper.RoleTemplateAccessMapper;
import com.hmm.dms.service.message.RoleTemplateAccessDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleTemplateAccessServiceImpl implements RoleTemplateAccessService {

    private final RoleTemplateAccessRepository roleTemplateAccessRepository;
    private final RoleTemplateAccessMapper roleTemplateAccessMapper;
    private final MetaDataRepository metaDataRepository;
    private final MetaDataHeaderMapper metaDataHeaderMapper;
    private final MetaDataMapper metaDataMapper;

    public RoleTemplateAccessServiceImpl(
        RoleTemplateAccessRepository roleTemplateAccessRepository,
        RoleTemplateAccessMapper roleTemplateAccessMapper,
        MetaDataRepository metaDataRepository,
        MetaDataHeaderMapper metaDataHeaderMapper,
        MetaDataMapper metaDataMapper
    ) {
        this.roleTemplateAccessRepository = roleTemplateAccessRepository;
        this.roleTemplateAccessMapper = roleTemplateAccessMapper;
        this.metaDataRepository = metaDataRepository;
        this.metaDataHeaderMapper = metaDataHeaderMapper;
        this.metaDataMapper = metaDataMapper;
    }

    @Override
    public List<RoleTemplateAccessDTO> getAllTemplateAccessByRole(Long roleId) {
        List<RoleTemplateAccess> entityList = this.roleTemplateAccessRepository.findAllByUserRoleId(roleId);
        List<RoleTemplateAccessDTO> dtoList = this.roleTemplateAccessMapper.toDto(entityList);
        return dtoList;
    }

    @Override
    public List<MetaDataHeaderDTO> getAllMetaDataHeaderAccessByRole(Long roleId) {
        List<MetaDataHeaderDTO> metaDataHeaderList = null;
        List<RoleTemplateAccess> entityList = this.roleTemplateAccessRepository.findAllByUserRoleId(roleId);
        List<RoleTemplateAccessDTO> dtoList = this.roleTemplateAccessMapper.toDto(entityList);
        if (dtoList != null && dtoList.size() > 0) {
            metaDataHeaderList = new ArrayList<MetaDataHeaderDTO>();
            for (RoleTemplateAccessDTO data : dtoList) {
                MetaDataHeaderDTO headerDto = data.getMetaDataHeader();
                List<MetaData> metaDataEntityList = this.metaDataRepository.findAllByMetaDataHeaderId(headerDto.getId());
                List<MetaDataDTO> metaDataDTOList = this.metaDataMapper.toDto(metaDataEntityList);
                headerDto.setMetaDataDetails(metaDataDTOList);
                metaDataHeaderList.add(headerDto);
            }
        }
        return metaDataHeaderList;
    }
}
