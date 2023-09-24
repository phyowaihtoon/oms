package creatip.oms.service.mapper;

import creatip.oms.domain.*;
import creatip.oms.service.dto.ApplicationUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ApplicationUser} and its DTO {@link ApplicationUserDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ApplicationUserMapper extends EntityMapper<ApplicationUserDTO, ApplicationUser> {}
