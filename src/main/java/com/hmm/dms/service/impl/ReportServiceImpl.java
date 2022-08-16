package com.hmm.dms.service.impl;

import com.hmm.dms.service.ReportService;
import com.hmm.dms.service.dto.RptDataDTO;
import com.hmm.dms.service.dto.RptParamsDTO;
import com.hmm.dms.service.dto.UserDTO;
import com.hmm.dms.util.ReportPrint;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import org.springframework.beans.factory.annotation.Autowired;
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
        List<RptDataDTO> documentList = new ArrayList<RptDataDTO>();
        RptDataDTO doc1 = new RptDataDTO();
        doc1.setDataS1("PaySlip");
        doc1.setDataInt1(1);
        doc1.setDataS2("01-08-2022");

        RptDataDTO doc2 = new RptDataDTO();
        doc2.setDataS1("Account Opening Letter");
        doc2.setDataInt1(4);
        doc2.setDataS2("02-08-2022");

        RptDataDTO doc3 = new RptDataDTO();
        doc3.setDataS1("Agreement Letter");
        doc3.setDataInt1(6);
        doc3.setDataS2("03-08-2022");

        documentList.add(doc1);
        documentList.add(doc2);
        documentList.add(doc3);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("frmDate", rptPara.getRptPS1());
        parameters.put("toDate", rptPara.getRptPS2());
        String rptFilePath = ReportPrint.print(documentList, rptPara, parameters);
        return rptFilePath;
    }
}
