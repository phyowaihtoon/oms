package creatip.oms.web.rest;

import creatip.oms.domain.User;
import creatip.oms.service.ApplicationUserService;
import creatip.oms.service.DocumentDeliveryService;
import creatip.oms.service.RoleDashboardAccessService;
import creatip.oms.service.RoleMenuAccessService;
import creatip.oms.service.SysConfigService;
import creatip.oms.service.UserService;
import creatip.oms.service.dto.ApplicationUserDTO;
import creatip.oms.service.dto.DashboardTemplateDto;
import creatip.oms.service.message.MenuGroupMessage;
import creatip.oms.service.message.NotificationMessage;
import creatip.oms.service.message.SysConfigMessage;
import creatip.oms.service.message.UserAuthorityMessage;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final DocumentDeliveryService documentDeliveryService;

    private final Logger log = LoggerFactory.getLogger(UserAuthorityResource.class);

    public UserAuthorityResource(
        ApplicationUserService applicationUserService,
        UserService userService,
        RoleMenuAccessService roleMenuAccessService,
        SysConfigService sysConfigService,
        RoleDashboardAccessService roleDashboardAccessService,
        DocumentDeliveryService documentDeliveryService
    ) {
        this.applicationUserService = applicationUserService;
        this.userService = userService;
        this.roleMenuAccessService = roleMenuAccessService;
        this.sysConfigService = sysConfigService;
        this.roleDashboardAccessService = roleDashboardAccessService;
        this.documentDeliveryService = documentDeliveryService;
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

        List<MenuGroupMessage> menuGroupList = this.roleMenuAccessService.getAllMenuGroupByRole(appUserDTO.getUserRole().getId());
        userAuthorityMessage.setMenuGroups(menuGroupList);

        List<DashboardTemplateDto> dashboardTemplateList =
            this.roleDashboardAccessService.getAllDashboardTemplateByRole(appUserDTO.getUserRole().getId());
        userAuthorityMessage.setDashboardTemplates(dashboardTemplateList);

        userAuthorityMessage.setDepartment(appUserDTO.getDepartment());

        return userAuthorityMessage;
    }

    @GetMapping("/noticount")
    public List<NotificationMessage> getUserNotiCount() {
        log.debug("Request to get Notification");

        User loginUser = userService.getUserWithAuthorities().get();
        ApplicationUserDTO appUserDTO = applicationUserService.findOneByUserID(loginUser.getId());
        if (appUserDTO == null || appUserDTO.getDepartment() == null) {
            String message = loginUser.getLogin() + " is not linked with any department.";
            log.debug(message);
        }

        log.debug("Login User Information: User ID [{}] , Department [{}] ", loginUser.getLogin(), appUserDTO.getDepartment());

        List<NotificationMessage> notiList = this.documentDeliveryService.getNotification(appUserDTO.getDepartment().getId());

        log.debug("Total notification count : {{}}", notiList.size());

        return notiList;
    }
}
