package com.hmm.dms.service.mapper;

import com.hmm.dms.domain.RoleDashboardAccess;
import com.hmm.dms.service.dto.RoleDashboardAccessDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface RoleDashboardAccessMapper extends EntityMapper<RoleDashboardAccessDTO, RoleDashboardAccess> {}
