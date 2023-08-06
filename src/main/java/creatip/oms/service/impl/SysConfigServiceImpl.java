package creatip.oms.service.impl;

import creatip.oms.domain.SysConfig;
import creatip.oms.repository.SysConfigRepository;
import creatip.oms.service.SysConfigService;
import creatip.oms.service.message.ReplyMessage;
import creatip.oms.service.message.SysConfigMessage;
import creatip.oms.util.FTPSessionFactory;
import creatip.oms.util.ResponseCode;
import creatip.oms.util.SysConfigVariables;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SysConfigServiceImpl implements SysConfigService {

    private final SysConfigRepository sysConfigRepository;
    private final FTPSessionFactory ftpSessionFactory;

    public SysConfigServiceImpl(SysConfigRepository sysConfigRepository, FTPSessionFactory ftpSessionFactory) {
        this.sysConfigRepository = sysConfigRepository;
        this.ftpSessionFactory = ftpSessionFactory;
    }

    @Override
    public SysConfigMessage defineSysConfig() {
        SysConfigMessage message = new SysConfigMessage();
        List<SysConfig> configList = this.sysConfigRepository.findAll();
        for (SysConfig data : configList) {
            if (data.getCode().equals(SysConfigVariables.WORKFLOW_AUTHORITY)) {
                SysConfigVariables.WORKFLOW_ENABLED = data.getEnabled();
                message.setWorkflowEnabled(data.getEnabled());
            }
            if (data.getCode().equals(SysConfigVariables.PDF_PREVIEW_LIMIT)) {
                SysConfigVariables.PDF_PREVIEW_ENABLED = data.getEnabled();
                SysConfigVariables.PDF_PREVIEW_VALUE = data.getValue();
            }
        }
        return message;
    }

    @Override
    public ReplyMessage<List<SysConfig>> getAllSysConfig() {
        ReplyMessage<List<SysConfig>> replyMessage = new ReplyMessage<List<SysConfig>>();
        List<SysConfig> configList = this.sysConfigRepository.findAll();
        replyMessage.setCode(ResponseCode.SUCCESS);
        replyMessage.setData(configList);
        return replyMessage;
    }

    @Override
    public ReplyMessage<List<SysConfig>> saveAllSysConfig(List<SysConfig> data) {
        ReplyMessage<List<SysConfig>> replyMessage = new ReplyMessage<List<SysConfig>>();
        List<SysConfig> configList = this.sysConfigRepository.saveAll(data);
        replyMessage.setCode(ResponseCode.SUCCESS);
        replyMessage.setMessage("Updated Successfully");
        replyMessage.setData(configList);
        return replyMessage;
    }
}
