package creatip.oms.service.mapper;

import creatip.oms.domain.Announcement;
import creatip.oms.service.dto.AnnouncementDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Announcement} and its DTO {@link AnnouncementDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AnnouncementMapper extends EntityMapper<AnnouncementDTO, Announcement> {}
