package com.hmm.dms.service.impl;

import com.hmm.dms.domain.MetaData;
import com.hmm.dms.repository.MetaDataRepository;
import com.hmm.dms.service.MetaDataService;
import com.hmm.dms.service.dto.MetaDataDTO;
import com.hmm.dms.service.mapper.MetaDataMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MetaData}.
 */
@Service
@Transactional
public class MetaDataServiceImpl implements MetaDataService {

    private final Logger log = LoggerFactory.getLogger(MetaDataServiceImpl.class);

    private final MetaDataRepository metaDataRepository;

    private final MetaDataMapper metaDataMapper;

    public MetaDataServiceImpl(MetaDataRepository metaDataRepository, MetaDataMapper metaDataMapper) {
        this.metaDataRepository = metaDataRepository;
        this.metaDataMapper = metaDataMapper;
    }

    @Override
    public MetaDataDTO save(MetaDataDTO metaDataDTO) {
        log.debug("Request to save MetaData : {}", metaDataDTO);
        MetaData metaData = metaDataMapper.toEntity(metaDataDTO);
        metaData = metaDataRepository.save(metaData);
        return metaDataMapper.toDto(metaData);
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
    public List<MetaDataDTO> findAll() {
        log.debug("Request to get all MetaData");
        return metaDataRepository.findAll().stream().map(metaDataMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MetaDataDTO> findOne(Long id) {
        log.debug("Request to get MetaData : {}", id);
        return metaDataRepository.findById(id).map(metaDataMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MetaData : {}", id);
        metaDataRepository.deleteById(id);
    }
}
