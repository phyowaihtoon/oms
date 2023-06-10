package creatip.oms.service.mapper;

import creatip.oms.domain.CodeDefinition;
import creatip.oms.service.dto.CodeDefinitionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CodeDefinition} and its DTO {@link CodeDefinitionDTO}.
 */
@Mapper(componentModel = "spring")
public interface CodeDefinitionMapper extends EntityMapper<CodeDefinitionDTO, CodeDefinition> {}
