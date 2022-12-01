package com.hmm.dms.service.impl;

import com.hmm.dms.domain.SysConfig;
import com.hmm.dms.repository.SysConfigRepository;
import com.hmm.dms.service.SysConfigService;
import com.hmm.dms.service.message.SysConfigMessage;
import com.hmm.dms.util.SysConfigData;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SysConfigServiceImpl implements SysConfigService {

    private final SysConfigRepository sysConfigRepository;

    public SysConfigServiceImpl(SysConfigRepository sysConfigRepository) {
        this.sysConfigRepository = sysConfigRepository;
    }

    @Override
    public SysConfigMessage defineSysConfig() {
        SysConfigMessage message = new SysConfigMessage();
        List<SysConfig> configList = this.sysConfigRepository.findAll();
        for (SysConfig data : configList) {
            if (data.getCode().equals("WORKFLOW_AUTHORITY")) {
                SysConfigData.WORKFLOW_ENABLED = data.getEnabled();
                message.setWorkflowEnabled(data.getEnabled());
            }
        }
        return message;
    }
}
