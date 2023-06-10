package creatip.oms.service.mapper;

import creatip.oms.domain.*;
import creatip.oms.service.dto.ApplicationUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ApplicationUser} and its DTO {@link ApplicationUserDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, UserRoleMapper.class, DepartmentMapper.class })
public interface ApplicationUserMapper extends EntityMapper<ApplicationUserDTO, ApplicationUser> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "userRole", source = "userRole")
    @Mapping(target = "department", source = "department")
    ApplicationUserDTO toDto(ApplicationUser s);
}
