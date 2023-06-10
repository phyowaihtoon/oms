package creatip.oms.service.impl;

import creatip.oms.domain.CodeDefinition;
import creatip.oms.repository.CodeDefinitionRepository;
import creatip.oms.service.CodeDefinitionService;
import creatip.oms.service.dto.CodeDefinitionDTO;
import creatip.oms.service.mapper.CodeDefinitionMapper;
import java.util.List;
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
        Page<CodeDefinitionDTO> page = codeDefinitionRepository.findAll(pageable).map(codeDefinitionMapper::toDto);
        return page;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodeDefinitionDTO> findCodesByRole(Long roleID) {
        log.debug("Request to get all CodeDefinitions");
        List<CodeDefinitionDTO> dtoList = this.codeDefinitionMapper.toDto(this.codeDefinitionRepository.findCodesByRole(roleID));
        return dtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CodeDefinitionDTO> findOne(Long id) {
        log.debug("Request to get CodeDefinition : {}", id);
        Optional<CodeDefinition> entity = this.codeDefinitionRepository.findById(id);
        CodeDefinitionDTO dto = this.codeDefinitionMapper.toDto(entity.get());
        Optional<CodeDefinitionDTO> optional = Optional.of(dto);
        return optional;
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CodeDefinition : {}", id);
        codeDefinitionRepository.deleteById(id);
    }
}
