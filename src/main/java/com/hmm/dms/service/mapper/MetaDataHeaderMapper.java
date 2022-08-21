package com.hmm.dms.service.mapper;

import com.hmm.dms.domain.MetaData;
import com.hmm.dms.domain.MetaDataHeader;
import com.hmm.dms.service.dto.MetaDataHeaderDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link MetaData} and its DTO {@link MetaDataHeaderDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MetaDataHeaderMapper extends EntityMapper<MetaDataHeaderDTO, MetaDataHeader> {}
