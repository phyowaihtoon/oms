package creatip.oms.service.mapper;

import creatip.oms.domain.MenuGroup;
import creatip.oms.service.dto.MenuGroupDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface MenuGroupMapper extends EntityMapper<MenuGroupDTO, MenuGroup> {}
