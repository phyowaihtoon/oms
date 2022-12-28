package com.hmm.dms.service;

import com.hmm.dms.domain.SysConfig;
import com.hmm.dms.service.message.ReplyMessage;
import com.hmm.dms.service.message.SysConfigMessage;
import java.util.List;

public interface SysConfigService {
    SysConfigMessage defineSysConfig();

    ReplyMessage<List<SysConfig>> getAllSysConfig();

    ReplyMessage<List<SysConfig>> saveAllSysConfig(List<SysConfig> data);

    List<String> updateFileVersion() throws Exception;
}
