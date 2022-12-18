package com.hmm.dms.service.impl;

import com.hmm.dms.domain.RepositoryDomain;
import com.hmm.dms.domain.RepositoryHeader;
import com.hmm.dms.repository.RepositoryDetailRepository;
import com.hmm.dms.repository.RepositoryHeaderRepository;
import com.hmm.dms.service.RepositoryService;
import com.hmm.dms.service.dto.RepositoryDTO;
import com.hmm.dms.service.dto.RepositoryHeaderDTO;
import com.hmm.dms.service.mapper.RepositoryHeaderMapper;
import com.hmm.dms.service.mapper.RepositoryMapper;
import com.hmm.dms.service.message.BaseMessage;
import com.hmm.dms.service.message.RepositoryInquiryMessage;
import com.hmm.dms.util.ResponseCode;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Repository}.
 */
@Service
@Transactional
public class RepositoryServiceImpl implements RepositoryService {

    private final Logger log = LoggerFactory.getLogger(MetaDataServiceImpl.class);

    private final RepositoryHeaderRepository repositoryHeaderRepository;

    private final RepositoryDetailRepository repositoryRepo;

    private final RepositoryHeaderMapper repositoryHeaderMapper;

    private final RepositoryMapper repositoryMapper;

    public RepositoryServiceImpl(
        RepositoryHeaderRepository repositoryHeaderRepository,
        RepositoryDetailRepository repositoryRepo,
        RepositoryHeaderMapper repositoryHeaderMapper,
        RepositoryMapper repositoryMapper
    ) {
        this.repositoryHeaderRepository = repositoryHeaderRepository;
        this.repositoryRepo = repositoryRepo;
        this.repositoryHeaderMapper = repositoryHeaderMapper;
        this.repositoryMapper = repositoryMapper;
    }

    @Override
    public RepositoryHeaderDTO save(RepositoryHeaderDTO repositoryDTO) {
        log.debug("Request to save Repository : {}", repositoryDTO);
        RepositoryHeader header = repositoryHeaderMapper.toEntity(repositoryDTO);
        List<RepositoryDomain> detailList = repositoryDTO
            .getRepositoryDetails()
            .stream()
            .map(repositoryMapper::toEntity)
            .collect(Collectors.toList());

        //MetaData metaData = metaDataMapper.toEntity(metaDataDTO);
        header = repositoryHeaderRepository.save(header);
        for (RepositoryDomain repository : detailList) {
            repository.setHeaderId(header.getId());
        }
        repositoryRepo.deleteByHeaderId(header.getId());
        detailList = repositoryRepo.saveAll(detailList);
        return repositoryHeaderMapper.toDto(header);
    }

    @Override
    public Optional<RepositoryDTO> partialUpdate(RepositoryDTO repositoryDTO) {
        log.debug("Request to partially update Repository : {}", repositoryDTO);

        return repositoryRepo
            .findById(repositoryDTO.getId())
            .map(
                existingMetaData -> {
                    repositoryMapper.partialUpdate(existingMetaData, repositoryDTO);
                    return existingMetaData;
                }
            )
            .map(repositoryRepo::save)
            .map(repositoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RepositoryHeaderDTO> findAll() {
        log.debug("Request to get all Repository");
        return repositoryHeaderRepository
            .findAll()
            .stream()
            .map(repositoryHeaderMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RepositoryHeaderDTO> findOne(Long id) {
        log.debug("Request to get Repository : {}", id);
        Optional<RepositoryHeaderDTO> repositoryHeaderDto = repositoryHeaderRepository.findById(id).map(repositoryHeaderMapper::toDto);
        repositoryHeaderDto
            .get()
            .setRepositoryDetails(repositoryRepo.findByHeaderId(id).map(repositoryMapper::toDto).collect(Collectors.toList()));
        return repositoryHeaderDto;
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Repository : {}", id);
        repositoryHeaderRepository.updateById(id);
        repositoryRepo.updateByHeaderId(id);
    }

    @Override
    public Page<RepositoryHeaderDTO> findAll(Pageable pageable) {
        log.debug("Requesting to get all Categories");
        return repositoryHeaderRepository.findAll(pageable).map(repositoryHeaderMapper::toDto);
    }

    @Override
    public Page<RepositoryHeaderDTO> getAllRepositoryData(RepositoryInquiryMessage message, Pageable pageable) {
        String repoName = message.getRepositoryName();
        if (repoName == null || repoName.equals("null") || repoName.isEmpty()) repoName = ""; else repoName = repoName.trim();

        if (message.getCreatedDate() != null && message.getCreatedDate().trim().length() > 0) {
            String createdDate = message.getCreatedDate();
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
    public Page<RepositoryHeaderDTO> getAllRepositoryDataInTrashBin(RepositoryInquiryMessage message, Pageable pageable) {
        Page<RepositoryHeaderDTO> headerDTOPage = null;
        String repoName = message.getRepositoryName();
        if (repoName == null || repoName.equals("null") || repoName.isEmpty()) repoName = ""; else repoName = repoName.trim();

        if (message.getCreatedDate() != null && message.getCreatedDate().trim().length() > 0) {
            String createdDate = message.getCreatedDate();
            Page<RepositoryHeader> pageWithEntity =
                this.repositoryHeaderRepository.findAllByRepositoryNameAndDate("Y", repoName, createdDate, pageable);
            headerDTOPage = pageWithEntity.map(repositoryHeaderMapper::toDto);
        } else {
            Page<RepositoryHeader> pageWithEntity = this.repositoryHeaderRepository.findAllByRepositoryName("Y", repoName, pageable);
            headerDTOPage = pageWithEntity.map(repositoryHeaderMapper::toDto);
        }

        // Extracting Sub Repository
        if (headerDTOPage != null) for (int i = 0; i < headerDTOPage.getContent().size(); i++) {
            headerDTOPage
                .getContent()
                .get(i)
                .setRepositoryDetails(
                    repositoryRepo
                        .findByHeaderId(headerDTOPage.getContent().get(i).getId())
                        .map(repositoryMapper::toDto)
                        .collect(Collectors.toList())
                );
        }

        return headerDTOPage;
    }

    @Override
    public BaseMessage restoreRepository(Long id) {
        BaseMessage replyMessage = new BaseMessage();
        try {
            this.repositoryHeaderRepository.restoreRepository(id);
            replyMessage.setCode(ResponseCode.SUCCESS);
            replyMessage.setMessage("Repository has been restored.");
        } catch (Exception ex) {
            System.out.println("Error while restoring Repository :" + ex.getMessage());
            ex.printStackTrace();
            replyMessage.setCode(ResponseCode.ERROR_E01);
            replyMessage.setMessage("Repository Restore failed : " + ex.getMessage());
        }

        return replyMessage;
    }
}
