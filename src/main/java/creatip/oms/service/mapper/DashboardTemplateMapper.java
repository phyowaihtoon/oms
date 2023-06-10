package creatip.oms.service.mapper;

import creatip.oms.domain.DashboardTemplate;
import creatip.oms.service.dto.DashboardTemplateDto;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link DashboardTemplate} and its DTO {@link DashboardTemplateDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DashboardTemplateMapper extends EntityMapper<DashboardTemplateDto, DashboardTemplate> {}
