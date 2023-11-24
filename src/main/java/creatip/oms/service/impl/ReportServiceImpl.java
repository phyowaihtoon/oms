package creatip.oms.service.impl;

import creatip.oms.domain.Department;
import creatip.oms.repository.DepartmentRepository;
import creatip.oms.service.ReportService;
import creatip.oms.service.dto.DepartmentDTO;
import creatip.oms.service.message.ReplyMessage;
import creatip.oms.service.message.RptDataMessage;
import creatip.oms.service.message.RptParamsMessage;
import creatip.oms.util.ReportPrint;
import creatip.oms.util.ResponseCode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public ReplyMessage<RptParamsMessage> generateDocumentListRpt(RptParamsMessage rptPara) {
        ReplyMessage<RptParamsMessage> replyMessage = new ReplyMessage<RptParamsMessage>();

        try {
        	
            StoredProcedureQuery query = em.createStoredProcedureQuery("DocumentDeliveryListRpt");
            query.registerStoredProcedureParameter("frmDate", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("toDate", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("deptid", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("loginDeptid", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("docstatus", Integer.class, ParameterMode.IN);
            
            query.setParameter("frmDate", rptPara.getRptPS1());
            query.setParameter("toDate", rptPara.getRptPS2());
            query.setParameter("deptid", rptPara.getRptPS3() == null? 0L: Long.parseLong(rptPara.getRptPS3()));
            query.setParameter("loginDeptid", rptPara.getRptPS4() == null? 0L: Long.parseLong(rptPara.getRptPS4()));
            query.setParameter("docstatus", Integer.parseInt(rptPara.getRptPS5()));
            
            List<Object[]> resultList = query.getResultList();
            if (resultList == null || resultList.size() == 0) {
                replyMessage.setCode(ResponseCode.WARNING);
                replyMessage.setMessage("NO DATA FOUND");
                return replyMessage;
            }
            List<RptDataMessage> documentList = null;
            if (resultList != null) {
                documentList = new ArrayList<RptDataMessage>();
                for (Object[] arr : resultList) {
                    RptDataMessage dto = new RptDataMessage();
                    dto.setDataS3(String.valueOf(arr[0]));
                    dto.setDataS1(String.valueOf(arr[1]));
                    dto.setDataS2(String.valueOf(arr[2]));
                    dto.setDataS6(String.valueOf(arr[3]));
                    dto.setDataS7(String.valueOf(arr[4]));
                    documentList.add(dto);
                }
            }

            Map<String, Object> parameters = new HashMap<String, Object>();
            
            parameters.put("frmDate", rptPara.getRptPS1());
            parameters.put("toDate", rptPara.getRptPS2());
            parameters.put("deptName", rptPara.getRptPS6()==null?"အားလုံး": rptPara.getRptPS6());           
            
            String rptFilePath = ReportPrint.print(documentList, rptPara, parameters);
            if (rptFilePath == null || rptFilePath.equals("")) {
                replyMessage.setCode(ResponseCode.ERROR_E00);
                replyMessage.setMessage("Report File cannot be generated");
                return replyMessage;
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
            replyMessage.setCode(ResponseCode.EXCEP_EX);
            replyMessage.setMessage("Report columns do not match." + ex.getMessage());
            return replyMessage;
        } catch (Exception ex) {
            ex.printStackTrace();
            replyMessage.setCode(ResponseCode.EXCEP_EX);
            replyMessage.setMessage(ex.getMessage());
            return replyMessage;
        }

        replyMessage.setCode(ResponseCode.SUCCESS);
        replyMessage.setMessage("Report is successfully generated");
        replyMessage.setData(rptPara);
        return replyMessage;
    }    
}
