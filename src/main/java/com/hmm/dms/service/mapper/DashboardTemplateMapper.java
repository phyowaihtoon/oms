package com.hmm.dms.service.mapper;

import com.hmm.dms.domain.DashboardTemplate;
import com.hmm.dms.service.dto.DashboardTemplateDto;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link DashboardTemplate} and its DTO {@link DashboardTemplateDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DashboardTemplateMapper extends EntityMapper<DashboardTemplateDto, DashboardTemplate> {}
