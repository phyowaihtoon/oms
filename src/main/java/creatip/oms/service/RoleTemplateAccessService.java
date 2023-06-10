package creatip.oms.service;

import creatip.oms.service.dto.MetaDataHeaderDTO;
import creatip.oms.service.message.RoleTemplateAccessDTO;
import java.util.List;

public interface RoleTemplateAccessService {
    public List<RoleTemplateAccessDTO> getAllTemplateAccessByRole(Long roleId);

    public List<MetaDataHeaderDTO> getAllMetaDataHeaderAccessByRole(Long roleId);
}
