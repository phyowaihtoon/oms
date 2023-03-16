package com.hmm.dms.service;

import com.hmm.dms.service.message.ReplyMessage;
import com.hmm.dms.service.message.RptParamsMessage;

public interface ReportService {
    public ReplyMessage<RptParamsMessage> generateDocumentListRpt(RptParamsMessage rptPara);

    public ReplyMessage<RptParamsMessage> generateUploadedDocumentListRpt(RptParamsMessage rptPara, Long loginUserId);
}
