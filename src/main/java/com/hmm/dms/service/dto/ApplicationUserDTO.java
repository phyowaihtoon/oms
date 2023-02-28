package com.hmm.dms.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.hmm.dms.domain.ApplicationUser} entity.
 */
public class ApplicationUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private Integer workflowAuthority;

    private UserDTO user;

    private UserRoleDTO userRole;

    private MetaDataHeaderDTO department;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWorkflowAuthority() {
        return workflowAuthority;
    }

    public void setWorkflowAuthority(Integer workflowAuthority) {
        this.workflowAuthority = workflowAuthority;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public UserRoleDTO getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRoleDTO userRole) {
        this.userRole = userRole;
    }

    public MetaDataHeaderDTO getDepartment() {
        return department;
    }

    public void setDepartment(MetaDataHeaderDTO department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationUserDTO)) {
            return false;
        }

        ApplicationUserDTO applicationUserDTO = (ApplicationUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, applicationUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationUserDTO{" +
            "id=" + getId() +
            ", workflowAuthority=" + getWorkflowAuthority() +
            ", user=" + getUser() +
            ", userRole=" + getUserRole() +
            ", department=" + getDepartment() +
            "}";
    }
}
