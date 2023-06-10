package creatip.oms.service.mapper;

import creatip.oms.domain.MenuItem;
import creatip.oms.service.dto.MenuItemDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface MenuItemMapper extends EntityMapper<MenuItemDTO, MenuItem> {}
