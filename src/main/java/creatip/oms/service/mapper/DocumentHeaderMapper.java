package creatip.oms.service.mapper;

import creatip.oms.domain.DocumentHeader;
import creatip.oms.service.dto.DocumentHeaderDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface DocumentHeaderMapper extends EntityMapper<DocumentHeaderDTO, DocumentHeader> {}
