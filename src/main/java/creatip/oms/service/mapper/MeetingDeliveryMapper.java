package creatip.oms.service.mapper;

import creatip.oms.domain.MeetingDelivery;
import creatip.oms.service.dto.MeetingDeliveryDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface MeetingDeliveryMapper extends EntityMapper<MeetingDeliveryDTO, MeetingDelivery> {}
