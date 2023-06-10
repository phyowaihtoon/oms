package creatip.oms.service;

import creatip.oms.service.dto.DashboardTemplateDto;
import creatip.oms.service.dto.MetaDataDTO;
import creatip.oms.service.dto.MetaDataHeaderDTO;
import creatip.oms.service.dto.RepositoryHeaderDTO;
import creatip.oms.service.message.RepositoryInquiryMessage;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoadSetupService {
    public List<MetaDataHeaderDTO> getAllMetaDataHeader();

    public List<MetaDataDTO> getMetaDatabyHeaderId(Long id);

    public Page<RepositoryHeaderDTO> getAllRepositoryData(RepositoryInquiryMessage dto, Pageable pageable);

    public List<MetaDataHeaderDTO> getAllMetaDataHeaderAccessByRole(Long roleId);

    public List<DashboardTemplateDto> loadAllDashboardTemplate();
}
