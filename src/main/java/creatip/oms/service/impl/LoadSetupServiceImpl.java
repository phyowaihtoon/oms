package creatip.oms.service.impl;

import creatip.oms.domain.DashboardTemplate;
import creatip.oms.domain.MetaData;
import creatip.oms.domain.MetaDataHeader;
import creatip.oms.domain.RepositoryHeader;
import creatip.oms.domain.RoleTemplateAccess;
import creatip.oms.repository.DashboardTemplateRepository;
import creatip.oms.repository.MetaDataHeaderRepository;
import creatip.oms.repository.MetaDataRepository;
import creatip.oms.repository.RepositoryDetailRepository;
import creatip.oms.repository.RepositoryHeaderRepository;
import creatip.oms.repository.RoleTemplateAccessRepository;
import creatip.oms.service.LoadSetupService;
import creatip.oms.service.dto.DashboardTemplateDto;
import creatip.oms.service.dto.MetaDataDTO;
import creatip.oms.service.dto.MetaDataHeaderDTO;
import creatip.oms.service.dto.RepositoryHeaderDTO;
import creatip.oms.service.mapper.DashboardTemplateMapper;
import creatip.oms.service.mapper.MetaDataHeaderMapper;
import creatip.oms.service.mapper.MetaDataMapper;
import creatip.oms.service.mapper.RepositoryHeaderMapper;
import creatip.oms.service.mapper.RepositoryMapper;
import creatip.oms.service.mapper.RoleTemplateAccessMapper;
import creatip.oms.service.message.RepositoryInquiryMessage;
import creatip.oms.service.message.RoleTemplateAccessDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoadSetupServiceImpl implements LoadSetupService {

    private final MetaDataHeaderRepository metaDataHeaderRepository;
    private final MetaDataHeaderMapper metaDataHeaderMapper;
    private final MetaDataRepository metaDataRepository;
    private final MetaDataMapper metaDataMapper;

    private final RepositoryHeaderRepository repositoryHeaderRepository;
    private final RepositoryDetailRepository repositoryRepo;
    private final RepositoryHeaderMapper repositoryHeaderMapper;
    private final RepositoryMapper repositoryMapper;

    private final RoleTemplateAccessRepository roleTemplateAccessRepository;
    private final RoleTemplateAccessMapper roleTemplateAccessMapper;

    private final DashboardTemplateRepository dashboardTemplateRepository;
    private final DashboardTemplateMapper dashboardTemplateMapper;

    public LoadSetupServiceImpl(
        MetaDataHeaderRepository metaDataHeaderRepository,
        MetaDataHeaderMapper metaDataHeaderMapper,
        MetaDataRepository metaDataRepository,
        MetaDataMapper metaDataMapper,
        RepositoryHeaderRepository repositoryHeaderRepository,
        RepositoryDetailRepository repositoryRepo,
        RepositoryHeaderMapper repositoryHeaderMapper,
        RepositoryMapper repositoryMapper,
        RoleTemplateAccessRepository roleTemplateAccessRepository,
        RoleTemplateAccessMapper roleTemplateAccessMapper,
        DashboardTemplateRepository dashboardTemplateRepository,
        DashboardTemplateMapper dashboardTemplateMapper
    ) {
        this.metaDataHeaderRepository = metaDataHeaderRepository;
        this.metaDataHeaderMapper = metaDataHeaderMapper;
        this.metaDataRepository = metaDataRepository;
        this.metaDataMapper = metaDataMapper;
        this.repositoryHeaderRepository = repositoryHeaderRepository;
        this.repositoryRepo = repositoryRepo;
        this.repositoryHeaderMapper = repositoryHeaderMapper;
        this.repositoryMapper = repositoryMapper;
        this.roleTemplateAccessRepository = roleTemplateAccessRepository;
        this.roleTemplateAccessMapper = roleTemplateAccessMapper;
        this.dashboardTemplateRepository = dashboardTemplateRepository;
        this.dashboardTemplateMapper = dashboardTemplateMapper;
    }

    @Override
    public List<MetaDataHeaderDTO> getAllMetaDataHeader() {
        List<MetaDataHeader> metaDataHeaderList = this.metaDataHeaderRepository.findByDelFlagEquals("N");
        List<MetaDataHeaderDTO> dtoList = this.metaDataHeaderMapper.toDto(metaDataHeaderList);
        if (dtoList != null && dtoList.size() > 0) {
            for (MetaDataHeaderDTO data : dtoList) {
                List<MetaData> metaDataList = this.metaDataRepository.findByHeaderId(data.getId()).collect(Collectors.toList());
                List<MetaDataDTO> detailDTOList = this.metaDataMapper.toDto(metaDataList);
                data.setMetaDataDetails(detailDTOList);
            }
        }
        return dtoList;
    }

    @Override
    public List<MetaDataDTO> getMetaDatabyHeaderId(Long id) {
        List<MetaData> metaDataList = this.metaDataRepository.findByHeaderId(id).collect(Collectors.toList());
        return this.metaDataMapper.toDto(metaDataList);
    }

    @Override
    public Page<RepositoryHeaderDTO> getAllRepositoryData(RepositoryInquiryMessage dto, Pageable pageable) {
        String repoName = dto.getRepositoryName();
        if (repoName == null || repoName.equals("null") || repoName.isEmpty()) repoName = ""; else repoName = repoName.trim();

        if (dto.getCreatedDate() != null && dto.getCreatedDate().trim().length() > 0) {
            String createdDate = dto.getCreatedDate();
            Page<RepositoryHeader> pageWithEntity =
                this.repositoryHeaderRepository.findAllByRepositoryNameAndDate("N", repoName, createdDate, pageable);
            Page<RepositoryHeaderDTO> data = pageWithEntity.map(repositoryHeaderMapper::toDto);
            for (int i = 0; i < pageWithEntity.getContent().size(); i++) {
                data
                    .getContent()
                    .get(i)
                    .setRepositoryDetails(
                        repositoryRepo
                            .findByHeaderId(data.getContent().get(i).getId())
                            .map(repositoryMapper::toDto)
                            .collect(Collectors.toList())
                    );
            }

            return data;
        }

        Page<RepositoryHeader> pageWithEntity = this.repositoryHeaderRepository.findAllByRepositoryName("N", repoName, pageable);
        Page<RepositoryHeaderDTO> data = pageWithEntity.map(repositoryHeaderMapper::toDto);
        for (int i = 0; i < pageWithEntity.getContent().size(); i++) {
            data
                .getContent()
                .get(i)
                .setRepositoryDetails(
                    repositoryRepo
                        .findByHeaderId(data.getContent().get(i).getId())
                        .map(repositoryMapper::toDto)
                        .collect(Collectors.toList())
                );
        }

        return data;
    }

    @Override
    public List<MetaDataHeaderDTO> getAllMetaDataHeaderAccessByRole(Long roleId) {
        List<MetaDataHeaderDTO> metaDataHeaderList = null;
        List<RoleTemplateAccess> entityList =
            this.roleTemplateAccessRepository.findAllByUserRoleIdAndMetaDataHeaderDelFlagEquals(roleId, "N");
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

    @Override
    public List<DashboardTemplateDto> loadAllDashboardTemplate() {
        List<DashboardTemplate> entityList = this.dashboardTemplateRepository.findAll();
        return this.dashboardTemplateMapper.toDto(entityList);
    }
}