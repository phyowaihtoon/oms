package creatip.oms.service.mapper;

import creatip.oms.domain.RepositoryDomain;
import creatip.oms.service.dto.RepositoryDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link MetaData} and its DTO {@link MetaDataDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RepositoryMapper extends EntityMapper<RepositoryDTO, RepositoryDomain> {}
