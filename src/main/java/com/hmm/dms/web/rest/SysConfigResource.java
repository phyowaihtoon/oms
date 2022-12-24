package com.hmm.dms.web.rest;

import com.hmm.dms.domain.SysConfig;
import com.hmm.dms.service.SysConfigService;
import com.hmm.dms.service.dto.CategoryDTO;
import com.hmm.dms.service.message.ReplyMessage;
import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class SysConfigResource {

    private final SysConfigService sysConfigService;

    public SysConfigResource(SysConfigService sysConfigService) {
        this.sysConfigService = sysConfigService;
    }

    @GetMapping("/sysconfig")
    public ReplyMessage<List<SysConfig>> getAllSysConfig() {
        return this.sysConfigService.getAllSysConfig();
    }

    @PostMapping("/sysconfig")
    public ReplyMessage<List<SysConfig>> saveAllSysConfig(@Valid @RequestBody List<SysConfig> data) {
        return this.sysConfigService.saveAllSysConfig(data);
    }

    @GetMapping("/updateFileVersion")
    public void updateFileVersion() throws Exception {
        this.sysConfigService.updateFileVersion();
    }
}
