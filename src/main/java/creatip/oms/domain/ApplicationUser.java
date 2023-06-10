package creatip.oms.domain;

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
    @Column(name = "workflow_authority", nullable = false)
    private Integer workflowAuthority;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @ManyToOne(optional = false)
    @NotNull
    private UserRole userRole;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private MetaDataHeader department;

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

    public Integer getWorkflowAuthority() {
        return this.workflowAuthority;
    }

    public ApplicationUser workflowAuthority(Integer workflowAuthority) {
        this.workflowAuthority = workflowAuthority;
        return this;
    }

    public void setWorkflowAuthority(Integer workflowAuthority) {
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

    public UserRole getUserRole() {
        return this.userRole;
    }

    public ApplicationUser userRole(UserRole userRole) {
        this.setUserRole(userRole);
        return this;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public MetaDataHeader getDepartment() {
        return this.department;
    }

    public ApplicationUser department(MetaDataHeader department) {
        this.setDepartment(department);
        return this;
    }

    public void setDepartment(MetaDataHeader department) {
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
            ", workflowAuthority=" + getWorkflowAuthority() +
            "}";
    }
}
