package com.hmm.dms.service.mapper;

import com.hmm.dms.domain.*;
import com.hmm.dms.service.dto.RepositoryDocDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RepositoryDoc} and its DTO {@link RepositoryDocDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RepositoryDocMapper extends EntityMapper<RepositoryDocDTO, RepositoryDoc> {}
