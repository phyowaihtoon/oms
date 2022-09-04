package com.hmm.dms.service.mapper;

import com.hmm.dms.domain.RepositoryDomain;
import com.hmm.dms.service.dto.RepositoryDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link MetaData} and its DTO {@link MetaDataDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RepositoryMapper extends EntityMapper<RepositoryDTO, RepositoryDomain> {}
