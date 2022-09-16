package com.hmm.dms.service;

import com.hmm.dms.service.dto.MetaDataDTO;
import com.hmm.dms.service.dto.MetaDataHeaderDTO;
import com.hmm.dms.service.dto.RepositoryHeaderDTO;
import com.hmm.dms.service.dto.RepositoryInquiryDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoadSetupService {
    public List<MetaDataHeaderDTO> getMetaDataHeader();

    public List<MetaDataDTO> getMetaDatabyHeaderId(Long id);

    public Page<RepositoryHeaderDTO> getAllRepositoryData(RepositoryInquiryDTO dto, Pageable pageable);
}
