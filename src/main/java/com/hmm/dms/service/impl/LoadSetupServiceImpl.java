package com.hmm.dms.service.impl;

import com.hmm.dms.domain.MetaData;
import com.hmm.dms.domain.MetaDataHeader;
import com.hmm.dms.domain.RepositoryHeader;
import com.hmm.dms.repository.MetaDataHeaderRepository;
import com.hmm.dms.repository.MetaDataRepository;
import com.hmm.dms.repository.RepositoryHeaderRepository;
import com.hmm.dms.repository.RepositoryRepo;
import com.hmm.dms.service.LoadSetupService;
import com.hmm.dms.service.dto.MetaDataDTO;
import com.hmm.dms.service.dto.MetaDataHeaderDTO;
import com.hmm.dms.service.dto.RepositoryHeaderDTO;
import com.hmm.dms.service.dto.RepositoryInquiryDTO;
import com.hmm.dms.service.mapper.MetaDataHeaderMapper;
import com.hmm.dms.service.mapper.MetaDataMapper;
import com.hmm.dms.service.mapper.RepositoryHeaderMapper;
import com.hmm.dms.service.mapper.RepositoryMapper;
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
    private final MetaDataRepository metadataReposistory;
    private final MetaDataMapper metaDataMapper;

    private final RepositoryHeaderRepository repositoryHeaderRepository;
    private final RepositoryRepo repositoryRepo;
    private final RepositoryHeaderMapper repositoryHeaderMapper;
    private final RepositoryMapper repositoryMapper;

    public LoadSetupServiceImpl(
        MetaDataHeaderRepository metaDataHeaderRepository,
        MetaDataHeaderMapper metaDataHeaderMapper,
        MetaDataRepository metadataReposistory,
        MetaDataMapper metaDataMapper,
        RepositoryHeaderRepository repositoryHeaderRepository,
        RepositoryRepo repositoryRepo,
        RepositoryHeaderMapper repositoryHeaderMapper,
        RepositoryMapper repositoryMapper
    ) {
        this.metaDataHeaderRepository = metaDataHeaderRepository;
        this.metaDataHeaderMapper = metaDataHeaderMapper;
        this.metadataReposistory = metadataReposistory;
        this.metaDataMapper = metaDataMapper;
        this.repositoryHeaderRepository = repositoryHeaderRepository;
        this.repositoryRepo = repositoryRepo;
        this.repositoryHeaderMapper = repositoryHeaderMapper;
        this.repositoryMapper = repositoryMapper;
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

    @Override
    public Page<RepositoryHeaderDTO> getAllRepositoryData(RepositoryInquiryDTO dto, Pageable pageable) {
        String repoName = dto.getRepositoryName();
        if (repoName == null || repoName.equals("null") || repoName.isEmpty()) repoName = ""; else repoName = repoName.trim();

        if (dto.getCreatedDate() != null && dto.getCreatedDate().trim().length() > 0) {
            String createdDate = dto.getCreatedDate();
            Page<RepositoryHeader> pageWithEntity =
                this.repositoryHeaderRepository.findAllByRepositoryNameAndDate(repoName, createdDate, pageable);
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

        Page<RepositoryHeader> pageWithEntity = this.repositoryHeaderRepository.findAllByRepositoryName(repoName, pageable);
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
}
