package com.hmm.dms.service;

import com.hmm.dms.service.dto.MetaDataDTO;
import com.hmm.dms.service.dto.MetaDataHeaderDTO;
import com.hmm.dms.service.dto.RepositoryHeaderDTO;
import com.hmm.dms.service.message.RepositoryInquiryMessage;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoadSetupService {
    public List<MetaDataHeaderDTO> getAllMetaDataHeader();

    public List<MetaDataDTO> getMetaDatabyHeaderId(Long id);

    public Page<RepositoryHeaderDTO> getAllRepositoryData(RepositoryInquiryMessage dto, Pageable pageable);

    public List<MetaDataHeaderDTO> getAllMetaDataHeaderAccessByRole(Long roleId);
}
