package creatip.oms.service.mapper;

import creatip.oms.domain.DocumentDelivery;
import creatip.oms.domain.MeetingDelivery;
import creatip.oms.service.dto.DocumentDeliveryDTO;
import creatip.oms.service.dto.MeetingDeliveryDTO;
import java.time.Instant;
import java.time.OffsetDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {})
public interface DocumentDeliveryMapper extends EntityMapper<DocumentDeliveryDTO, DocumentDelivery> {
    @Override
    @Mappings({ @Mapping(target = "sentDate", source = "sentDate", qualifiedByName = "stringToInstant") })
    DocumentDelivery toEntity(DocumentDeliveryDTO documentDeliveryDTO);

    @Override
    @Mappings({ @Mapping(target = "sentDate", source = "sentDate", qualifiedByName = "instantToString") })
    DocumentDeliveryDTO toDto(DocumentDelivery documentDelivery);

    @Named("stringToInstant")
    default Instant stringToInstant(String datetime) {
        if (datetime == null || datetime.isEmpty()) return null;

        OffsetDateTime offsetDateTime = OffsetDateTime.parse(datetime);
        return offsetDateTime.toInstant();
    }

    @Named("instantToString")
    default String instantToString(Instant datetime) {
        if (datetime == null) return null;
        return datetime.toString();
    }
}
