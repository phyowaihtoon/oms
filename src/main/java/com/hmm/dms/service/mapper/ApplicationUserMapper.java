package com.hmm.dms.service.mapper;

import com.hmm.dms.domain.*;
import com.hmm.dms.service.dto.ApplicationUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ApplicationUser} and its DTO {@link ApplicationUserDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, DepartmentMapper.class })
public interface ApplicationUserMapper extends EntityMapper<ApplicationUserDTO, ApplicationUser> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "department", source = "department")
    ApplicationUserDTO toDto(ApplicationUser s);
}
