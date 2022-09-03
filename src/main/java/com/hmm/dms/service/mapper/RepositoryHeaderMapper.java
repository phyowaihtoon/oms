package com.hmm.dms.service.mapper;

import com.hmm.dms.domain.RepositoryHeader;
import com.hmm.dms.service.dto.RepositoryHeaderDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link MetaData} and its DTO {@link MetaDataDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RepositoryHeaderMapper extends EntityMapper<RepositoryHeaderDTO, RepositoryHeader> {}
