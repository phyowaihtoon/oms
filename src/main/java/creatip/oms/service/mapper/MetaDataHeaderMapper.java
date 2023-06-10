package creatip.oms.service.mapper;

import creatip.oms.domain.MetaData;
import creatip.oms.domain.MetaDataHeader;
import creatip.oms.service.dto.MetaDataHeaderDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link MetaData} and its DTO {@link MetaDataHeaderDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MetaDataHeaderMapper extends EntityMapper<MetaDataHeaderDTO, MetaDataHeader> {}
