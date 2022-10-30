package com.hmm.dms.service.mapper;

import com.hmm.dms.domain.MenuGroup;
import com.hmm.dms.service.dto.MenuGroupDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface MenuGroupMapper extends EntityMapper<MenuGroupDTO, MenuGroup> {}
