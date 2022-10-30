package com.hmm.dms.service.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.hmm.dms.domain.UserRole} entity.
 */
public class UserRoleDTO implements Serializable {

    private Long id;

    @NotNull
    private String roleName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserRoleDTO)) {
            return false;
        }

        UserRoleDTO userRoleDTO = (UserRoleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userRoleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserRoleDTO{" +
            "id=" + getId() +
            ", roleName='" + getRoleName() + "'" +
            "}";
    }
}
