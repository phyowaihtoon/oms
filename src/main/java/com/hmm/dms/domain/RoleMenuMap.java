package com.hmm.dms.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RoleMenuMap.
 */
@Entity
@Table(name = "role_menu_map")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RoleMenuMap extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "role_id", nullable = false)
    private String roleId;

    @NotNull
    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @NotNull
    @Size(max = 1)
    @Column(name = "allowed", length = 1, nullable = false)
    private String allowed;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleMenuMap id(Long id) {
        this.id = id;
        return this;
    }

    public String getRoleId() {
        return this.roleId;
    }

    public RoleMenuMap roleId(String roleId) {
        this.roleId = roleId;
        return this;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Long getMenuId() {
        return this.menuId;
    }

    public RoleMenuMap menuId(Long menuId) {
        this.menuId = menuId;
        return this;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getAllowed() {
        return this.allowed;
    }

    public RoleMenuMap allowed(String allowed) {
        this.allowed = allowed;
        return this;
    }

    public void setAllowed(String allowed) {
        this.allowed = allowed;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoleMenuMap)) {
            return false;
        }
        return id != null && id.equals(((RoleMenuMap) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoleMenuMap{" +
            "id=" + getId() +
            ", roleId='" + getRoleId() + "'" +
            ", menuId=" + getMenuId() +
            ", allowed='" + getAllowed() + "'" +
            "}";
    }
}
