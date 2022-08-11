package com.hmm.dms.service.mapper;

import com.hmm.dms.domain.*;
import com.hmm.dms.service.dto.RoleMenuMapDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RoleMenuMap} and its DTO {@link RoleMenuMapDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RoleMenuMapMapper extends EntityMapper<RoleMenuMapDTO, RoleMenuMap> {}
