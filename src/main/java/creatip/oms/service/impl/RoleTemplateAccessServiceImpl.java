package creatip.oms.service.impl;

import creatip.oms.domain.MetaData;
import creatip.oms.domain.RoleTemplateAccess;
import creatip.oms.repository.MetaDataRepository;
import creatip.oms.repository.RoleTemplateAccessRepository;
import creatip.oms.service.RoleTemplateAccessService;
import creatip.oms.service.dto.MetaDataDTO;
import creatip.oms.service.dto.MetaDataHeaderDTO;
import creatip.oms.service.mapper.MetaDataMapper;
import creatip.oms.service.mapper.RoleTemplateAccessMapper;
import creatip.oms.service.message.RoleTemplateAccessDTO;
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
    private final MetaDataMapper metaDataMapper;

    public RoleTemplateAccessServiceImpl(
        RoleTemplateAccessRepository roleTemplateAccessRepository,
        RoleTemplateAccessMapper roleTemplateAccessMapper,
        MetaDataRepository metaDataRepository,
        MetaDataMapper metaDataMapper
    ) {
        this.roleTemplateAccessRepository = roleTemplateAccessRepository;
        this.roleTemplateAccessMapper = roleTemplateAccessMapper;
        this.metaDataRepository = metaDataRepository;
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
