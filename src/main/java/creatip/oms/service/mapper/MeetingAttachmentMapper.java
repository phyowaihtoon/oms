package creatip.oms.service.mapper;

import creatip.oms.domain.MeetingAttachment;
import creatip.oms.service.dto.MeetingAttachmentDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface MeetingAttachmentMapper extends EntityMapper<MeetingAttachmentDTO, MeetingAttachment> {}
