package com.hmm.dms.service.impl;

import com.hmm.dms.service.ReportService;
import com.hmm.dms.service.dto.RptDataDTO;
import com.hmm.dms.service.dto.RptParamsDTO;
import com.hmm.dms.util.ReportPrint;
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
    public String generateDocumentListRpt(RptParamsDTO rptPara) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("SP_DOCMAPPING_RPT");
        query.registerStoredProcedureParameter("frmDate", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("toDate", String.class, ParameterMode.IN);
        query.setParameter("frmDate", rptPara.getRptPS1());
        query.setParameter("toDate", rptPara.getRptPS2());
        List<Object[]> resultList = query.getResultList();
        List<RptDataDTO> documentList = null;
        if (resultList != null) {
            documentList = new ArrayList<RptDataDTO>();
            for (Object[] arr : resultList) {
                RptDataDTO dto = new RptDataDTO();
                //dto.setStartDateStr(String.valueOf(arr[0]));
                dto.setDataS1(String.valueOf(arr[1]));
                dto.setDataS2(String.valueOf(arr[2]));
                dto.setDataS3(String.valueOf(arr[3]));
                dto.setDataInt1(Integer.parseInt(String.valueOf(arr[4])));
                documentList.add(dto);
            }
        }

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("frmDate", rptPara.getRptPS1());
        parameters.put("toDate", rptPara.getRptPS2());
        String rptFilePath = ReportPrint.print(documentList, rptPara, parameters);
        return rptFilePath;
    }
}
