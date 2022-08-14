package com.hmm.dms.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Repository.
 */
@Entity
@Table(name = "repository")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Repository extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_id")
    private Integer parentID;

    @NotNull
    @Column(name = "repository_name", nullable = false)
    private String repositoryName;

    @Column(name = "description")
    private String description;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Repository id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getParentID() {
        return this.parentID;
    }

    public Repository parentID(Integer parentID) {
        this.parentID = parentID;
        return this;
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    public String getRepositoryName() {
        return this.repositoryName;
    }

    public Repository repositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
        return this;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getDescription() {
        return this.description;
    }

    public Repository description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Repository)) {
            return false;
        }
        return id != null && id.equals(((Repository) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Repository{" +
            "id=" + getId() +
            ", parentID=" + getParentID() +
            ", repositoryName='" + getRepositoryName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
