package creatip.oms.service.mapper;

import creatip.oms.domain.RepositoryHeader;
import creatip.oms.service.dto.RepositoryHeaderDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link MetaData} and its DTO {@link MetaDataDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RepositoryHeaderMapper extends EntityMapper<RepositoryHeaderDTO, RepositoryHeader> {}
