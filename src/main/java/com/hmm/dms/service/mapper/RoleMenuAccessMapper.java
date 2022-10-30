package com.hmm.dms.service.mapper;

import com.hmm.dms.domain.RoleMenuAccess;
import com.hmm.dms.service.dto.RoleMenuAccessDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface RoleMenuAccessMapper extends EntityMapper<RoleMenuAccessDTO, RoleMenuAccess> {}
