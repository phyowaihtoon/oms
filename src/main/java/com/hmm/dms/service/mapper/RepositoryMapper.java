package com.hmm.dms.service.mapper;

import com.hmm.dms.domain.*;
import com.hmm.dms.service.dto.RepositoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Repository} and its DTO {@link RepositoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RepositoryMapper extends EntityMapper<RepositoryDTO, Repository> {}
