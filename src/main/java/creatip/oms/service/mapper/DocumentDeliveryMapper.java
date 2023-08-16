package creatip.oms.service.mapper;

import creatip.oms.domain.DocumentDelivery;
import creatip.oms.service.dto.DocumentDeliveryDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface DocumentDeliveryMapper extends EntityMapper<DocumentDeliveryDTO, DocumentDelivery> {}
