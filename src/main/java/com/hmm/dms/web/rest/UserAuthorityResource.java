package com.hmm.dms.web.rest;

import com.hmm.dms.domain.User;
import com.hmm.dms.service.ApplicationUserService;
import com.hmm.dms.service.UserService;
import com.hmm.dms.service.dto.ApplicationUserDTO;
import com.hmm.dms.service.dto.UserAuthorityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class UserAuthorityResource {

    private final Logger log = LoggerFactory.getLogger(ApplicationUserResource.class);

    private final ApplicationUserService applicationUserService;
    private final UserService userService;

    public UserAuthorityResource(ApplicationUserService applicationUserService, UserService userService) {
        this.applicationUserService = applicationUserService;
        this.userService = userService;
    }

    @GetMapping("/userauthority")
    public UserAuthorityDTO getUserAuthority() {
        User loginUser = userService.getUserWithAuthorities().get();
        ApplicationUserDTO appUserDTO = applicationUserService.findOneByUserID(loginUser.getId());

        UserAuthorityDTO userAuthorityDTO = new UserAuthorityDTO();
        userAuthorityDTO.setUserID(loginUser.getLogin());
        userAuthorityDTO.setUserName(
            loginUser.getFirstName() != null
                ? loginUser.getFirstName()
                : "" + " " + loginUser.getLastName() != null ? loginUser.getLastName() : ""
        );
        userAuthorityDTO.setRoleName(appUserDTO.getUserRole().getRoleName());
        if (appUserDTO.getDepartment() != null) userAuthorityDTO.setDepartmentName(appUserDTO.getDepartment().getDepartmentName());
        userAuthorityDTO.setWorkflowAuthority(appUserDTO.getWorkflowAuthority());

        return userAuthorityDTO;
    }
}
