package com.hmm.dms.service.mapper;

import com.hmm.dms.domain.DocumentHeader;
import com.hmm.dms.service.dto.DocumentHeaderDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface DocumentHeaderMapper extends EntityMapper<DocumentHeaderDTO, DocumentHeader> {}
