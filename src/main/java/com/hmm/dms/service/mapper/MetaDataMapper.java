package com.hmm.dms.service.mapper;

import com.hmm.dms.domain.*;
import com.hmm.dms.service.dto.MetaDataDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MetaData} and its DTO {@link MetaDataDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MetaDataMapper extends EntityMapper<MetaDataDTO, MetaData> {}
