package com.hmm.dms.service.impl;

import com.hmm.dms.service.ReportService;
import com.hmm.dms.service.dto.ReplyMessage;
import com.hmm.dms.service.dto.RptDataDTO;
import com.hmm.dms.service.dto.RptParamsDTO;
import com.hmm.dms.util.ReportPrint;
import com.hmm.dms.util.ResponseCode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {

    @PersistenceContext
    EntityManager em;

    public ReportServiceImpl() {}

    @Override
    public ReplyMessage<RptParamsDTO> generateDocumentListRpt(RptParamsDTO rptPara) {
        ReplyMessage<RptParamsDTO> replyMessage = new ReplyMessage<RptParamsDTO>();

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("SP_DOCMAPPING_RPT");
            query.registerStoredProcedureParameter("frmDate", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("toDate", String.class, ParameterMode.IN);
            query.setParameter("frmDate", rptPara.getRptPS1());
            query.setParameter("toDate", rptPara.getRptPS2());
            List<Object[]> resultList = query.getResultList();
            if (resultList == null || resultList.size() == 0) {
                replyMessage.setCode(ResponseCode.WARNING);
                replyMessage.setMessage("NO DATA FOUND");
                return replyMessage;
            }
            List<RptDataDTO> documentList = null;
            if (resultList != null) {
                documentList = new ArrayList<RptDataDTO>();
                for (Object[] arr : resultList) {
                    RptDataDTO dto = new RptDataDTO();
                    dto.setDataS1(String.valueOf(arr[0]));
                    dto.setDataS2(String.valueOf(arr[1]));
                    dto.setDataS3(String.valueOf(arr[2]));
                    dto.setDataS4(String.valueOf(arr[3]));
                    dto.setDataS5(String.valueOf(arr[4]));
                    documentList.add(dto);
                }
            }

            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("frmDate", rptPara.getRptPS1());
            parameters.put("toDate", rptPara.getRptPS2());
            String rptFilePath = ReportPrint.print(documentList, rptPara, parameters);
            if (rptFilePath == null || rptFilePath.equals("")) {
                replyMessage.setCode(ResponseCode.ERROR_E00);
                replyMessage.setMessage("Report File cannot be generated");
                return replyMessage;
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
            replyMessage.setCode(ResponseCode.ERROR_E01);
            replyMessage.setMessage("Report columns do not match." + ex.getMessage());
            return replyMessage;
        } catch (Exception ex) {
            ex.printStackTrace();
            replyMessage.setCode(ResponseCode.ERROR_E01);
            replyMessage.setMessage(ex.getMessage());
            return replyMessage;
        }

        replyMessage.setCode(ResponseCode.SUCCESS);
        replyMessage.setMessage("Report is successfully generated");
        replyMessage.setData(rptPara);
        return replyMessage;
    }
}
