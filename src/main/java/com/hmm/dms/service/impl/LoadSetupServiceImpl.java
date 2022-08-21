package com.hmm.dms.service.impl;

import com.hmm.dms.domain.MetaDataHeader;
import com.hmm.dms.repository.MetaDataHeaderRepository;
import com.hmm.dms.service.LoadSetupService;
import com.hmm.dms.service.dto.MetaDataHeaderDTO;
import com.hmm.dms.service.mapper.MetaDataHeaderMapper;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoadSetupServiceImpl implements LoadSetupService {

    private final MetaDataHeaderRepository metaDataHeaderRepository;
    private final MetaDataHeaderMapper metaDataHeaderMapper;

    public LoadSetupServiceImpl(MetaDataHeaderRepository metaDataHeaderRepository, MetaDataHeaderMapper metaDataHeaderMapper) {
        this.metaDataHeaderRepository = metaDataHeaderRepository;
        this.metaDataHeaderMapper = metaDataHeaderMapper;
    }

    @Override
    public List<MetaDataHeaderDTO> getMetaDataHeader() {
        List<MetaDataHeader> metaDataHeaderList = this.metaDataHeaderRepository.findAll();
        return this.metaDataHeaderMapper.toDto(metaDataHeaderList);
    }
}
