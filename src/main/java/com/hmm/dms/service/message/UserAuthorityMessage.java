package com.hmm.dms.service.message;

import com.hmm.dms.service.dto.DashboardTemplateDto;
import java.io.Serializable;
import java.util.List;

public class UserAuthorityMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userID;
    private String userName;
    private String departmentName;
    private Long roleID;
    private String roleName;
    private int roleType;
    private int workflowAuthority;
    private SysConfigMessage sysConfigMessage;
    private List<MenuGroupMessage> menuGroups;
    private List<DashboardTemplateDto> dashboardTemplates;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getRoleID() {
        return roleID;
    }

    public void setRoleID(Long roleID) {
        this.roleID = roleID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    public int getWorkflowAuthority() {
        return workflowAuthority;
    }

    public void setWorkflowAuthority(int workflowAuthority) {
        this.workflowAuthority = workflowAuthority;
    }

    public SysConfigMessage getSysConfigMessage() {
        return sysConfigMessage;
    }

    public void setSysConfigMessage(SysConfigMessage sysConfigMessage) {
        this.sysConfigMessage = sysConfigMessage;
    }

    public List<MenuGroupMessage> getMenuGroups() {
        return menuGroups;
    }

    public void setMenuGroups(List<MenuGroupMessage> menuGroups) {
        this.menuGroups = menuGroups;
    }

    public List<DashboardTemplateDto> getDashboardTemplates() {
        return dashboardTemplates;
    }

    public void setDashboardTemplates(List<DashboardTemplateDto> dashboardTemplates) {
        this.dashboardTemplates = dashboardTemplates;
    }
}
