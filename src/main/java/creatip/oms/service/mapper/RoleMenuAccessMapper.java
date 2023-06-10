package creatip.oms.service.mapper;

import creatip.oms.domain.RoleMenuAccess;
import creatip.oms.service.dto.RoleMenuAccessDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface RoleMenuAccessMapper extends EntityMapper<RoleMenuAccessDTO, RoleMenuAccess> {}
