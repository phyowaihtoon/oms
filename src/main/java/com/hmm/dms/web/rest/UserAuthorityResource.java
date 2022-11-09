package com.hmm.dms.web.rest;

import com.hmm.dms.domain.User;
import com.hmm.dms.service.ApplicationUserService;
import com.hmm.dms.service.RoleMenuAccessService;
import com.hmm.dms.service.UserService;
import com.hmm.dms.service.dto.ApplicationUserDTO;
import com.hmm.dms.service.message.MenuGroupMessage;
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

    public UserAuthorityResource(
        ApplicationUserService applicationUserService,
        UserService userService,
        RoleMenuAccessService roleMenuAccessService
    ) {
        this.applicationUserService = applicationUserService;
        this.userService = userService;
        this.roleMenuAccessService = roleMenuAccessService;
    }

    @GetMapping("/userauthority")
    public UserAuthorityMessage getUserAuthority() {
        User loginUser = userService.getUserWithAuthorities().get();
        ApplicationUserDTO appUserDTO = applicationUserService.findOneByUserID(loginUser.getId());

        UserAuthorityMessage userAuthorityDTO = new UserAuthorityMessage();
        userAuthorityDTO.setUserID(loginUser.getLogin());
        userAuthorityDTO.setUserName(
            loginUser.getFirstName() != null
                ? loginUser.getFirstName()
                : "" + " " + loginUser.getLastName() != null ? loginUser.getLastName() : ""
        );
        userAuthorityDTO.setRoleName(appUserDTO.getUserRole().getRoleName());
        if (appUserDTO.getDepartment() != null) userAuthorityDTO.setDepartmentName(appUserDTO.getDepartment().getDepartmentName());
        userAuthorityDTO.setWorkflowAuthority(appUserDTO.getWorkflowAuthority());

        List<MenuGroupMessage> menuGroupList = this.roleMenuAccessService.getAllMenuGroupByRole(appUserDTO.getUserRole().getId());
        userAuthorityDTO.setMenuGroups(menuGroupList);
        return userAuthorityDTO;
    }
}
