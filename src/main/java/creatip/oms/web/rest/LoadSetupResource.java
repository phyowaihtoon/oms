package creatip.oms.web.rest;

import creatip.oms.enumeration.CommonEnum.PriorityEnum;
import creatip.oms.service.LoadSetupService;
import creatip.oms.service.dto.DashboardTemplateDto;
import creatip.oms.service.dto.DepartmentDTO;
import creatip.oms.service.dto.HeadDepartmentDTO;
import creatip.oms.service.message.SetupEnumMessage;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/setup")
public class LoadSetupResource {

    private final LoadSetupService loadSetupService;

    public LoadSetupResource(LoadSetupService loadSetupService) {
        this.loadSetupService = loadSetupService;
    }

    @GetMapping("/priority")
    public List<SetupEnumMessage<Integer, String>> loadPriority() {
        List<SetupEnumMessage<Integer, String>> priorityList = new ArrayList<SetupEnumMessage<Integer, String>>();
        for (PriorityEnum enumData : PriorityEnum.values()) {
            SetupEnumMessage<Integer, String> setupDTO = new SetupEnumMessage<Integer, String>();
            setupDTO.setValue(enumData.value);
            setupDTO.setDescription(enumData.description);
            priorityList.add(setupDTO);
        }
        return priorityList;
    }

    @GetMapping("/dashboard")
    public List<DashboardTemplateDto> loadAllDashboardTemplate() {
        return this.loadSetupService.loadAllDashboardTemplate();
    }

    @GetMapping("/headdept")
    public List<HeadDepartmentDTO> loadAllHeadDepartments() {
        return this.loadSetupService.getAllHeadDepartments();
    }

    @GetMapping("/subdept")
    public List<DepartmentDTO> loadAllSubDepartments() {
        return this.loadSetupService.getAllSubDepartments();
    }
}
