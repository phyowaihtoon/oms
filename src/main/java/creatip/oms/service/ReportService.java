package creatip.oms.service;

import creatip.oms.service.message.ReplyMessage;
import creatip.oms.service.message.RptParamsMessage;

public interface ReportService {
    public ReplyMessage<RptParamsMessage> generateDocumentListRpt(RptParamsMessage rptPara);

    public ReplyMessage<RptParamsMessage> generateDocumentReceivedListRptByDepartment(RptParamsMessage rptPara);

    public ReplyMessage<RptParamsMessage> generateDocumentSentListRpt(RptParamsMessage rptPara);
}
