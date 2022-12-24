package com.hmm.dms.service.impl;

import com.hmm.dms.domain.CodeDefinition;
import com.hmm.dms.repository.CodeDefinitionRepository;
import com.hmm.dms.service.CodeDefinitionService;
import com.hmm.dms.service.dto.CodeDefinitionDTO;
import com.hmm.dms.service.mapper.CodeDefinitionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CodeDefinition}.
 */
@Service
@Transactional
public class CodeDefinitionServiceImpl implements CodeDefinitionService {

    private final Logger log = LoggerFactory.getLogger(CodeDefinitionServiceImpl.class);

    private final CodeDefinitionRepository codeDefinitionRepository;

    private final CodeDefinitionMapper codeDefinitionMapper;

    public CodeDefinitionServiceImpl(CodeDefinitionRepository codeDefinitionRepository, CodeDefinitionMapper codeDefinitionMapper) {
        this.codeDefinitionRepository = codeDefinitionRepository;
        this.codeDefinitionMapper = codeDefinitionMapper;
    }

    @Override
    public CodeDefinitionDTO save(CodeDefinitionDTO codeDefinitionDTO) {
        log.debug("Request to save CodeDefinition : {}", codeDefinitionDTO);
        CodeDefinition codeDefinition = codeDefinitionMapper.toEntity(codeDefinitionDTO);
        codeDefinition = codeDefinitionRepository.save(codeDefinition);
        return codeDefinitionMapper.toDto(codeDefinition);
    }

    @Override
    public CodeDefinitionDTO update(CodeDefinitionDTO codeDefinitionDTO) {
        log.debug("Request to update CodeDefinition : {}", codeDefinitionDTO);
        CodeDefinition codeDefinition = codeDefinitionMapper.toEntity(codeDefinitionDTO);
        codeDefinition = codeDefinitionRepository.save(codeDefinition);
        return codeDefinitionMapper.toDto(codeDefinition);
    }

    @Override
    public Optional<CodeDefinitionDTO> partialUpdate(CodeDefinitionDTO codeDefinitionDTO) {
        log.debug("Request to partially update CodeDefinition : {}", codeDefinitionDTO);

        return codeDefinitionRepository
            .findById(codeDefinitionDTO.getId())
            .map(
                existingCodeDefinition -> {
                    codeDefinitionMapper.partialUpdate(existingCodeDefinition, codeDefinitionDTO);

                    return existingCodeDefinition;
                }
            )
            .map(codeDefinitionRepository::save)
            .map(codeDefinitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CodeDefinitionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CodeDefinitions");
        return codeDefinitionRepository.findAll(pageable).map(codeDefinitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CodeDefinitionDTO> findOne(Long id) {
        log.debug("Request to get CodeDefinition : {}", id);
        return codeDefinitionRepository.findById(id).map(codeDefinitionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CodeDefinition : {}", id);
        codeDefinitionRepository.deleteById(id);
    }
}