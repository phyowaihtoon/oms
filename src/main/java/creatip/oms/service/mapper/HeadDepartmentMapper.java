package creatip.oms.service.mapper;

import creatip.oms.domain.HeadDepartment;
import creatip.oms.service.dto.HeadDepartmentDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface HeadDepartmentMapper extends EntityMapper<HeadDepartmentDTO, HeadDepartment> {}
