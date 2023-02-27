package com.hmm.dms.web.rest;

import com.hmm.dms.domain.User;
import com.hmm.dms.service.ApplicationUserService;
import com.hmm.dms.service.RoleDashboardAccessService;
import com.hmm.dms.service.RoleMenuAccessService;
import com.hmm.dms.service.SysConfigService;
import com.hmm.dms.service.UserService;
import com.hmm.dms.service.dto.ApplicationUserDTO;
import com.hmm.dms.service.dto.DashboardTemplateDto;
import com.hmm.dms.service.message.MenuGroupMessage;
import com.hmm.dms.service.message.SysConfigMessage;
import com.hmm.dms.service.message.UserAuthorityMessage;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class UserAuthorityResource {

    private final ApplicationUserService applicationUserService;
    private final UserService userService;
    private final RoleMenuAccessService roleMenuAccessService;
    private final SysConfigService sysConfigService;
    private final RoleDashboardAccessService roleDashboardAccessService;

    public UserAuthorityResource(
        ApplicationUserService applicationUserService,
        UserService userService,
        RoleMenuAccessService roleMenuAccessService,
        SysConfigService sysConfigService,
        RoleDashboardAccessService roleDashboardAccessService
    ) {
        this.applicationUserService = applicationUserService;
        this.userService = userService;
        this.roleMenuAccessService = roleMenuAccessService;
        this.sysConfigService = sysConfigService;
        this.roleDashboardAccessService = roleDashboardAccessService;
    }

    @GetMapping("/userauthority")
    public UserAuthorityMessage getUserAuthority() {
        SysConfigMessage sysConfig = this.sysConfigService.defineSysConfig();

        User loginUser = userService.getUserWithAuthorities().get();
        ApplicationUserDTO appUserDTO = applicationUserService.findOneByUserID(loginUser.getId());

        UserAuthorityMessage userAuthorityMessage = new UserAuthorityMessage();
        userAuthorityMessage.setSysConfigMessage(sysConfig);
        userAuthorityMessage.setUserID(loginUser.getLogin());
        userAuthorityMessage.setUserName(
            loginUser.getFirstName() != null
                ? loginUser.getFirstName()
                : "" + " " + loginUser.getLastName() != null ? loginUser.getLastName() : ""
        );
        userAuthorityMessage.setRoleID(appUserDTO.getUserRole().getId());
        userAuthorityMessage.setRoleName(appUserDTO.getUserRole().getRoleName());
        userAuthorityMessage.setRoleType(appUserDTO.getUserRole().getRoleType());

        if (appUserDTO.getDepartment() != null) userAuthorityMessage.setDepartmentName(appUserDTO.getDepartment().getDepartmentName());
        userAuthorityMessage.setWorkflowAuthority(appUserDTO.getWorkflowAuthority());

        List<MenuGroupMessage> menuGroupList = this.roleMenuAccessService.getAllMenuGroupByRole(appUserDTO.getUserRole().getId());
        userAuthorityMessage.setMenuGroups(menuGroupList);

        List<DashboardTemplateDto> dashboardTemplateList =
            this.roleDashboardAccessService.getAllDashboardTemplateByRole(appUserDTO.getUserRole().getId());
        userAuthorityMessage.setDashboardTemplates(dashboardTemplateList);

        return userAuthorityMessage;
    }
}
