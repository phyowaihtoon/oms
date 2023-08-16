package creatip.oms.service.mapper;

import creatip.oms.domain.DocumentAttachment;
import creatip.oms.service.dto.DocumentAttachmentDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface DocumentAttachmentMapper extends EntityMapper<DocumentAttachmentDTO, DocumentAttachment> {}
