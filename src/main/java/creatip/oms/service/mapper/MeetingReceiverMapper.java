package creatip.oms.service.mapper;

import creatip.oms.domain.MeetingReceiver;
import creatip.oms.service.dto.MeetingReceiverDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface MeetingReceiverMapper extends EntityMapper<MeetingReceiverDTO, MeetingReceiver> {}
