package com.hmm.dms.service.mapper;

import com.hmm.dms.domain.RoleTemplateAccess;
import com.hmm.dms.service.message.RoleTemplateAccessDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface RoleTemplateAccessMapper extends EntityMapper<RoleTemplateAccessDTO, RoleTemplateAccess> {}
