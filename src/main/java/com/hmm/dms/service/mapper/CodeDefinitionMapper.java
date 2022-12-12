package com.hmm.dms.service.mapper;

import com.hmm.dms.domain.CodeDefinition;
import com.hmm.dms.service.dto.CodeDefinitionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CodeDefinition} and its DTO {@link CodeDefinitionDTO}.
 */
@Mapper(componentModel = "spring")
public interface CodeDefinitionMapper extends EntityMapper<CodeDefinitionDTO, CodeDefinition> {}
