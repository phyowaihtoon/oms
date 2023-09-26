package creatip.oms.web.rest;

import creatip.oms.domain.User;
import creatip.oms.enumeration.CommonEnum.PriorityEnum;
import creatip.oms.service.ApplicationUserService;
import creatip.oms.service.LoadSetupService;
import creatip.oms.service.UserService;
import creatip.oms.service.dto.ApplicationUserDTO;
import creatip.oms.service.dto.DashboardTemplateDto;
import creatip.oms.service.dto.DepartmentDTO;
import creatip.oms.service.dto.DraftSummaryDTO;
import creatip.oms.service.dto.HeadDepartmentDTO;
import creatip.oms.service.message.SetupEnumMessage;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/setup")
public class LoadSetupResource {

    private final Logger log = LoggerFactory.getLogger(LoadSetupResource.class);

    private final LoadSetupService loadSetupService;

    private final UserService userService;

    private final ApplicationUserService applicationUserService;

    public LoadSetupResource(LoadSetupService loadSetupService, UserService userService, ApplicationUserService applicationUserService) {
        this.loadSetupService = loadSetupService;
        this.userService = userService;
        this.applicationUserService = applicationUserService;
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

    @GetMapping("/draftsummary")
    public DraftSummaryDTO loadDraftSummary() {
        User loginUser = userService.getUserWithAuthorities().get();
        ApplicationUserDTO appUserDTO = applicationUserService.findOneByUserID(loginUser.getId());
        if (appUserDTO == null || appUserDTO.getDepartment() == null) {
            String message = loginUser.getLogin() + " is not linked with any department.";
            log.debug("Response Message : {}", message);
            return null;
        }
        return this.loadSetupService.getDraftSummary(appUserDTO.getDepartment());
    }
}
