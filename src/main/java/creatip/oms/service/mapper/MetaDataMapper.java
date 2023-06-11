package creatip.oms.service.mapper;

import creatip.oms.domain.*;
import creatip.oms.service.dto.MetaDataDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MetaData} and its DTO {@link MetaDataDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MetaDataMapper extends EntityMapper<MetaDataDTO, MetaData> {}