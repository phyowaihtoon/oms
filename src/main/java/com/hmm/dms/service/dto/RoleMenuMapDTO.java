package com.hmm.dms.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.hmm.dms.domain.RoleMenuMap} entity.
 */
public class RoleMenuMapDTO implements Serializable {

    private Long id;

    @NotNull
    private String roleId;

    @NotNull
    private Long menuId;

    @NotNull
    @Size(max = 1)
    private String allowed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getAllowed() {
        return allowed;
    }

    public void setAllowed(String allowed) {
        this.allowed = allowed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoleMenuMapDTO)) {
            return false;
        }

        RoleMenuMapDTO roleMenuMapDTO = (RoleMenuMapDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, roleMenuMapDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoleMenuMapDTO{" +
            "id=" + getId() +
            ", roleId='" + getRoleId() + "'" +
            ", menuId=" + getMenuId() +
            ", allowed='" + getAllowed() + "'" +
            "}";
    }
}
