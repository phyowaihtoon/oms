package creatip.oms.service.mapper;

import creatip.oms.domain.RoleDashboardAccess;
import creatip.oms.service.dto.RoleDashboardAccessDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface RoleDashboardAccessMapper extends EntityMapper<RoleDashboardAccessDTO, RoleDashboardAccess> {}
