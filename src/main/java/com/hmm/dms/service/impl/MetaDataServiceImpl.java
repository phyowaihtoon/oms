package com.hmm.dms.service.impl;

import com.hmm.dms.domain.MetaData;
import com.hmm.dms.domain.MetaDataHeader;
import com.hmm.dms.repository.MetaDataHeaderRepository;
import com.hmm.dms.repository.MetaDataRepository;
import com.hmm.dms.service.MetaDataService;
import com.hmm.dms.service.dto.MetaDataDTO;
import com.hmm.dms.service.dto.MetaDataHeaderDTO;
import com.hmm.dms.service.dto.MetaDataInquiryDTO;
import com.hmm.dms.service.mapper.MetaDataHeaderMapper;
import com.hmm.dms.service.mapper.MetaDataMapper;
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
 * Service Implementation for managing {@link MetaData}.
 */
@Service
@Transactional
public class MetaDataServiceImpl implements MetaDataService {

    private final Logger log = LoggerFactory.getLogger(MetaDataServiceImpl.class);

    private final MetaDataHeaderRepository metaDataHeaderRepository;

    private final MetaDataRepository metaDataRepository;

    private final MetaDataHeaderMapper metaDataHeaderMapper;

    private final MetaDataMapper metaDataMapper;

    public MetaDataServiceImpl(
        MetaDataHeaderRepository metaDataHeaderRepository,
        MetaDataRepository metaDataRepository,
        MetaDataHeaderMapper metaDataHeaderMapper,
        MetaDataMapper metaDataMapper
    ) {
        this.metaDataHeaderRepository = metaDataHeaderRepository;
        this.metaDataRepository = metaDataRepository;
        this.metaDataHeaderMapper = metaDataHeaderMapper;
        this.metaDataMapper = metaDataMapper;
    }

    @Override
    public MetaDataHeaderDTO save(MetaDataHeaderDTO metaDataDTO) {
        log.debug("Request to save MetaData : {}", metaDataDTO);
        MetaDataHeader metaDataHeader = metaDataHeaderMapper.toEntity(metaDataDTO);
        List<MetaData> metaDataList = metaDataDTO.getMetaDataDetails().stream().map(metaDataMapper::toEntity).collect(Collectors.toList());

        //MetaData metaData = metaDataMapper.toEntity(metaDataDTO);
        metaDataHeader = metaDataHeaderRepository.save(metaDataHeader);
        for (MetaData metaData : metaDataList) {
            metaData.setHeaderId(metaDataHeader.getId());
        }
        metaDataRepository.deleteByHeaderId(metaDataHeader.getId());
        metaDataList = metaDataRepository.saveAll(metaDataList);
        return metaDataHeaderMapper.toDto(metaDataHeader);
    }

    @Override
    public Optional<MetaDataDTO> partialUpdate(MetaDataDTO metaDataDTO) {
        log.debug("Request to partially update MetaData : {}", metaDataDTO);

        return metaDataRepository
            .findById(metaDataDTO.getId())
            .map(
                existingMetaData -> {
                    metaDataMapper.partialUpdate(existingMetaData, metaDataDTO);
                    return existingMetaData;
                }
            )
            .map(metaDataRepository::save)
            .map(metaDataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MetaDataHeaderDTO> findAll() {
        log.debug("Request to get all MetaData");
        return metaDataHeaderRepository
            .findAll()
            .stream()
            .map(metaDataHeaderMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MetaDataHeaderDTO> findOne(Long id) {
        log.debug("Request to get MetaData : {}", id);
        Optional<MetaDataHeaderDTO> metaDataHeaderDto = metaDataHeaderRepository.findById(id).map(metaDataHeaderMapper::toDto);
        metaDataHeaderDto
            .get()
            .setMetaDataDetails(metaDataRepository.findByHeaderId(id).map(metaDataMapper::toDto).collect(Collectors.toList()));
        return metaDataHeaderDto;
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MetaData : {}", id);
        metaDataHeaderRepository.updateById(id);
        metaDataRepository.updateByHeaderId(id);
    }

    @Override
    public Page<MetaDataHeaderDTO> findAll(Pageable pageable) {
        log.debug("Requesting to get all Categories");
        return metaDataHeaderRepository.findAll(pageable).map(metaDataHeaderMapper::toDto);
    }

    @Override
    public Page<MetaDataHeaderDTO> getAllMetaData(MetaDataInquiryDTO dto, Pageable pageable) {
        String docTitle = dto.getDocTitle();
        if (docTitle == null || docTitle.equals("null") || docTitle.isEmpty()) docTitle = ""; else docTitle = docTitle.trim();

        if (dto.getCreatedDate() != null && dto.getCreatedDate().trim().length() > 0) {
            String createdDate = dto.getCreatedDate();
            Page<MetaDataHeader> pageWithEntity = this.metaDataHeaderRepository.findAllByDocTitleAndDate(docTitle, createdDate, pageable);
            return pageWithEntity.map(metaDataHeaderMapper::toDto);
        }

        Page<MetaDataHeader> pageWithEntity = this.metaDataHeaderRepository.findAllByDocTitle(docTitle, pageable);
        return pageWithEntity.map(metaDataHeaderMapper::toDto);
    }
}
