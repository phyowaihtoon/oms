package com.hmm.dms.service;

import com.hmm.dms.service.dto.ReplyMessage;
import com.hmm.dms.service.dto.RptParamsDTO;

public interface ReportService {
    public ReplyMessage<RptParamsDTO> generateDocumentListRpt(RptParamsDTO rptPara);
}
