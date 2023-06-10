package creatip.oms.service.mapper;

import creatip.oms.domain.RoleTemplateAccess;
import creatip.oms.service.message.RoleTemplateAccessDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface RoleTemplateAccessMapper extends EntityMapper<RoleTemplateAccessDTO, RoleTemplateAccess> {}
