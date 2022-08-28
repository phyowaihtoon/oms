package com.hmm.dms.service.impl;

import com.hmm.dms.domain.MetaData;
import com.hmm.dms.domain.MetaDataHeader;
import com.hmm.dms.repository.MetaDataHeaderRepository;
import com.hmm.dms.repository.MetaDataRepository;
import com.hmm.dms.service.LoadSetupService;
import com.hmm.dms.service.dto.MetaDataDTO;
import com.hmm.dms.service.dto.MetaDataHeaderDTO;
import com.hmm.dms.service.mapper.MetaDataHeaderMapper;
import com.hmm.dms.service.mapper.MetaDataMapper;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoadSetupServiceImpl implements LoadSetupService {

    private final MetaDataHeaderRepository metaDataHeaderRepository;
    private final MetaDataHeaderMapper metaDataHeaderMapper;
    private final MetaDataRepository metadataReposistory;
    private final MetaDataMapper metaDataMapper;

    public LoadSetupServiceImpl(
        MetaDataHeaderRepository metaDataHeaderRepository,
        MetaDataHeaderMapper metaDataHeaderMapper,
        MetaDataRepository metadataReposistory,
        MetaDataMapper metaDataMapper
    ) {
        this.metaDataHeaderRepository = metaDataHeaderRepository;
        this.metaDataHeaderMapper = metaDataHeaderMapper;
        this.metadataReposistory = metadataReposistory;
        this.metaDataMapper = metaDataMapper;
    }

    @Override
    public List<MetaDataHeaderDTO> getMetaDataHeader() {
        List<MetaDataHeader> metaDataHeaderList = this.metaDataHeaderRepository.findAll();
        return this.metaDataHeaderMapper.toDto(metaDataHeaderList);
    }

    @Override
    public List<MetaDataDTO> getMetaDatabyHeaderId(Long id) {
        List<MetaData> metaDataList = this.metadataReposistory.findByHeaderId(id).collect(Collectors.toList());
        return this.metaDataMapper.toDto(metaDataList);
    }
}
