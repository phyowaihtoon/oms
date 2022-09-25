package com.hmm.dms.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ApplicationUser.
 */
@Entity
@Table(name = "application_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApplicationUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_role", nullable = false)
    private String userRole;

    @NotNull
    @Column(name = "workflow_authority", nullable = false)
    private String workflowAuthority;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApplicationUser id(Long id) {
        this.id = id;
        return this;
    }

    public String getUserRole() {
        return this.userRole;
    }

    public ApplicationUser userRole(String userRole) {
        this.userRole = userRole;
        return this;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getWorkflowAuthority() {
        return this.workflowAuthority;
    }

    public ApplicationUser workflowAuthority(String workflowAuthority) {
        this.workflowAuthority = workflowAuthority;
        return this;
    }

    public void setWorkflowAuthority(String workflowAuthority) {
        this.workflowAuthority = workflowAuthority;
    }

    public User getUser() {
        return this.user;
    }

    public ApplicationUser user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Department getDepartment() {
        return this.department;
    }

    public ApplicationUser department(Department department) {
        this.setDepartment(department);
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationUser)) {
            return false;
        }
        return id != null && id.equals(((ApplicationUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationUser{" +
            "id=" + getId() +
            ", userRole='" + getUserRole() + "'" +
            ", workflowAuthority='" + getWorkflowAuthority() + "'" +
            "}";
    }
}
