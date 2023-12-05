package creatip.oms.service.mapper;

import creatip.oms.domain.DocumentReceiver;
import creatip.oms.service.dto.ReceiverInfoDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {})
public interface DocumentReceiverInfoMapper extends EntityMapper<ReceiverInfoDTO, DocumentReceiver> {
    @Override
    @Mappings(
        {
            @Mapping(target = "departmentId", source = "receiver.id"),
            @Mapping(target = "departmentName", source = "receiver.departmentName"),
            @Mapping(target = "receiverType", source = "receiverType"),
        }
    )
    ReceiverInfoDTO toDto(DocumentReceiver documentReceiver);

    @Override
    @Mappings(
        {
            @Mapping(target = "departmentId", source = "receiver.id"),
            @Mapping(target = "departmentName", source = "receiver.departmentName"),
            @Mapping(target = "receiverType", source = "receiverType"),
        }
    )
    List<ReceiverInfoDTO> toDto(List<DocumentReceiver> entityList);
}
