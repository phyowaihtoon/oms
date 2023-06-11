package creatip.oms.service.impl;

import creatip.oms.domain.MetaDataHeader;
import creatip.oms.repository.MetaDataHeaderRepository;
import creatip.oms.service.ReportService;
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

    private final MetaDataHeaderRepository metaDataHeaderRepository;

    @PersistenceContext
    EntityManager em;

    public ReportServiceImpl(MetaDataHeaderRepository metaDataHeaderRepository) {
        this.metaDataHeaderRepository = metaDataHeaderRepository;
    }

    @Override
    public ReplyMessage<RptParamsMessage> generateDocumentListRpt(RptParamsMessage rptPara) {
        ReplyMessage<RptParamsMessage> replyMessage = new ReplyMessage<RptParamsMessage>();

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
            List<RptDataMessage> documentList = null;
            if (resultList != null) {
                documentList = new ArrayList<RptDataMessage>();
                for (Object[] arr : resultList) {
                    RptDataMessage dto = new RptDataMessage();
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

    @Override
    public ReplyMessage<RptParamsMessage> generateUploadedDocumentListRpt(RptParamsMessage rptPara, Long loginUserId) {
        ReplyMessage<RptParamsMessage> replyMessage = new ReplyMessage<RptParamsMessage>();
        Optional<MetaDataHeader> metaDtaHeader = Optional.empty();

        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("SP_DOCLIST_RPT");
            query.registerStoredProcedureParameter("frmDate", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("toDate", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("metadata", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("userid", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("loginUserId", Long.class, ParameterMode.IN);

            query.setParameter("frmDate", rptPara.getRptPS1());
            query.setParameter("toDate", rptPara.getRptPS2());
            query.setParameter("metadata", Long.parseLong(rptPara.getRptPS3() == null ? "0" : rptPara.getRptPS3()));
            query.setParameter("userid", rptPara.getRptPS4().trim());
            query.setParameter("loginUserId", loginUserId);

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
                    dto.setDataS1(String.valueOf(arr[0]));
                    dto.setDataS2(String.valueOf(arr[1]));
                    dto.setDataS3(String.valueOf(arr[2]));
                    dto.setDataS4(String.valueOf(arr[3]));
                    dto.setDataS5(String.valueOf(arr[4]));
                    dto.setDataS6(String.valueOf(arr[5]));
                    dto.setDataS7(String.valueOf(arr[6]));
                    documentList.add(dto);
                }
            }

            metaDtaHeader = metaDataHeaderRepository.findById(Long.parseLong(rptPara.getRptPS3() == null ? "0" : rptPara.getRptPS3()));

            System.out.println(metaDtaHeader);

            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("frmDate", rptPara.getRptPS1());
            parameters.put("toDate", rptPara.getRptPS2());
            parameters.put("metadata", metaDtaHeader.isEmpty() ? "All" : metaDtaHeader.get().getDocTitle());
            parameters.put("userid", rptPara.getRptPS4().trim() == "" ? "All" : rptPara.getRptPS4());

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