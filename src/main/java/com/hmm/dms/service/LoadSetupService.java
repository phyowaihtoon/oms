package com.hmm.dms.service;

import com.hmm.dms.service.dto.MetaDataDTO;
import com.hmm.dms.service.dto.MetaDataHeaderDTO;
import java.util.List;

public interface LoadSetupService {
    public List<MetaDataHeaderDTO> getMetaDataHeader();

    public List<MetaDataDTO> getMetaDatabyHeaderId(Long id);
}
