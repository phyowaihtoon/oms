package creatip.oms.service;

import creatip.oms.domain.SysConfig;
import creatip.oms.service.message.ReplyMessage;
import creatip.oms.service.message.SysConfigMessage;
import java.util.List;

public interface SysConfigService {
    SysConfigMessage defineSysConfig();

    ReplyMessage<List<SysConfig>> getAllSysConfig();

    ReplyMessage<List<SysConfig>> saveAllSysConfig(List<SysConfig> data);
}
