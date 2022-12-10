package com.hmm.dms.service;

import com.hmm.dms.service.dto.MetaDataHeaderDTO;
import com.hmm.dms.service.message.RoleTemplateAccessDTO;
import java.util.List;

public interface RoleTemplateAccessService {
    public List<RoleTemplateAccessDTO> getAllTemplateAccessByRole(Long roleId);

    public List<MetaDataHeaderDTO> getAllMetaDataHeaderAccessByRole(Long roleId);
}
