package creatip.oms.web.rest;

import creatip.oms.domain.SysConfig;
import creatip.oms.service.SysConfigService;
import creatip.oms.service.dto.CategoryDTO;
import creatip.oms.service.message.ReplyMessage;
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
    public List<String> updateFileVersion() throws Exception {
        return this.sysConfigService.updateFileVersion();
    }
}
