package creatip.oms.service.mapper;

import creatip.oms.domain.MeetingReceiver;
import creatip.oms.service.dto.ReceiverInfoDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {})
public interface MeetingReceiverInfoMapper extends EntityMapper<ReceiverInfoDTO, MeetingReceiver> {
    @Override
    @Mappings(
        {
            @Mapping(target = "departmentId", source = "receiver.id"),
            @Mapping(target = "departmentName", source = "receiver.departmentName"),
            @Mapping(target = "receiverType", source = "receiverType"),
        }
    )
    ReceiverInfoDTO toDto(MeetingReceiver meetingReceiver);

    @Override
    @Mappings(
        {
            @Mapping(target = "departmentId", source = "receiver.id"),
            @Mapping(target = "departmentName", source = "receiver.departmentName"),
            @Mapping(target = "receiverType", source = "receiverType"),
        }
    )
    List<ReceiverInfoDTO> toDto(List<MeetingReceiver> entityList);
}
