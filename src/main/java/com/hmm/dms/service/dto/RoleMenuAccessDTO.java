package com.hmm.dms.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;

public class RoleMenuAccessDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private int isAllow;

    private int isRead;

    private int isWrite;

    private int isDelete;

    @NotNull
    private MenuItemDTO menuItem;

    private UserRoleDTO userRole;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIsAllow() {
        return isAllow;
    }

    public void setIsAllow(int isAllow) {
        this.isAllow = isAllow;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getIsWrite() {
        return isWrite;
    }

    public void setIsWrite(int isWrite) {
        this.isWrite = isWrite;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public MenuItemDTO getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItemDTO menuItem) {
        this.menuItem = menuItem;
    }

    public UserRoleDTO getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRoleDTO userRole) {
        this.userRole = userRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoleMenuAccessDTO)) {
            return false;
        }

        RoleMenuAccessDTO roleMenuAccessDTO = (RoleMenuAccessDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, roleMenuAccessDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoleMenuAccessDTO{" +
            "id=" + getId() +
            ", isAllow='" + getIsAllow() + "'" +
            ", isRead='" + getIsRead() + "'" +
            ", isWrite='" + getIsWrite() + "'" +
            ", isDelete='" + getIsDelete() + "'" +
            ", menuItem ["+getMenuItem().toString()+"]"+
            ", userRole ["+getUserRole() !=null ? getUserRole().toString():""+"]"+
            "}";
    }
}
