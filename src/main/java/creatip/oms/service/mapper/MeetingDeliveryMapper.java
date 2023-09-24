package creatip.oms.service.mapper;

import creatip.oms.domain.MeetingDelivery;
import creatip.oms.service.dto.MeetingDeliveryDTO;
import java.time.Instant;
import java.time.OffsetDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {})
public interface MeetingDeliveryMapper extends EntityMapper<MeetingDeliveryDTO, MeetingDelivery> {
    @Override
    @Mappings(
        {
            @Mapping(target = "sentDate", source = "sentDate", qualifiedByName = "stringToInstant"),
            @Mapping(target = "startDate", source = "startDate", qualifiedByName = "stringToInstant"),
            @Mapping(target = "endDate", source = "endDate", qualifiedByName = "stringToInstant"),
        }
    )
    MeetingDelivery toEntity(MeetingDeliveryDTO meetingDeliveryDTO);

    @Override
    @Mappings(
        {
            @Mapping(target = "sentDate", source = "sentDate", qualifiedByName = "instantToString"),
            @Mapping(target = "startDate", source = "startDate", qualifiedByName = "instantToString"),
            @Mapping(target = "endDate", source = "endDate", qualifiedByName = "instantToString"),
        }
    )
    MeetingDeliveryDTO toDto(MeetingDelivery meetingDelivery);

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
