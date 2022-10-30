package com.hmm.dms.web.rest;

import com.hmm.dms.service.RoleMenuAccessService;
import com.hmm.dms.service.dto.RoleMenuAccessDTO;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RoleMenuAccessResource {

    private final RoleMenuAccessService roleMenuAccessService;

    public RoleMenuAccessResource(RoleMenuAccessService roleMenuAccessService) {
        this.roleMenuAccessService = roleMenuAccessService;
    }

    @GetMapping("/menu-item")
    public List<RoleMenuAccessDTO> getAllMenuItems() {
        List<RoleMenuAccessDTO> menuAccessList = roleMenuAccessService.getAllMenuItems();
        return menuAccessList;
    }
}
