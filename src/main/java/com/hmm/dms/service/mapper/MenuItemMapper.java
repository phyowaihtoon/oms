package com.hmm.dms.service.mapper;

import com.hmm.dms.domain.MenuItem;
import com.hmm.dms.service.dto.MenuItemDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface MenuItemMapper extends EntityMapper<MenuItemDTO, MenuItem> {}
