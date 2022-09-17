package com.hmm.dms.service.impl;

import com.hmm.dms.domain.Document;
import com.hmm.dms.domain.DocumentHeader;
import com.hmm.dms.domain.MetaData;
import com.hmm.dms.domain.MetaDataHeader;
import com.hmm.dms.repository.DocumentHeaderRepository;
import com.hmm.dms.repository.DocumentRepository;
import com.hmm.dms.service.DocumentHeaderService;
import com.hmm.dms.service.dto.DocumentDTO;
import com.hmm.dms.service.dto.DocumentHeaderDTO;
import com.hmm.dms.service.dto.MetaDataHeaderDTO;
import com.hmm.dms.service.mapper.DocumentHeaderMapper;
import com.hmm.dms.service.mapper.DocumentMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Document}.
 */
@Service
@Transactional
public class DocumentHeaderServiceImpl implements DocumentHeaderService {

    private final Logger log = LoggerFactory.getLogger(DocumentHeaderServiceImpl.class);

    private final DocumentHeaderRepository documentHeaderRepository;
    private final DocumentHeaderMapper documentHeaderMapper;
    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;

    public DocumentHeaderServiceImpl(
        DocumentHeaderRepository documentHeaderRepository,
        DocumentHeaderMapper documentHeaderMapper,
        DocumentRepository documentRepository,
        DocumentMapper documentMapper
    ) {
        this.documentHeaderRepository = documentHeaderRepository;
        this.documentHeaderMapper = documentHeaderMapper;
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
    }

    /*@Override
    public MetaDataHeaderDTO save(MetaDataHeaderDTO metaDataDTO) {
        log.debug("Request to save MetaData : {}", metaDataDTO);
        MetaDataHeader metaDataHeader = metaDataHeaderMapper.toEntity(metaDataDTO);
        List<MetaData> metaDataList = metaDataDTO.getMetaDataDetails().stream().map(metaDataMapper::toEntity)
        .collect(Collectors.toList());

        //MetaData metaData = metaDataMapper.toEntity(metaDataDTO);
        metaDataHeader = metaDataHeaderRepository.save(metaDataHeader);
        for (MetaData metaData : metaDataList) {
            metaData.setHeaderId(metaDataHeader.getId());
        }
        metaDataRepository.deleteByHeaderId(metaDataHeader.getId());
        metaDataList = metaDataRepository.saveAll(metaDataList);
        return metaDataHeaderMapper.toDto(metaDataHeader);
    }**/

    @Override
    public DocumentHeaderDTO save(DocumentHeaderDTO documentHeaderDTO) {
        log.debug("Request to save Document : {}", documentHeaderDTO);
        DocumentHeader documentHeader = documentHeaderMapper.toEntity(documentHeaderDTO);
        List<Document> documentList = documentHeaderDTO.getDocList().stream().map(documentMapper::toEntity).collect(Collectors.toList());

        documentHeader = documentHeaderRepository.save(documentHeader);

        for (Document document : documentList) {
            document.setHeaderId(documentHeader.getId());
            document.setDelFlag("N");
        }

        documentRepository.deleteByHeaderId(documentHeader.getId());
        documentList = documentRepository.saveAll(documentList);
        return documentHeaderMapper.toDto(documentHeader);
    }

    @Override
    public Optional<DocumentHeaderDTO> partialUpdate(DocumentHeaderDTO documentHeaderDTO) {
        log.debug("Request to partially update Document : {}", documentHeaderDTO);

        return documentHeaderRepository
            .findById(documentHeaderDTO.getId())
            .map(
                existingDocument -> {
                    documentHeaderMapper.partialUpdate(existingDocument, documentHeaderDTO);
                    return existingDocument;
                }
            )
            .map(documentHeaderRepository::save)
            .map(documentHeaderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentHeaderDTO> findAll() {
        log.debug("Request to get all Documents");
        return documentHeaderRepository
            .findAll()
            .stream()
            .map(documentHeaderMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentHeaderDTO> findOne(Long id) {
        log.debug("Request to get Document : {}", id);
        return documentHeaderRepository.findById(id).map(documentHeaderMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Document : {}", id);
        documentHeaderRepository.deleteById(id);
    }
}
