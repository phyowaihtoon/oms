package com.hmm.dms.service.dto;

import java.io.Serializable;

public class UserAuthorityDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userID;
    private String userName;
    private String departmentName;
    private String roleName;
    private int workflowAuthority;

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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getWorkflowAuthority() {
        return workflowAuthority;
    }

    public void setWorkflowAuthority(int workflowAuthority) {
        this.workflowAuthority = workflowAuthority;
    }
}
