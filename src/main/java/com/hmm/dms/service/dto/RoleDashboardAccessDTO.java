package com.hmm.dms.service.dto;

import java.io.Serializable;

public class RoleDashboardAccessDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private DashboardTemplateDto dashboardTemplate;
    private UserRoleDTO userRole;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DashboardTemplateDto getDashboardTemplate() {
        return dashboardTemplate;
    }

    public void setDashboardTemplate(DashboardTemplateDto dashboardTemplate) {
        this.dashboardTemplate = dashboardTemplate;
    }

    public UserRoleDTO getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRoleDTO userRole) {
        this.userRole = userRole;
    }
}
