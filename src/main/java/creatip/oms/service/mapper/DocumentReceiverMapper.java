package creatip.oms.service.mapper;

import creatip.oms.domain.DocumentReceiver;
import creatip.oms.service.dto.DocumentReceiverDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface DocumentReceiverMapper extends EntityMapper<DocumentReceiverDTO, DocumentReceiver> {}
